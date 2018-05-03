package com.ciicsh.gto.salarymanagement.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

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

    // 对比列mapping
    private LinkedHashMap<String, String> mapping = new LinkedHashMap<>();
}
