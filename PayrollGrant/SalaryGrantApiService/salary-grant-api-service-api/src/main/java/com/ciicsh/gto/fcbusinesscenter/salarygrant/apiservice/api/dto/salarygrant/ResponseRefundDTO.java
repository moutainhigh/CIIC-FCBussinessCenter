package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 薪资发放退票任务请求结果
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseRefundDTO {
    /**
     * 处理结果
     */
    private boolean processResult;
    /**
     * 处理结果信息
     */
    private String processMessage;
}
