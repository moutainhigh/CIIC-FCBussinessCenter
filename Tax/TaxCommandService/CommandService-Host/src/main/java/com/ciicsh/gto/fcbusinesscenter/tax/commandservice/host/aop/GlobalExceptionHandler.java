package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.message.AuthException;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 * @author wuhua
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //token异常
    private final String TOKEN_ERROR = "TOKEN_ERROR";

    //controller异常
//    private final String CL_ERROR = "CL_ERROR";

    /**
     * token异常处理
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public com.ciicsh.common.entity.JsonResult<Object> tokenExceptionHandler() {
        com.ciicsh.common.entity.JsonResult<Object> jr = new com.ciicsh.common.entity.JsonResult<>();
//        jr.fill(JsonResult.ReturnCode.TOKEN_ERROR);
        jr.setErrCode(TOKEN_ERROR);
        return jr;
    }

    /**
     * controller异常拦截
     */
    @ExceptionHandler(ControllerException.class)
    @ResponseBody
    public com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult<Object> controllerExceptionHandler() {
        com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult<Object> jr = new com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult<>();
        jr.error();
        return jr;
    }
}
