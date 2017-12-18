package com.ciicsh.gto.salarymanagement.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by houwanhua on 2017/12/11.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeExtensionPO {
    private String relationId;
    private String empGroupId;
    private String employeeId;
    private String employeeName;
    private Integer idCardType;
    private String idNum;
    private Boolean gender;
    private Date birthday;
    private Date joinDate;
    private String department;
    private String position;
}