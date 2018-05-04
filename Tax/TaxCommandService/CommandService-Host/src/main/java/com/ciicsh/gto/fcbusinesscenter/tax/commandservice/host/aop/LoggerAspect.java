package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.aop;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.SourceTypeForClass;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常日志切面
 * @author wuhua
 */
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("execution(* com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller.*.*(..))")
    public void cutService() {
    }

    @AfterThrowing(value = "cutService()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, Throwable e) {

        //获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        //自定义类型编号
        String sourceType = null;
        //日志接口source参数
        String source = null;
        try {
            sourceType = SourceTypeForClass.valueOf(className).getMessage();
            source = EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, sourceType);
        } catch (IllegalArgumentException e1) {
            source = "其他";
        }

        //日志接口title参数
        String title = className + "." + methodName;

        //日志接口tags参数
        Object[] params = joinPoint.getArgs();
        Map<String, String> tags = new HashMap<>(params.length);
        try {
            for(int i = 0; i<params.length; i++){
                tags.put("tags"+i,params[i].toString());
            }
        } catch (Exception e1) {
        }
        LogTaskFactory.getLogger().error(e, title, source, LogType.APP,tags);

        //抛出异常，由全局异常处理器捕获后组织返回值
        throw new ControllerException();

    }
}
