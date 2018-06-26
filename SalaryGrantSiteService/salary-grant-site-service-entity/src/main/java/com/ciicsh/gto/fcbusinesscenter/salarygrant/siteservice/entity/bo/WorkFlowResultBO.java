package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工作流结果
 * </p>
 *
 * @author chenpb
 * @since 2018-06-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkFlowResultBO {
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
}
