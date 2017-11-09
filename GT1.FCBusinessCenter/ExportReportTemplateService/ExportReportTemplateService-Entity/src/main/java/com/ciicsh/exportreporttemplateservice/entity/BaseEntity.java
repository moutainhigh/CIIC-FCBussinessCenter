package com.ciicsh.exportreporttemplateservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by houwanhua on 2017/11/9.
 */
public class BaseEntity {
    /**
     * 是否启用
     */
    @Getter
    @Setter
    protected Boolean isActive;

    // 标准时间戳字段
    /**
     * 创建时间
     */
    @Getter @Setter protected Date datachangeCreatetime;

    /**
     * 创建者登录名
     */
    @Getter @Setter protected String createdby;

    /**
     * 更新时间
     */
    @Getter @Setter protected Date datachangeLasttime;

    /**
     * 更新者登录名
     */
    @Getter @Setter protected String modifiedby;
}
