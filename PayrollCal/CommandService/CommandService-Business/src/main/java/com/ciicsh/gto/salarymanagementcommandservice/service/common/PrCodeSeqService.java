package com.ciicsh.gto.salarymanagementcommandservice.service.common;

/**
 * Created by jiangtianning on 2017/11/23.
 * @author jiangtianning
 */
public interface PrCodeSeqService {

    /**
     * 获取seq
     * @param seqName
     * @return
     */
    long getCode(String seqName);
}
