package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PayrollAccountItemRelationExtDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountItemRelationDTO;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by houwanhua on 2017/12/20.
 */
@RestController
public class AccountItemRelationController {
    @Autowired
    private PrPayrollAccountItemRelationService relationService;

    @GetMapping("/getPayrollAccountItemRelationExts")
    @ResponseBody
    public JsonResult getPayrollAccountItemRelationExts(@RequestParam String accountSetCode){
        List<PayrollAccountItemRelationExtPO> relationPOS = relationService.getAccountItemRelationExts(accountSetCode);
        List<PayrollAccountItemRelationExtDTO> relationExtDTOS = relationPOS
                .stream()
                .map(PayrollAccountSetTranslator::toPayrollAccountItemRelationExtDTO)
                .collect(Collectors.toList());
        return JsonResult.success(relationExtDTOS);
    }

    @PostMapping("/editAccountItemRelation")
    @ResponseBody
    public JsonResult editAccountItemRelation(@RequestBody PrPayrollAccountItemRelationDTO relationDTO){
        relationDTO.setModifiedBy("bill");
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
