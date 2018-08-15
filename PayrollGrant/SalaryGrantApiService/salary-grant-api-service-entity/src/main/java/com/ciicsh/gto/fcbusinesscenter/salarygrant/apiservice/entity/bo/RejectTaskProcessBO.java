package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 薪资发放驳回任务处理结果
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RejectTaskProcessBO {
    /**
     * 处理结果
     */
    private boolean processResult;
    /**
     * 处理结果信息
     */
    private String processMessage;
}
