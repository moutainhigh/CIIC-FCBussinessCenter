package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 退票发放信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantRefundDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 薪酬计算批次号
     */
    private String batchCode;

    private List<SalaryGrantEmployeeRefundDTO> employeeList;
}
