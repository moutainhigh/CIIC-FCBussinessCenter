package com.ciicsh.gt1.fcbusinesscenter.engine;

import org.springframework.stereotype.Service;

@Service
public interface ComputeEngine {

    void processShardingCompute(String batchCode, int shardingIndex);

    void processCompute(String batchCode);
}
