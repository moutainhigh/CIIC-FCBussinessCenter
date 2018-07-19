package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;

import java.io.Serializable;

/**
 * <p>
 * 日志响应DTO
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-16
 */
public class LogResponseDTO extends PagingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作对象的id
     */
    private String id;
    /**
     * 操作名称
     */
    private String act;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作对象
     */
    private String objectName;
    /**
     * 对象的值
     */
    private Object object;
    /**
     * 原对象的值
     */
    private Object oldObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getOldObject() {
        return oldObject;
    }

    public void setOldObject(Object oldObject) {
        this.oldObject = oldObject;
    }
}
