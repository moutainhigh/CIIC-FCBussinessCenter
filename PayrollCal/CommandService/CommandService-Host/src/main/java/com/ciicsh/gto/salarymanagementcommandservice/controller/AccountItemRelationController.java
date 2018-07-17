package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PayrollAccountItemRelationExtDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountItemRelationDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrPayrollAccountItemRelationService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by houwanhua on 2017/12/20.
 */
@RestController
public class AccountItemRelationController {
    @Autowired
    private PrPayrollAccountItemRelationService relationService;

    /**
     * 获取薪资账套的薪资项
     * @param accountSetCode 薪资账套编码
     * @param payrollGroupTemplateCode 薪资模板编码
     * @param payrollGroupCode 薪资组编码
     * @param managementId 管理方ID
     * @return 薪资项
     */
    @GetMapping("/getPayrollAccountItemRelationExts")
    @ResponseBody
    public JsonResult getPayrollAccountItemRelationExts(@RequestParam String accountSetCode,
                                                        @RequestParam String payrollGroupTemplateCode,
                                                        @RequestParam String payrollGroupCode,
                                                        @RequestParam String managementId){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accountSetCode", accountSetCode);
        paramMap.put("payrollGroupTemplateCode", payrollGroupTemplateCode);
        paramMap.put("payrollGroupCode", payrollGroupCode);
        paramMap.put("managementId", managementId);
        List<PayrollAccountItemRelationExtPO> relationPOS = relationService.getAccountItemRelationExts(paramMap);
        List<PayrollAccountItemRelationExtDTO> relationExtDTOS = relationPOS
                .stream()
                .map(PayrollAccountSetTranslator::toPayrollAccountItemRelationExtDTO)
                .collect(Collectors.toList());
        return JsonResult.success(relationExtDTOS);
    }

    @PostMapping("/editAccountItemRelation")
    @ResponseBody
    public JsonResult editAccountItemRelation(@RequestBody PrPayrollAccountItemRelationDTO relationDTO){
        relationDTO.setModifiedBy(UserContext.getUserId());
        relationDTO.setModifiedTime(new Date());

        PrPayrollAccountItemRelationPO relationPO = PayrollAccountSetTranslator.toPrPayrollAccountItemRelationPO(relationDTO);
        Integer val = relationService.editAccountItemRelation(relationPO);
        if(val > 0){
            return JsonResult.success("修改成功！");
        }
        else{
            return JsonResult.faultMessage("修改失败！");
        }
    }
}
