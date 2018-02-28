package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/12/8.
 * @author jiangtianning
 */
@Component
public class GroupTranslator {

    private static ManagementProxy managementProxy;

    @Autowired
    public void setManagementProxy(ManagementProxy managementProxy) {
        GroupTranslator.managementProxy = managementProxy;
    }

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
//        if (prPayrollGroupDTO.getManagementId() != null) {
//            GetManagementRequestDTO param = new GetManagementRequestDTO();
//            String managementLabel;
//            param.setQueryWords(prPayrollGroupDTO.getManagementId());
//            com.ciicsh.gto.salecenter.apiservice.api.dto.core.JsonResult<List<DetailResponseDTO>> managementResult
//                    = managementProxy.getManagement(param);
//            if (managementResult == null) {
//                managementLabel = "";
//            } else {
//                List<DetailResponseDTO> managementList = managementResult.getObject();
//                if (managementList == null) {
//                    managementLabel = "";
//                } else {
//                    managementLabel = managementList.get(0).getTitle();
//                }
//            }
//            prPayrollGroupDTO.setManagementLabel(managementLabel);
//        }
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
