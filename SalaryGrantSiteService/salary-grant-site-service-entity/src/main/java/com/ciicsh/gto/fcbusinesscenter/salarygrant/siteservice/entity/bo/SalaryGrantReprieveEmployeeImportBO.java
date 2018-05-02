package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 薪资发放暂缓名单导入表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantReprieveEmployeeImportBO extends SalaryGrantReprieveEmployeeImportPO implements Serializable {
    private static final long serialVersionUID = 1L;

}
