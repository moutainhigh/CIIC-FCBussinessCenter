package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo;

public class UserContext {
    private static ThreadLocal<UserInfoBO> threadLocal = new ThreadLocal();

    /**
     * 设置用户信息
     *
     * @param userInfoBO
     */
    public static void setUser(UserInfoBO userInfoBO) {
        threadLocal.set(userInfoBO);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserInfoBO getUser() {
        return threadLocal.get();
    }

    /**
     * 移动ThreadLocal中的用户信息
     */
    public static void remove() {
        threadLocal.remove();
    }
}
