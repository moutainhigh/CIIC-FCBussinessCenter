package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.config.MongoConfig;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.ScheduleTaskService;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsMainTaskPO;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

    @Autowired
    private PrsMainTaskMapper taskMapper;

    @Autowired
    private MongoConfig mongoConfig;

    @Override
//    @Scheduled(fixedDelay=5000)
    @Scheduled(cron = "0 */4 * * * ?")
    public void uploadEmps() {
        long now = System.currentTimeMillis();
        List<String> taskIds = taskMapper.selectList(new EntityWrapper<PrsMainTaskPO>().eq("upload_state", 1)).stream().filter(po -> po.getUploadDate() != null &&  now >= po.getUploadDate().getTime()).map(po -> po.getMainTaskId()).collect(Collectors.toList());

        MongoCollection<Document> taskEmpsColl = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");
        MongoCollection<Document> pubEmpsColl = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");
        Bson queryCond = Filters.in("task_id", taskIds);
        MongoCursor<Document> cursor = taskEmpsColl.find(queryCond).iterator();

        try {
            while (cursor.hasNext()) {
                Document emp = cursor.next();
                Map<String, Object> pubEmp = new HashMap<String, Object>(){
                    {
                        put("task_id", emp.get("task_id"));
                        put("batch_id", emp.get("batch_id"));
                        put("mgr_id", emp.get("mgr_id"));
                        put("mgr_name", emp.get("mgr_name"));
                        put("income_year_month", emp.get("income_year_month"));
                        put("emp_id", emp.get("emp_id"));
                        put("emp_name", emp.get("emp_name"));
                        put("net_pay", emp.get("net_pay"));
                        put("template_id", emp.get("template_id"));
                        put("template_name", emp.get("template_name"));
                        put("items", emp.get("items"));
                        put("sorted_items", emp.get("sorted_items"));
                    }
                };
                pubEmpsColl.insertOne(new Document(pubEmp));
            }
        } finally {
            cursor.close();
        }

        Map<String, Object> updateEmp = new HashMap<String, Object>(){
            {
                put("upload_state", 3);
            }
        };
        taskEmpsColl.updateMany(queryCond, new BasicDBObject("$set", new BasicDBObject(updateEmp)));

        taskIds.forEach(taskId -> {
            Map<String, Object> updateTask = new HashMap<String, Object>(){
                {
                    put("mainTaskId", taskId);
                    put("uploadState", 3);
                    put("uploadExecDate", new Date(now));
                }
            };
            taskMapper.update(updateTask);
        });




    }


}
