package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.CalResultMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import com.ciicsh.gt1.config.MongoConfig;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.FcPayrollCalcResultMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.FcPayrollCalcResultService;
import java.util.*;

/**
 * 薪资计算批次结果表(雇员维度) 服务实现类
 *
 * @author taka
 * @since 2018-02-09
 */
@Service
public class FcPayrollCalcResultServiceImpl implements FcPayrollCalcResultService {



    @Autowired
    private MongoConfig mongoConfig;




    @Override
    public List<Document> listFcPayrollCalcResults(String params) {

        List<Document> records = new ArrayList<Document>();

        Document doc = Document.parse(params);

        MongoCursor<Document> cursor = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("fc_payroll_calc_result_table").find(doc).iterator();

        try {
            while (cursor.hasNext()) {
                records.add(cursor.next());
            }
        } finally {
            cursor.close();
        }

        return records;
    }











}
