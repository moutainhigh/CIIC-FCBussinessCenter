package com.ciicsh.gto.salarymanagement.entity.dto;

import java.util.List;

/**
 * Created by bill on 18/4/22.
 */
public class AdjustItem {
    private String itemName;
    private Object before;
    private Object after;
    private Object gap;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Object getBefore() {
        return before;
    }

    public void setBefore(Object before) {
        this.before = before;
    }

    public Object getAfter() {
        return after;
    }

    public void setAfter(Object after) {
        this.after = after;
    }

    public Object getGap() {
        return gap;
    }

    public void setGap(Object gap) {
        this.gap = gap;
    }


}
