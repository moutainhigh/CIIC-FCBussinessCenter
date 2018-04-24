package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 配置表，雇员个人基本信息表。
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrEmployeeDTO{

    private static final long serialVersionUID = 1L;
    /**
     * 自增ID号
     */
	private Long id;
    /**
     * 雇员ID
     */
	private String empId;
    /**
     * 雇员名称
     */
	private String empName;
    /**
     * 雇员英文名称
     */
	private String empNameEn;
    /**
     * 曾用名
     */
    private String formerName;

    /**
     * 证件类别
     1:身份证 2:护照 3:军(警)官证 4:士兵证 5:台胞证 6:回乡证 7:其他
     */
    private Integer idCardType;
    /**
     *
     */
    private String idCardTypeName;
    /**
     * 证件号
     */
    private String idNum;
    /**
     * 性别  1:男 0:女
     */
    private Boolean gender;
    /**
     * 性别
     */
    private String genderName;
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
     * 国家
     */
    private String countryName;
    /**
     * 省份代码
     */
    private String provinceCode;
    /**
     * 省份
     */
    private String provinceName;
    /**
     * 城市代码
     */
    private String cityCode;
    /**
     * 城市
     */
    private String cityName;
    /**
     * 部门
     */
    private String department;
    /**
     * 职位
     */
    private String position;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否有效
     */
    private Boolean isActive;
    /**
     * 数据创建时间
     */
    private Date createdTime;
    /**
     * 最后修改时间
     */
    private Date modifiedTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 最后修改人
     */
    private String modifiedBy;
}
