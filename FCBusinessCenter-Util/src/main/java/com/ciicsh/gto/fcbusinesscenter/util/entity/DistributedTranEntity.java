package com.ciicsh.gto.fcbusinesscenter.util.entity;

import com.mongodb.DBObject;

import java.util.List;

/**
 * Created by bill on 18/5/29.
 */
public class DistributedTranEntity {

    private String transactionName; // 事务名称
    private String batchCode;       // 批次号
    private int batchType;          // 批次类型
    private boolean completed;      // 整个事务是否完成

    private List<DBObject> events; // 参与分布式的事务列表

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }

    public List<DBObject> getEvents() {
        return events;
    }

    public void setEvents(List<DBObject> events) {
        this.events = events;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
