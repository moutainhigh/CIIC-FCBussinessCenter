package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单子表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantSubTaskBO extends SalaryGrantSubTaskPO implements Serializable {
    private static final long serialVersionUID = 1L;

}
