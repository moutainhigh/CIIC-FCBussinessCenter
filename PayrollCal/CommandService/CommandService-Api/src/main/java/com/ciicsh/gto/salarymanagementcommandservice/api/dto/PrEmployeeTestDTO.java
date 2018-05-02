package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 雇员主数据册数
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrEmployeeTestDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 终身雇员ID
     */
	private String employeeId;
    /**
     * 证件类别  
    1:身份证 2:护照 3:军(警)官证 4:士兵证 5:台胞证 6:回乡证 7:其他
     */
	private Integer idCardType;
    /**
     * 证件号
     */
	private String idNum;
    /**
     * 雇员姓名
     */
	private String employeeName;
    /**
     * 曾用名
     */
	private String formerName;
    /**
     * 所属管理方ID
     */
	private String managementId;
    /**
     * 性别  1:男 0:女
     */
	private Boolean gender;
    /**
     * 生日
     */
	private Date birthday;

    /**
     * 入职日期
     */
    private Date joinDate;
    /**
     * 国家代码
     */
	private String countryCode;
    /**
     * 省份代码
     */
	private String provinceCode;
    /**
     * 城市代码
     */
	private String cityCode;
    /**
     * 部门
     */
	private String department;
    /**
     * 职位
     */
	private String position;
    /**
     * 数据创建时间
     */
	private Date createdTime;
    /**
     * 最后修改时间
     */
	private Date modifiedTime;
    /**
     * 数据创建人
     */
	private String createdBy;
    /**
     * 最后修改人
     */
	private String modifiedBy;
}
