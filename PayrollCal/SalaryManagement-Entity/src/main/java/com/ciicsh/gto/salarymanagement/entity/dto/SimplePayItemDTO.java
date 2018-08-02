package com.ciicsh.gto.salarymanagement.entity.dto;

/**
 * Created by bill on 17/12/27.
 */
public class SimplePayItemDTO {

    private String name;     //薪资项名称
    private Object val;      //薪资项值
    private int dataType;    // 数据格式: 1-文本,2-数字,3-日期,4-布尔
    private double amount;   //统计该薪资项的和

    /**
     * 薪资项类型：
     1 - 固定输入项，一次初始化，很少变更，建立批次时一般直接使用
     2 - 可变输入项，每建立一个批次，需要从FC客户中心服务接口导入值
     3-计算项，通过配置好的公式计算得出的薪资项，如：加班工资，病假工资等
     */
    private int itemType;

    private int display;

    private boolean canLock;

    private boolean locked;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCanLock() {
        return canLock;
    }

    public void setCanLock(boolean canLock) {
        this.canLock = canLock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
