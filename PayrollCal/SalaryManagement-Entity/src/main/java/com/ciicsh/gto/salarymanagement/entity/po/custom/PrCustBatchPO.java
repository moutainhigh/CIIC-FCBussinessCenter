package com.ciicsh.gto.salarymanagement.entity.po.custom;

import lombok.Data;

/**
 * Created by bill on 17/12/9.
 */
@Data
public class PrCustBatchPO {
    /**
     * 批次编号： 客户ID-计算日期-4位序号
     */
    private String code;

    /**
     * 批次类型
     */
    private Integer batchType;

    //管理方ID
    private String managementId;

    //管理方名称
    private String managementName;

    //薪资帐套编码
    private String accountSetCode;

    //薪资帐套名称
    private String accountSetName;

    //薪资日期
    private String period;

    //薪资开始日
    private int startDay;

    //开始日期
    private String beginDate;

    //薪资结束日
    private int endDay;

    //结束日期
    private String endDate;

    /**
     * 批次状态：
     1-新建
     2-计算中
     3-计算完成
     4-审核中
     5-审核完成
     6-关账
     7-已发放
     8-个税已申报
     */
    private int status;

    //雇员组名称
    private String empGroupName;

    //雇员组编码
    private String empGroupCode;

    //薪资组名称
    private String prGroupName;

    //薪资组编码
    private String prGroupCode;

    //薪资期间类型：本月、上月、下月
    private int payrollType;

    //是否继承薪资组模版
    private boolean isTemplate;

}
