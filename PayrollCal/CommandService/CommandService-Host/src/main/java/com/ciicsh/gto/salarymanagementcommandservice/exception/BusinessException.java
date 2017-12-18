package com.ciicsh.gto.salarymanagementcommandservice.exception;

/**
 * Created by guwei on 2017/11/16.
 */
public class BusinessException extends RuntimeException{
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
