package com.ciicsh.gto.salarymanagementcommandservice.dao.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/5.
 */
@Component
public class AdjustBatchOpt extends BaseOpt {

    private static final String ADJUST_BATCH_TABLE = "adjust_batch_table";

    public AdjustBatchOpt() {
        super(ADJUST_BATCH_TABLE);
    }
}
