package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 财务报表
 * </p>
 *
 * @author chenpb
 * @since 2018-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantFinanceDTO {
    /**
     * 任务单
     */
    private FinanceTaskDTO financeTask;
    /**
     * 雇员列表
     */
    private List<FinanceEmployeeDTO> empList;
}
