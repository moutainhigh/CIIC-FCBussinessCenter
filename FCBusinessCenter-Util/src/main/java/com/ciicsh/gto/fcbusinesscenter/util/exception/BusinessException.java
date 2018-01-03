package com.ciicsh.gto.fcbusinesscenter.util.exception;

/**
 * 业务异常类
 *
 * @author guwei
 * @date 2017-12-18
 */
public class BusinessException extends RuntimeException {
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
