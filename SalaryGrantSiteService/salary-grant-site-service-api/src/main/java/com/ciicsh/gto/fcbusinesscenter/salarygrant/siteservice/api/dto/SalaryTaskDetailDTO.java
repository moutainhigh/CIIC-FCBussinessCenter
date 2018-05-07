package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class SalaryTaskDetailDTO extends PagingDTO implements Serializable {
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
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 发放状态
     */
    private String grantStatus;
    /**
     * 薪资发放任务单信息
     */
    private SalaryGrantDetailDTO task;
    /**
     * 薪资发放雇员信息
     */
    private Pagination<SalaryGrantEmpDTO> empSgInfo;
}
