package com.ciicsh.gto.salarymanagement.entity.bo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeoJiang on 2018/5/5.
 */
@Data
public class BatchCompareEmpBO {

    /**
     * 雇员ID
     */
    private String employeeId;

    /**
     * 雇员名称
     */
    private String employeeName;

    /**
     * 公司编号
     */
    private String companyId;

    /**
     * 对比keys 暂时不使用保留今后使用
     */
    private String compareKeys;

    /**
     * 对比项目
     */
    private List<BatchCompareItemBO> itemList = new ArrayList<>();

    /**
     * 当前雇员存在于哪个批次中
     * 0: 存在于源批次
     * 1: 存在于对比批次1
     * 2: 存在于对比批次2
     * 99: 都存在
     */
    private Integer inWhichBatchForEmpId;

}
