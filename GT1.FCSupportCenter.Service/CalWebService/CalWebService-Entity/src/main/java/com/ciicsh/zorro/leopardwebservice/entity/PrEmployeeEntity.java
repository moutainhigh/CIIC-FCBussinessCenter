package com.ciicsh.zorro.leopardwebservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author jiangtianning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrEmployeeEntity extends PrBaseEntity implements Serializable {

    /**
     * 雇员ID
     */
    private String employeeId;

    /**
     * 雇员名称
     */
    private String employeeName;

    /**
     * 雇员英文名称
     */
    private String employeeNameEn;

    /**
     * 曾用名（如果有多个，名字之间用逗号分隔）
     */
    private String formerName;

    /**
     * 性别（0：其他  1 ：男   2：女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 证件类型(1. 身份证  2.护照  3.军（警）官证  4.士兵证   5.台胞证  6.回乡证  7.其他)
     */
    private Integer certificateType;

    /**
     * 证件号码
     */
    private String certificateNumber;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 城市三字码
     */
    private String cityCode;

    /**
     * 港澳台标志
     */
    private Boolean isHKMT;

    /**
     * 户口性质
     */
    private Integer personalProperty;

    /**
     * 档案所在地
     */
    private Integer archiveLocation;

    /**
     * 婚姻状况
     */
    private Integer marriage;

    /**
     * 结婚日期
     */
    private Date marriageDate;

    /**
     * 子女数量
     */
    private Integer childrenNumber;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 职位
     */
    private String position;

    /**
     * 入职日期
     */
    private Date onBoardDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 该雇员所在雇员组
     */
    List<PrEmployeeGroupEntity> employeeGroupEntities;

}