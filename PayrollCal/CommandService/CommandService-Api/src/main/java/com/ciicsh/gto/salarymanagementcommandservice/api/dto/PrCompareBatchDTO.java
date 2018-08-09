package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-08-06 17:17
 * @description 所有参与对比的批次PO
 */
@Data
public class PrCompareBatchDTO implements Serializable {
    /**
     * 序列化实体类
     */
    private static final long serialVersionUID = 1109931593270081075L;
    /**
     * 批次编号： 客户ID-计算日期-4位序号
     */
    private String code;

    // 薪资日期
    private String period;

    // 薪资帐套编码
    private String accountSetCode;

    // 薪资帐套名称
    private String accountSetName;

    // 管理方ID
    private String managementId;

    // 管理方名称
    private String managementName;

    // 开始日期
    private String beginDate;

    // 结束日期
    private String endDate;

    /**
     * 批次状态：
     * 1-新建
     * 2-计算中
     * 3-计算完成
     * 4-审核中
     * 5-审核完成
     * 6-关账
     * 7-已发放
     * 8-个税已申报
     */
    private int status;

    /**
     * 批次类型
     * 1,正常批次
     * 2,调整批次
     * 3,回溯批次
     * 4,测试批次
     */
    private Integer batchType;

    private int pageNum;

    private int pageSize;
}
