package com.ciicsh.gto.salarymanagementcommandservice.api.page;

import java.util.List;

/**
 * Created by NeoJiang on 2018/4/23.
 */
public class Pagination<T> {

    private int pageNum;
    private int pageSize;
    private long total;
    private List<T> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
