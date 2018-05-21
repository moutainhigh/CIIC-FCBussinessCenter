package com.ciicsh.gto.salarymanagementcommandservice.aop;

import com.ciicsh.common.entity.JsonResult;
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

    private final String TOKEN_ERROR = "TOKEN_ERROR";
    /**
     * token异常处理
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public JsonResult<Object> tokenExceptionHandler() {
        JsonResult<Object> jr = new JsonResult<>();
        jr.setErrCode(TOKEN_ERROR);
        return jr;
    }
}
