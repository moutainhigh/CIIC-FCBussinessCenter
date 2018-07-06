package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.ciicsh.gt1.config.MongoConfig;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsMainTaskMapper;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.SlipMessageChannel;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import java.util.*;

@EnableBinding(value = SlipMessageChannel.class)
@Component
public class SlipMessageChannelImpl {
    @Autowired
    private BatchProxy batchProxy;

    @Autowired
    private MongoConfig mongoConfig;

    @Autowired
    private PrsMainTaskMapper prsMainTaskMapper;

    @StreamListener("pr_compute-close-output-channel")
    public void receive(Map<String, Object> message){
        System.out.println("receive:");
        System.out.println(message);

        String batchId = (String)message.get("batchCode");
        int version = (int)message.get("version");

        String mainTaskId = "PT" + batchId + "-" + version;
        String title = batchId + "-" + version;
        PrBatchDTO batch = batchProxy.getBatchInfo(batchId, (int)message.get("batchType"));

        BasicDBObject queryCond= new BasicDBObject("batch_id", batchId);

        List<Map<String, Object>> emps = new ArrayList<>();
        int totalCount = 0;
        int chineseCount = 0;
        int foreignerCount = 0;
        boolean hasPaper = false;
        int publishState = 6;
        int uploadState = 6;

        MongoCursor<Document> cursor = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("fc_payroll_calc_result_table").find(queryCond).iterator();

        try {
            while (cursor.hasNext()) {
                Document ret = cursor.next();

                Document empInfo = (Document)(ret.get("emp_info"));

                Document contract = null;
                if ( empInfo.get("雇员服务协议") != null ) {
                    contract = (Document) (empInfo.get("雇员服务协议"));
                }

                ArrayList payType = new ArrayList();

                if (contract != null && contract.get("paySheetInfo") != null && ((Document)contract.get("paySheetInfo")).get("payType") != null) {
                    payType = (ArrayList) ((Document)contract.get("paySheetInfo")).get("payType");
                } else {
                    payType.add(2);
                    payType.add(3);
                }


                Map<String, Object> items = new HashMap<String, Object>(){
                    {
                        put("雇员编号", empInfo.get("雇员编号"));
                        put("员工工号", empInfo.get("员工工号"));
                        put("雇员名称", empInfo.get("雇员名称"));
                        put("出生日期", empInfo.get("出生日期"));
                        put("性别", empInfo.get("性别"));
                        put("证件类型", empInfo.get("证件类型") != null ? empInfo.get("证件类型").toString() : null);
                        put("入职日期", empInfo.get("入职日期"));
                        put("证件号码", empInfo.get("证件号码"));
                        put("公司编号", empInfo.get("公司编号"));
                        put("曾用名", empInfo.get("曾用名"));
                        put("国家代码", empInfo.get("国家代码"));
                        put("省份代码", empInfo.get("省份代码"));
                        put("城市代码", empInfo.get("城市代码"));
                        put("国籍", empInfo.get("国籍"));
                        put("离职日期", empInfo.get("离职日期"));
                    }
                };


                if (contract != null && contract.get("extendedInfo") != null && ((Document)contract.get("extendedInfo")).get("fields") != null) {
                    for (Document field : (List<Document>) ((Document)contract.get("extendedInfo")).get("fields")) {
                        items.put((String)field.get("fieldName"), field.get("fieldValue"));
                    }
                }

                if (ret.get("salary_calc_result_items") != null) {
                    for (Document item : (List<Document>) ret.get("salary_calc_result_items")) {
                        items.put((String) item.get("item_name"), item.get("item_value_str") != null ? item.get("item_value_str") : item.get("item_value"));
                    }
                }

                Map<String, Object> emp = new HashMap<String, Object>(){
                    {
                        put("task_id", mainTaskId);
                        put("emp_id", ret.get("emp_id"));
                        put("emp_name", empInfo.get("雇员名称"));
                        put("country_code", empInfo.get("国家代码"));
                        put("net_pay", ret.get("net_pay"));
                        put("batch_id", ret.get("batch_id"));
                        put("mgr_id", ret.get("mgr_id"));
                        put("mgr_name", ret.get("mgr_name"));
                        put("income_year_month", ret.get("income_year_month"));
                        put("items", items);
                        put("sorted_items", new ArrayList());
                        put("template_id", null);
                        put("template_name", null);
                    }
                };

                if (contract != null && contract.get("paySheetInfo") != null) {
                    emp.put("email", ((Document)contract.get("paySheetInfo")).get("email"));
                    emp.put("payrollPassword", ((Document)contract.get("paySheetInfo")).get("payrollPassword"));
                }

                emp.put("is_paper", payType.contains(1));
                emp.put("is_email", payType.contains(2));
                emp.put("is_ehome", payType.contains(3));
                emp.put("publish_state", payType.contains(2) ? 0 : 6);
                emp.put("upload_state", payType.contains(3) ? 0 : 6);


                emps.add(emp);

                totalCount += 1;
                if (empInfo.get("国家代码") == "CN") {
                    chineseCount += 1;
                } else {
                    foreignerCount += 1;
                }

                if (payType.contains(1)) {
                    hasPaper = true;
                }

                if (payType.contains(2)) {
                    publishState = 0;
                }

                if (payType.contains(3)) {
                    uploadState = 0;
                }
            }
        } finally {
            cursor.close();
        }

        Map<String, Object> task = new HashMap<String, Object>(){
            {
                put("mainTaskId", mainTaskId);
                put("title", title);
                put("managementId", batch.getManagementId());
                put("managementName", batch.getManagementName());
                put("batchId", batchId);
                put("personnelIncomeYearMonth", batch.getActualPeriod());
                put("status", 0);
                put("createdBy", "system");
                put("modifiedBy", "system");
            }
        };

        task.put("totalCount", totalCount);
        task.put("chineseCount", chineseCount);
        task.put("foreignerCount", foreignerCount);
        task.put("hasPaper", hasPaper);
        task.put("publishState", publishState);
        task.put("uploadState", uploadState);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        task.put("publishDate", cal.getTime());
        task.put("uploadDate", cal.getTime());

        prsMainTaskMapper.insert(task);

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("task_emps");

        for (Map<String, Object> emp : emps) {
            coll.insertOne(new Document(emp));
        }

    }
}
