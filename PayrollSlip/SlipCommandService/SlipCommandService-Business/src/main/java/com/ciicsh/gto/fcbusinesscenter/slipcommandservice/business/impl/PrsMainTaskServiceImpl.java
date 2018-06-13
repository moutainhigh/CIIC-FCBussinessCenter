package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsMainTaskService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工资单任务单主表 服务实现类
 *
 * @author taka
 * @since 2018-02-28
 */
@Service
public class PrsMainTaskServiceImpl implements PrsMainTaskService {

    @Autowired
    private PrsMainTaskMapper prsMainTaskMapper;

    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public List<PrsMainTaskPO> listPrsMainTasks(Map<String, Object> params) {

        List<PrsMainTaskPO> records = prsMainTaskMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsMainTaskPO> pagePrsMainTasks(Map<String, Object> params) {
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

        int total = prsMainTaskMapper.total(params);
        List<PrsMainTaskPO> records = prsMainTaskMapper.list(params);
        Page<PrsMainTaskPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsMainTaskPO getPrsMainTask(Map<String, Object> params) {
        return prsMainTaskMapper.get(params);
    }

    @Override
    public List<Document> getTaskEmps(Map<String, Object> params) {
        List<Document> emps = new ArrayList<Document>();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");
        BasicDBObject filter = new BasicDBObject();
        filter.put("task_id", params.get("mainTaskId"));
        MongoCursor<Document> cursor = coll.find(filter).iterator();

        try {
            while (cursor.hasNext()) {
                emps.add(cursor.next());
            }
        } finally {
            cursor.close();
        }

        return emps;
    }

    @Override
    public Boolean addPrsMainTask(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }

        if (params.get("publishDate") != null) {
            if (params.get("publishDate").equals("")) {
                params.put("publishDate", null);
            } else {
                params.put("publishDate", new Date((long) params.get("publishDate")));
            }
        }
        if (params.get("approveTime") != null) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }
        if (params.get("uploadDate") != null) {
            if (params.get("uploadDate").equals("")) {
                params.put("uploadDate", null);
            } else {
                params.put("uploadDate", new Date((long) params.get("uploadDate")));
            }
        }

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

        for (Object emp : (ArrayList)params.remove("employees")) {
            coll.insertOne(Document.parse(JSONObject.toJSONString(emp)));
        }

        prsMainTaskMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsMainTask(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }

        if (params.get("publishDate") != null) {
            if (params.get("publishDate").equals("")) {
                params.put("publishDate", null);
            } else {
                params.put("publishDate", new Date((long) params.get("publishDate")));
            }
        }

        if (params.get("publishExecDate") != null) {
            if (params.get("publishExecDate").equals("")) {
                params.put("publishExecDate", null);
            } else {
                params.put("publishExecDate", new Date((long) params.get("publishExecDate")));
            }
        }

        if (params.get("approveTime") != null) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }

        if (params.get("uploadDate") != null) {
            if (params.get("uploadDate").equals("")) {
                params.put("uploadDate", null);
            } else {
                params.put("uploadDate", new Date((long) params.get("uploadDate")));
            }
        }

        if (params.get("uploadExecDate") != null) {
            if (params.get("uploadExecDate").equals("")) {
                params.put("uploadExecDate", null);
            } else {
                params.put("uploadExecDate", new Date((long) params.get("uploadExecDate")));
            }
        }

        if (params.get("employees") != null) {
            MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

            coll.deleteMany(new BasicDBObject("task_id", params.get("mainTaskId")));

            for (Object emp : (ArrayList)params.remove("employees")) {
                coll.insertOne(Document.parse(JSONObject.toJSONString(emp)));
            }
        }

        prsMainTaskMapper.update(params);

        return true;
    }
}
