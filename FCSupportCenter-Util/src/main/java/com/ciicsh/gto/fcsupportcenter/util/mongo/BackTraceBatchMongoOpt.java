package com.ciicsh.gto.fcsupportcenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/14.
 */
@Component
public class BackTraceBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(BackTraceBatchMongoOpt.class);

    private static final String PR_BACK_TRACE_BATCH  = "pr_back_trace_batch_table";

    public BackTraceBatchMongoOpt() {
        super(PR_BACK_TRACE_BATCH);
    }


}
