package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.ConfigTaskMsgDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollConfigTaskPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollConfigTaskDTO;
import org.springframework.beans.BeanUtils;

/**
 * @author baofeng@ciicsh.com
 * @createTime 20180711 16:17
 * @description 薪资组实例变更entity互相转译
 */
public class PrPayrollConfigTaskTranslator {

    /**
     * 将PO转成DTO
     *
     * @param prPayrollConfigTaskPO po
     * @return dto
     */
    public static PrPayrollConfigTaskDTO toPrPayrollConfigTaskDTO(PrPayrollConfigTaskPO prPayrollConfigTaskPO) {
        PrPayrollConfigTaskDTO prPayrollConfigTaskDTO = new PrPayrollConfigTaskDTO();
        BeanUtils.copyProperties(prPayrollConfigTaskPO, prPayrollConfigTaskDTO);

        return prPayrollConfigTaskDTO;
    }

    /**
     * 将DTO转成PO
     * @param configTaskMsgDTO dto
     * @return po
     */
    public static PrPayrollConfigTaskPO toPrPayrollConfigTaskPO(ConfigTaskMsgDTO configTaskMsgDTO){
        PrPayrollConfigTaskPO prPayrollConfigTaskPO = new PrPayrollConfigTaskPO();
        BeanUtils.copyProperties(configTaskMsgDTO, prPayrollConfigTaskPO);

        return prPayrollConfigTaskPO;
    }

}
