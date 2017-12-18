package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by guwei on 2017/11/16
 * @author guwei
 */
@Data
@AllArgsConstructor
public class ResultEntity<T> implements Serializable {
    /**
     * 操作是否成功，默认为true
     */
    private boolean isSuccess = true;
    /**
     * 提示给用户看的信息，不管是成功还是失败的
     */
    private String message;
    /**
     * 错误码
     */
    private String errCode;
    /**
     * 返回给前端的数据对象
     */
    private T data;

    public ResultEntity(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    /**
     * 返回一条信息，自定义成功或失败状态
     * @param success
     * @param message
     * @return
     */
    public static ResultEntity message(boolean success, String message) {
        return new ResultEntity(success, message);
    }

    /**
     * 失败，自定义错误提示
     * @param message
     * @return
     */
    public static ResultEntity faultMessage(String message) {
        return new ResultEntity(false, message = message);
    }

    /**
     * 失败，使用默认提示
     * @return
     */
    public static ResultEntity faultMessage() {
        return new ResultEntity(false, "操作失败");
    }

    /**
     * 成功：有需要返回Date信息,自定义成功提示
     * @param data
     * @param message
     * @return
     */
    public static ResultEntity success(Object data, String message) {
        return new ResultEntity(true, message, null, data);
    }

    /**
     * 成功：有需要返回Date信息
     * @param data
     * @return
     */
    public static ResultEntity success(Object data) {
        return new ResultEntity(true, "操作成功", null, data);
    }

    /**
     * 失败：返回错误信息
     * @param errCode
     * @param message
     * @return
     */
    public static ResultEntity errorsInfo(String errCode, String message) {
        return new ResultEntity(false, message, errCode, null);
    }
}
