package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.exception;



import com.ciicsh.gto.fcsupportcenter.util.exception.BusinessException;
import com.ciicsh.gto.fcsupportcenter.util.result.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * 异常以及业务错误处理
 * 1.业务异常，可以在controller中抛出自定义的业务异常，将业务描述放入message中，这里将统一处理
 * 2.系统异常处理：异常一般是不可预知的，需要以server err的方式返回给前台
 * @author guwei
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    /**
     * 捕捉业务异常
     * @param ex
     * @param req
     * @return
     */
    @ExceptionHandler(value = {BusinessException.class})
    @ResponseBody
    public Object handleSysIllegalArgumentException(BusinessException ex, WebRequest req) {
        ex.printStackTrace();
        return JsonResult.errorsInfo("BusinessException",ex.getMessage());
    }

    /**
     * 捕捉系统异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception ex) {
        ex.printStackTrace();
        return JsonResult.errorsInfo("Exception","系统错误");
    }

}
