package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PayrollAccountItemRelationExtDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountItemRelationDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
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


    public static PrPayrollAccountItemRelationPO toPrPayrollAccountItemRelationPO(PrPayrollAccountItemRelationDTO relationDTO){
        PrPayrollAccountItemRelationPO relationPO = new PrPayrollAccountItemRelationPO();
        BeanUtils.copyProperties(relationDTO,relationPO);
        return relationPO;
    }

    public static PayrollAccountItemRelationExtDTO toPayrollAccountItemRelationExtDTO(PayrollAccountItemRelationExtPO relationExtPO){
        PayrollAccountItemRelationExtDTO relationExtDTO = new PayrollAccountItemRelationExtDTO();
        BeanUtils.copyProperties(relationExtPO,relationExtDTO);
        return relationExtDTO;
    }
}
