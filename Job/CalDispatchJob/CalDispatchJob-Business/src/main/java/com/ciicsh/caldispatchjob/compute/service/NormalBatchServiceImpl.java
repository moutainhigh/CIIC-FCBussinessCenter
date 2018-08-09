package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.TestBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class NormalBatchServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchServiceImpl.class);

    @Autowired
    private CommonServiceImpl commonService;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private TestBatchMongoOpt testBatchMongoOpt;

    @Autowired
    private EmpGroupServiceImpl empGroupService;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    /**
     * 批量更新或者插入数据
     *
     * @param batchCode
     * @param batchType
     * @return
     */
    public int batchInsertOrUpdateNormalBatch(String batchCode, int batchType) {

        PrCustBatchPO initialPO = new PrCustBatchPO();
        initialPO.setCode(batchCode);
        List<PrCustBatchPO> custBatchPOList = normalBatchMapper.selectBatchListByUseLike(initialPO);
        if (custBatchPOList == null || custBatchPOList.size() == 0) {
            return -1;
        }

        PrCustBatchPO batchPO = custBatchPOList.get(0);
        String empGroupCode = batchPO.getEmpGroupCode();
        List<DBObject> employees = empGroupService.getEmployeesByEmpGroupCode(empGroupCode);

        return commonService.batchInsertOrUpdateNormalBatch(batchCode, batchType, employees);
    }

    /**
     * 删除批次
     *
     * @param batchCodes
     * @return
     */
    public int deleteNormalBatch(String batchCodes, int batchType) {
        String[] codes = batchCodes.split(",");
        if (codes.length > 0) {
            int rowAffected;
            if (batchType == 4) { // 测试批次
                rowAffected = testBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
                logger.info("row affected : " + String.valueOf(rowAffected));
            } else { // 正常批次
                rowAffected = normalBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
                logger.info("row affected : " + String.valueOf(rowAffected));
            }
            return rowAffected;
        }
        return 0;
    }

    /**
     * 导入批次业务逻辑处理方法
     *
     * @param batchCode       新生成批次的batch_code
     * @param originBatchCode 源批次的batch_code
     */
    public int batchInsertOrUpdateImportBatch(String batchCode, String originBatchCode) {
        // 导入批次, 业务逻辑分为3步处理start
        // 1：取出MongoDB中源批次数据,originBatchCode为源批次batch_code
        Query queryOriginBatch = new Query(Criteria.where("batch_code").is(originBatchCode));
        List<DBObject> originBatchList = normalBatchMongoOpt.getMongoTemplate()
                .find(queryOriginBatch, DBObject.class, NormalBatchMongoOpt.PR_NORMAL_BATCH);
        // 2：循环拿到源批次中List的数据并copy到insertDBObject,删除insertDBObject的_id,替换batch_code为新批次的batch_code
        List<DBObject> insertBatchList = originBatchList.stream().map(dbObject -> {
            DBObject insertDBObject = dbObject;
            insertDBObject.removeField("_id");
            insertDBObject.put("batch_code", batchCode);

            return insertDBObject;
        }).collect(Collectors.toList());

        // 3：批量insert步骤2中的数据到MongoDB中, 返回插入行数
        return doBizTransaction(insertBatchList);
        // 导入批次, 业务逻辑分为3步处理end
    }

    /**
     * MongoDB中pr_normal_batch_table的批量操作
     *
     * @param batchList 批量操作List
     * @return 影响行数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    protected int doBizTransaction(List<DBObject> batchList) {
        int rowAffected = normalBatchMongoOpt.batchInsert(batchList);
        logger.info("Batch insert succeeded, Affected the number of rows: {}", rowAffected);
        return rowAffected;
    }
}
