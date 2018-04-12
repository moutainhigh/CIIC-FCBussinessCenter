package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by bill on 18/2/2.
 */
@Service
public class AdjustBatchServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(AdjustBatchServiceImpl.class);

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    /**
     * 删除批次
     * @param batchCodes
     * @return
     */
    public int deleteAdjustBatch(String batchCodes) {
        String[] codes = batchCodes.split(",");
        if (codes.length > 0) {
            int rowAffected = adjustBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
            logger.info("row affected : " + String.valueOf(rowAffected));
            return rowAffected;
        }
        return 0;
    }

}
