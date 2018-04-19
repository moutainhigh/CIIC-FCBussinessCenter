package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept;

/**
 * 获取当前session的工具类
 * @author wuhua
 */
public class LoginInfoHolder {

    private static ThreadLocal<LoginInfoContext> tl = new ThreadLocal<LoginInfoContext>();

    public static void put(LoginInfoContext s) {
        tl.set(s);
    }

    public static LoginInfoContext get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }

}
