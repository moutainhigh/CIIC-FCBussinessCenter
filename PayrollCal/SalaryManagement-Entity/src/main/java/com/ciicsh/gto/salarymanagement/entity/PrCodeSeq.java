package com.ciicsh.gto.salarymanagement.entity;

import lombok.Data;

/**
 * @author jiangtianning
 */
@Data
public class PrCodeSeq {

    /**
     * id
     */
    private int id;

    /**
     * 序列名
     */
    private String seqName;

    /**
     * 当前序列值
     */
    private Long currentVal;

    /**
     * 增幅
     */
    private Long incrementVal;

}