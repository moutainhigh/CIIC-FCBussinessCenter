package com.ciicsh.gto.fcsupportcenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/14.
 */
@Component
public class AdjustBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(AdjustBatchMongoOpt.class);

    private static final String PR_ADJUST_BATCH  = "pr_adjust_batch_table";

    public AdjustBatchMongoOpt() {
        super(PR_ADJUST_BATCH);
    }

}