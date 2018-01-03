package com.ciicsh.gto.fcbusinesscenter.util.page;

/**
 * PageUtil
 *
 * @author guwei
 * @date 2017-12-27
 */
public class PageUtil {

    private static final Integer defaultPageNum = 1;

    private static final Integer defaultPageSize = 10;

    public static Integer setPageNum(Integer pageNum) {
        if (pageNum != null) {
            return pageNum;
        }
        return defaultPageNum;
    }

    public static Integer setPageSize(Integer pageSize) {
        if (pageSize != null) {
            return pageSize;
        }
        return defaultPageSize;
    }
}
