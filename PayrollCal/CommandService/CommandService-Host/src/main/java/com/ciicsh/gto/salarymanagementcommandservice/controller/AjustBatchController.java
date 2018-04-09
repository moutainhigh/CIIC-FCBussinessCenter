package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/1/23.
 */
@RestController
public class AjustBatchController {

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

    @PostMapping("/addAdjustBatch")
    public JsonResult addAdjustBatch(@RequestParam String batchCode, @RequestParam(required = false, defaultValue = "") String originCode) {

        PrAdjustBatchPO adjustBatchPO = new PrAdjustBatchPO();

        String rootCode = "";

        if(StringUtils.isNotEmpty(originCode)){
            rootCode = originCode;

        }else {
            rootCode = batchCode;
        }

        PrNormalBatchPO batchPO = batchService.getBatchByCode(rootCode);
        if(batchPO == null){
            adjustBatchPO.setAdjustBatchCode(batchCode);
            PrAdjustBatchPO find = adjustBatchService.getAdjustBatchPO(adjustBatchPO);
            rootCode = find.getRootBatchCode();
            batchPO = batchService.getBatchByCode(rootCode);
        }
        String code = codeGenerator.genPrNormalBatchCode(batchPO.getManagementId(),batchPO.getActualPeriod());
        adjustBatchPO.setAdjustBatchCode(code);
        adjustBatchPO.setRootBatchCode(rootCode);
        adjustBatchPO.setOriginBatchCode(batchCode); // normal or adjust code
        LocalDate today = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        adjustBatchPO.setPeriod(today.format(dtf));

        adjustBatchPO.setStatus(BatchStatusEnum.NEW.getValue());
        adjustBatchPO.setCreatedBy("bill");
        adjustBatchPO.setModifiedBy("bill");
        adjustBatchService.insert(adjustBatchPO);

        return JsonResult.success(code);
    }

    @DeleteMapping("/deleteAdjustBatch/{codes}")
    public JsonResult deleteBatch(@PathVariable("codes") String batchCodes) {
        String[] codes = batchCodes.split(",");
        int i = adjustBatchService.deleteAdjustBatchByCodes(Arrays.asList(codes));
        if (i >= 1){
            //send message to kafka
            PayrollMsg msg = new PayrollMsg();
            msg.setBatchCode(batchCodes);
            msg.setOperateType(OperateTypeEnum.DELETE.getValue());
            sender.Send(msg);

            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

    @GetMapping("/getAdjustBatch")
    public JsonResult getAdjustBatch(
            @RequestParam(required = false, defaultValue = "") String empCode,
            @RequestParam(required = false, defaultValue = "") String empName,
            @RequestParam(required = false, defaultValue = "") String customKey,
            @RequestParam(required = false, defaultValue = "") String customValue,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
            @RequestParam String batchCode) {


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

        long totalCount = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH).stream().count();
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
                include("catalog.pay_items.item_value")
        ;
        query.skip((pageNum-1) * pageSize);
        query.limit(pageSize);

        List<DBObject> list = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);


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

    @PostMapping("/updateBatchValue")
    public JsonResult updateBatchValue(@RequestParam int batchType, @RequestParam String batchCode, @RequestParam String empCode,
                                       @RequestParam String payItemName, @RequestParam String payItemVal){

        int rowAffected = 0;
        if(batchType == BatchTypeEnum.NORMAL.getValue()){
            rowAffected = normalBatchMongoOpt.update(Criteria.where("batch_code").is(batchCode)
                            .andOperator(
                                    Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                                    Criteria.where("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName))
                            ),"catalog.pay_items.$.item_value",payItemVal);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){

            updateMongoAdjust(batchType,batchCode,empCode,payItemName,payItemVal);

            rowAffected = adjustBatchMongoOpt.update(Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                            Criteria.where("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName))
                    ),"catalog.pay_items.$.item_value",payItemVal);

        }else {

            updateMongoAdjust(batchType,batchCode,empCode,payItemName,payItemVal);

            rowAffected = backTraceBatchMongoOpt.update(Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                            Criteria.where("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName))
                    ),"catalog.pay_items.$.item_value",payItemVal);

        }
        return JsonResult.success(rowAffected);
    }

    private int updateMongoAdjust(int batchType, String batchCode, String empCode, String name, String val){
        int affected = 0;
        /**
         * adjust_items like [ { name: '', origin:'', new:''}, { name: '', origin:'', new:''},... ]
         */
        DBObject dbObject = null;
        if(batchType == BatchTypeEnum.ADJUST.getValue()){
            dbObject = adjustBatchMongoOpt.get(Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                            Criteria.where("catalog.adjust_items").elemMatch(Criteria.where("name").is(name))
                    ));
        }else {
            dbObject = backTraceBatchMongoOpt.get(Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                            Criteria.where("catalog.adjust_items").elemMatch(Criteria.where("name").is(name))
                    ));
        }
        if(dbObject == null){ // 没有原始值，初始化
            DBObject batch = null;
            if(batchType == BatchTypeEnum.ADJUST.getValue()) {
                 batch = adjustBatchMongoOpt.get(Criteria.where("batch_code").is(batchCode).andOperator(
                        Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode)));
            }else {
                batch = backTraceBatchMongoOpt.get(Criteria.where("batch_code").is(batchCode).andOperator(
                        Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode)));
            }

            DBObject catalog = (DBObject) batch.get("catalog");
            List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");

            DBObject updateObj = new BasicDBObject();
            updateObj.put("name",name);
            updateObj.put("new", val);

            Optional<DBObject> findItem = payItems.stream().filter(item-> item.get("item_name").equals(name)).findFirst();
            if(findItem.isPresent()){
                updateObj.put("origin", findItem.get().get("item_value"));
            }

            List<DBObject> adjustItems = null;
            if(catalog.get("adjust_items") == null){
                adjustItems = new ArrayList<>();
            }else {
                adjustItems = (List<DBObject>) catalog.get("adjust_items");
            }

            adjustItems.add(updateObj);

            if(batchType == BatchTypeEnum.ADJUST.getValue()) {

                affected = adjustBatchMongoOpt.upsert(Query.query(Criteria.where("batch_code").is(batchCode)
                                .andOperator(
                                        Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode)
                                )),
                        Update.update("catalog.adjust_items", adjustItems)
                );
            }else {
                affected = backTraceBatchMongoOpt.upsert(Query.query(Criteria.where("batch_code").is(batchCode)
                                .andOperator(
                                        Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode)
                                )),
                        Update.update("catalog.adjust_items", adjustItems)
                );
            }
        }else {
            if (batchType == BatchTypeEnum.ADJUST.getValue()) {

                affected = adjustBatchMongoOpt.update(Criteria.where("batch_code").is(batchCode)
                        .andOperator(
                                Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                                Criteria.where("catalog.adjust_items").elemMatch(Criteria.where("name").is(name))
                        ), "catalog.adjust_items.$.new", val);
            } else {
                affected = backTraceBatchMongoOpt.update(Criteria.where("batch_code").is(batchCode)
                        .andOperator(
                                Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                                Criteria.where("catalog.adjust_items").elemMatch(Criteria.where("name").is(name))
                        ), "catalog.adjust_items.$.new", val);
            }
        }

        return affected;
    }

    @PostMapping("/checkAdjustBatch")
    public JsonResult checkAdjustBatch(@RequestParam String originBatchCode){
        int count = adjustBatchService.checkAdjustBatch(originBatchCode);
        return JsonResult.success(count);
    }

    @PostMapping("/deleteNew")
    public JsonResult deleteNew(@RequestParam String batchCodes, @RequestParam int batchType) {
        String[] codes = batchCodes.split(",");
        int rowAffected = 0;
        if(batchType == BatchTypeEnum.ADJUST.getValue()){
            rowAffected = adjustBatchService.deleteAdjustBatchByCodes(Arrays.asList(codes));
        }else {
            rowAffected = backTrackingBatchService.deleteBackTraceBatchByCodes(Arrays.asList(codes));
        }
        if (rowAffected >= 1){
            //send message to kafka
            PayrollMsg msg = new PayrollMsg();
            msg.setBatchCode(batchCodes);
            msg.setBatchType(batchType);
            msg.setOperateType(OperateTypeEnum.DELETE.getValue());
            sender.Send(msg);
            return JsonResult.success(rowAffected,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

}
