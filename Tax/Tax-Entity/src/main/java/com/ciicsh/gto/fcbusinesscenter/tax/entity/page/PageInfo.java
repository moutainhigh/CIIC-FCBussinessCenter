package com.ciicsh.gto.fcbusinesscenter.tax.entity.page;

import java.io.Serializable;

/**
 * @author yuantongqing on 2017/12/12
 */
public class PageInfo implements Serializable {
    /**
     * 当前页数
     */
    protected Integer currentNum;

    /**
     * 每页显示的数目
     */
    protected Integer pageSize;

    /**
     * 总数目
     */
    protected Integer totalNum;


    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", totalNum=" + totalNum +
                '}';
    }
}
