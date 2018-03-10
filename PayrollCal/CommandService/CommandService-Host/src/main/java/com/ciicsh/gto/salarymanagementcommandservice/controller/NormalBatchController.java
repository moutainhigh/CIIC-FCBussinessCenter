package com.ciicsh.gto.salarymanagementcommandservice.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.BatchAuditDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.dto.EmpFilterDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.BathTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.BatchUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/6.
 */
@RestController
public class NormalBatchController {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchController.class);

    @Autowired
    private KafkaSender sender;

    @Autowired
    private PrNormalBatchService batchService;

    @Autowired
    private PrAdjustBatchService adjustBatchService;

    @Autowired
    private PrBackTrackingBatchService backTrackingBatchService;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private PrAccountSetService accountSetService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CodeGenerator codeGenerator;

    @GetMapping("/checkEmployees/{empGroupCode}")
    public JsonResult checkEmployees(@PathVariable("empGroupCode") String empGroupCode){
        int rowAffected = employeeService.hasEmployees(empGroupCode);
        return JsonResult.success(rowAffected);
    }

    @PostMapping("/addNormalBatch")
    public JsonResult addNormalBatch(@RequestBody PrNormalBatchDTO batchDTO) {
        PrNormalBatchPO prNormalBatchPO = new PrNormalBatchPO();
        prNormalBatchPO.setAccountSetCode(batchDTO.getAccountSetCode());
        prNormalBatchPO.setManagementId(batchDTO.getManagementId());
        prNormalBatchPO.setManagementName(batchDTO.getManagementName());
        prNormalBatchPO.setPeriod(batchDTO.getPeriod());
        prNormalBatchPO.setStatus(BatchStatusEnum.NEW.getValue());
        prNormalBatchPO.setCreatedBy("bill");
        prNormalBatchPO.setModifiedBy("bill");

        PrPayrollAccountSetPO accountSetPO = accountSetService.getAccountSetInfo(batchDTO.getAccountSetCode());

        //计算薪资期间
        String actualPeriod = BatchUtils.getPeriod(batchDTO.getPeriod(),accountSetPO.getPayrollPeriod());
        String code = codeGenerator.genPrNormalBatchCode(batchDTO.getManagementId(),actualPeriod);
        prNormalBatchPO.setCode(code);
        prNormalBatchPO.setActualPeriod(actualPeriod);

        String [] dates = BatchUtils.getPRDates(batchDTO.getPeriod(),accountSetPO.getStartDay(),accountSetPO.getEndDay(),accountSetPO.getPayrollPeriod());
        prNormalBatchPO.setStartDate(dates[0]);
        prNormalBatchPO.setEndDate(dates[1]);

        System.out.println("normal batch code : "+ code);

        int result = 0;
        try {
            result = batchService.insert(prNormalBatchPO);
            if(result > 0){
                //send message to kafka
                PayrollMsg msg = new PayrollMsg();
                msg.setBatchCode(code);
                msg.setBatchType(BatchTypeEnum.NORMAL.getValue());
                msg.setOperateType(OperateTypeEnum.ADD.getValue());
                sender.Send(msg);

                return JsonResult.success(result);
            }
            else {
                return JsonResult.faultMessage("插入记录重复");
            }
        }
        catch (Exception e){
            return JsonResult.faultMessage("保持失败");
        }
    }

    @PostMapping("/getBatchList")
    public JsonResult getBatchList(@RequestBody PrCustomBatchDTO param){

        PrCustBatchPO custBatchPO = BathTranslator.toPrBatchPO(param);
        int pageNum = param.getPageNum() == 0 ? 1 : param.getPageNum();
        int pageSize = param.getPageSize() == 0 ? 50 : param.getPageSize();

        PageInfo<PrCustBatchPO> list = batchService.getList(custBatchPO,pageNum,pageSize);
        return JsonResult.success(list);

    }

    @PostMapping("/updateNormalBatch")
    public JsonResult updateNormalBatch(@RequestBody PrNormalBatchDTO batchDTO){

        PrNormalBatchPO prNormalBatchPO = new PrNormalBatchPO();
        prNormalBatchPO.setAccountSetCode(batchDTO.getAccountSetCode());
        prNormalBatchPO.setManagementId(batchDTO.getManagementId());
        prNormalBatchPO.setManagementName(batchDTO.getManagementName());
        prNormalBatchPO.setPeriod(batchDTO.getPeriod());
        prNormalBatchPO.setCode(batchDTO.getCode());
        prNormalBatchPO.setModifiedBy("bill");

        PrPayrollAccountSetPO accountSetPO = accountSetService.getAccountSetInfo(batchDTO.getAccountSetCode());

        //计算薪资期间
        String actualPeriod = BatchUtils.getPeriod(batchDTO.getPeriod(),accountSetPO.getPayrollPeriod());
        prNormalBatchPO.setActualPeriod(actualPeriod);

        String [] dates = BatchUtils.getPRDates(batchDTO.getPeriod(),accountSetPO.getStartDay(),accountSetPO.getEndDay(),accountSetPO.getPayrollPeriod());
        prNormalBatchPO.setStartDate(dates[0]);
        prNormalBatchPO.setEndDate(dates[1]);

        int result = batchService.update(prNormalBatchPO);

        if(result > 0) {
            return JsonResult.success("更新成功");
        }else {
            return JsonResult.faultMessage("更新失败");
        }
    }

    @GetMapping("/getSubBatchList")
    public JsonResult getSubBatchList(@RequestParam String code){
        List<PrCustSubBatchPO> list = batchService.selectSubBatchList(code);
        return JsonResult.success(list);
    }

    @PostMapping("/uploadExcel")
    public JsonResult importExcel(String batchCode, String empGroupCode, int batchType, int importType, MultipartFile file){

        int sucessRows = batchService.uploadEmpPRItemsByExcel(batchCode, empGroupCode,batchType,importType,file);
        if(sucessRows == -1){
            return JsonResult.faultMessage("雇员组中已有雇员，不能用于覆盖导入");
        }
        return JsonResult.success(sucessRows);

    }

    @DeleteMapping("/deleteBatch/{codes}")
    public JsonResult deleteBatch(@PathVariable("codes") String batchCodes) {
        String[] codes = batchCodes.split(",");
        int i = batchService.deleteBatchByCodes(Arrays.asList(codes));
        if (i >= 1){
                //send message to kafka
            PayrollMsg msg = new PayrollMsg();
            msg.setBatchCode(batchCodes);
            msg.setBatchType(BatchTypeEnum.NORMAL.getValue());
            msg.setOperateType(OperateTypeEnum.DELETE.getValue());
            sender.Send(msg);

            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }


    /**
     * 从雇员组中过滤雇员列表
     * @param filterDTO
     * @return
     */
    @PostMapping("/filterEmployees")
    public JsonResult getEmployeeListByBatchCode(@RequestBody EmpFilterDTO filterDTO){

        long start = System.currentTimeMillis(); //begin

        String batchCode = filterDTO.getBatchCode();
        String empCodes = filterDTO.getEmpCodes();
        if(StringUtils.isEmpty(empCodes)){ // 该雇员组没有雇员
            return JsonResult.success(0,"");
        }else if(empCodes.equals("all")){
            normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is(batchCode), "catalog.emp_info.is_active", false);
            int rowAffected = normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is(batchCode),"catalog.emp_info.is_active",true);
            logger.info("update all " + String.valueOf(rowAffected));
            return JsonResult.success(rowAffected,"更新成功");

        }else {
            int rowAffected1 = normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is(batchCode), "catalog.emp_info.is_active", false);

            logger.info("update all " + String.valueOf(rowAffected1));

            String[] codes = empCodes.split(",");
            List<String> codeList = Arrays.asList(codes);

            int rowAffected = normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_CODE_CN).in(codeList), "catalog.emp_info.is_active", true);
            logger.info("update specific " + String.valueOf(rowAffected));

            long end = System.currentTimeMillis();
            logger.info("filterEmployees cost time : " + String.valueOf((end - start)));


            return JsonResult.success(rowAffected, "更新成功");
        }

    }

    /**
     * 从mongodb，根据批次号，获取雇员列表
     * @param pageNum
     * @param pageSize
     * @param batchCode
     * @return
     */
    @PostMapping("/getFilterEmployees/{batchCode}")
    public JsonResult getFilterEmployees(
                                    @RequestParam(required = false, defaultValue = "") String empCode,
                                    @RequestParam(required = false, defaultValue = "") String empName,
                                    @RequestParam(required = false, defaultValue = "") String customKey,
                                    @RequestParam(required = false, defaultValue = "") String customValue,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
                                    @PathVariable("batchCode") String batchCode){

        long start = System.currentTimeMillis(); //begin

        Criteria criteria = Criteria.where("batch_code").is(batchCode);//.and("catalog.emp_info.is_active").is(true);

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

        long totalCount = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,normalBatchMongoOpt.PR_NORMAL_BATCH).stream().count();
        if(totalCount == 0){
            return JsonResult.success(0);
        }

        long begin = System.currentTimeMillis(); //begin 5秒
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

        List<DBObject> list = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,normalBatchMongoOpt.PR_NORMAL_BATCH);

        //List<DBObject> list = normalBatchMongoOpt.list(criteria).stream().skip((pageNum-1) * pageSize).limit(pageSize).collect(Collectors.toList());

        logger.info("获取翻页时间 : " + String.valueOf((System.currentTimeMillis() - start)));

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

        long end = System.currentTimeMillis();
        logger.info("getFilterEmployees cost time : " + String.valueOf((end - start)));

        return JsonResult.success(result);

    }

    @PostMapping("/doCompute")
    public JsonResult doComputeAction(@RequestParam String batchCode, @RequestParam int batchType){
        try {
            if(batchType == BatchTypeEnum.NORMAL.getValue()) {
                batchService.auditBatch(batchCode, "", BatchStatusEnum.COMPUTING.getValue(), "bill",""); //TODO
            }else if(batchType == BatchTypeEnum.ADJUST.getValue()){
                adjustBatchService.auditBatch(batchCode,"", BatchStatusEnum.COMPUTING.getValue(), "bill","");
            }else if(batchType == BatchTypeEnum.BACK.getValue()){
                backTrackingBatchService.auditBatch(batchCode,"", BatchStatusEnum.COMPUTING.getValue(), "bill", "");
            }
            // 发送薪资计算消息到kafka
            ComputeMsg computeMsg = new ComputeMsg();
            computeMsg.setBatchCode(batchCode);
            sender.SendComputeAction(computeMsg);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.faultMessage("发送计算任务失败");
        }
        return JsonResult.success("发送计算任务成功");

    }

    @PostMapping("/auditBatch")
    public JsonResult auditBatch(@RequestBody BatchAuditDTO batchAuditDTO){
        String modifiedBy = "bill"; //TODO
        int rowAffected = 0 ;
        if(batchAuditDTO.getBatchType() == BatchTypeEnum.NORMAL.getValue()) {
            if (batchAuditDTO.getStatus() == BatchStatusEnum.APPROVAL.getValue() || batchAuditDTO.getStatus() == BatchStatusEnum.CLOSED.getValue()) {
                List<DBObject> results = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchAuditDTO.getBatchCode()).and("catalog.emp_info.is_active").is(true));
                String jsonReuslt = JSON.serialize(results);
                batchAuditDTO.setResult(jsonReuslt);
            }
            rowAffected = batchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), modifiedBy, batchAuditDTO.getResult());
        }else if(batchAuditDTO.getBatchType() == BatchTypeEnum.ADJUST.getValue()) {
            if (batchAuditDTO.getStatus() == BatchStatusEnum.APPROVAL.getValue() || batchAuditDTO.getStatus() == BatchStatusEnum.CLOSED.getValue()) {
                List<DBObject> results = adjustBatchMongoOpt.list(Criteria.where("batch_code").is(batchAuditDTO.getBatchCode()).and("catalog.emp_info.is_active").is(true));
                String jsonReuslt = JSON.serialize(results);
                batchAuditDTO.setResult(jsonReuslt);
            }
            rowAffected = adjustBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), modifiedBy, batchAuditDTO.getResult());
        }else {
            if (batchAuditDTO.getStatus() == BatchStatusEnum.APPROVAL.getValue() || batchAuditDTO.getStatus() == BatchStatusEnum.CLOSED.getValue()) {
                List<DBObject> results = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(batchAuditDTO.getBatchCode()).and("catalog.emp_info.is_active").is(true));
                String jsonReuslt = JSON.serialize(results);
                batchAuditDTO.setResult(jsonReuslt);
            }
            rowAffected = backTrackingBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), modifiedBy, batchAuditDTO.getResult());

        }
        if(rowAffected > 0) {
            return JsonResult.success(rowAffected);
        }
        else {
            return JsonResult.faultMessage("更新失败");
        }
    }

    @GetMapping(value = "/downLoad")
    public void downLoadOtherBatchImportFile(@RequestParam String batchCode, @RequestParam int batchType, HttpServletResponse response) {

        List<DBObject> dbObjects = null;
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            dbObjects = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("catalog.emp_info.is_active").is(true));
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            dbObjects = adjustBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("catalog.emp_info.is_active").is(true));
        }else {
            dbObjects = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("catalog.emp_info.is_active").is(true));
        }

        List<ExcelExportEntity> excelExportEntities = new ArrayList<ExcelExportEntity>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        int count = 0;
        for (DBObject dbObject: dbObjects) {

            DBObject catalog = (DBObject) dbObject.get("catalog");

            List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");

            HashMap<String, Object> map = payItems.stream().collect(
                    HashMap<String, Object>::new,
                    (m,c) -> m.put((String)c.get("item_name"), c.get("item_value")),
                    (m,u)-> {}
            );
            list.add(map);
            count ++;
            if(count == 1) {
                payItems.stream().forEach(item -> {
                    excelExportEntities.add(new ExcelExportEntity((String) item.get("item_name"), item.get("item_name")));
                });
            }
        }

        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("计算结果");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, excelExportEntities, list);

        if (workbook != null);{
            downLoadExcel("计算结果.xlsx", response, workbook);
        }
    }

    private  void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/clearBatchField")
    public JsonResult clearBatchField(@RequestParam String batchCode, @RequestParam String empCodes, @RequestParam String itemNames, @RequestParam int batchType){
        int rowAffected = 0;

        List<String> empCodeList = Arrays.asList(empCodes.split(","));
        List<String> itemNameList = Arrays.asList(itemNames.split(","));

        List<DBObject> findObjs = null;

        for (String payItemName: itemNameList) {

            Criteria numQuery = Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).in(empCodeList),
                            Criteria.where("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName).and("data_type").is(DataTypeEnum.NUM.getValue()))
                    );

            Criteria strQuery = Criteria.where("batch_code").is(batchCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).in(empCodeList),
                            Criteria.where("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName))
                    );

            if (batchType == BatchTypeEnum.NORMAL.getValue()) {

                findObjs = normalBatchMongoOpt.list(numQuery);
                if(findObjs == null || findObjs.size() == 0){
                    rowAffected += normalBatchMongoOpt.batchUpdate(strQuery,"catalog.pay_items.$.item_value", "");
                }else {
                    rowAffected += normalBatchMongoOpt.batchUpdate(numQuery,"catalog.pay_items.$.item_value", 0);
                }

            } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
                findObjs = adjustBatchMongoOpt.list(numQuery);
                if(findObjs == null || findObjs.size() == 0){
                    rowAffected += adjustBatchMongoOpt.batchUpdate(strQuery,"catalog.pay_items.$.item_value", "");
                }else {
                    rowAffected += adjustBatchMongoOpt.batchUpdate(numQuery,"catalog.pay_items.$.item_value", 0);
                }

            } else {
                findObjs = backTraceBatchMongoOpt.list(numQuery);
                if(findObjs == null){
                    rowAffected += backTraceBatchMongoOpt.batchUpdate(strQuery,"catalog.pay_items.$.item_value", "");
                }else {
                    rowAffected += backTraceBatchMongoOpt.batchUpdate(numQuery,"catalog.pay_items.$.item_value", 0);
                }
            }
        }

        return JsonResult.success(rowAffected);

    }
}
