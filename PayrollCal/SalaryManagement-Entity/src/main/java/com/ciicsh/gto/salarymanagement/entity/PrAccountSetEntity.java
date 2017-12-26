package com.ciicsh.gto.salarymanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by jiangtianning on 2017/10/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrAccountSetEntity extends PrBaseEntity {

    /**
     * 薪资账套Id, 格式为PRAS+NNNNNN
     */
    private String entityId;

    /**
     * 所属管理方Id
     */
    private String managementId;

    /**
     * 薪资账套编码
     */
    private String code;

    /**
     * 薪资组名称
     */
    private String name;

    /**
     * 薪资组ID
     */
    private String prGroupId;

    /**
     * 薪资组
     */
    private PrGroupEntity prGroupEntity;

    /**
     * 雇员组ID
     */
    private String prEmployeeGroupId;

    /**
     * 雇员组
     */
    private PrEmployeeGroupEntity prEmployeeGroupEntity;

    /**
     * 工资期间开始日
     */
    private Date startSalaryDate;

    /**
     * 工资期间结束日
     */
    private Date endSalaryDate;

    /**
     * 个税期间：0 - 本月报税，1 - 下月报税
     */
    private Integer taxPeriod;

    /**
     * 备注
     */
    private String remark;
}
