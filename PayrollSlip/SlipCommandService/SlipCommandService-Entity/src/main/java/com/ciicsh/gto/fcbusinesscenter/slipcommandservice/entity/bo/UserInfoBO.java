package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo;

import java.util.Date;

/**
 * 用户信息BO类
 *
 * @author sunjian
 * @since 2018-3-5
 */
public class UserInfoBO {
    private String token;
    private Date expireTime;
    private String userId;
    private String belongOrg;
    private String customerId;
    private String cityCodeThree;
    private String cityName;
    private Long serviceOrganizationId;
    private String serviceOrganizationName;
    private String loginName;
    private String displayName;
    private Integer userType;
    private String employeeNumber;
    private String phone;
    private String extension;
    private String departmentId;
    private String email;
    private Integer userStatus;
    private String description;
    private Integer pswErrorCount;
    private String headPortrait;
    private String managerId;
    private Date lastLoginTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(String belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCityCodeThree() {
        return cityCodeThree;
    }

    public void setCityCodeThree(String cityCodeThree) {
        this.cityCodeThree = cityCodeThree;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getServiceOrganizationId() {
        return serviceOrganizationId;
    }

    public void setServiceOrganizationId(Long serviceOrganizationId) {
        this.serviceOrganizationId = serviceOrganizationId;
    }

    public String getServiceOrganizationName() {
        return serviceOrganizationName;
    }

    public void setServiceOrganizationName(String serviceOrganizationName) {
        this.serviceOrganizationName = serviceOrganizationName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPswErrorCount() {
        return pswErrorCount;
    }

    public void setPswErrorCount(Integer pswErrorCount) {
        this.pswErrorCount = pswErrorCount;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
