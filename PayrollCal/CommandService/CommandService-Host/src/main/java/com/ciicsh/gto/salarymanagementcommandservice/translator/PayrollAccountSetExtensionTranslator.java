package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountSetExtensionDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import org.springframework.beans.BeanUtils;

/**
 * Created by houwanhua on 2017/12/15.
 */
public class PayrollAccountSetExtensionTranslator {
    public static PrPayrollAccountSetExtensionPO toPrPayrollAccountSetExtensionPO(PrPayrollAccountSetExtensionDTO extensionDTO){
        PrPayrollAccountSetExtensionPO extensionPO = new PrPayrollAccountSetExtensionPO();
        BeanUtils.copyProperties(extensionDTO,extensionPO);
        return extensionPO;
    }

    public static PrPayrollAccountSetExtensionDTO toPrPayrollAccountSetExtensionDTO(PrPayrollAccountSetExtensionPO extensionPO){
        PrPayrollAccountSetExtensionDTO extensionDTO = new PrPayrollAccountSetExtensionDTO();
        BeanUtils.copyProperties(extensionPO,extensionDTO);
        return extensionDTO;
    }
}
