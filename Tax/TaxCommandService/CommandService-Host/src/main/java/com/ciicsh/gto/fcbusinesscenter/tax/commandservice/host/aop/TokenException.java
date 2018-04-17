package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.aop;

/**
 * 自定义异常类型：token验证异常
 * @author wuhua
 */
public class TokenException extends RuntimeException {

    public TokenException() {
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
