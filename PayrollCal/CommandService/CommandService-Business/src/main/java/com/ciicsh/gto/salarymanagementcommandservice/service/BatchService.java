package com.ciicsh.gto.salarymanagementcommandservice.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by NeoJiang on 2018/5/2.
 */
public interface BatchService {

    /**
     * 对比批次
     * @param sourceBatchCode
     * @param targetBatchCode
     * @return
     */
    HashMap<String, ?> compareBatch(String sourceBatchCode,
                                    Integer srcBatchType,
                                    String targetBatchCode,
                                    Integer tgtBatchType,
                                    LinkedHashMap<String, String> compareMap);

}
