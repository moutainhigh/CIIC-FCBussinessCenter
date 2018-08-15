package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
public class ResponseRejectBO {
    /**
     * 处理结果
     */
    private boolean processResult;
    /**
     * 驳回任务单处理失败结果
     */
    private List<SalaryGrantTaskBO> taskList;
}
