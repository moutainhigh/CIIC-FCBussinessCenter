package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

import com.ciicsh.gto.fcbusinesscenter.tax.util.commondata.BasicData;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;

import java.util.Map;

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
     * 任务状态
     */
    public static final String TASK_STATUS = "TS";

    /**
     * 日志来源(log平台使用)
     */
    public static final String SOURCE_TYPE = "ST";

    /**
     * 划款状态(记录结算中心划款状态使用)
     */
    public static final String PAY_STATUS = "PS";
    /**
     * 批次类型
     */
    public static final String BATCH_TYPE = "BT";

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
                for (BatchNoStatus b : BatchNoStatus.values()) {
                    if (b.getCode().equals(key)) {
                        return b.getDesc();
                    }
                }
                return null;
            } else if (type.equals(EnumUtil.IT_TYPE)) {
                String value = "";
                Map<String,String> map = BasicData.getInstance().getCertType();
                if(map.containsKey(key)){
                    value = map.get(key);
                }
                return value;
            } else if (type.equals(EnumUtil.INCOME_SUBJECT)) {
                for (IncomeSubject is : IncomeSubject.values()) {
                    if (is.getCode().equals(key)) {
                        return is.getDesc();
                    }
                }
                return null;
            } else if (type.equals(EnumUtil.VOUCHER_STATUS)) {
                return VoucherStatus.valueOf(EnumUtil.VOUCHER_STATUS + key).getMessage();
            } else if (type.equals(EnumUtil.BUSINESS_STATUS)) {
                return BusinessStatus.valueOf(EnumUtil.BUSINESS_STATUS + key).getMessage();
            } else if (type.equals(EnumUtil.BUSINESS_STATUS_TYPE)) {
                return BusinessStatusType.valueOf(key).getMessage();
            } else if (type.equals(EnumUtil.TASK_STATUS)) {
                return TaskStatus.valueOf(EnumUtil.TASK_STATUS + key).getMessage();
            } else if (type.equals(EnumUtil.SOURCE_TYPE)) {
                return SourceTypeForLog.valueOf(EnumUtil.SOURCE_TYPE + key).getMessage();
            } else if(type.equals(EnumUtil.PAY_STATUS)){
                return PayStatus.valueOf(EnumUtil.PAY_STATUS + key).getMessage();
            } else if(type.equals(EnumUtil.BATCH_TYPE)){
                for (BatchType is : BatchType.values()) {
                    if (is.getCode().equals(key)) {
                        return is.getDesc();
                    }
                }
                return null;
            }
        }

        return null;
    }
}
