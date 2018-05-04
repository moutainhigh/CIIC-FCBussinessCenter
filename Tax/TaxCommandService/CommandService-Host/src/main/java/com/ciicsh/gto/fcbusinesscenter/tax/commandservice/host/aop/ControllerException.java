package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.aop;

/**
 * 自定义异常类型
 * @author wuhua
 */
public class ControllerException extends RuntimeException {

    public ControllerException() {
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
