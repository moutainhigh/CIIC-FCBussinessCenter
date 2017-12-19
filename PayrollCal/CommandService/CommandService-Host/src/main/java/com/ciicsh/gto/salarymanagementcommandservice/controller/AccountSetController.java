package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountSetExtensionDTO;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeGroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetExtensionTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.PrEntityIdClient;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/1.
 */
@RestController
public class AccountSetController {

    class WorkCalendar{
        public String ManagementId;
        public String WorkCalendarName;
        public String WorkCalendarValue;

        public WorkCalendar(String managementId,String workCalendarName,String workCalendarValue){
            this.ManagementId = managementId;
            this.WorkCalendarName = workCalendarName;
            this.WorkCalendarValue = workCalendarValue;
        }
    }

    @Autowired
    protected CodeGenerator codeGenerator;

    @Autowired
    private PrAccountSetService prAccountSetService;

    @Autowired
    private PrEntityIdClient entityIdClient;



    @GetMapping("/getWorkCalendarList")
    public JsonResult getWorkCalendarList(@RequestParam String managementId) {

        List<WorkCalendar> workCalendars = new ArrayList<>();
        WorkCalendar workCalendar = null;
        workCalendar = new WorkCalendar("glf-0009","上海微软中国工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-0009","上海微软中国工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-0009","上海微软中国工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-00091","北京微软中国工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-00091","北京微软中国工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-00091","北京微软中国工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);


        workCalendar = new WorkCalendar("glf-00092","深圳微软中国工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-00092","深圳微软中国工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("glf-00092","深圳微软中国工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);


        workCalendar = new WorkCalendar("ymx-0001","上海亚马逊工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0001","上海亚马逊工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0001","上海亚马逊工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0002","北京亚马逊工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0002","北京亚马逊工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0002","北京亚马逊工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);


        workCalendar = new WorkCalendar("ymx-0003","深圳亚马逊工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0003","深圳亚马逊工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("ymx-0003","深圳亚马逊工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);


        List<WorkCalendar> result = new ArrayList<>();
        result.clear();

        result = workCalendars
                .stream()
                .filter(x->x.ManagementId.equals(managementId))
                .collect(Collectors.toList());
        return JsonResult.success(result);
    }

    @PostMapping("/addAccountSet")
    public JsonResult addAccountSet(@RequestBody PrPayrollAccountSetDTO accountSetDTO) {
        String accountSetCode = codeGenerator.genPrAccountSetCode(accountSetDTO.getManagementId());
        accountSetDTO.setAccountSetCode(accountSetCode);
        accountSetDTO.setCreatedBy("macor");
        accountSetDTO.setModifiedBy("macor");
        PrPayrollAccountSetPO payrollAccountSetPO  = PayrollAccountSetTranslator.toPrPayrollAccountSetPO(accountSetDTO);
        boolean result = prAccountSetService.addAccountSet(payrollAccountSetPO);
        if(result){
            return new JsonResult(true,"添加成功！");
        }
        else
        {
            return new JsonResult(false,"添加失败！");
        }
    }

    @GetMapping("/getPayrollAccountSetNames")
    @ResponseBody
    public  JsonResult getPayrollAccountSetNames(@RequestParam String managementId,@RequestParam String query){
        List<KeyValuePO> keyValues = prAccountSetService.getPayrollAccountSetNames(managementId);
        List<KeyValuePO> result = new ArrayList<>();
        result.clear();
        if(CommonUtils.isContainChinese(query)){
            keyValues.forEach(model ->{
                if(model.getValue().indexOf(query) >-1){
                    result.add(model);
                }
            });
        }else {
            keyValues.forEach(model ->{
                if(model.getKey().indexOf(query) >-1){
                    result.add(model);
                }
            });
        }
        return JsonResult.success(result);
    }

    @PostMapping("/getPayrollAccountSets")
    @ResponseBody
    public JsonResult getPayrollAccountSets(@RequestBody PrPayrollAccountSetDTO payrollAccountSetDTO,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
        PrPayrollAccountSetPO payrollAccountSetPO  = PayrollAccountSetTranslator.toPrPayrollAccountSetPO(payrollAccountSetDTO);
        PageInfo<PrPayrollAccountSetPO> pageInfo = prAccountSetService.getPayrollAccountSets(payrollAccountSetPO, pageNum,pageSize);
        List<PrPayrollAccountSetDTO> prPayrollAccountSets = pageInfo.getList()
                .stream()
                .map(PayrollAccountSetTranslator::toPrPayrollAccountSetDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollAccountSetDTO> resultPage = new PageInfo<>(prPayrollAccountSets);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }


    @PostMapping("/getPayrollAccountSetExts")
    @ResponseBody
    public JsonResult getPayrollAccountSetExts(@RequestBody PrPayrollAccountSetExtensionDTO extensionDTO,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
        PrPayrollAccountSetExtensionPO extensionPO  = PayrollAccountSetExtensionTranslator.toPrPayrollAccountSetExtensionPO(extensionDTO);
        PageInfo<PrPayrollAccountSetExtensionPO> pageInfo = prAccountSetService.getPayrollAccountSetExts(extensionPO, pageNum,pageSize);
        List<PrPayrollAccountSetExtensionDTO> extensions = pageInfo.getList()
                .stream()
                .map(PayrollAccountSetExtensionTranslator::toPrPayrollAccountSetExtensionDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollAccountSetExtensionDTO> resultPage = new PageInfo<>(extensions);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }


//    @GetMapping(value = "/prAccountSet")
//    public ResultEntity getPrAccountSetList(@RequestParam String managementId,
//                                            @RequestParam Integer pageNum) {
//
//        PrAccountSetEntity param = new PrAccountSetEntity();
//        param.setManagementId(managementId);
//        PageInfo<PrAccountSetEntity> pageInfo = prAccountSetService.getList(param, pageNum);
//        List<PrAccountSetEntity> resultList = pageInfo.getList();
//        return ResultEntity.success(resultList);
//    }
//
//    @PostMapping(value = "/prAccountSetQuery")
//    public ResultEntity searchPrAccountSetList(@RequestBody PrAccountSetEntity param,
//                                                           @RequestParam Integer pageNum) {
//
//        PageInfo<PrAccountSetEntity> pageInfo = prAccountSetService.getList(param, pageNum);
//        List<PrAccountSetEntity> resultList = pageInfo.getList();
//        return ResultEntity.success(resultList);
//    }
//
//    @PostMapping(value = "/prAccountSet")
//    public ResultEntity newPrAccountSet(@RequestBody PrAccountSetEntity param) {
//
//        Random random = new Random();
//        DecimalFormat df6 = new DecimalFormat("000000");
//        Integer tempCode = random.nextInt(999999);
//        if (param.getEntityId()==null){
//            param.setEntityId(entityIdClient.getEntityId(PrEntityIdClient.PR_ACCOUNT_SET_CAT_ID));
//        }
//
//        param.setCode("XZZT-CMY" + df6.format(tempCode) + "-001-01");
//        param.setCreatedBy("jiang");
//        param.setModifiedBy("jiang");
//        int result = prAccountSetService.addItem(param);
//        return ResultEntity.success(result);
//    }
//
//    @GetMapping(value = "/prAccountSet/{id}")
//    public ResultEntity getPrAccountSet(@PathVariable("id") String id) {
//
//        PrAccountSetEntity result = prAccountSetService.getItemById(id);
//        return ResultEntity.success(result);
//    }
//
//    @PutMapping(value = "/prAccountSet/{id}")
//    public ResultEntity updatePrAccountSet(@PathVariable("id") String id,
//                                  @RequestBody PrAccountSetEntity param) {
//
//        param.setEntityId(id);
//        param.setModifiedBy("jiang");
//        param.setDataChangeLastTime(new Date());
//        int result = prAccountSetService.updateItemById(param);
//        return ResultEntity.success(result);
//    }
//
//    @GetMapping(value = "/prAccountSetName")
//    public ResultEntity getPrAccountSetNameList(@RequestParam String managementId) {
//
//        List<String> resultList = prAccountSetService.getNameList(managementId);
//        return ResultEntity.success(resultList);
//    }
//
//    @DeleteMapping(value = "/prAccount/{id}")
//    public ResultEntity deleteAccountSet(@PathVariable("id") String id){
//        int result = prAccountSetService.deleteItemById(id);
//        if (result==1){
//            return ResultEntity.success(result,"删除成功");
//        }else {
//            return ResultEntity.faultMessage();
//        }
//    }
}
