package com.ciicsh.gto.salarymanagement.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by jiangtianning on 2017/11/14.
 */
@Data
public class PrFixedInputItemEntity extends PrBaseEntity{

    /**
     * 导入固定项Seq
     */
    private Integer fixedInputItemId;

    /**
     * 管理方ID
     */
    private String managementId;

    /**
     * 雇员ID
     */
    private String employeeId;

    /**
     * 薪资项ID
     */
    private String prItemId;

    /**
     * 薪资项名称
     */
    private String prItemName;

    /**
     * 薪资项计算结果
     */
    private BigDecimal prItemValue;
}
