package com.ciicsh.gto.salarymanagementcommandservice.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
                                    List<String> compareKeys,
                                    LinkedHashMap<String, String> compareMap);

}
