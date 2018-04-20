package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;

import java.io.Serializable;

/**
 * <p>
 * 计算批次信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-02
 */
public class PayrollBatchDTO extends PagingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 批次编号
     */
    private String batchCode;
    /**
     * 批次类型:1-正常、2-调整、3-回溯
     */
    private String batchType;
    /**
     * 管理方编号
     */
    private String managementId;
    /**
     * 管理方名称
     */
    private String managementName;
    /**
     * 实际计薪期间
     */
    private String actualPeriod;
    /**
     * 批次状态：
     1-新建
     2-计算中
     3-计算完成
     4-审核中
     5-审核完成
     6-关账
     7-已发放
     8-个税已申报
     */
    private Integer status;
    /**
     * 是否垫付：0 表示未垫付，1表示已经垫付
     */
    private Boolean hasAdvance;
    /**
     * 是否来款：0表示未来款，1表示已来款
     */
    private Boolean hasMoney;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getManagementName() {
        return managementName;
    }

    public void setManagementName(String managementName) {
        this.managementName = managementName;
    }

    public String getActualPeriod() {
        return actualPeriod;
    }

    public void setActualPeriod(String actualPeriod) {
        this.actualPeriod = actualPeriod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean isHasAdvance() {
        return hasAdvance;
    }

    public void setHasAdvance(Boolean hasAdvance) {
        this.hasAdvance = hasAdvance;
    }

    public Boolean isHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(Boolean hasMoney) {
        this.hasMoney = hasMoney;
    }
}
