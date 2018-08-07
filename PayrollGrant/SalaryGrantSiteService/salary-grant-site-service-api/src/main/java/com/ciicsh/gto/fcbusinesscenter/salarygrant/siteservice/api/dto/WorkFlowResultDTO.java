package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放
     */
    private Integer grantType;
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
    /**
     * 修改时间
     */
    private Date modifiedTime;
}
