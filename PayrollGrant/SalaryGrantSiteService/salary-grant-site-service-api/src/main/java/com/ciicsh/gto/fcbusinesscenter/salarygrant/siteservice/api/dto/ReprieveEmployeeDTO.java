package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 暂缓雇员信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-24
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)

public class ReprieveEmployeeDTO {
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员姓名
     */
    private String employeeName;
}
