package com.ciicsh.gto.salarymanagement.entity.po.custom;

import lombok.Data;

/**
 * Created by bill on 17/12/14.
 */
@Data
public class PrCustSubBatchPO {

    /*原正常批次ID号*/
    private String originCode;

    /*当前批次ID号*/
    private String code;

    /*调整字段*/
    private String adjustFields;

    /*计算结果*/
    private String result;

    /*是否来款*/
    private Boolean hasMoneny;

    /*是否垫付*/
    private Boolean hasAdvance;

    /*批次类型 1表示调整，2表示回溯*/
    private int batchType;

    /*状态*/
    private int status;
}
