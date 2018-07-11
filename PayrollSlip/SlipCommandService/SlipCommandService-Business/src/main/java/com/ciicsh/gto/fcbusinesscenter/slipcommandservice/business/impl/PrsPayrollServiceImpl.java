package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.config.MongoConfig;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollService;
import java.util.*;

/**
 * 工资单 服务实现类
 *
 * @author taka
 * @since 2018-02-09
 */
@Service
public class PrsPayrollServiceImpl implements PrsPayrollService {

    @Autowired
    private PrsPayrollMapper prsPayrollMapper;

    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public List<Document> listPubEmps(Map<String, Object> params) {
        BasicDBObject queryCond= new BasicDBObject((Map)params.get("query"));

        Map orderBy = params.containsKey("orderBy") ? (LinkedHashMap)params.get("orderBy") : new LinkedHashMap();
        BasicDBObject orderByCond= new BasicDBObject(orderBy);

        List<Document> emps = new ArrayList<Document>();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");

        MongoCursor<Document> cursor = coll.find(queryCond).sort(orderByCond).iterator();

        try {
            while (cursor.hasNext()) {
                Document emp = cursor.next();
                emp.put("id", ((ObjectId)emp.get("_id")).toHexString());
                emps.add(emp);
            }
        } finally {
            cursor.close();
        }

        return emps;
    }

    @Override
    public Page<Document> pagePubEmps(Map<String, Object> params) {
        int limit = 20;
        int offset = 0;

        BasicDBObject queryCond= new BasicDBObject((Map)params.get("query"));

        Map orderBy = params.containsKey("orderBy") ? (LinkedHashMap)params.get("orderBy") : new LinkedHashMap();
        BasicDBObject orderByCond= new BasicDBObject(orderBy);


        int currentPage = params.containsKey("currentPage") ? (int) params.get("currentPage") : 1;
        if (params.containsKey("pageSize")) {
            limit = (int) params.get("pageSize");
        }
        if (currentPage > 1) {
            offset = (currentPage - 1) * limit;
        }

        List<Document> emps = new ArrayList<Document>();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");

        long total = coll.count(queryCond);

        MongoCursor<Document> cursor = coll.find(queryCond).skip(offset).limit(limit).sort(orderByCond).iterator();

        try {
            while (cursor.hasNext()) {
                Document emp = cursor.next();
                emp.put("id", ((ObjectId)emp.get("_id")).toHexString());
                emps.add(emp);
            }
        } finally {
            cursor.close();
        }

        Page<Document> page = new Page<>();
        page.setRecords(emps);
        page.setCurrent(currentPage);
        page.setTotal((int)total);
        page.setSize(limit);

        return page;
    }

    @Override
    public Boolean deletePubEmps(ArrayList<String> ids) {
        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");

        for (String id : ids) {
            coll.deleteOne(new Document("_id", new ObjectId(id)));
        }

        return true;
    }

    @Override
    public PrsPayrollPO getPrsPayroll(Map<String, Object> params) {
        return prsPayrollMapper.get(params);
    }

    @Override
    public Boolean addPrsPayroll(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }



        prsPayrollMapper.insert(params);

        return true;
    }

    @Override
    public Boolean addPrsPayrolls(ArrayList<Map<String, Object>> objs) {
        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");

        for (Map<String, Object> params : objs) {
            coll.insertOne(Document.parse(JSONObject.toJSONString(params)));
        }

        return true;
    }

    @Override
    public Boolean updatePrsPayroll(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }



        prsPayrollMapper.update(params);

        return true;
    }
}
