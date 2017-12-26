package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import org.springframework.beans.BeanUtils;

/**
 * Created by houwanhua on 2017/12/5.
 */
public class EmployeeGroupTranslator {
    public static PrEmpGroupPO toPrEmpGroupPO(PrEmpGroupDTO empGroupDTO){
        PrEmpGroupPO empGroupPO = new PrEmpGroupPO();
        BeanUtils.copyProperties(empGroupDTO,empGroupPO);
        return empGroupPO;
    }

    public static PrEmpGroupDTO toPrEmpGroupDTO(PrEmpGroupPO empGroupPO){
        PrEmpGroupDTO empGroupDTO = new PrEmpGroupDTO();
        BeanUtils.copyProperties(empGroupPO,empGroupDTO);
        return empGroupDTO;
    }
}
