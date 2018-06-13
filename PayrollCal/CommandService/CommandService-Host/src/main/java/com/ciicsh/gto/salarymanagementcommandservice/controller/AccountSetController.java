package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.companycenter.webcommandservice.api.WorkingCalendarProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.WorkingCalendarRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.WorkingCalendarResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountSetExtensionDTO;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetExtensionTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
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

    @Autowired
    private PrAccountSetService prAccountSetService;

    @Autowired
    private WorkingCalendarProxy workingCalendarProxy;

    @GetMapping("/getWorkCalendarList")
    public JsonResult getWorkCalendarList(@RequestParam String managementId) {

        WorkingCalendarRequestDTO param = new WorkingCalendarRequestDTO();
        param.setManagementId(managementId);
        JsonResult<WorkingCalendarResponseDTO> result;
        try{
            result = workingCalendarProxy.getWorkingCalendars(param);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.faultMessage("获取工作日历失败");
        }
        return JsonResult.success(result);
    }

    @PostMapping("/addAccountSet")
    public JsonResult addAccountSet(@RequestBody PrPayrollAccountSetDTO accountSetDTO) {
        PrAccountSetOptPO optPO = new PrAccountSetOptPO();
        optPO.setManagementId(accountSetDTO.getManagementId());
        optPO.setAccountSetName(accountSetDTO.getAccountSetName());
        Integer val = prAccountSetService.isExistPayrollAccountSet(optPO);
        if(val > 0){
            return JsonResult.faultMessage("添加失败,已经存在在同一个管理方下相同的薪资账套，请检查！");
        }
        else{
            String accountSetCode = codeGenerator.genPrAccountSetCode(accountSetDTO.getManagementId());
            accountSetDTO.setAccountSetCode(accountSetCode);
            accountSetDTO.setCreatedBy(UserContext.getUserId());
            accountSetDTO.setModifiedBy(UserContext.getUserId());
            PrPayrollAccountSetPO payrollAccountSetPO  = PayrollAccountSetTranslator.toPrPayrollAccountSetPO(accountSetDTO);
            boolean result;
            try {
                result = prAccountSetService.addAccountSet(payrollAccountSetPO);
            } catch (BusinessException be) {
                return JsonResult.faultMessage(be.getMessage());
            }
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
        PrAccountSetOptPO optPO = new PrAccountSetOptPO();
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
    public  JsonResult getPayrollAccountSetNames(@RequestParam String managementId){
        List<KeyValuePO> keyValues = prAccountSetService.getPayrollAccountSetNames(managementId);
        return JsonResult.success(keyValues);
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

        extensionDTO.setManagementId(CommonHelper.getManagementIDs());
        PrPayrollAccountSetExtensionPO extensionPO  = PayrollAccountSetExtensionTranslator.toPrPayrollAccountSetExtensionPO(extensionDTO);
        PageInfo<PrPayrollAccountSetExtensionPO> pageInfo = prAccountSetService.getPayrollAccountSetExts(extensionPO, pageNum,pageSize);
        List<PrPayrollAccountSetExtensionDTO> extensions = pageInfo.getList()
                .stream()
                .map(PayrollAccountSetExtensionTranslator::toPrPayrollAccountSetExtensionDTO)
                .map(this::setPayrollAccountSetExtDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollAccountSetExtensionDTO> resultPage = new PageInfo<>(extensions);
        //UserContext.getManagementInfo()
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
