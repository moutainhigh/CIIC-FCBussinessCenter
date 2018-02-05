package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @PostMapping("/addBackTraceBatch")
    public JsonResult addBackTraceBatch(@RequestParam String batchCode) {

        PrNormalBatchPO batchPO = batchService.getBatchByCode(batchCode);
        String code = codeGenerator.genPrNormalBatchCode(batchPO.getManagementId(),batchPO.getActualPeriod());
        PrBackTrackingBatchPO trackingBatchPO = new PrBackTrackingBatchPO();
        trackingBatchPO.setBackTrackingBatchCode(code);
        trackingBatchPO.setOriginBatchCode(batchCode);
        trackingBatchPO.setStatus(BatchStatusEnum.NEW.getValue());
        trackingBatchPO.setCreatedBy("bill");
        trackingBatchPO.setModifiedBy("bill");
        backTrackingBatchService.insert(trackingBatchPO);

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
            @RequestParam String batchCode,
            @RequestParam String originCode) {

        List<DBObject> backTraceList = backTrackingBatchService.getBackTrackingBatch(batchCode,originCode); //从mongodb里检查是否有该数据
        if(backTraceList == null || backTraceList.size() == 0){
            return JsonResult.success(0);
        }

        Criteria criteria = Criteria.where("batch_code").is(batchCode).and("catalog.emp_info.is_active").is(true);

        if(StringUtils.isNotEmpty(empCode)){
            criteria.and(PayItemName.EMPLOYEE_CODE_CN).regex(empCode);
        }
        if(StringUtils.isNotEmpty(empName)){
            criteria.and("catalog.emp_info."+ PayItemName.EMPLOYEE_NAME_CN).regex(empName);
        }
        if(StringUtils.isNotEmpty(customKey) && StringUtils.isNotEmpty(customValue)){
            criteria.and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(customKey).and("item_value").regex(customValue));
        }

        List<DBObject> list = backTraceBatchMongoOpt.list(criteria);
        int totalCount = list.size();
        if(totalCount == 0){
            return JsonResult.success(0);
        }

        list = list.stream().skip((pageNum-1) * pageSize).limit(pageSize).collect(Collectors.toList());

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = list.stream().map(dbObject -> {
            SimpleEmpPayItemDTO itemPO = new SimpleEmpPayItemDTO();
            itemPO.setEmpCode(String.valueOf(dbObject.get(PayItemName.EMPLOYEE_CODE_CN)));

            DBObject calalog = (DBObject)dbObject.get("catalog");
            DBObject empInfo = (DBObject)calalog.get("emp_info");
            itemPO.setEmpName(empInfo.get(PayItemName.EMPLOYEE_NAME_CN) == null ? "" : (String)empInfo.get(PayItemName.EMPLOYEE_NAME_CN)); //雇员姓名
            itemPO.setTaxPeriod(empInfo.get(PayItemName.EMPLOYEE_TAX_CN) == null ? "本月" : (String)empInfo.get(PayItemName.EMPLOYEE_TAX_CN)); //雇员个税期间 TODO

            List<DBObject> items = (List<DBObject>)calalog.get("pay_items");
            List<SimplePayItemDTO> simplePayItemDTOList = new ArrayList<>();
            items.forEach( dbItem -> {
                SimplePayItemDTO simplePayItemDTO = new SimplePayItemDTO();
                simplePayItemDTO.setDataType(dbItem.get("data_type") == null ? -1 : (int)dbItem.get("data_type"));
                simplePayItemDTO.setItemType(dbItem.get("item_type") == null ? -1 : (int)dbItem.get("item_type"));
                simplePayItemDTO.setVal(dbItem.get("item_value") == null ? dbItem.get("default_value"): dbItem.get("item_value"));
                simplePayItemDTO.setName(dbItem.get("item_name") == null ? "" : (String)dbItem.get("item_name"));
                simplePayItemDTOList.add(simplePayItemDTO);
            });
            itemPO.setPayItemDTOS(simplePayItemDTOList);
            return itemPO;
        }).collect(Collectors.toList());

        PageInfo<SimpleEmpPayItemDTO> result = new PageInfo<>(simplePayItemDTOS);
        result.setTotal(totalCount);
        result.setPageNum(pageNum);
        //adjustBatchService.insert();

        return JsonResult.success(result);
    }

}
