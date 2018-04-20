package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;

import java.io.Serializable;

/**
 * <p>
 * 雇员基本信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-06
 */
public class EmployeeDTO extends PagingDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 雇员中智终身编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 国家代码
     */
    private String countryCode;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
