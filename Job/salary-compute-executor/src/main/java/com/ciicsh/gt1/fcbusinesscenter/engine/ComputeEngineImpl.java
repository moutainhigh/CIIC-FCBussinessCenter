package com.ciicsh.gt1.fcbusinesscenter.engine;

import com.ciicsh.gt1.fcbusinesscenter.config.XxlJobConfig;
import com.ciicsh.gt1.fcbusinesscenter.mongo.BatchMongoOpt;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComputeEngineImpl implements ComputeEngine {

    private static Logger logger = LoggerFactory.getLogger(ComputeEngineImpl.class);

    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.ciicsh.gt1", artifactId = "payroll_common_function", version = "1.0.0-SNAPSHOT")
    private KieSession kSession;

    @Autowired
    private BatchMongoOpt batchMongoOpt;

    @Autowired
    private XxlJobConfig config;

    /**
     * 处理当前批次号的分片数据的薪资计算
     * @param batchCode 批次号
     * @param shardingIndex 分片索引号：0 开始
     */
    @Override
    public void processShardingCompute(String batchCode, int shardingIndex) {
        int shardingCount = config.ShardingCount; //分片数量
        logger.info(String.format("batchCode = %s, shardingIndex = %d, shardingCount = %d", batchCode, shardingIndex,shardingCount));
    }

    /**
     * 处理当前批次号的所有数据的薪资计算
     * @param batchCode 批次号
     */
    @Override
    public void processCompute(String batchCode) {
        logger.info("batchCode = " + batchCode);
    }
}
