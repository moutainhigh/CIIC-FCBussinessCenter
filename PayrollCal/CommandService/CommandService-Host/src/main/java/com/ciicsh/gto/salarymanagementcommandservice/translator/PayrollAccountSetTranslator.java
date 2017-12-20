package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PayrollAccountItemRelationExtDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import org.springframework.beans.BeanUtils;

/**
 * Created by houwanhua on 2017/12/15.
 */
public class PayrollAccountSetTranslator {
    public static PrPayrollAccountSetPO toPrPayrollAccountSetPO(PrPayrollAccountSetDTO payrollAccountSetDTO){
        PrPayrollAccountSetPO payrollAccountSetPO = new PrPayrollAccountSetPO();
        BeanUtils.copyProperties(payrollAccountSetDTO,payrollAccountSetPO);
        return payrollAccountSetPO;
    }

    public static PrPayrollAccountSetDTO toPrPayrollAccountSetDTO(PrPayrollAccountSetPO payrollAccountSetPO){
        PrPayrollAccountSetDTO payrollAccountSetDTO = new PrPayrollAccountSetDTO();
        BeanUtils.copyProperties(payrollAccountSetPO,payrollAccountSetDTO);
        return payrollAccountSetDTO;
    }

    public static PayrollAccountItemRelationExtDTO toPayrollAccountItemRelationExtDTO(PayrollAccountItemRelationExtPO relationExtPO){
        PayrollAccountItemRelationExtDTO relationExtDTO = new PayrollAccountItemRelationExtDTO();
        BeanUtils.copyProperties(relationExtPO,relationExtDTO);
        return relationExtDTO;
    }
}
