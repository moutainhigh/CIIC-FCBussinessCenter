package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 薪资发放任务单提交
 * </p>
 *
 * @author chenpeb
 * @since 2018-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryTaskHandleDTO extends PagingDTO {
    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 计算批次号
     */
    private String batchCode;
    /**
     * 发放方式
     */
    private String grantMode;
    /**
     * 发放类型
     */
    private Integer grantType;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
    private Integer grantTime;
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
     * 审批意见
     */
    private String approvedOpinion;
    /**
     * 失效原因
     */
    private String invalidReason;
    /**
     * 撤回原因
     */
    private String remark;
    /**
     * 修改时间
     */
    private Date modifiedTime;
}
