package com.ciicsh.gto.fcbusinesscenter.site.service.host.aop;


import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



/**
 * package: com.ciicsh.gto.agentcenter.site.service.host.aop
 * describe: TODO
 * creat_user: cuixiaoguang
 * creat_date: 2018/1/15
 * creat_time: 10:16
 **/
@Aspect
@Component
public class ControllerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

	/**
	 * 定义拦截规则：拦截com.ciicsh.gto.agentcenter.site.service包下面的所有类中的方法。
	 */
	@Pointcut("execution(* com.ciicsh.gto.fcbusinesscenter.site.service..*(..))")
	public void controllerMethodPointcut(){}

	/**
	 * 拦截器具体实现
	 * @param pjp
	 * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
	 */
	@Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.ciicsh.gto.agentcenter.........)”写进这里
	public Object Interceptor(ProceedingJoinPoint pjp){
		long beginTime = System.currentTimeMillis();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod(); //获取被拦截的方法
		String methodName = method.getName(); //获取被拦截的方法名

		Set<Object> allParams = new LinkedHashSet<>();

		logger.info("请求开始，方法：{}", methodName);

		Object result = null;

		Object[] args = pjp.getArgs();
		for(Object arg : args){
			//logger.debug("arg: {}", arg);
			if (arg instanceof Map<?, ?>) {
				//提取方法中的MAP参数，用于记录进日志中
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) arg;

				allParams.add(map);
			}else if(arg instanceof HttpServletRequest){
				HttpServletRequest request = (HttpServletRequest) arg;

				//获取query string 或 posted form data参数
				Map<String, String[]> paramMap = request.getParameterMap();
				if(paramMap!=null && paramMap.size()>0){
					allParams.add(paramMap);
				}
			}else if(arg instanceof HttpServletResponse){
				//do nothing...
			}else{
				//allParams.add(arg);
			}
		}

		try {
			if(result == null){
				// 一切正常的情况下，继续执行被拦截的方法
				result = pjp.proceed();
			}
		} catch (Throwable e) {
			logger.info("exception: ", e);
			//result = new Result<>(ResultCode.EXCEPTION, "发生异常："+e.getMessage());
		}

//		if(result instanceof JsonResult){
//			long costMs = System.currentTimeMillis() - beginTime;
//			logger.info("{}请求结束，耗时：{}ms", methodName, costMs);
//		}

		return result;
	}
}