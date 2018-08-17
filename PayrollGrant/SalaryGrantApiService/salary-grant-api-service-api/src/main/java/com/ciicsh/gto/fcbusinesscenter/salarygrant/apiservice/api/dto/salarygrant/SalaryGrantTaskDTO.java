package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 薪资发放任务单信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantTaskDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 任务单编号（发放批次号）
     */
    private String taskCode;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
}
