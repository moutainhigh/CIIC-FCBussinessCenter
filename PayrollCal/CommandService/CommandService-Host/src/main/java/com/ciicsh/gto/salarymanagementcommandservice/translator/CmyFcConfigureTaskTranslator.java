package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.ConfigTaskMsgDTO;
import com.ciicsh.gto.salarymanagement.entity.po.CmyFcConfigureTaskPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.CmyFcConfigureTaskDTO;
import org.springframework.beans.BeanUtils;

/**
 * @author baofeng@ciicsh.com
 * @createTime 20180711 16:17
 * @description 薪资组实例变更entity互相转译
 */
public class CmyFcConfigureTaskTranslator {

    /**
     * 将PO转成DTO
     *
     * @param cmyFcConfigureTaskPO po
     * @return dto
     */
    public static CmyFcConfigureTaskDTO toCmyFcConfigureTaskDTO(CmyFcConfigureTaskPO cmyFcConfigureTaskPO) {
        CmyFcConfigureTaskDTO cmyFcConfigureTaskDTO = new CmyFcConfigureTaskDTO();
        BeanUtils.copyProperties(cmyFcConfigureTaskPO, cmyFcConfigureTaskDTO);

        return cmyFcConfigureTaskDTO;
    }

    /**
     * 将DTO转成PO
     * @param configTaskMsgDTO dto
     * @return po
     */
    public static CmyFcConfigureTaskPO toCmyFcConfigureTaskPO(ConfigTaskMsgDTO configTaskMsgDTO){
        CmyFcConfigureTaskPO cmyFcConfigureTaskPO = new CmyFcConfigureTaskPO();
        BeanUtils.copyProperties(configTaskMsgDTO, cmyFcConfigureTaskPO);

        return cmyFcConfigureTaskPO;
    }

}
