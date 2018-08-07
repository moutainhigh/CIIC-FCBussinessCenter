package com.ciicsh.gto.salarymanagement.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Created by NeoJiang on 2018/4/28.
 */
@Data
@NoArgsConstructor
public class BatchCompareRequestDTO {

    // 源批次
    private String src;

    // 源批次类型
    private Integer srcBatchType;

    // 对比批次
    private String tgt;

    // 对比批次类型
    private Integer tgtBatchType;

    // 对比批次2
    private String tgtTwo;

    // 对比批次2类型
    private Integer tgtTwoBatchType;

    // 对比key
    private String compareKeysStr;

    // 源批次与对比批次的对比列映射List
    private List<Map<String, String>> compareMappingList = new ArrayList<>();

    /**
     * 对比批次的数量
     */
    private Integer batchCount;
}
