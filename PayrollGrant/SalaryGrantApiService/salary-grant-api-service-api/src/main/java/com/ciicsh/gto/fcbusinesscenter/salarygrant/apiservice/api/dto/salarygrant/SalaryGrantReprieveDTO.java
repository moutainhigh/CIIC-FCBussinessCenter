package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 暂缓发放信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantReprieveDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 任务单编号
     */
    private String taskCode;

    private List<SalaryGrantEmployeeReprieveDTO> employeeList;
}
