package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantTaskHistoryPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单历史表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantTaskHistoryBO extends SalaryGrantTaskHistoryPO implements Serializable {
    private static final long serialVersionUID = 1L;

}
