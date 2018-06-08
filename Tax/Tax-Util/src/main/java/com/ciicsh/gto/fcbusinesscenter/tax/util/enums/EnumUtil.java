package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

import com.ciicsh.gto.fcbusinesscenter.tax.util.commondata.BasicData;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.gd.*;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz.CountryEnumsAboutSzOffline;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz.IdTypeAboutSzReductionOffline;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz.IdTypeEnumsAboutSzOffline;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz.IncomeSubjectEnumsAboutSzOffline;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.online.*;
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
     * 股权收入类型
     */
    public static final String STOCK_INCOME_TYPE = "STT";

    /**
     * 性别
     */
    public static final String GENDER_TYPE = "GENDER";

    /**
     * 线下模板证件类型(广东)
     */
    public static final String ID_TYPE_OFFLINE_GD = "ID_TYPE_OFFLINE_GD";

    /**
     * 线下模板国籍(广东)
     */
    public static final String COUNTRY_OFFLINE_GD = "COUNTRY_OFFLINE_GD";

    /**
     * 线下模板所得项目(广东)
     */
    public static final String INCOME_SUBJECT_OFFLINE_GD = "INCOME_SUBJECT_OFFLINE_GD";

    /**
     * 线下模板税款负担方式(广东)
     */
    public static final String BURDEN_OFFLINE_GD = "BURDEN_OFFLINE_GD";

    /**
     * 线下模板职务(广东)
     */
    public static final String POST_OFFLINE_GD = "POST_OFFLINE_GD";

    /**
     * 线下模板证件类型(广东-减免事项和商业保险)
     */
    public static final String ID_TYPE_OFFLINE_GD_REDUCTION = "ID_TYPE_OFFLINE_GD_REDUCTION";

    /**
     * 线下模板证件类型(深圳网页版)
     */
    public static final String ID_TYPE_OFFLINE_SZ = "ID_TYPE_OFFLINE_SZ";

    /**
     * 线下模板国籍(深圳网页版)
     */
    public static final String COUNTRY_OFFLINE_SZ = "COUNTRY_OFFLINE_GD";

    /**
     * 线下模板所得项目(深圳网页版)
     */
    public static final String INCOME_SUBJECT_OFFLINE_SZ = "INCOME_SUBJECT_OFFLINE_SZ";
    /**
     * 线下模板证件类型(深圳网页版-减免事项和商业保险)
     */
    public static final String ID_TYPE_OFFLINE_SZ_REDUCTION = "ID_TYPE_OFFLINE_SZ_REDUCTION";
    /**
     * 线上模板国籍(公用)
     */
    public static final String COUNTRY_ONLINE_COMMON = "COUNTRY_ONLINE_COMMON";
    /**
     * 线上证件类型(外国人永久居留证全写)
     */
    public static final String ID_TYPE_ONLINE_SIMPLE_COMMON = "ID_TYPE_ONLINE_SIMPLE_COMMON";
    /**
     * 线上证件类型(外国人永久居留证全写)
     */
    public static final String ID_TYPE_ONLINE_COMPLEX_COMMON = "ID_TYPE_ONLINE_COMPLEX_COMMON";
    /**
     * 线上模板职务
     */
    public static final String POST_LEVEL_ONLINE_COMMON = "POST_LEVEL_ONLINE_COMMON";
    /**
     * 线上模板税款负担方式
     */
    public static final String TAX_BURDENS_ONLINE_COMMON = "TAX_BURDENS_ONLINE_COMMON";
    /**
     * 线上模板支付地
     */
    public static final String PAY_PLACE_ONLINE_COMMON = "PAY_PLACE_ONLINE_COMMON";
    /**
     * 线上模板适用公式
     */
    public static final String APPLICABLE_FORMULA_ONLINE_COMMON = "APPLICABLE_FORMULA_ONLINE_COMMON";


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
//                return IncomeSubject.valueOf(EnumUtil.INCOME_SUBJECT + key).getMessage();
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
            } else if(type.equals(EnumUtil.STOCK_INCOME_TYPE)){
                for (StockIncomeType stt : StockIncomeType.values()) {
                    if (stt.getCode().equals(key)) {
                        return stt.getDesc();
                    }
                }
                return null;
            } else if(type.equals(EnumUtil.GENDER_TYPE)){
                for (Gender g : Gender.values()) {
                    if (g.getCode().equals(key)) {
                        return g.getDesc();
                    }
                }
                return null;
            } else if(type.equals(EnumUtil.ID_TYPE_OFFLINE_GD)){
                for (IdTypeEnumsAboutGdOffline item : IdTypeEnumsAboutGdOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.COUNTRY_OFFLINE_GD)){
                for (CountryEnumsAboutGdOffline item : CountryEnumsAboutGdOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            }  else if(type.equals(EnumUtil.INCOME_SUBJECT_OFFLINE_GD)){
                for (IncomeSubjectEnumsAboutGdOffline item : IncomeSubjectEnumsAboutGdOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.BURDEN_OFFLINE_GD)){
                for (TaxBurdensEnumsAboutGdOffline item : TaxBurdensEnumsAboutGdOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.POST_OFFLINE_GD)){
                for (PostEnumsAboutGdOffline item : PostEnumsAboutGdOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.ID_TYPE_OFFLINE_GD_REDUCTION)){
                for (IdTypeAboutGdReductionOffline item : IdTypeAboutGdReductionOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.ID_TYPE_OFFLINE_SZ)){
                for (IdTypeEnumsAboutSzOffline item : IdTypeEnumsAboutSzOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.COUNTRY_OFFLINE_SZ)){
                for (CountryEnumsAboutSzOffline item : CountryEnumsAboutSzOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            }  else if(type.equals(EnumUtil.INCOME_SUBJECT_OFFLINE_SZ)){
                for (IncomeSubjectEnumsAboutSzOffline item : IncomeSubjectEnumsAboutSzOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.ID_TYPE_OFFLINE_SZ_REDUCTION)){
                for (IdTypeAboutSzReductionOffline item : IdTypeAboutSzReductionOffline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.COUNTRY_ONLINE_COMMON)){
                for (CountryEnumsCommonOnline item : CountryEnumsCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.ID_TYPE_ONLINE_SIMPLE_COMMON)){
                for (IdTypeEnumsAboutSimpleCommonOnline item : IdTypeEnumsAboutSimpleCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.ID_TYPE_ONLINE_COMPLEX_COMMON)){
                for (IdTypeEnumsAboutComplexCommonOnline item : IdTypeEnumsAboutComplexCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.POST_LEVEL_ONLINE_COMMON)){
                for (PostEnumsAboutCommonOnline item : PostEnumsAboutCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.TAX_BURDENS_ONLINE_COMMON)){
                for (TaxBurdensEnumsAboutCommonOnline item : TaxBurdensEnumsAboutCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.PAY_PLACE_ONLINE_COMMON)){
                for (PayPlaceEnumsAboutCommonOnline item : PayPlaceEnumsAboutCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            } else if(type.equals(EnumUtil.APPLICABLE_FORMULA_ONLINE_COMMON)){
                for (ApplicableFormulaEnumsAboutCommonOnline item : ApplicableFormulaEnumsAboutCommonOnline.values()) {
                    if (item.getCode().equals(key)) {
                        return item.getDesc();
                    }
                }
                return "";
            }
        }

        return null;
    }
}
