package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by bill on 17/12/9.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrCustomBatchDTO {

    /**
     * 批次编号： 客户ID-计算日期-4位序号
     */
    private String code;

    /**
     * 所属管理方ID
     */
    private String managementId;

    //管理方名称
    private String managementName;

    //雇员组名称
    private String empGroupName;

    /**
     * 薪资账套ID
     */
    private String accountSetId;

    //薪资帐套名称
    private String acccSetName;

    //薪资帐套设置的工资开始日
    private int startDay;

    //薪资帐套设置的工资结束日
    private int endDay;


    //薪资开始日期
    private String salaryBegin;

    //薪资结束日期
    private String salaryEnd;

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
    /**
     * 薪资期间 年/月
     */
    private String period;

}
