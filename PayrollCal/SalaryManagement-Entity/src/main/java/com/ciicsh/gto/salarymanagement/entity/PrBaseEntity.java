package com.ciicsh.gto.salarymanagement.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by jiangtianning on 2017/10/25.
 */
public abstract class PrBaseEntity {

    /**
     * 是否启用
     */
    @Getter @Setter protected Boolean isActive;

    // 标准时间戳字段
    /**
     * 创建时间
     */
    @Getter @Setter protected Date dataChangeCreateTime;

    /**
     * 创建者登录名
     */
    @Getter @Setter protected String createdBy;

    /**
     * 更新时间
     */
    @Getter @Setter protected Date dataChangeLastTime;

    /**
     * 更新者登录名
     */
    @Getter @Setter protected String modifiedBy;
}
