package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryClaimPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 薪资发放工资认领信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryClaimBO extends SalaryClaimPO implements Serializable {
    private static final long serialVersionUID = 1L;
}
