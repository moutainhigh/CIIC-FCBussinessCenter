package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountSetExtensionDTO;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountItemOptPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeGroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetExtensionTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/1.
 */
@RestController
public class AccountSetController extends BaseController {

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
    private PrAccountSetService prAccountSetService;


    @GetMapping("/getWorkCalendarList")
    public JsonResult getWorkCalendarList(@RequestParam String managementId) {

        List<WorkCalendar> workCalendars = new ArrayList<>();
        WorkCalendar workCalendar = null;

        workCalendar = new WorkCalendar("GL170001","蓝天科技工作日历A","1,2,3,4,5,6,7,8,9,10");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("GL170001","蓝天科技工作日历B","1,2,3,4,5,6,7,8,9,10,11,12");
        workCalendars.add(workCalendar);

        workCalendar = new WorkCalendar("GL170001","蓝天科技工作日历C","1,2,3,4,5,6,7,8,9,10,11,12,13,14");
        workCalendars.add(workCalendar);

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
        PrAccountItemOptPO optPO = new PrAccountItemOptPO();
        optPO.setManagementId(accountSetDTO.getManagementId());
        optPO.setAccountSetName(accountSetDTO.getAccountSetName());
        Integer val = prAccountSetService.isExistPayrollAccountSet(optPO);
        if(val > 0){
            return JsonResult.faultMessage("添加失败,已经存在在同一个管理方下相同的薪资账套，请检查！");
        }
        else{
            String accountSetCode = codeGenerator.genPrAccountSetCode(accountSetDTO.getManagementId());
            accountSetDTO.setAccountSetCode(accountSetCode);
            accountSetDTO.setCreatedBy("macor");
            accountSetDTO.setModifiedBy("macor");
            PrPayrollAccountSetPO payrollAccountSetPO  = PayrollAccountSetTranslator.toPrPayrollAccountSetPO(accountSetDTO);
            boolean result = prAccountSetService.addAccountSet(payrollAccountSetPO);
            if(result){
                PrPayrollAccountSetExtensionPO extensionPO = prAccountSetService.getPayrollAccountSetExtByCode(accountSetCode);
                PrPayrollAccountSetExtensionDTO extensionDTO = null;
                if(null != extensionPO){
                    extensionDTO = PayrollAccountSetExtensionTranslator.toPrPayrollAccountSetExtensionDTO(extensionPO);
                    if(getManagement().containsKey(extensionDTO.getManagementId())){
                        extensionDTO.setManagementName(getManagement().get(extensionDTO.getManagementId()));
                    }
                }
                return JsonResult.success(extensionDTO,"添加成功！");
            }
            else
            {
                return JsonResult.faultMessage("添加失败！");
            }
        }
    }

    @PostMapping("/editAccountSet")
    public JsonResult editAccountSet(@RequestBody PrPayrollAccountSetDTO accountSetDTO) {
        PrAccountItemOptPO optPO = new PrAccountItemOptPO();
        optPO.setManagementId(accountSetDTO.getManagementId());
        optPO.setAccountSetName(accountSetDTO.getAccountSetName());
        optPO.setAccountSetCode(accountSetDTO.getAccountSetCode());
        Integer val = prAccountSetService.isExistPayrollAccountSet(optPO);
        if(val > 0){
            return JsonResult.faultMessage("编辑失败,已经存在在同一个管理方下相同的薪资账套，请检查！");
        }
        else{

            accountSetDTO.setModifiedBy("bill");
            accountSetDTO.setModifiedTime(new Date());
            PrPayrollAccountSetPO payrollAccountSetPO  = PayrollAccountSetTranslator.toPrPayrollAccountSetPO(accountSetDTO);
            boolean result = prAccountSetService.editAccountSet(payrollAccountSetPO);
            if(result){
                PrPayrollAccountSetExtensionPO extensionPO = prAccountSetService.getPayrollAccountSetExtByCode(accountSetDTO.getAccountSetCode());
                PrPayrollAccountSetExtensionDTO extensionDTO = null;
                if(null != extensionPO){
                    extensionDTO = PayrollAccountSetExtensionTranslator.toPrPayrollAccountSetExtensionDTO(extensionPO);
                    if(getManagement().containsKey(extensionDTO.getManagementId())){
                        extensionDTO.setManagementName(getManagement().get(extensionDTO.getManagementId()));
                    }
                }
                return JsonResult.success(extensionDTO,"编辑成功！");
            }
            else
            {
                return JsonResult.faultMessage("编辑失败！");
            }
        }
    }


    @PostMapping("/editIsActive")
    public JsonResult editIsActive(@RequestBody PrPayrollAccountSetDTO accountSetDTO) {
        accountSetDTO.setModifiedBy("bill");
        accountSetDTO.setModifiedTime(new Date());
        PrPayrollAccountSetPO accountSetPO = PayrollAccountSetTranslator.toPrPayrollAccountSetPO(accountSetDTO);
        Integer val = prAccountSetService.editIsActive(accountSetPO);
        if(val > 0){
            return JsonResult.success("修改成功！");
        }
        else{
            return JsonResult.faultMessage("修改失败！");
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
                .map(this::setPayrollAccountSetExtDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollAccountSetExtensionDTO> resultPage = new PageInfo<>(extensions);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }


    private PrPayrollAccountSetExtensionDTO setPayrollAccountSetExtDTO(PrPayrollAccountSetExtensionDTO accountSetExtensionDTO){
        if(getManagement().containsKey(accountSetExtensionDTO.getManagementId())){
            accountSetExtensionDTO.setManagementName(getManagement().get(accountSetExtensionDTO.getManagementId()));
        }
        return accountSetExtensionDTO;
    }


    @GetMapping("/getAccountSetInfo")
    public JsonResult getAccountSetInfo(@RequestParam("accSetCode") String accSetCode) {
        PrPayrollAccountSetPO prPayrollAccountSetPO = prAccountSetService.getAccountSetInfo(accSetCode);
        return JsonResult.success(prPayrollAccountSetPO);

    }
}
