package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.interceptor;

import com.ciicsh.gto.commonservice.util.dto.Result;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.CommonTransform;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.exception.AuthException;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import com.ciicsh.gto.identityservice.api.IdentityServiceProxy;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

/**
 * 用户验证拦截器
 *
 * @author taka
 * @since 2018-3-5
 */
public class AuthenticateInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private IdentityServiceProxy identityServiceProxy;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headers = request.getHeader("Access-Control-Request-Headers");

        if (headers == null || Stream.of(headers.split(",")).noneMatch(header ->
                header.equalsIgnoreCase("token"))) {
            String token = request.getHeader("token");
            System.out.println("token: " + token);
            if (StringUtils.hasText(token)) {
                Result<UserInfoResponseDTO> result = identityServiceProxy.getUserInfoByToken(token);
                System.out.println("result.getCode(): " + result.getCode());
                System.out.println("result.getObject(): " + result.getObject());
                if (result.getCode() == 0) {
                    UserContext.setUser(CommonTransform.convertToEntity(result.getObject(), UserInfoBO.class));
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        UserContext.remove();
    }
}
