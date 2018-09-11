package com.ciicsh.gt1.fcbusinesscenter.engine;

import com.ciicsh.caldispatchjob.entity.DroolsContext;
import com.ciicsh.caldispatchjob.entity.FuncEntity;
import com.ciicsh.gt1.fcbusinesscenter.config.XxlJobConfig;
import com.ciicsh.gt1.fcbusinesscenter.mongo.BatchMongoOpt;
import com.mongodb.DBObject;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

@Service
public class ComputeEngineImpl implements ComputeEngine {

    private static final Logger logger = LoggerFactory.getLogger(ComputeEngineImpl.class);

    private static final StampedLock sl = new StampedLock();

    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.ciicsh.gt1", artifactId = "payroll_common_function", version = "1.0.0-SNAPSHOT")
    private KieSession kSession;

    @Autowired
    private BatchMongoOpt batchMongoOpt;

    @Autowired
    private XxlJobConfig config;

    /**
     * 处理当前批次号的分片数据的薪资计算
     *
     * @param batchCode     批次号
     * @param shardingIndex 分片索引号：0 开始
     */
    @Override
    public void processShardingCompute(String batchCode, int shardingIndex) {
        int shardingCount = config.ShardingCount; //分片数量
        logger.info(String.format("batchCode = %s, shardingIndex = %d, shardingCount = %d", batchCode, shardingIndex, shardingCount));

        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);
        query.fields().include(""); //TODO
        query.skip(shardingIndex * shardingCount); //索引开始位置
        query.limit(shardingCount);

        List<DBObject> batchList = batchMongoOpt.getMongoTemplate().find(query, DBObject.class, BatchMongoOpt.PR_NORMAL_BATCH);

        int cores = Runtime.getRuntime().availableProcessors() - 1; // 获取当前服务器core数量－1
        final ForkJoinPool pool = new ForkJoinPool(cores); // fork multiple threads then join result
        try {
            //multiple cores parallel compute
            List<DBObject> resultList = pool.submit(() -> batchList.parallelStream().map(dbObject -> runCompute(dbObject,batchCode)).collect(Collectors.toList())).get();
            logger.info("get total: " + String.valueOf(resultList.size()));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 处理当前批次号的所有数据的薪资计算
     *
     * @param batchCode 批次号
     */
    @Override
    public void processCompute(String batchCode) {
        logger.info("batchCode = " + batchCode);
    }

    /**
     * 根据正则表达式匹配触发一个或者多个规则名
     *
     * @param context 函数名称列表对应规则名称
     * @return
     */
    private int fireRules(DroolsContext context) {
        if (context.getFuncEntityList() == null || context.getFuncEntityList().size() == 0) {
            return 0;
        }
        Set set = new HashSet();
        for (FuncEntity func : context.getFuncEntityList()) {
            set.add(func.getFuncName());
        }
        long stamp = sl.writeLock();
        try {
            FactHandle factHandle = kSession.insert(context); // 插入上下文信息
            int count = kSession.fireAllRules(new CustomAgendaFilter(set));
            kSession.delete(factHandle);
            logger.info(String.format("emp_code: %s, total excute rule counts: %d", context.getEmpPayItem().getEmpCode(), count));
            return count;
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            context.getFuncEntityList().clear();
            return 0;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    /**
     * 薪资计算
     * @param dbObject
     * @param batchCode
     * @return
     */
    private DBObject runCompute(DBObject dbObject, String batchCode) {
        return null;
    }
}
