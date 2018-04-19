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
     * 薪酬计算批次号
     */
    private String batchCode;
}
