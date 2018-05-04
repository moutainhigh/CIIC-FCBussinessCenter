package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.service.BatchService;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by NeoJiang on 2018/5/2.
 */
@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Override
    public HashMap<String, ?> compareBatch(String srcBatchCode, Integer srcBatchType,
                                           String tgtBatchCode, Integer tgtBatchType,
                                           List<String> compareKeys,
                                           LinkedHashMap<String, String> compareMap) {

        List<DBObject> srcBatchList = this.getBatchItemList(srcBatchType, srcBatchCode);
        List<DBObject> tgtBatchList = this.getBatchItemList(tgtBatchType, tgtBatchCode);
        if (srcBatchList == null || tgtBatchList == null || srcBatchList.size() == 0 || tgtBatchList.size() == 0) {
            throw new BusinessException("没有批次数据");
        }



        compareMap.forEach((k, v) -> {

        });

        //query = query.skip(pageindex*size).limit(size);

        return null;
    }

    private List<DBObject> getBatchItemList(Integer batchType, String batchCode) {

        List<DBObject> batchList = null;
        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN)
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
        ;

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        return batchList;
    }
}
