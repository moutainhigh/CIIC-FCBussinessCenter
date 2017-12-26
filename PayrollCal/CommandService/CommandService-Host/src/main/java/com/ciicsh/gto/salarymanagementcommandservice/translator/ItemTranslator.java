package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DecimalProcessTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DefaultValueStyleEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangtianning on 2017/12/13.
 * @author jiangtianning
 */
public class ItemTranslator {

    public static PrPayrollItemDTO toPrPayrollItemDTO(PrPayrollItemPO prPayrollItemPO){
        PrPayrollItemDTO prPayrollItemDTO = new PrPayrollItemDTO();
        BeanUtils.copyProperties(prPayrollItemPO, prPayrollItemDTO);
        if (prPayrollItemDTO.getItemType() != null) {
            prPayrollItemDTO.setItemTypeLabel(
                    ItemTypeEnum.getLabelByValue(prPayrollItemDTO.getItemType()));
        }
        if (prPayrollItemDTO.getDataType() != null) {
            prPayrollItemDTO.setDataTypeLabel(
                    DataTypeEnum.getLabelByValue(prPayrollItemDTO.getDataType()));
        }
        if (prPayrollItemDTO.getDecimalProcessType() != null) {
            prPayrollItemDTO.setDecimalProcessTypeLabel(
                    DecimalProcessTypeEnum.getLabelByValue(prPayrollItemDTO.getDecimalProcessType()));
        }
        if (prPayrollItemDTO.getDefaultValueStyle() != null) {
            prPayrollItemDTO.setDefaultValueStyleLabel(
                    DefaultValueStyleEnum.getLabelByValue(prPayrollItemDTO.getDefaultValueStyle()));
        }
        return prPayrollItemDTO;
    }
}
