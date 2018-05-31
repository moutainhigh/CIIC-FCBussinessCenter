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
@TableName("pr_employee")
public class PrEmployeePO extends Model<PrEmployeePO> {

    private static final long serialVersionUID = 1L;

    /**
     * 终身雇员ID
     */
    @TableId("employee_id")
	private String employeeId;

	/**
	 * 员工ID
	 */
	@TableField("emp_code")
    private String empCode;
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
	 * 离职日期
	 */
	@TableField("leave_date")
	private Date outDate;

	/**
	 * 入职日期
	 */
	@TableField("join_date")
	private Date inDate;
	/**
	 * 国家代码
	 */
	@TableField("country_code")
	private String countryCode;
	/**
	 * 国家名
	 */
	@TableField("country_name")
	private String countryName;
	/**
	 * 省份代码
	 */
	@TableField("province_code")
	private String provinceCode;
	/**
	 * 省份
	 */
	@TableField("province_name")
	private String provinceName;
	/**
	 * 城市代码
	 */
	@TableField("city_code")
	private String cityCode;
	/**
	 * 城市代码
	 */
	@TableField("city_name")
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
	/**
	 * 是否有效
	 */
	@TableField("is_active")
	private Boolean isActive;

	@TableField("company_id")
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}

	@Override
	protected Serializable pkVal() {
		return this.employeeId;
	}

	@Override
	public String toString() {
		return "PrEmployeePO{" +
			"employeeId=" + employeeId +
				"empCode=" + empCode +
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
