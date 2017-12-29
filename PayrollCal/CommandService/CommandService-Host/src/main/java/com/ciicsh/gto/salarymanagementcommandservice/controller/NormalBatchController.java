package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.fcsupportcenter.util.constants.PayItemName;
import com.ciicsh.gto.fcsupportcenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.fcsupportcenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.dto.EmpFilterDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrGroupTemplateServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.BathTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.BatchUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import com.ciicsh.gto.salarymanagementcommandservice.util.excel.PRItemExcelReader;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import com.github.pagehelper.PageInfo;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/6.
 */
@RestController
public class NormalBatchController {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchController.class);

    class MgrData{
        public String Code;
        public String Name;

        public MgrData(String code, String name){
            this.Code = code;
            this.Name = name;
        }
    }

    @Autowired
    private KafkaSender sender;

    @Autowired
    private PrNormalBatchService batchService;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private EmpGroupMongoOpt empGroupMongoOpt;

    @Autowired
    private PrAccountSetService accountSetService;

    @Autowired
    private PrGroupService prGroupService;

    @Autowired
    private PrItemService prItemService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PrGroupTemplateServiceImpl prGroupTemplateService;

    @Autowired
    private CodeGenerator codeGenerator;

    @GetMapping("/getMgrList")
    public JsonResult getMgrList(@RequestParam String query) {

        List<MgrData> datas = new ArrayList<>();
        MgrData data = null;
        data = new MgrData("glf-00090","微软中国（上海）");
        datas.add(data);

        data = new MgrData("glf-00091","微软中国（北京）");
        datas.add(data);

        data = new MgrData("glf-00092","微软中国（深圳）");
        datas.add(data);

        data = new MgrData("ymx-0001","亚马逊（上海）");
        datas.add(data);

        data = new MgrData("ymx-0002","亚马逊（北京）");
        datas.add(data);

        data = new MgrData("ymx-0003","亚马逊（深圳）");
        datas.add(data);

        List<MgrData> result = new ArrayList<>();
        result.clear();
        if(CommonUtils.isContainChinese(query)){
            datas.forEach(item ->{
                if(item.Name.indexOf(query) >-1){
                    result.add(item);
                }
            });
        }else {
            datas.forEach(item ->{
                if(item.Code.indexOf(query) >-1){
                    result.add(item);
                }
            });
        }
        //依赖第三方接口：获取管理方列表
        return JsonResult.success(result);
    }

    @PostMapping("/addNormalBatch")
    public JsonResult addNormalBatch(@RequestBody PrNormalBatchDTO batchDTO)
    {
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
    public JsonResult getBatchList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
                                   @RequestBody PrCustomBatchDTO param){

        PrCustBatchPO custBatchPO = BathTranslator.toPrBatchPO(param);
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
    public JsonResult getSubBatchList(@RequestParam String code, @RequestParam Integer status){
        List<PrCustSubBatchPO> list = batchService.selectSubBatchList(code,status);
        return JsonResult.success(list);
    }

    @PostMapping("/uploadExcel")
    public JsonResult importExcel(String pr_code, String pr_template_code, String emp_group_code,String batchCode, MultipartFile file){

        List<PrPayrollItemPO> prList = null;
        String groupCode = "";
        if(StringUtils.isNotEmpty(pr_code)){ // 薪资组编码存在
            prList = prItemService.getListByGroupCode(pr_code,0,0).getList();
            groupCode = pr_code;

        }else if(StringUtils.isNotEmpty(pr_template_code)) { // 薪资组模版编码存在
            prList = prItemService.getListByGroupTemplateCode(pr_code,0,0).getList();
            groupCode = pr_template_code;
        }

        // get employee list by group code from mongodb
        List<DBObject> empList = empGroupMongoOpt.list(Criteria.where("emp_group_code").is(emp_group_code));
        if(empList == null || empList.size() == 0){
            // 如果mongodb不存在，则从数据库里面读取，并更新到mongodb里面
            PageInfo<EmployeeExtensionPO> pageInfo =  employeeService.getEmployees(emp_group_code, 0,0);
            empList = new ArrayList<>();
            for (EmployeeExtensionPO item: pageInfo.getList()) {
                DBObject basicDBObject = new BasicDBObject();
                basicDBObject = new BasicDBObject();
                basicDBObject.put("emp_group_code",item.getEmpGroupCode());
                basicDBObject.put("雇员编号",item.getEmployeeId());
                basicDBObject.put("雇员姓名",item.getEmployeeName());
                basicDBObject.put("出生日期",item.getBirthday());
                basicDBObject.put("部门",item.getDepartment());
                basicDBObject.put("性别",item.getGender());
                basicDBObject.put("证件类型",item.getIdCardType());
                basicDBObject.put("入职日期",item.getJoinDate());
                basicDBObject.put("证件号码",item.getIdNum());
                basicDBObject.put("职位",item.getPosition());
                empList.add(basicDBObject);

                empGroupMongoOpt.batchInsert(empList);
                //end
            }
        }
        List<DBObject> results = new ArrayList<>();
        try {
            InputStream stream = file.getInputStream();//new FileInputStream("/Users/bill/Documents/样本数据.xls");
            PoiItemReader<List<PrPayrollItemPO>> reader = PRItemExcelReader.getPrGroupReader(stream, prList,empList);
            reader.open(new ExecutionContext());
            List<PrPayrollItemPO> row = null;
            DBObject dbObject = null;
            do {
                row = reader.read();
                if(row != null) {
                    dbObject = new BasicDBObject();
                    dbObject.put("batch_code", batchCode);
                    dbObject.put("emp_group_code", groupCode);
                    PrPayrollItemPO itemPO = row.stream().filter(item -> item.getItemName().equals(PayItemName.EMPLOYEE_CODE_CN)).collect(Collectors.toList()).get(0);
                    dbObject.put("employee_id", itemPO.getItemValue());
                    dbObject.put("pay_items", BathTranslator.transPrPayrollItemPOToDBObject(row));
                    results.add(dbObject);
                }
            }while (row != null);

            normalBatchMongoOpt.batchInsert(results);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return JsonResult.success("");

    }

    @DeleteMapping("/deleteBatch/{codes}")
    public JsonResult deleteBatch(@PathVariable("codes") String batchCodes) {
        String[] codes = batchCodes.split(",");
        int i = batchService.deleteBatchByCodes(Arrays.asList(codes));
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


    /**
     * 从雇员组中过滤雇员列表
     * @param filterDTO
     * @return
     */
    @PostMapping("/filterEmployees")
    public JsonResult getEmployeeListByBatchCode(@RequestBody EmpFilterDTO filterDTO){

        long start = System.currentTimeMillis(); //begin

        String batchCode = filterDTO.getBatchCode();
        String [] codes = filterDTO.getEmpCodes().split(",");
        List<String> codeList = Arrays.asList(codes);
        int rowAffected1 = normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is(batchCode),"catalog.emp_info.is_active",false);
        logger.info("update all " + String.valueOf(rowAffected1));
        int rowAffected = normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is(batchCode).and("雇员编号").in(codeList),"catalog.emp_info.is_active",true);
        logger.info("update specific " + String.valueOf(rowAffected));

        long end = System.currentTimeMillis();
        logger.info("filterEmployees cost time : " + String.valueOf((end - start)));


        return JsonResult.success(rowAffected,"更新成功");

    }

    /**
     * 从mongodb，根据批次号，获取雇员列表
     * @param pageNum
     * @param pageSize
     * @param batchCode
     * @return
     */
    @PostMapping("/getFilterEmployees/{batchCode}")
    public JsonResult getFilterEmployees(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
                                    @PathVariable("batchCode") String batchCode){

        long start = System.currentTimeMillis(); //begin

        //根据批次号，获取选中的雇员列表
        List<DBObject> list = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("catalog.emp_info.is_active").is(true));

        list = list.stream().skip((pageNum-1) * pageSize).limit(pageSize).collect(Collectors.toList());

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = list.stream().map(dbObject -> {
            SimpleEmpPayItemDTO itemPO = new SimpleEmpPayItemDTO();
            itemPO.setEmpCode(String.valueOf(dbObject.get("雇员编号")));

            DBObject calalog = (DBObject)dbObject.get("catalog");
            DBObject empInfo = (DBObject)calalog.get("emp_info");
            itemPO.setEmpName(empInfo.get("雇员姓名") == null ? "" : (String)empInfo.get("雇员姓名")); //雇员姓名
            itemPO.setTaxPeriod(empInfo.get("个税期间") == null ? "本月" : (String)empInfo.get("个税期间")); //雇员个税期间 TODO

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

        long end = System.currentTimeMillis();
        logger.info("getFilterEmployees cost time : " + String.valueOf((end - start)));

        return JsonResult.success(simplePayItemDTOS);

    }


}
