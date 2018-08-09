package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 加载雇员分组条件
 * </p>
 *
 * @author chenpb
 * @since 2018-08-07
 */
public class EmployeeOfferFileGroupExtTranslator {
    public static EmployeeOfferFileGroupExt getEmployeeOfferFileGroup(SalaryGrantEmployeePO po) {
        EmployeeOfferFileGroupExt ext = BeanUtils.instantiate(EmployeeOfferFileGroupExt.class);
        BeanUtils.copyProperties(po, ext);
        return ext;
    }
}
