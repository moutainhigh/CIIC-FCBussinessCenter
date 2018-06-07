package com.ciicsh.gto.fcbusinesscenter.util.entity;

/**
 * Created by bill on 18/6/4.
 */
public enum TransactionStatusEnum {

    UNCOMPLETE(0),
    COMPLETE(1);

    private int value;

    private TransactionStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
