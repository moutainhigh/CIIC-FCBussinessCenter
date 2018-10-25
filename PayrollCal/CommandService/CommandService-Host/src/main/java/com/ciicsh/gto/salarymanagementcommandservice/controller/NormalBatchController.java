package com.ciicsh.gto.salarymanagementcommandservice.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.*;
import com.ciicsh.gto.salarymanagement.entity.bo.ExcelUploadStatistics;
import com.ciicsh.gto.salarymanagement.entity.dto.ExcelMapDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.PrBatchExcelMapDTO;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import com.ciicsh.gto.salarymanagementcommandservice.translator.BathTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.BatchUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private TestBatchMongoOpt testBatchMongoOpt;

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

    @Autowired
    private CommonServiceImpl commonService;

    @Autowired
    private PrItemService itemService;

    @Autowired
    private PrBatchExcelMapService excelMapService;

    @Autowired
    private FCBizTransactionMongoOpt fcBizTransactionMongoOpt;


    @GetMapping("/checkEmployees/{empGroupCode}")
    public JsonResult checkEmployees(@PathVariable("empGroupCode") String empGroupCode) {
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
        prNormalBatchPO.setCreatedBy(UserContext.getUserId());
        prNormalBatchPO.setModifiedBy(UserContext.getUserId());
        Integer isTestBatch = batchDTO.getIsTestBatch(); //表示是否为测试批次：1 表示 是； 0 表示 否；
        prNormalBatchPO.setIsTestBatch(isTestBatch);
        // 批次类型 1 正常批次, 2 调整批次, 3 回溯批次, 4 测试批次, 5 导入批次;
        int batchType = batchDTO.getBatchType();
        // 源批次code
        String originBatchCode = batchDTO.getCode();

        PrPayrollAccountSetPO accountSetPO = accountSetService.getAccountSetInfo(batchDTO.getAccountSetCode());

        //计算薪资期间
        String actualPeriod = BatchUtils.getPeriod(batchDTO.getPeriod(), accountSetPO.getPayrollPeriod());
        String code = codeGenerator.genPrNormalBatchCode(batchDTO.getManagementId(), actualPeriod);
        prNormalBatchPO.setCode(code);
        prNormalBatchPO.setActualPeriod(actualPeriod);

        String[] dates = BatchUtils.getPRDates(
                batchDTO.getPeriod(),
                accountSetPO.getStartDay(),
                accountSetPO.getEndDay(),
                accountSetPO.getPayrollPeriod());
        prNormalBatchPO.setStartDate(dates[0]);
        prNormalBatchPO.setEndDate(dates[1]);

        int result;
        try {
            result = batchService.insert(prNormalBatchPO);
            if (result > 0) {
                //send message to kafka
                PayrollMsg msg = new PayrollMsg();
                // 新建批次生成的batchCode
                msg.setBatchCode(code);
                // 源批次Code, 导入批次时需要; 新建批次时不需要此字段,为默认值
                msg.setOriginBatchCode(originBatchCode);
                // 批次类型 1 正常批次, 2 调整批次, 3 回溯批次, 4 测试批次, 5 导入批次;
                msg.setBatchType(
                        isTestBatch == 1 ? BatchTypeEnum.Test.getValue()
                                : (batchType == BatchTypeEnum.IMPORT.getValue() ? BatchTypeEnum.IMPORT.getValue()
                                        : BatchTypeEnum.NORMAL.getValue()));
                // 操作类型 1 增加, 2 更新, 3 删除, 4 查询, 5 导入;
                msg.setOperateType(batchType == OperateTypeEnum.IMPORT.getValue() ? OperateTypeEnum.IMPORT.getValue()
                        : OperateTypeEnum.ADD.getValue());

                sender.Send(msg);
                logger.info("新增/导入薪资帐套：{}", msg.toString());

                return JsonResult.success(result);
            } else {
                return JsonResult.faultMessage("插入记录重复");
            }
        } catch (Exception e) {
            return JsonResult.faultMessage("保存失败");
        }
    }

    @RequestMapping("/getNormalBatch")
    public JsonResult getNormalBatch(@RequestParam String batchCode) {
        PrNormalBatchPO prNormalBatchPO = new PrNormalBatchPO();
        prNormalBatchPO.setCode(batchCode);
        PrCustBatchPO prCustBatchPO = batchService.getCustBatchInfo(batchCode);
        return JsonResult.success(prCustBatchPO);
    }

    @PostMapping("/getBatchList")
    public JsonResult getBatchList(@RequestBody PrCustomBatchDTO param) {

        String managementIds = CommonHelper.getManagementIDs();
        param.setManagementId(managementIds);

        PrCustBatchPO custBatchPO = BathTranslator.toPrBatchPO(param);
        int pageNum = param.getPageNum() == 0 ? 1 : param.getPageNum();
        int pageSize = param.getPageSize() == 0 ? 50 : param.getPageSize();

        PageInfo<PrCustBatchPO> list = batchService.getList(custBatchPO, pageNum, pageSize);
        return JsonResult.success(list);

    }

    /**
     * 查询所有对比的批次列表
     * @param prCompareBatchDTO
     * @return
     */
    @PostMapping("/getCompareBatchList")
    public JsonResult getCompareBatchList(@RequestBody PrCompareBatchDTO prCompareBatchDTO) {
        String managementIds = CommonHelper.getManagementIDs();
        prCompareBatchDTO.setManagementId(managementIds);

        PrCompareBatchPO prCompareBatchPO = BathTranslator.toPrCompareBatchPO(prCompareBatchDTO);
        PageInfo<PrCompareBatchPO> compareBatchList = batchService.selectCompareBatchList(prCompareBatchPO);
        return JsonResult.success(compareBatchList);

    }

    @PostMapping("/updateNormalBatch")
    public JsonResult updateNormalBatch(@RequestBody PrNormalBatchDTO batchDTO) {

        PrNormalBatchPO prNormalBatchPO = new PrNormalBatchPO();
        prNormalBatchPO.setAccountSetCode(batchDTO.getAccountSetCode());
        prNormalBatchPO.setManagementId(batchDTO.getManagementId());
        prNormalBatchPO.setManagementName(batchDTO.getManagementName());
        prNormalBatchPO.setPeriod(batchDTO.getPeriod());
        prNormalBatchPO.setCode(batchDTO.getCode());
        prNormalBatchPO.setModifiedBy(UserContext.getName());

        PrPayrollAccountSetPO accountSetPO = accountSetService.getAccountSetInfo(batchDTO.getAccountSetCode());

        //计算薪资期间
        String actualPeriod = BatchUtils.getPeriod(batchDTO.getPeriod(), accountSetPO.getPayrollPeriod());
        prNormalBatchPO.setActualPeriod(actualPeriod);

        String[] dates = BatchUtils.getPRDates(batchDTO.getPeriod(), accountSetPO.getStartDay(), accountSetPO.getEndDay(), accountSetPO.getPayrollPeriod());
        prNormalBatchPO.setStartDate(dates[0]);
        prNormalBatchPO.setEndDate(dates[1]);

        int result = batchService.update(prNormalBatchPO);

        if (result > 0) {
            return JsonResult.success("更新成功");
        } else {
            return JsonResult.faultMessage("更新失败");
        }
    }

    @GetMapping("/getSubBatchList")
    public JsonResult getSubBatchList(@RequestParam String code) {
        List<PrCustSubBatchPO> list = batchService.selectSubBatchList(code);
        return JsonResult.success(list);
    }

    @PostMapping("/uploadExcel")
    public JsonResult importExcel(String batchCode, String empGroupCode, int batchType, int importType, MultipartFile file) {

        try {
            ExcelUploadStatistics statistics = batchService.uploadEmpPRItemsByExcel(batchCode, empGroupCode, batchType, importType, file);
            return JsonResult.success(statistics);
        } catch (BusinessException ex) {
            return JsonResult.faultMessage(ex.getMessage());
        }

    }

    @DeleteMapping("/deleteBatch/{codes}")
    public JsonResult deleteBatch(@PathVariable("codes") String batchCodes) {
        String[] codes = batchCodes.split(",");
        int i = batchService.deleteBatchByCodes(Arrays.asList(codes));
        if (i >= 1) {
            //send message to kafka
            PayrollMsg msg = new PayrollMsg();
            msg.setBatchCode(batchCodes);
            msg.setBatchType(BatchTypeEnum.NORMAL.getValue());
            msg.setOperateType(OperateTypeEnum.DELETE.getValue());
            sender.Send(msg);

            return JsonResult.success(i, "删除成功");
        } else {
            return JsonResult.faultMessage();
        }
    }

    /**
     * 从mongodb，根据批次号，获取雇员列表
     *
     * @param pageNum
     * @param pageSize
     * @param batchCode
     * @return
     */
    @PostMapping("/getEmployees/{batchCode}/{batchType}")
    public JsonResult getFilterEmployees(
            @RequestParam(required = false, defaultValue = "") String empCode,
            @RequestParam(required = false, defaultValue = "") String empName,
            @RequestParam(required = false, defaultValue = "") String customKey,   //格式：字段名称,字段类型
            @RequestParam(required = false, defaultValue = "") String customValue,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "50") Integer pageSize,
            @PathVariable("batchCode") String batchCode,
            @PathVariable("batchType") Integer batchType) {

        long start = System.currentTimeMillis(); //begin

        Criteria criteria = Criteria.where("batch_code").is(batchCode);

        if (StringUtils.isNotEmpty(empCode)) {
            criteria.and(PayItemName.EMPLOYEE_CODE_CN).regex(empCode);
        }
        if (StringUtils.isNotEmpty(empName)) {
            criteria.and("catalog.emp_info." + PayItemName.EMPLOYEE_NAME_CN).regex(empName);
        }
        if (StringUtils.isNotEmpty(customKey) && StringUtils.isNotEmpty(customValue)) {
            String fieldName = customKey.split(",")[0];
            String dataType = customKey.split(",")[1];
            if (dataType.equals(String.valueOf(DataTypeEnum.NUM.getValue()))) {
                double val = Double.parseDouble(customValue);
                criteria.and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(fieldName).and("item_value").is(val));
            } else {
                criteria.and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(fieldName).and("item_value").regex(customValue));
            }
        }
        Query query = new Query(criteria);
        query.fields().include("batch_code");

        long totalCount;
        if (batchType == 4) {
            //测试批次时
            totalCount = testBatchMongoOpt.getMongoTemplate().find(query, DBObject.class, TestBatchMongoOpt.PR_TEST_BATCH).stream().count();
        } else {
            totalCount = normalBatchMongoOpt.getMongoTemplate().find(query, DBObject.class, NormalBatchMongoOpt.PR_NORMAL_BATCH).stream().count();
        }
        if (totalCount == 0) {
            return JsonResult.success(0);
        }

        long begin = System.currentTimeMillis(); //begin
        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN).
                include(PayItemName.EMPLOYEE_COMPANY_ID).
                include("catalog.emp_info." + PayItemName.EMPLOYEE_NAME_CN).
                include("catalog.pay_items.data_type").
                include("catalog.pay_items.canLock").
                include("catalog.pay_items.isLocked").
                include("catalog.pay_items.item_type").
                include("catalog.pay_items.item_name").
                include("catalog.pay_items.item_value").
                include("catalog.pay_items.display_priority").
                include("catalog.pay_items.is_show")
        ;
        query.skip((pageNum - 1) * pageSize);
        query.limit(pageSize);

        List<DBObject> list;
        if (batchType == 4) {
            //测试批次时
            list = testBatchMongoOpt.getMongoTemplate().find(query, DBObject.class, TestBatchMongoOpt.PR_TEST_BATCH);
        } else {
            list = normalBatchMongoOpt.getMongoTemplate().find(query, DBObject.class, NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }
        if (list.size() == 1) { // 如果有一条纪录，但 emp_info 为 "" 时，说明雇员组没有雇员
            DBObject checkEmpInfo = list.get(0);
            DBObject catalog = (DBObject) checkEmpInfo.get("catalog");
            if (catalog.get("emp_info") == null) {
                return JsonResult.success(0);
            }
        }
        //List<DBObject> list = normalBatchMongoOpt.list(criteria).stream().skip((pageNum-1) * pageSize).limit(pageSize).collect(Collectors.toList());

        logger.info("获取翻页时间 : " + String.valueOf((System.currentTimeMillis() - start)));
        List<SimpleEmpPayItemDTO> simplePayItemDTOS = commonService.translate(list);
        PageInfo<SimpleEmpPayItemDTO> result = new PageInfo<>(simplePayItemDTOS);
        result.setTotal(totalCount);
        result.setPageNum(pageNum);

        long end = System.currentTimeMillis();
        logger.info("getFilterEmployees cost time : " + String.valueOf((end - start)));

        return JsonResult.success(result);

    }

    @PostMapping("/updateEmployees")
    public JsonResult updateEmployees(@RequestBody List<PrEmployeeTestDTO> employeeTestDTOS,
                                      @RequestParam String batchCode,
                                      @RequestParam int batchType) {
        if (batchType != BatchTypeEnum.NORMAL.getValue()) {
            return JsonResult.faultMessage("不能添加雇员");
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<DBObject> employees = employeeTestDTOS.stream().map(employee -> {
            DBObject emp = new BasicDBObject();
            emp.put(PayItemName.EMPLOYEE_NAME_CN, employee.getEmployeeName());
            emp.put(PayItemName.EMPLOYEE_CODE_CN, employee.getEmployeeId());
            emp.put(PayItemName.EMPLOYEE_BIRTHDAY_CN, formatter.format(employee.getBirthday()));
            emp.put(PayItemName.EMPLOYEE_SEX_CN, employee.getGender().equals(Boolean.TRUE) ? "男" : "女");
            emp.put(PayItemName.EMPLOYEE_ID_TYPE_CN, employee.getIdCardType());
            emp.put(PayItemName.EMPLOYEE_ONBOARD_CN, formatter.format(employee.getJoinDate()));
            emp.put(PayItemName.EMPLOYEE_ID_NUM_CN, employee.getIdNum());
            emp.put(PayItemName.EMPLOYEE_POSITION_CN, employee.getPosition());
            emp.put(PayItemName.EMPLOYEE_FORMER_CN, employee.getFormerName());

            emp.put(PayItemName.EMPLOYEE_COUNTRY_CODE_CN, employee.getCountryCode());
            emp.put(PayItemName.EMPLOYEE_PROVINCE_CODE_CN, employee.getProvinceCode());
            emp.put(PayItemName.EMPLOYEE_CITY_CODE_CN, employee.getCityCode());

            //emp.put(PayItemName.ACTUAL_SALARY)


            return emp;
        }).collect(Collectors.toList());

        //批次类型为0，表示非测试批次
        int rowAffected = commonService.batchInsertOrUpdateNormalBatch(batchCode, 0, employees);
        if (rowAffected > 0) {
            return JsonResult.success(rowAffected, "添加雇员成功");
        } else {
            return JsonResult.success(-1, "添加雇员失败");
        }

    }

    @PostMapping("/deleteEmps")
    public JsonResult deleteEmps(@RequestParam String batchCode, @RequestParam String employeeIds, @RequestParam int batchType) {
        String[] empIDs = employeeIds.split(",");
        int rowAffected = 0;
        if (batchType == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = normalBatchMongoOpt.batchDelete(Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_CODE_CN).in(Arrays.asList(empIDs)));
        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchMongoOpt.batchDelete(Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_CODE_CN).in(Arrays.asList(empIDs)));
        } else {
            rowAffected = backTraceBatchMongoOpt.batchDelete(Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_CODE_CN).in(Arrays.asList(empIDs)));
        }
        if (rowAffected > 0) {
            return JsonResult.success(rowAffected, "删除成功");
        } else {
            return JsonResult.success("删除失败");
        }
    }

    @PostMapping("/api/getBatchStatus")
    public JsonResult getBatchStatus(@RequestParam String batchCode, @RequestParam int batchType) {
        Boolean result = false;

        int status = fcBizTransactionMongoOpt.getTransactionStatus(batchCode);
        /*
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            PrNormalBatchPO normalBatchPO = batchService.getBatchByCode(batchCode);
            result = normalBatchPO.getStatus() >= BatchStatusEnum.ISSUED.getValue();
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            PrAdjustBatchPO adjustBatchPO = adjustBatchService.getAdjustBatchPO(batchCode);
            result = adjustBatchPO.getStatus() >= BatchStatusEnum.ISSUED.getValue();
        }else {
            PrBackTrackingBatchPO backTrackingBatchPO = backTrackingBatchService.getPrBackTrackingBatchPO(batchCode);
            result = backTrackingBatchPO.getStatus() >= BatchStatusEnum.ISSUED.getValue();
        }*/
        if (status > 0) {
            return JsonResult.faultMessage("该批次不能取消关帐，请联系相关人员处理!");
        } else {
            return JsonResult.success("可以取消关帐");
        }
    }

    @PostMapping("/auditBatch")
    public JsonResult auditBatch(@RequestBody BatchAuditDTO batchAuditDTO) {
        String modifiedBy = UserContext.getUserId();
        int rowAffected = 0;
        if (batchAuditDTO.getBatchType() == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = batchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), modifiedBy, "", batchAuditDTO.getResult());
        } else if (batchAuditDTO.getBatchType() == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), modifiedBy, "", batchAuditDTO.getResult());

        } else {
            rowAffected = backTrackingBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), modifiedBy, "", batchAuditDTO.getResult());
        }
        if (rowAffected > 0) {
            if (StringUtils.isNotEmpty(batchAuditDTO.getAction()) && batchAuditDTO.getStatus() == BatchStatusEnum.APPROVAL.getValue()) { //取消关帐通知

                CancelClosingMsg cancelClosingMsg = new CancelClosingMsg();
                cancelClosingMsg.setBatchType(batchAuditDTO.getBatchType());
                cancelClosingMsg.setOptID(UserContext.getUserId());
                cancelClosingMsg.setOptName(UserContext.getName());
                cancelClosingMsg.setBatchCode(batchAuditDTO.getBatchCode());

                long version = commonService.getBatchVersion(batchAuditDTO.getBatchCode());
                cancelClosingMsg.setVersion(version);


                logger.info("发送取消关帐通知给各个业务部门 : " + cancelClosingMsg.toString());
                sender.SendComputeUnClose(cancelClosingMsg);

            } else if (batchAuditDTO.getStatus() == BatchStatusEnum.CLOSED.getValue()) { //关帐通知

                ComputeMsg computeMsg = new ComputeMsg();
                computeMsg.setBatchType(batchAuditDTO.getBatchType());
                computeMsg.setComputeStatus(BatchStatusEnum.CLOSED.getValue());
                computeMsg.setBatchCode(batchAuditDTO.getBatchCode());
                computeMsg.setOptID(UserContext.getUserId());
                computeMsg.setOptName(UserContext.getName());

                logger.info("关帐通知给 job sync : " + computeMsg.toString());
                sender.SendComputeCompleteAction(computeMsg);
            }
            return JsonResult.success(rowAffected);
        } else {
            return JsonResult.faultMessage("更新失败");
        }
    }

    @GetMapping(value = "/downLoad")
    public void downLoadOtherBatchImportFile(@RequestParam String batchCode, @RequestParam int batchType, @RequestParam(required = false) String token, HttpServletResponse response) {

        List<DBObject> dbObjects = null;
        if (batchType == BatchTypeEnum.NORMAL.getValue()) {
            dbObjects = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode));
        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
            dbObjects = adjustBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode));
        } else {
            dbObjects = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode));
        }

        List<ExcelExportEntity> excelExportEntities = new ArrayList<ExcelExportEntity>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        int count = 0;
        for (DBObject dbObject : dbObjects) {

            DBObject catalog = (DBObject) dbObject.get("catalog");

            List<DBObject> payItems = (List<DBObject>) catalog.get("pay_items");

            HashMap<String, Object> map = payItems.stream().collect(
                    HashMap<String, Object>::new,
                    (m, c) -> m.put((String) c.get("item_name"), c.get("item_value")),
                    (m, u) -> {
                    }
            );
            list.add(map);
            count++;
            if (count == 1) {
                payItems.forEach(item -> {
                    excelExportEntities.add(new ExcelExportEntity((String) item.get("item_name"), item.get("item_name")));
                });
            }
        }

        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("计算结果");
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, excelExportEntities, list);

        if (workbook != null) ;
        {
            downLoadExcel("计算结果.xlsx", response, workbook);
        }
    }

    private void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
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
    public JsonResult clearBatchField(@RequestParam String batchCode, @RequestParam String empCodes, @RequestParam String itemNames, @RequestParam int batchType) {
        int rowAffected = 0;
        List<String> empCompanyList = null;
        List<String> itemNameList = null;
        if (StringUtils.isNotEmpty(empCodes)) {
            empCompanyList = Arrays.asList(empCodes.split(","));
        }
        if (StringUtils.isNotEmpty(itemNames)) {
            itemNameList = Arrays.asList(itemNames.split(","));
        }

        if (empCompanyList == null) {
            rowAffected = update(batchType, batchCode, null, null, itemNameList);

        } else {
            for (String empCompany : empCompanyList) {
                String empCode = empCompany.split("-")[0];
                String companyId = empCompany.split("-")[1];
                //criteria = criteria.and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId).and(PayItemName.EMPLOYEE_CODE_CN).is(empCode);
                rowAffected = update(batchType, batchCode, empCode, companyId, itemNameList);
            }
        }

        return JsonResult.success(rowAffected);

    }

    private int update(int batchType, String batchCode, String empCode, String companyId, List<String> itemNameList) {
        int rowAffected = 0;
        Criteria numQuery = null;
        Criteria strQuery = null;

        if (itemNameList == null) {
            if (StringUtils.isEmpty(empCode)) {
                numQuery = Criteria.where("batch_code").is(batchCode).and("catalog.pay_items").elemMatch(Criteria.where("data_type").is(DataTypeEnum.NUM.getValue()).and("item_name").nin(PayItemName.EMPLOYEE_CODE_CN, PayItemName.EMPLOYEE_COMPANY_ID, PayItemName.EMPLOYEE_NAME_CN));
                strQuery = Criteria.where("batch_code").is(batchCode).and("catalog.pay_items").elemMatch(Criteria.where("data_type").nin(DataTypeEnum.NUM.getValue()).and("item_name").nin(PayItemName.EMPLOYEE_CODE_CN, PayItemName.EMPLOYEE_COMPANY_ID, PayItemName.EMPLOYEE_NAME_CN));
            } else {
                numQuery = Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId).and(PayItemName.EMPLOYEE_CODE_CN).is(empCode).and("catalog.pay_items").elemMatch(Criteria.where("data_type").is(DataTypeEnum.NUM.getValue()).and("item_name").nin(PayItemName.EMPLOYEE_CODE_CN, PayItemName.EMPLOYEE_COMPANY_ID, PayItemName.EMPLOYEE_NAME_CN));
                strQuery = Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId).and(PayItemName.EMPLOYEE_CODE_CN).is(empCode).and("catalog.pay_items").elemMatch(Criteria.where("data_type").nin(DataTypeEnum.NUM.getValue()).and("item_name").nin(PayItemName.EMPLOYEE_CODE_CN, PayItemName.EMPLOYEE_COMPANY_ID, PayItemName.EMPLOYEE_NAME_CN));
            }
            rowAffected = updateBatchMongo(batchType, numQuery, strQuery);

        } else {
            for (String payItemName : itemNameList) {
                if (StringUtils.isEmpty(empCode)) {
                    numQuery = Criteria.where("batch_code").is(batchCode).and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName).and("data_type").is(DataTypeEnum.NUM.getValue()));
                    strQuery = Criteria.where("batch_code").is(batchCode).and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName).and("data_type").nin(DataTypeEnum.NUM.getValue()));
                } else {
                    numQuery = Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId).and(PayItemName.EMPLOYEE_CODE_CN).is(empCode).and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName).and("data_type").is(DataTypeEnum.NUM.getValue()));
                    strQuery = Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId).and(PayItemName.EMPLOYEE_CODE_CN).is(empCode).and("catalog.pay_items").elemMatch(Criteria.where("item_name").is(payItemName).and("data_type").nin(DataTypeEnum.NUM.getValue()));
                }
                rowAffected += updateBatchMongo(batchType, numQuery, strQuery);
            }
        }
        return rowAffected;
    }

    private int updateBatchMongo(int batchType, Criteria numQuery, Criteria strQuery) {
        int rowAffected = 0;
        if (batchType == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected += normalBatchMongoOpt.batchUpdate(strQuery, "catalog.pay_items.$.item_value", "");
            rowAffected += normalBatchMongoOpt.batchUpdate(numQuery, "catalog.pay_items.$.item_value", 0);

        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected += adjustBatchMongoOpt.batchUpdate(strQuery, "catalog.pay_items.$.item_value", "");
            rowAffected += adjustBatchMongoOpt.batchUpdate(numQuery, "catalog.pay_items.$.item_value", 0);

        } else {
            rowAffected += backTraceBatchMongoOpt.batchUpdate(strQuery, "catalog.pay_items.$.item_value", "");
            rowAffected += backTraceBatchMongoOpt.batchUpdate(numQuery, "catalog.pay_items.$.item_value", 0);
        }
        return rowAffected;
    }

    @PostMapping("/getHistoryBatchInfoList")
    public JsonResult getHistoryBatchInfoList() {
        List<String> Ids = UserContext.getManagementInfoLists().stream().map(p -> p.getManagementId()).collect(Collectors.toList());
        //ist<String> Ids = Arrays.asList(mgrIds.split(","));
        List<PrNormalBatchPO> normalBatchPOS = batchService.getHistoryBatchInfoList(Ids);
        if (normalBatchPOS == null || normalBatchPOS.size() == 0) {
            return JsonResult.faultMessage("批次列表为空！");
        }

        List<HistoryBatchDTO> historyBatchDTOS = normalBatchPOS.stream().map(p -> {
            HistoryBatchDTO historyBatchDTO = new HistoryBatchDTO();
            historyBatchDTO.setAccountSetCode(p.getAccountSetCode());
            historyBatchDTO.setNormalBatchCode(p.getCode());
            historyBatchDTO.setManagerId(p.getManagementId());
            return historyBatchDTO;
        }).collect(Collectors.toList());

        return JsonResult.success(historyBatchDTOS, "批次列表获取");
    }

    @PostMapping("api/uploadExcelCols")
    public JsonResult uploadExcelCols(String batchCode, MultipartFile file) {

        ExcelMapDTO mapDTO = new ExcelMapDTO();
        try {
            InputStream stream = file.getInputStream();
            String[] cols = batchService.readExcelColumns(batchCode, stream);
            mapDTO.setExcelCols(cols);
            String[] itemNames = itemService.selectItemNames(batchCode).stream().map(p -> p.getPayrollItemName()).toArray(String[]::new);
            mapDTO.setPayItemNames(itemNames);

        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }

        return JsonResult.success(mapDTO, "获取列和薪资项");
    }

    @PostMapping("api/updateExcelCols")
    public JsonResult updateExcelCols(@RequestBody PrBatchExcelMapDTO batchExcelMapDTO) {

        String user = UserContext.getUserId();
        PrBatchExcelMapPO batchExcelMapPO = excelMapService.getBatchExcelMap(batchExcelMapDTO.getBatchCode());
        int rowAffected = 0;
        if (batchExcelMapPO == null) {
            batchExcelMapPO = new PrBatchExcelMapPO();
            batchExcelMapPO.setBatchCode(batchExcelMapDTO.getBatchCode());
            batchExcelMapPO.setCreatedBy(user);
            batchExcelMapPO.setModifiedBy(user);
            batchExcelMapPO.setExcelCols(batchExcelMapDTO.getExcelCols());
            batchExcelMapPO.setMappingResult(batchExcelMapDTO.getMappingResult());
            batchExcelMapPO.setIdentityResult(batchExcelMapDTO.getIdentityResult());
            rowAffected = excelMapService.insert(batchExcelMapPO);
        } else {
            batchExcelMapPO.setModifiedBy(user);
            batchExcelMapPO.setExcelCols(batchExcelMapDTO.getExcelCols());
            batchExcelMapPO.setMappingResult(batchExcelMapDTO.getMappingResult());
            batchExcelMapPO.setIdentityResult(batchExcelMapDTO.getIdentityResult());
            rowAffected = excelMapService.update(batchExcelMapPO);
        }
        if (rowAffected > 0) {
            return JsonResult.success("更新成功");
        } else {
            return JsonResult.faultMessage("更新失败");
        }

    }

    @PostMapping("api/getExcelCols")
    public JsonResult getExcelCols(@RequestParam String batchCode) {
        PrBatchExcelMapPO batchExcelMapPO = excelMapService.getBatchExcelMap(batchCode);
        if (batchExcelMapPO == null) {
            return JsonResult.success(null);

        } else {
            PrBatchExcelMapDTO batchExcelMapDTO = new PrBatchExcelMapDTO();
            batchExcelMapDTO.setBatchCode(batchExcelMapPO.getBatchCode());
            batchExcelMapDTO.setExcelCols(batchExcelMapPO.getExcelCols());
            batchExcelMapDTO.setMappingResult(batchExcelMapPO.getMappingResult());
            batchExcelMapDTO.setIdentityResult(batchExcelMapPO.getIdentityResult());
            return JsonResult.success(batchExcelMapDTO);
        }

    }

    @PostMapping("api/deleteBatchExcel")
    public JsonResult deleteBatchExcel(@RequestParam String batchCode) {
        int rowAffected = excelMapService.deleteBatchExcel(batchCode);
        if (rowAffected > 0) {
            return JsonResult.success("删除成功");
        } else {
            return JsonResult.faultMessage("删除失败");
        }
    }

    @GetMapping("api/checkMapping")
    public JsonResult checkMapping(@RequestParam String batchCode) {
        PrBatchExcelMapPO batchExcelMapPO = excelMapService.getBatchExcelMap(batchCode);
        if (batchExcelMapPO == null) {
            return JsonResult.faultMessage("请先配置Excel映射关系!");
        } else {
            return JsonResult.success("");
        }

    }

}
