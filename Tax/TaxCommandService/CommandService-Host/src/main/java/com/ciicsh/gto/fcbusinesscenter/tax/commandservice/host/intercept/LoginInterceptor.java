package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept;

import com.ciicsh.gto.commonservice.util.dto.Result;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.aop.TokenException;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.HttpKit;
import com.ciicsh.gto.identityservice.api.IdentityServiceProxy;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * login的拦截器
 * @author wuhua
 */
@Aspect
@Component
public class LoginInterceptor{

    @Autowired
    private IdentityServiceProxy identityServiceProxy;

    @Pointcut("execution(* com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {

        try {
            HttpServletRequest request = HttpKit.getRequest();
            String headers = request.getHeader("Access-Control-Request-Headers");
            if (headers == null || Stream.of(headers.split(",")).noneMatch(header ->
                    header.equalsIgnoreCase("token"))) {
                String token = request.getHeader("token");
                if (StringUtils.isEmpty(token)) {
                    token = request.getParameter("token");
                }
                //获取登录信息
                Result<UserInfoResponseDTO> result = identityServiceProxy.getUserInfoByToken(token);
                if(result.getCode() != 0){
                    throw new TokenException("check token error...");
                }
                LoginInfoContext loginInfoContext = new LoginInfoContext();
                loginInfoContext.setResult(result);
                LoginInfoHolder.put(loginInfoContext);
            }else{
                throw new TokenException("check token error...");
            }
            return point.proceed();
        }catch(Exception e){
            throw new TokenException("check token error...");
        }finally {
            LoginInfoHolder.remove();
        }
    }
}
