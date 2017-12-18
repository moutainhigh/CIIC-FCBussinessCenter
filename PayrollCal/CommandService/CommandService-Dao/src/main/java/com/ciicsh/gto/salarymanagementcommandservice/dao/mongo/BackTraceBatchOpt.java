package com.ciicsh.gto.salarymanagementcommandservice.dao.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/5.
 */
@Component
public class BackTraceBatchOpt extends BaseOpt {

    private static final String BACK_TRACE_BATCH_TABLE = "back_trace_batch_table";

    public BackTraceBatchOpt() {
        super(BACK_TRACE_BATCH_TABLE);
    }
}
