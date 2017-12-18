package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangtianning on 2017/12/8.
 * @author jiangtianning
 */
public class GroupTranslator {

    /**
     * 将PrPayrollGroupPO转换成PrPayrollGroupDTO
     * @param prPayrollGroupPO
     * @return
     */
    public static PrPayrollGroupDTO toPrPayrollGroupDTO(PrPayrollGroupPO prPayrollGroupPO){
        PrPayrollGroupDTO prPayrollGroupDTO = new PrPayrollGroupDTO();
        BeanUtils.copyProperties(prPayrollGroupPO, prPayrollGroupDTO);
        if (prPayrollGroupDTO.getApprovalStatus() != null) {
            prPayrollGroupDTO.setApprovalStatusLabel(
                    ApprovalStatusEnum.getLabelByValue(prPayrollGroupDTO.getApprovalStatus()));
        }
        return prPayrollGroupDTO;
    }

    /**
     * 将PrPayrollGroupDTO转换成PrPayrollGroupPO
     * @param prPayrollGroupDTO
     * @return
     */
    public static PrPayrollGroupPO toPrPayrollGroupPO(PrPayrollGroupDTO prPayrollGroupDTO){
        PrPayrollGroupPO prPayrollGroupPO = new PrPayrollGroupPO();
        BeanUtils.copyProperties(prPayrollGroupDTO, prPayrollGroupPO);
        return prPayrollGroupPO;
    }
}
