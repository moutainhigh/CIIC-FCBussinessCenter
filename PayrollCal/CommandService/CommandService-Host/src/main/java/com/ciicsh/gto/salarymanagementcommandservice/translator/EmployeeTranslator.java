package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.EmployeeExtensionDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmployeeDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.GenderEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.IdCardTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagement.entity.utils.EnumHelpUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by houwanhua on 2017/12/11.
 */
public class EmployeeTranslator {
    public static PrEmployeePO toPrEmployeePO(PrEmployeeDTO employeeDTO){
        PrEmployeePO employeePO = new PrEmployeePO();
        BeanUtils.copyProperties(employeeDTO,employeePO);
        return employeePO;
    }

    public static PrEmployeeDTO toPrEmployeeDTO(PrEmployeePO employeePO){
        PrEmployeeDTO employeeDTO = new PrEmployeeDTO();
        BeanUtils.copyProperties(employeePO,employeeDTO);
        if (employeePO.getEmployeeId() != null) {
            employeeDTO.setEmpId(employeePO.getEmployeeId());
        }
        if (employeePO.getEmployeeName() != null) {
            employeeDTO.setEmpName(employeePO.getEmployeeName());
        }
        if (employeePO.getGender() != null) {
            employeeDTO.setGenderName(employeePO.getGender() ? "男" : "女");
        }
        if (employeePO.getIdCardType() != null) {
            employeeDTO.setIdCardTypeName(
                    EnumHelpUtil.getLabelByValue(IdCardTypeEnum.class, employeePO.getIdCardType()));
        }
        return employeeDTO;
    }

    public static EmployeeExtensionDTO toEmployeeExtensionDTO(EmployeeExtensionPO employeeExtensionPO){
        EmployeeExtensionDTO employeeExtensionDTO = new EmployeeExtensionDTO();
        BeanUtils.copyProperties(employeeExtensionPO,employeeExtensionDTO);
        return employeeExtensionDTO;
    }
}
