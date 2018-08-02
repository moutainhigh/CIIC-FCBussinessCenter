package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 流程结果
 * </p>
 *
 * @author chenpb
 * @since 2018-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkFlowResultDTO {
    /**
     * 批次号
     */
    private String batchCode;
    /**
     * 任务单
     */
    private String taskCode;
    /**
     * 任务单类型
     */
    private Integer taskType;
    /**
     * 处理结果
     */
    private Integer result;
    /**
     * 消息
     */
    private String message;
}
