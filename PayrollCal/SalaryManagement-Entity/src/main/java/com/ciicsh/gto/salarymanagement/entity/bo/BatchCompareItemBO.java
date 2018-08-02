package com.ciicsh.gto.salarymanagement.entity.bo;

import lombok.Data;

/**
 * Created by NeoJiang on 2018/5/2.
 */
@Data
public class BatchCompareItemBO {

    private String mappingKey;

    private String srcValue;

    private String tgtValue;

    private String tgtTwoValue;

    /**
     * 当前批次
     * 0: 源批次
     * 1: 对比批次1
     * 2: 对比批次2
     */
    private Integer currentBatch;
}
