package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.ApprovalHistoryDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.utils.EnumHelpUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by NeoJiang on 2018/2/2.
 * 
 * @author NeoJiang 
 */
public class ApprovalHistoryTranslator {

    /**
     * 将ApprovalHistoryPO转换成ApprovalHistoryDTO
     * @param approvalHistoryPO
     * @return
     */
    public static ApprovalHistoryDTO toApprovalHistoryDTO(ApprovalHistoryPO approvalHistoryPO){
        ApprovalHistoryDTO approvalHistoryDTO = new ApprovalHistoryDTO();
        BeanUtils.copyProperties(approvalHistoryPO, approvalHistoryDTO);
        if (approvalHistoryDTO.getBizType() != null) {
            approvalHistoryDTO.setBizTypeLabel(
                    EnumHelpUtil.getLabelByValue(BizTypeEnum.class, approvalHistoryDTO.getBizType()));

            approvalHistoryDTO.setResultLabel(EnumHelpUtil.getLabelByValue(ApprovalStatusEnum.class, approvalHistoryDTO.getApprovalResult()));
        }
        return approvalHistoryDTO;
    }
}
