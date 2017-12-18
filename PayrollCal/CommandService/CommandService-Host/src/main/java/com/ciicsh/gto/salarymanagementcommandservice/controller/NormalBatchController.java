package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupTemplateDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.BathTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.BatchUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill on 17/12/6.
 */
@RestController
public class NormalBatchController {

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
    private CodeGenerator codeGenerator;

    @GetMapping("/getMgrList")
    public JsonResult getMgrList(@RequestParam String query) {

        List<MgrData> datas = new ArrayList<>();
        MgrData data = null;
        data = new MgrData("glf-0009","微软中国（上海）");
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

    @GetMapping("/getAccSetList")
    public JsonResult getAccountSetList(@RequestParam String query) {

        System.out.println(query);

        List<MgrData> datas = new ArrayList<>();
        MgrData data = null;
        data = new MgrData("acc-00009","欧莱雅工资帐套");
        datas.add(data);

        data = new MgrData("acc-10008","欧莱雅综合工时帐套");
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

        PrPayrollAccountSetPO accountSetPO = null; // TODO  get accountset by code;

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
                msg.setBatchId(code);
                msg.setEmpGroupId("emp_groupId");
                msg.setPayrollGroupId("payroll_id");
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

        PrPayrollAccountSetPO accountSetPO = null; // TODO  get accountset by code;

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

}
