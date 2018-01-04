package com.ciicsh.gto.fcbusinesscenter.tax.queryservice.api.dto;

import java.io.Serializable;
import java.util.Date;

public abstract class TestDTO implements Serializable {
    public enum RelationType {
        Product_Depended_Relation,
        Product_Excluded_Relation,
        Package_Depended_Relation,
        Package_Excluded_Relation;
    }
    private Integer relationId;
    private Boolean isActive;
    private Date dataChangeCreatetime;
    private Date dataChangeLasttime;
    private String createdBy;
    private String modifiedBy;

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getDataChangeCreatetime() {
        return dataChangeCreatetime;
    }

    public void setDataChangeCreatetime(Date dataChangeCreatetime) {
        this.dataChangeCreatetime = dataChangeCreatetime;
    }

    public Date getDataChangeLasttime() {
        return dataChangeLasttime;
    }

    public void setDataChangeLasttime(Date dataChangeLasttime) {
        this.dataChangeLasttime = dataChangeLasttime;
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
}
