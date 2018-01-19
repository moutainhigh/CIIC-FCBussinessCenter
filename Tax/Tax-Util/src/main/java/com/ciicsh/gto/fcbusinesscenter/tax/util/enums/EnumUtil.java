package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;

/**
 * @author wuhua
 */
public class EnumUtil {

    /**
     * 薪酬计算批次状态
     */
    public static final String BATCH_NO_STATUS = "BNS";
    /**
     * 证件类型
     */
    public static final String IT_TYPE = "IT";
    /**
     * 个税所得项目
     */
    public static final String INCOME_SUBJECT = "IS";
    /**
     * 完税凭证任务状态
     */
    public static final String VOUCHER_STATUS = "VS";
    /**
     * 业务任务状态
     */
    public static final String BUSINESS_STATUS = "BS";
    /**
     * 业务任务状态类型
     */
    public static final String BUSINESS_STATUS_TYPE = "BS_TYPE";

    /**
     * 获取枚举中文
     *
     * @param type
     * @param key
     * @return
     */
    public static final String getMessage(String type, String key) {

        if (StrKit.isNotEmpty(type) && StrKit.isNotEmpty(key)) {
            if (type.equals(EnumUtil.BATCH_NO_STATUS)) {
                return BatchNoStatus.valueOf(EnumUtil.BATCH_NO_STATUS + key).getMessage();
            } else if (type.equals(EnumUtil.IT_TYPE)) {
                return IdType.valueOf(EnumUtil.IT_TYPE + key).getMessage();
            } else if (type.equals(EnumUtil.INCOME_SUBJECT)) {
                return IncomeSubject.valueOf(EnumUtil.INCOME_SUBJECT + key).getMessage();
            } else if (type.equals(EnumUtil.VOUCHER_STATUS)) {
                return VoucherStatus.valueOf(EnumUtil.VOUCHER_STATUS + key).getMessage();
            } else if (type.equals(EnumUtil.BUSINESS_STATUS)) {
                return BusinessStatus.valueOf(EnumUtil.BUSINESS_STATUS + key).getMessage();
            } else if (type.equals(EnumUtil.BUSINESS_STATUS_TYPE)) {
                return BusinessStatusType.valueOf(key).getMessage();
            }
        }

        return null;
    }
}
