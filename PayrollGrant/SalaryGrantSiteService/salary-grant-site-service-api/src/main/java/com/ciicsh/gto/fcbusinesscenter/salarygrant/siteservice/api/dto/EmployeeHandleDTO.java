package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 雇员操作用 ：暂缓，批量暂缓
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeHandleDTO {
    /**
     * 雇员信息编号
     */
    private Long salaryGrantEmployeeId;
    /**
     * 发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
     */
    private Integer grantStatus;
}
