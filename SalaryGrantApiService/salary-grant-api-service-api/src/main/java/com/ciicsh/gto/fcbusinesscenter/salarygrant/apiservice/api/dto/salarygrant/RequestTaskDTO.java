package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chenpb
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RequestTaskDTO {
    /**
     * 薪酬计算批次号，可以传入多个值通过逗号分隔。
     */
    private String batchCode;

    /**
     * 任务单主表编号
     */
    private String taskCode;
}
