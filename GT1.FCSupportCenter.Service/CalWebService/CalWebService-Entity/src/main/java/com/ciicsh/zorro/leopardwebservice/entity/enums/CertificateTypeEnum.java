package com.ciicsh.zorro.leopardwebservice.entity.enums;

/**
 * Created by jiangtianning on 2017/9/12.
 */
public enum CertificateTypeEnum implements IValuedEnum {
    ID_CARD(1), //身份证
    PASSPORT(2), //护照
    OFFICER_CARD(3), //军警官证
    MILITARY_CARD(4), //士兵证
    TAIWAN_CARD(5), //台胞证
    RETURN_CARD(6), //回乡证
    OTHERS(7); //其他

    private int value;

    CertificateTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
