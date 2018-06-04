package com.ciicsh.caldispatchjob.entity;

/**
 * Created by bill on 18/6/2.
 */
public class BatchContext {
    /*薪资计算周期*/
    private String period;

    /*工作日历的最后一个天*/
    private int workCalendar;


    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getWorkCalendar() {
        return workCalendar;
    }

    public void setWorkCalendar(int workCalendar) {
        this.workCalendar = workCalendar;
    }
}
