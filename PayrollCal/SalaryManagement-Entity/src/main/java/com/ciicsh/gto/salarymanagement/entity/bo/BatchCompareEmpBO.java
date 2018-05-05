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
     * 对比keys 暂时不使用保留今后使用
     */
    private String compareKeys;

    /**
     * 对比项目
     */
    private List<BatchCompareItemBO> itemList = new ArrayList<>();

    /**
     * 对比项存在于那个批次中 0: 都存在 1: 仅存在源批次 2: 仅存在于目标批次
     */
    private Integer inWhichBatch;
}
