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
    private FcPayrollCalcResultMapper fcPayrollCalcResultMapper;

    @Autowired
    private MongoConfig mongoConfig;

    @Autowired
    private CalResultMongoOpt calResultMongoOpt;


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

    @Override
    public Page<FcPayrollCalcResultPO> pageFcPayrollCalcResults(Map<String, Object> params) {
        int limit = 20;
        int offset = 0;

        int currentPage = params.get("currentPage") == null ? 1 : (int) params.get("currentPage");

        if (params.get("pageSize") != null) {
            limit =  (int) params.get("pageSize");
        }

        if (currentPage > 1) {
            offset = (currentPage - 1) * limit;
        }

        params.put("limit", limit);
        params.put("offset", offset);

        int total = fcPayrollCalcResultMapper.total(params);
        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.list(params);
        Page<FcPayrollCalcResultPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public FcPayrollCalcResultPO getFcPayrollCalcResult(Map<String, Object> params) {
        return fcPayrollCalcResultMapper.get(params);
    }

    @Override
    public Boolean addFcPayrollCalcResult(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }



        fcPayrollCalcResultMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updateFcPayrollCalcResult(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }



        fcPayrollCalcResultMapper.update(params);

        return true;
    }


    @Override
    public List<FcPayrollCalcResultPO> listBatchIds(Map<String, Object> params) {

        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.listBatchIds(params);

        return records;
    }

    @Override
    public List<FcPayrollCalcResultPO> listPayrollTypes(Map<String, Object> params) {

        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.listPayrollTypes(params);

        return records;
    }


    /**
     * 获取自定义查询结果
     * @param params
     * @param sorts    1 表示升序，－1表示降序
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public List<DBObject> getCustomSearchResult(Map<String, Object> params, Map<String,Integer> sorts, int pageSize, int pageNum) {
        if(params == null || params.size() == 0) return null;
        List<DBObject> list = null;

        //设置排序字段： 1 升序； －1 降序
        DBObject fields = new BasicDBObject();
        Iterator<String> sortKeys = sorts.keySet().iterator();
        while (sortKeys.hasNext()){
            fields.put(sortKeys.next(), sorts.get(sortKeys.next()));
        }

        //设置查询字段
        BasicDBList condList = new BasicDBList();//存放查询条件的集合
        Iterator<String> keys = params.keySet().iterator();
        BasicDBObject query = new BasicDBObject();
        while (keys.hasNext()){
            query.put(keys.next(), params.get(keys.next()));
            condList.add(query);
        }
        BasicDBObject condition= new BasicDBObject();//最后在将查询结果放到一个查询对象中去
        condition.put("$and", condList);//多条件查询使用and

        DBCursor cursor = calResultMongoOpt.getMongoTemplate()
                .getCollection(CalResultMongoOpt.PR_BATCH).find(condition).sort(fields)
                .skip((pageNum-1)*pageSize).limit(pageSize);
        while (cursor.hasNext()){
            //todo
        }
        return list;
    }


}
