package com.ciicsh.gto.salarymanagement.entity.po;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 雇员主数据册数
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-08
 */
@TableName("pr_employee_test")
public class PrEmployeeTestPO extends Model<PrEmployeeTestPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 终身雇员ID
     */
    @TableId("employee_id")
	private String employeeId;
    /**
     * 证件类别  
1:身份证 2:护照 3:军(警)官证 4:士兵证 5:台胞证 6:回乡证 7:其他
     */
	@TableField("id_card_type")
	private Integer idCardType;
    /**
     * 证件号
     */
	@TableField("id_num")
	private String idNum;
    /**
     * 雇员姓名
     */
	@TableField("employee_name")
	private String employeeName;
    /**
     * 曾用名
     */
	@TableField("former_name")
	private String formerName;
    /**
     * 所属管理方ID
     */
	@TableField("management_id")
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
	@TableField("join_date")
	private Date joinDate;
    /**
     * 国家代码
     */
	@TableField("country_code")
	private String countryCode;
    /**
     * 省份代码
     */
	@TableField("province_code")
	private String provinceCode;
    /**
     * 城市代码
     */
	@TableField("city_code")
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
	@TableField("created_time")
	private Date createdTime;
    /**
     * 最后修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;
    /**
     * 数据创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 最后修改人
     */
	@TableField("modified_by")
	private String modifiedBy;


	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(Integer idCardType) {
		this.idCardType = idCardType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getFormerName() {
		return formerName;
	}

	public void setFormerName(String formerName) {
		this.formerName = formerName;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	protected Serializable pkVal() {
		return this.employeeId;
	}

	@Override
	public String toString() {
		return "PrEmployeeTestPO{" +
			"employeeId=" + employeeId +
			", idCardType=" + idCardType +
			", idNum=" + idNum +
			", employeeName=" + employeeName +
			", formerName=" + formerName +
			", managementId=" + managementId +
			", gender=" + gender +
			", birthday=" + birthday +
			", countryCode=" + countryCode +
			", provinceCode=" + provinceCode +
			", cityCode=" + cityCode +
			", department=" + department +
			", position=" + position +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
