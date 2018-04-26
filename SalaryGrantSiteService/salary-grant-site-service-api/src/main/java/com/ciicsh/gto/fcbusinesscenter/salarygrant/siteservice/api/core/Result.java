package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core;

import java.io.Serializable;
import java.util.List;

/**
 * int code，接口调用成功=0，错误码=其他值
 * T object，具体返回值
 * String error，字符串错误码，可选
 * String message，错误消息，可选，error=message可以配置成属性文件
 * Exception exception，异常消息，可选
 */
public class Result<T> implements Serializable {

    private int code;
    private T object;
    private List<T> records;
    private String error;
    private String message;
    private Exception exception;

    public Result() {
    }

    public Result(int code, String message, T object, List<T> records) {
        this.code = code;
        this.object = object;
        this.records = records;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", object=" + object +
                ", records=" + records +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", exception=" + exception +
                '}';
    }
}
