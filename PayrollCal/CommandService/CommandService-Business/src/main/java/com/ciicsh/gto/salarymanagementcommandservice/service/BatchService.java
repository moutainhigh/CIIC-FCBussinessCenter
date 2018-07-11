package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareEmpBO;
import com.ciicsh.gto.salarymanagement.entity.po.AdvanceBatchInfoPO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by NeoJiang on 2018/5/2.
 */
public interface BatchService {

    /**
     * @param sourceBatchCode
     * @param srcBatchType
     * @param targetBatchCode
     * @param tgtBatchType
     * @param compareKeys
     * @param compareMap
     * @return
     */
    List<BatchCompareEmpBO> compareBatch(String sourceBatchCode,
                                         Integer srcBatchType,
                                         String targetBatchCode,
                                         Integer tgtBatchType,
                                         List<String> compareKeys,
                                         LinkedHashMap<String, String> compareMap);

    int updateAdvancedBatch(AdvanceBatchInfoPO advanceBatchInfoPO);

}
