package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagement.entity.po.PrFunctionsPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrFunctionsDTO;
import org.springframework.beans.BeanUtils;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-08-10 18:05
 * @description 标准函数库DTO和PO互相转换类
 */
public class PrFunctionsTranslator {

    public static PrFunctionsDTO toPrFunctionsDTO(PrFunctionsPO prFunctionsPO) {
        PrFunctionsDTO prFunctionsDTO = new PrFunctionsDTO();
        BeanUtils.copyProperties(prFunctionsPO, prFunctionsDTO);

        return prFunctionsDTO;
    }
}
