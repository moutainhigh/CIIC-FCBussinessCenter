package com.ciicsh.gto.salarymanagementcommandservice.exception;

import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * 异常以及业务错误处理
 * 1.业务异常，可以在controller中抛出自定义的业务异常，将业务描述放入message中，这里将统一处理
 * 2.系统异常处理：异常一般是不可预知的，需要以server err的方式返回给前台
 * Created by guwei on 2017/11/16.
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    //捕捉业务异常
    @ExceptionHandler(value = {BusinessException.class})
    public Object handleSysIllegalArgumentException(BusinessException ex, WebRequest req) {
        ex.printStackTrace();
        return JsonResult.errorsInfo("BusinessException",ex.getMessage());
    }

    //捕捉系统异常
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex) {
        ex.printStackTrace();
        return JsonResult.errorsInfo("Exception","系统错误");
    }

}
