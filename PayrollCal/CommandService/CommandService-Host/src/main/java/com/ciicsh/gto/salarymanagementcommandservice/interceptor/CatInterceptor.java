package com.ciicsh.gto.salarymanagementcommandservice.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuyong
 * @date 2018/1/8
 */
@Component
public class CatInterceptor implements HandlerInterceptor {

    private ThreadLocal<Transaction> tranLocal = new ThreadLocal<Transaction>();
    private ThreadLocal<Transaction> pageLocal = new ThreadLocal<Transaction>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        Transaction t = Cat.newTransaction("URL", uri);
        Cat.logEvent("URL.Method", request.getMethod(), Message.SUCCESS, request.getRequestURL().toString());
        Cat.logEvent("URL.Host", request.getMethod(), Message.SUCCESS, request.getRemoteHost());
        tranLocal.set(t);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String viewName = modelAndView != null ? modelAndView.getViewName() : "null";
        Object res = modelAndView != null ? modelAndView.getModel() : "null";
        Transaction t = Cat.newTransaction("View", viewName);
        Cat.logEvent("Model", res.toString());
        pageLocal.set(t);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            Transaction tx = Cat.newTransaction("Invoice.Exception", ex.getClass().getName());
            Cat.logError("Invoice.Message", ex);
            Cat.logTrace("Invoice.Exception", ex.getClass().getName());
            tx.setStatus(Transaction.SUCCESS);
            tx.complete();
        }
        // 总计
        Transaction t = tranLocal.get();
        tranLocal.remove();
        t.setStatus(Transaction.SUCCESS);
        t.complete();
    }

}

