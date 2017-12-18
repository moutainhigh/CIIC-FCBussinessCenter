package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmployeeTestDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeTestPO;
import org.springframework.beans.BeanUtils;

/**
 * Created by houwanhua on 2017/12/11.
 */
public class EmployeeTestTranslator {
    public static PrEmployeeTestPO toPrEmployeeTestPO(PrEmployeeTestDTO employeeTestDTO){
        PrEmployeeTestPO employeeTestPO = new PrEmployeeTestPO();
        BeanUtils.copyProperties(employeeTestDTO,employeeTestPO);
        return employeeTestPO;
    }

    public static PrEmployeeTestDTO toPrEmployeeTestDTO(PrEmployeeTestPO employeeTestPO){
        PrEmployeeTestDTO employeeTestDTO = new PrEmployeeTestDTO();
        BeanUtils.copyProperties(employeeTestPO,employeeTestDTO);
        return employeeTestDTO;
    }
}
