package com.ciicsh.gto.salarymanagementcommandservice.service.mongo;

import com.ciicsh.gto.salarymanagementcommandservice.dao.mongo.NormalBathOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public class NormalBatchMongo {

    @Autowired
    private NormalBathOpt normalBathOpt;

    public void BatchInsertEmployees(){
        //normalBathOpt.insert();
    }

    public void QueryEmployee(){
        //normalBathOpt.query();
    }
}
