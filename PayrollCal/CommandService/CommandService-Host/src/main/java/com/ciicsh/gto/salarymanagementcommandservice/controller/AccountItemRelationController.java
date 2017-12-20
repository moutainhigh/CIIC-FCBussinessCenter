package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PayrollAccountItemRelationExtDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrPayrollAccountItemRelationService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
