package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core;


import java.util.Collections;
import java.util.List;

public class Pagination<T> {
    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();
    /**
     * 总数
     */
    private int total;

    /**
     * 每页显示条数，默认 10
     */
    private int size = 10;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页
     */
    private int current = 1;

    public Pagination() {
    }

    public Pagination(int current, int size) {
        this.current = current;
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}

