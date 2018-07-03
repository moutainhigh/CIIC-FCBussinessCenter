package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.config.MongoConfig;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsMainTaskService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.*;

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
    public List<Document> listTaskEmps(Map<String, Object> params) {
        BasicDBObject queryCond= new BasicDBObject((Map)params.get("query"));

        Map orderBy = params.containsKey("orderBy") ? (LinkedHashMap)params.get("orderBy") : new LinkedHashMap();
        BasicDBObject orderByCond= new BasicDBObject(orderBy);


        List<Document> emps = new ArrayList<Document>();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

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
    public Page<Document> pageTaskEmps(Map<String, Object> params) {
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

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

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
    public Boolean deleteTaskEmps(Map<String, Object> query) {
        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

        coll.deleteMany(new BasicDBObject(query));

        return true;
    }

    @Override
    public Boolean updateTaskEmp(Map<String, Object> params) {
        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

        BasicDBObject filter = new BasicDBObject((Map)params.remove("updConds"));
        BasicDBObject fields = new BasicDBObject((Map)params.remove("updFields"));
        BasicDBObject update = new BasicDBObject("$set", fields);

        coll.updateMany(filter, update);

        return true;
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

        if (params.containsKey("employees")) {
            MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

            for (Map<String, Object> emp : (ArrayList<Map<String, Object>>) params.remove("employees")) {
                coll.insertOne(new Document(emp));
            }
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

        if (params.containsKey("publishDate")) {
            if (params.get("publishDate").equals("")) {
                params.put("publishDate", null);
            } else {
                params.put("publishDate", new Date((long) params.get("publishDate")));
            }
        }

        if (params.containsKey("publishExecDate")) {
            if (params.get("publishExecDate").equals("")) {
                params.put("publishExecDate", null);
            } else {
                params.put("publishExecDate", new Date((long) params.get("publishExecDate")));
            }
        }

        if (params.containsKey("approveTime")) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }

        if (params.containsKey("uploadDate")) {
            if (params.get("uploadDate").equals("")) {
                params.put("uploadDate", null);
            } else {
                params.put("uploadDate", new Date((long) params.get("uploadDate")));
            }
        }

        if (params.containsKey("uploadExecDate")) {
            if (params.get("uploadExecDate").equals("")) {
                params.put("uploadExecDate", null);
            } else {
                params.put("uploadExecDate", new Date((long) params.get("uploadExecDate")));
            }
        }

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

        if (params.containsKey("employees")) {
            if (params.containsKey("isClearOld")) {
                coll.deleteMany(new BasicDBObject("task_id", params.get("mainTaskId")));
            }
            for (Map<String, Object> emp : (ArrayList<Map<String, Object>>)params.remove("employees")) {
                emp.remove("id");
                emp.remove("_id");
                coll.insertOne(new Document(emp));
            }
        }

        if (params.containsKey("updConds")) {
            BasicDBObject filter = new BasicDBObject((Map)params.remove("updConds"));
            BasicDBObject fields = new BasicDBObject((Map)params.remove("updFields"));
            BasicDBObject update = new BasicDBObject("$set", fields);

            coll.updateMany(filter, update);
        }

        if (params.containsKey("delObjectIds")) {
            for (String id : (ArrayList<String>)params.remove("delObjectIds")) {
                coll.deleteOne(new Document("_id", new ObjectId(id)));
            }
        }

        if (params.containsKey("ignoreTaskEmpObjIds")) {
            ArrayList<ObjectId> objIds = new ArrayList<>();
            for (String id : (ArrayList<String>)params.remove("ignoreTaskEmpObjIds")) {
                objIds.add(new ObjectId(id));
            }
            Bson filter = Filters.in("_id", objIds);
            BasicDBObject fields = new BasicDBObject((Map)params.remove("ignoreFields"));
            BasicDBObject update = new BasicDBObject("$set", fields);

            coll.updateMany(filter, update);
        }

        if (params.containsKey("multiUpdEmps")) {
            for (Map<String, Object> updEmp : (ArrayList<Map<String, Object>>)params.remove("multiUpdEmps")) {
                BasicDBObject filter = new BasicDBObject((Map)updEmp.get("updConds"));
                BasicDBObject fields = new BasicDBObject((Map)updEmp.get("updFields"));
                BasicDBObject update = new BasicDBObject("$set", fields);
                coll.updateOne(filter, update);
            }
        }



        prsMainTaskMapper.update(params);

        return true;
    }
}
