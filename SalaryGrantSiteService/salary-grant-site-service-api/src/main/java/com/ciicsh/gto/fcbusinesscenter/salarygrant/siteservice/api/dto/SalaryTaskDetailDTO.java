package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 薪资发放详情
 * </p>
 *
 * @author chenpeb
 * @since 2018-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryTaskDetailDTO {
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 任务单类型
     */
    private Integer taskType;
    /**
     * 任务单状态
     */
    private String taskStatus;
    /**
     * 薪资发放任务单信息
     */
    private SalaryGrantDetailDTO task;
    /**
     * 薪资发放雇员信息
     */
    private List<T> emp;
}
