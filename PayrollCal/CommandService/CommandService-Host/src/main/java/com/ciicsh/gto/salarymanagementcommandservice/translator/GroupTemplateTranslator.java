package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollGroupTemplateDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangtianning on 2017/12/6.
 * @author jiangtianning
 */
public class GroupTemplateTranslator {

    /**
     * 将PrPayrollGroupTemplatePO转换成PrPayrollGroupTemplateDTO
     * @param prPayrollGroupTemplatePO
     * @return
     */
    public static PrPayrollGroupTemplateDTO toPrPayrollGroupTemplateDTO(PrPayrollGroupTemplatePO prPayrollGroupTemplatePO){
        PrPayrollGroupTemplateDTO prPayrollGroupTemplateDTO = new PrPayrollGroupTemplateDTO();
        BeanUtils.copyProperties(prPayrollGroupTemplatePO, prPayrollGroupTemplateDTO);
        if (prPayrollGroupTemplateDTO.getApprovalStatus() != null) {
            prPayrollGroupTemplateDTO.setApprovalStatusLabel(
                    ApprovalStatusEnum.getLabelByValue(prPayrollGroupTemplateDTO.getApprovalStatus()));
        }
        return prPayrollGroupTemplateDTO;
    }

    /**
     * 将PrPayrollGroupTemplateDTO转换成PrPayrollGroupTemplatePO
     * @param prPayrollGroupTemplateDTO
     * @return
     */
    public static PrPayrollGroupTemplatePO toPrPayrollGroupTemplatePO(PrPayrollGroupTemplateDTO prPayrollGroupTemplateDTO){
        PrPayrollGroupTemplatePO prPayrollGroupTemplatePO = new PrPayrollGroupTemplatePO();
        BeanUtils.copyProperties(prPayrollGroupTemplateDTO, prPayrollGroupTemplatePO);
        return prPayrollGroupTemplatePO;
    }
}
