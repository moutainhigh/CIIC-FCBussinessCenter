package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单主表
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantMainTaskBO extends SalaryGrantMainTaskPO implements Serializable {
    private static final long serialVersionUID = 1L;

}
