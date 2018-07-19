package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 暂缓雇员
 * </p>
 *
 * @author chenpb
 * @since 2018-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReprieveEmployeeDTO {
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 发放状态
     */
    private Integer grantStatus;
    /**
     * 雇员编号列表
     */
    private List<String> employeeIds;
}
