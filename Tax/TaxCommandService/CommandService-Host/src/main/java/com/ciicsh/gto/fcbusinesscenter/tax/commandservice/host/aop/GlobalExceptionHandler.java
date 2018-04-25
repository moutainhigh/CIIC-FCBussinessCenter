package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.aop;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
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

    /**
     * token异常拦截
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public JsonResult<Object> tokenExceptionHandler() {
        JsonResult<Object> jr = new JsonResult<>();
        jr.fill(JsonResult.ReturnCode.TOKEN_ERROR);
        return jr;
    }
}
