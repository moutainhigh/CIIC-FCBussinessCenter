package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.dto.*;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.AdjustBatchMsg;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import com.github.pagehelper.PageInfo;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/1/23.
 */
@RestController
public class AdjustBatchController {

    private static final Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");

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
    private CommonServiceImpl commonService;

    @Autowired
    private CodeGenerator codeGenerator;


    @GetMapping("/getMainAdjustBatch")
    public JsonResult getMainAdjustBatch(@RequestParam String batchCode,
                                         @RequestParam String originCode,
                                         @RequestParam String rootCode,
                                         @RequestParam(required = false, defaultValue = "") String empCode,
                                         @RequestParam(required = false, defaultValue = "") String empName,
                                         @RequestParam(required = false, defaultValue = "") String companyId)
    {

        List<DBObject> list = null;

        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);
        if(StringUtils.isNotEmpty(empCode)){
            criteria.and(PayItemName.EMPLOYEE_CODE_CN).regex(empCode);
        }
        if(StringUtils.isNotEmpty(companyId)){
            criteria.and(PayItemName.EMPLOYEE_COMPANY_ID).regex(companyId);
        }
        if(StringUtils.isNotEmpty(empName)){
            criteria.and("catalog.emp_info."+PayItemName.EMPLOYEE_NAME_CN).regex(empName);
        }

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN).
                include(PayItemName.EMPLOYEE_COMPANY_ID).
                include("adjust_val").
                include("catalog.emp_info."+PayItemName.EMPLOYEE_NAME_CN)
        ;

        list = adjustBatchMongoOpt.getMongoTemplate().find(query, DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        List<AdjustValDTO> adjustValDTOS = list.stream().map(p -> {
            AdjustValDTO adjustValDTO = new AdjustValDTO();
            adjustValDTO.setAdjustVal(p.get("adjust_val") == null ? 0.0 : (double)p.get("adjust_val"));
            adjustValDTO.setEmpCode(p.get(PayItemName.EMPLOYEE_CODE_CN) == null ? "": (String)p.get(PayItemName.EMPLOYEE_CODE_CN));
            adjustValDTO.setCompanyId(p.get(PayItemName.EMPLOYEE_COMPANY_ID) == null ? "": (String)p.get(PayItemName.EMPLOYEE_COMPANY_ID));

            DBObject catalog = (DBObject)p.get("catalog");
            DBObject empInfo = (DBObject)catalog.get("emp_info");
            adjustValDTO.setEmpName(empInfo.get(PayItemName.EMPLOYEE_NAME_CN) == null ? "" : (String)empInfo.get(PayItemName.EMPLOYEE_NAME_CN));

            return adjustValDTO;
        }).collect(Collectors.toList());

        return JsonResult.success(adjustValDTOS);
    }

    @PostMapping("/UpdateAdjustVal")
    public JsonResult UpdateAdjustVal(@RequestBody List<AdjustItemDTO> adjustItemDTOs, @RequestParam String batchCode) {
        if(adjustItemDTOs.size() > 0) {

            int rowAffected = 0;
            for (AdjustItemDTO item:adjustItemDTOs) {
                Criteria criteria = Criteria.where("batch_code").is(batchCode);
                criteria.and(PayItemName.EMPLOYEE_CODE_CN).is(item.getEmpCode()).and(PayItemName.EMPLOYEE_COMPANY_ID).is(item.getCompanyId());
                Query query = Query.query(criteria);
                Update update = Update.update("adjust_val",item.getAdjustVal());
                WriteResult result = adjustBatchMongoOpt.getMongoTemplate().upsert(query,update,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
                rowAffected += result.getN();
            }

            return JsonResult.success(rowAffected);
        }else {
            return JsonResult.faultMessage();
        }
    }


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
            //adjustBatchPO.setAdjustBatchCode(batchCode);
            PrAdjustBatchPO find = adjustBatchService.getAdjustBatchPO(batchCode);
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
        adjustBatchPO.setCreatedBy(UserContext.getName());
        adjustBatchPO.setModifiedBy(UserContext.getName());
        adjustBatchService.insert(adjustBatchPO);

        return JsonResult.success(code);
    }

    @DeleteMapping("/deleteAdjustBatch/{codes}")
    public JsonResult deleteBatch(@PathVariable("codes") String batchCodes) {
        String[] codes = batchCodes.split(",");
        int rowAffected = adjustBatchService.deleteAdjustBatchByCodes(Arrays.asList(codes));
        if (rowAffected > 0){
            //send message to kafka
            AdjustBatchMsg msg = new AdjustBatchMsg();
            msg.setOperateTypeEnum(OperateTypeEnum.DELETE);
            msg.setAdjustBatchCode(batchCodes);
            sender.SendAdjustBatch(msg);

            return JsonResult.success(rowAffected,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

    @PostMapping("/getAdjustBatch/{batchCode}/{batchType}")
    public JsonResult getAdjustBatch(
            @RequestParam(required = false, defaultValue = "") String empCode,
            @RequestParam(required = false, defaultValue = "") String empName,
            @RequestParam(required = false, defaultValue = "") String customKey,
            @RequestParam(required = false, defaultValue = "") String customValue,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
            @RequestParam(required = false, defaultValue = "") Integer flag,
            @PathVariable("batchCode") String batchCode,
            @PathVariable("batchType") Integer batchType) {

        Criteria criteria = null;

        if(flag == null) {
            criteria = Criteria.where("batch_code").is(batchCode).orOperator(
                    Criteria.where("adjust_val").lt(0.0),
                    Criteria.where("adjust_val").gt(0.0)
            );
        }else {
            criteria = Criteria.where("batch_code").is(batchCode);
        }

        if(StringUtils.isNotEmpty(empCode)){
            criteria.and(PayItemName.EMPLOYEE_CODE_CN).regex(empCode);
        }
        if(StringUtils.isNotEmpty(empName)){
            criteria.and("catalog.emp_info."+ PayItemName.EMPLOYEE_NAME_CN).regex(empName);
        }

        if(StringUtils.isNotEmpty(customKey) && StringUtils.isNotEmpty(customValue)){
            String fieldName = customKey.split(",")[0];
            String dataType = customKey.split(",")[1];
            if(dataType.equals(String.valueOf(DataTypeEnum.NUM.getValue()))){
                double val = Double.parseDouble(customValue);
                criteria.and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(fieldName).and("item_value").is(val));
            }else {
                criteria.and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(fieldName).and("item_value").regex(customValue));
            }
        }

        Query query = new Query(criteria);
        query.fields().include("batch_code");

        long totalCount = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH).stream().count();
        if(totalCount == 0){
            return JsonResult.success(0);
        }

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN).
                include(PayItemName.EMPLOYEE_COMPANY_ID).
                include("adjust_val").    //调整值
                include("catalog.emp_info."+PayItemName.EMPLOYEE_NAME_CN).
                include("catalog.pay_items.data_type").
                include("catalog.pay_items.canLock").
                include("catalog.pay_items.isLocked").
                include("catalog.pay_items.item_type").
                include("catalog.pay_items.item_name").
                include("catalog.pay_items.item_value").
                include("catalog.pay_items.display_priority")
        ;
        query.skip((pageNum-1) * pageSize);
        query.limit(pageSize);

        List<DBObject> list = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        if(list.size() == 1){ // 如果有一条纪录，但 emp_info 为 "" 时，说明雇员组没有雇员
            DBObject checkEmpInfo = list.get(0);
            DBObject catalog = (DBObject)checkEmpInfo.get("catalog");
            if(catalog.get("emp_info") == null){
                return JsonResult.success(0);
            }
        }

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = commonService.translate(list);


        PageInfo<SimpleEmpPayItemDTO> result = new PageInfo<>(simplePayItemDTOS);
        result.setTotal(totalCount);
        result.setPageNum(pageNum);
        //adjustBatchService.insert();

        return JsonResult.success(result);
    }

    @PostMapping("/updateBatchValue")
    public JsonResult updateBatchValue(@RequestParam int batchType, @RequestParam String batchCode, @RequestParam String companyId, @RequestParam String empCode, @RequestParam int dataType,
                                       @RequestParam String payItemName, @RequestParam Object payItemVal){

        int rowAffected = 0;

        String key = "catalog.pay_items.$.item_value";

        if(dataType == DataTypeEnum.NUM.getValue()){
            payItemVal = Double.parseDouble(payItemVal.toString());
        }else if(dataType == DataTypeEnum.TEXT.getValue() || dataType == DataTypeEnum.DATE.getValue()){
            payItemVal = String.valueOf(payItemVal);
        }else {
            payItemVal = Boolean.parseBoolean(payItemVal.toString());
        }
        if(payItemVal instanceof Boolean){
            key = "catalog.pay_items.$.isLocked";
        }

        Criteria criteria = Criteria.where("batch_code").is(batchCode)
                .andOperator(
                        Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                        Criteria.where(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId),
                        Criteria.where("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName)));

        if(batchType == BatchTypeEnum.NORMAL.getValue()){
            rowAffected = normalBatchMongoOpt.update(criteria,key,payItemVal);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){

            if(dataType == DataTypeEnum.NUM.getValue()){
                updateMongoAdjust(criteria, payItemVal);
            }

            rowAffected = adjustBatchMongoOpt.update(criteria,key,payItemVal);

        }else {

            /*if(dataType == DataTypeEnum.NUM.getValue()){
                updateMongoAdjust(batchType, batchCode, empCode, companyId, payItemName, payItemVal);
            }

            rowAffected = backTraceBatchMongoOpt.update(criteria,key,payItemVal);*/
        }
        return JsonResult.success(rowAffected);
    }

    private int updateMongoAdjust(Criteria criteria, Object val) {
        int affected = 0;

        Query query = Query.query(criteria);
        query.fields().include("adjust_val").include("catalog.pay_items.$");

        DBObject find = adjustBatchMongoOpt.getMongoTemplate().findOne(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        if(find != null){
            double adjustVal = (double)find.get("adjust_val");
            double newVal = (double)val;

            DBObject catalog = (DBObject)find.get("catalog");
            List<DBObject> items = (List<DBObject>) catalog.get("pay_items");
            double oldVal = items.get(0).get("item_value") == null ? 0.0 : Double.valueOf(items.get(0).get("item_value").toString());

            adjustVal = adjustVal + oldVal - newVal;

            affected = adjustBatchMongoOpt.update(criteria,"adjust_val",adjustVal);
        }

        return affected;

    }

    /*
    private int updateMongoAdjust(int batchType, String batchCode, String empCode,String companyId, String name, Object val){
        int affected = 0;
        DBObject dbObject = null;
        if(batchType == BatchTypeEnum.ADJUST.getValue()){
            dbObject = adjustBatchMongoOpt.get(Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                            Criteria.where(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId),
                            Criteria.where("catalog.adjust_items").elemMatch(Criteria.where("name").is(name))
                    ));
        }else {
            dbObject = backTraceBatchMongoOpt.get(Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                            Criteria.where(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId),
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
    }*/

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
            if (rowAffected >= 1) {
                //send message to kafka
                AdjustBatchMsg msg = new AdjustBatchMsg();
                msg.setOperateTypeEnum(OperateTypeEnum.DELETE);
                msg.setAdjustBatchCode(batchCodes);
                sender.SendAdjustBatch(msg);
            }
        }else {
            rowAffected = backTrackingBatchService.deleteBackTraceBatchByCodes(Arrays.asList(codes));
        }
        return JsonResult.success(rowAffected,"删除成功");

    }

    private Query createQuery(String batchCode){
        Criteria criteria = Criteria.where("batch_code").is(batchCode).andOperator(Criteria.where("show_adj").is(true));
        Query query = Query.query(criteria);
        query.fields().include("batch_code");
        return query;
    }

    @GetMapping("/showAdjustInfo")
    public JsonResult showAdjustInfo(@RequestParam String batchCode, @RequestParam(required = false) int batchType){
        int findFlag = 0;
        if(batchType == BatchTypeEnum.ADJUST.getValue()){
            Query query = createQuery(batchCode);
            DBObject find = adjustBatchMongoOpt.getMongoTemplate().findOne(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
            if(find != null){
                findFlag = 1;
            }
        }
        return JsonResult.success(findFlag);
    }

    /**
     * 获取上次调整批次比对结果
     * @param pageNum
     * @param pageSize
     * @param batchCode
     * @return
     */
    @GetMapping("getCompareAdjustBatch")
    public JsonResult getCompareAdjustBatch( @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                             @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
                                             @RequestParam String batchCode){

        Query query = createQuery(batchCode);

        long totalCount = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH).stream().count();

        query.fields().
                include("origin_batch_code").
                include(PayItemName.EMPLOYEE_CODE_CN).
                include("catalog.emp_info." + PayItemName.EMPLOYEE_NAME_CN).
                include("catalog.pay_items.data_type").
                include("catalog.pay_items.item_name").
                include("catalog.pay_items.item_value").
                include("catalog.adjust_items")
        ;
        query.skip((pageNum-1) * pageSize);
        query.limit(pageSize);

        List<DBObject> list = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        List<ComparedAdjustBatchDTO> adjustBatchDTOS = list.stream().map(item->{
            ComparedAdjustBatchDTO adjustBatchDTO = new ComparedAdjustBatchDTO();
            DBObject catalog = (DBObject) item.get("catalog");
            DBObject empInfo = (DBObject) catalog.get("emp_info");
            String empCode = (String) item.get(PayItemName.EMPLOYEE_CODE_CN);
            String empName = empInfo.get(PayItemName.EMPLOYEE_NAME_CN)== null ? "" :(String)empInfo.get(PayItemName.EMPLOYEE_NAME_CN);
            List<DBObject> adjustItems = (List<DBObject>)catalog.get("adjust_items");
            String originBatchCode = (String) item.get("origin_batch_code");
            adjustBatchDTO.setOriginCode(originBatchCode);
            adjustBatchDTO.setBatchCode(batchCode);
            adjustBatchDTO.setEmpCode(empCode);
            adjustBatchDTO.setEmpName(empName);
            List<AdjustItem> adjusts = adjustItems.stream().map(p->{
                AdjustItem adjust = new AdjustItem();
                adjust.setItemName((String) p.get("name"));
                Object origin = p.get("origin");
                Object n = p.get("new");
                adjust.setBefore(origin);
                adjust.setAfter(n);
                if(n !=null && isNumeric(String.valueOf(n))){
                    if(origin == null){
                        adjust.setGap(n);
                    }else {
                       BigDecimal gap = (new BigDecimal(String.valueOf(n))).subtract(new BigDecimal(String.valueOf(origin)));
                       adjust.setGap(gap);
                    }
                }
                return adjust;
            }).collect(Collectors.toList());

            adjustBatchDTO.setAdjustItems(adjusts);

            return adjustBatchDTO;
        }).collect(Collectors.toList());

        PageInfo<ComparedAdjustBatchDTO> result = new PageInfo<>(adjustBatchDTOS);
        result.setTotal(totalCount);
        result.setPageNum(pageNum);

        return JsonResult.success(result);

    }

    private boolean isNumeric(String str) {
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
