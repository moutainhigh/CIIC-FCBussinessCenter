package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import com.github.pagehelper.PageInfo;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/1/23.
 */
@RestController
public class BackTraceBatchController {

    @Autowired
    private KafkaSender sender;

    @Autowired
    private PrNormalBatchService batchService;

    @Autowired
    private PrAdjustBatchService adjustBatchService;

    @Autowired
    private PrBackTrackingBatchService backTrackingBatchService;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private PrAccountSetService accountSetService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private CommonServiceImpl commonService;


    @PostMapping("/addBackTraceBatch")
    public JsonResult addBackTraceBatch(@RequestParam String batchCode, @RequestParam(required = false, defaultValue = "") String originCode) {

        PrBackTrackingBatchPO backTrackingBatchPO = new PrBackTrackingBatchPO();

        String rootCode = "";

        if(StringUtils.isNotEmpty(originCode)){
            rootCode = originCode;

        }else {
            rootCode = batchCode;
        }

        PrNormalBatchPO batchPO = batchService.getBatchByCode(rootCode);
        if(batchPO == null){
            backTrackingBatchPO.setBackTrackingBatchCode(batchCode);
            PrBackTrackingBatchPO find = backTrackingBatchService.getPrBackTrackingBatchPO(backTrackingBatchPO);
            rootCode = find.getRootBatchCode();
            batchPO = batchService.getBatchByCode(rootCode);
        }
        String code = codeGenerator.genPrNormalBatchCode(batchPO.getManagementId(),batchPO.getActualPeriod());
        backTrackingBatchPO.setBackTrackingBatchCode(code);
        backTrackingBatchPO.setRootBatchCode(rootCode);
        backTrackingBatchPO.setOriginBatchCode(batchCode); // normal or adjust code

        LocalDate today = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        backTrackingBatchPO.setPeriod(today.format(dtf));


        backTrackingBatchPO.setStatus(BatchStatusEnum.NEW.getValue());
        backTrackingBatchPO.setCreatedBy("bill");
        backTrackingBatchPO.setModifiedBy("bill");
        backTrackingBatchService.insert(backTrackingBatchPO);

        return JsonResult.success(code);
    }

    @GetMapping("/getBackTraceBatch")
    public JsonResult getBackTraceBatch(
            @RequestParam(required = false, defaultValue = "") String empCode,
            @RequestParam(required = false, defaultValue = "") String empName,
            @RequestParam(required = false, defaultValue = "") String customKey,
            @RequestParam(required = false, defaultValue = "") String customValue,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
            @RequestParam String batchCode) {

        List<DBObject> backTraceList = backTrackingBatchService.getBackTrackingBatch(batchCode); //从mongodb里检查是否有该数据
        if(backTraceList == null || backTraceList.size() == 0){
            return JsonResult.success(0);
        }

        Criteria criteria = Criteria.where("batch_code").is(batchCode);

        if(StringUtils.isNotEmpty(empCode)){
            criteria.and(PayItemName.EMPLOYEE_CODE_CN).regex(empCode);
        }
        if(StringUtils.isNotEmpty(empName)){
            criteria.and("catalog.emp_info."+ PayItemName.EMPLOYEE_NAME_CN).regex(empName);
        }
        if(StringUtils.isNotEmpty(customKey) && StringUtils.isNotEmpty(customValue)){
            criteria.and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(customKey).and("item_value").regex(customValue));
        }

        Query query = new Query(criteria);
        query.fields().include("batch_code");

        long totalCount = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH).stream().count();
        if(totalCount == 0){
            return JsonResult.success(0);
        }

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN).
                include("catalog.emp_info."+PayItemName.EMPLOYEE_NAME_CN).
                include("catalog.emp_info."+PayItemName.EMPLOYEE_TAX_CN).
                include("catalog.pay_items.data_type").
                include("catalog.pay_items.item_type").
                include("catalog.pay_items.item_name").
                include("catalog.pay_items.item_value").
                include("catalog.pay_items.display_priority")
        ;
        query.skip((pageNum-1) * pageSize);
        query.limit(pageSize);

        List<DBObject> list = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = commonService.translate(list);

        PageInfo<SimpleEmpPayItemDTO> result = new PageInfo<>(simplePayItemDTOS);
        result.setTotal(totalCount);
        result.setPageNum(pageNum);
        return JsonResult.success(result);
    }


    @PostMapping("/checkBackTraceBatch")
    public JsonResult checkBackTraceBatch(@RequestParam String originBatchCode){
        int count = backTrackingBatchService.checkBackTraceBatch(originBatchCode);
        return JsonResult.success(count);
    }

}
