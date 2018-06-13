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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<PrsPayrollPO> listPrsPayrolls(Map<String, Object> params) {

        List<PrsPayrollPO> records = prsPayrollMapper.list(params);

        return records;
    }

    @Override
    public Page<Document> pagePrsPayrolls(String params) {
        int limit = 20;
        int offset = 0;

        Document query = Document.parse(params);

        int currentPage = query.containsKey("currentPage") ? (int) query.remove("currentPage") : 1;

        if (query.containsKey("pageSize")) {
            limit =  (int) query.remove("pageSize");
        }

        if (currentPage > 1) {
            offset = (currentPage - 1) * limit;
        }

        List<Document> emps = new ArrayList<Document>();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");

        long total = coll.count(query);

        MongoCursor<Document> cursor = coll.find(query).skip(offset).limit(limit).iterator();

        try {
            while (cursor.hasNext()) {
                emps.add(cursor.next());
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
