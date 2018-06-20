package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gt1.config.MongoConfig;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollService;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * @author taka
 * @since 2018-02-09
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class PrsPayrollController {

    @Autowired
    private PrsPayrollService prsPayrollService;

    @Autowired
    private MongoConfig mongoConfig;

    @RequestMapping(value = "/listPubEmps")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollService.listPubEmps(params));
    }

    @RequestMapping(value = "/pagePubEmps")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollService.pagePubEmps(params));
    }

    @RequestMapping(value = "/deletePubEmps")
    public JsonResult deletePubEmps(@RequestBody ArrayList<String> ids) {
        return JsonResult.success(prsPayrollService.deletePubEmps(ids));
    }

    @RequestMapping(value = "/getPrsPayroll")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollService.getPrsPayroll(params));
    }

    @RequestMapping(value = "/addPrsPayroll")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollService.addPrsPayroll(params));
    }

    @RequestMapping(value = "/addPrsPayrolls")
    public JsonResult adds(@RequestBody ArrayList<Map<String, Object>> objs) {
        return JsonResult.success(prsPayrollService.addPrsPayrolls(objs));
    }

    @RequestMapping(value = "/updatePrsPayroll")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollService.updatePrsPayroll(params));
    }

    @RequestMapping(value = "/SalaryQuery")
    public Map<String, Object> SalaryQuery(HttpServletRequest request) {

        String empId = request.getParameter("EmployeeID");
        if (empId == null || empId.equals("")) {
            return new HashMap<String, Object>(){
                {
                    put("EmployeeID", empId);
                    put("ReturnObject", null);
                    put("ErrorCode", 0);
                    put("IsSuccess", false);
                    put("Message", "缺少参数EmployeeID");
                }
            };
        }

        List<Object> records = new ArrayList();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");

        MongoCursor<Document> cursor = coll.find(new BasicDBObject("emp_id", empId)).iterator();

        try {
            while (cursor.hasNext()) {
                Document emp = cursor.next();
                Map<String, Object> ele = new HashMap<String, Object>(){
                    {
                        put("SalaryID", ((ObjectId)emp.get("_id")).toHexString());
                        put("SalaryMonth", emp.get("income_year_month"));
                        put("Salary", emp.get("net_pay"));
                    }
                };
                records.add(ele);
            }
        } finally {
            cursor.close();
        }

        return new HashMap<String, Object>(){
            {
                put("EmployeeID", empId);
                put("ReturnObject", records);
                put("ErrorCode", 0);
                put("IsSuccess", true);
                put("Message", "获取成功");
            }
        };
    }

    @RequestMapping(value = "/DetailsSalaryQuery")
    public Map<String, Object> DetailsSalaryQuery(HttpServletRequest request) {

        String id = request.getParameter("SalaryID");
        if (id == null || id.equals("")) {
            return new HashMap<String, Object>(){
                {
                    put("SalaryID", id);
                    put("ReturnObject", null);
                    put("ErrorCode", 0);
                    put("IsSuccess", false);
                    put("Message", "缺少参数SalaryID");
                }
            };
        }

        ArrayList<Map<String, Object>> records = new ArrayList();

        MongoCollection<Document> coll = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("pub_emps");
        Document emp = coll.find(new Document("_id", new ObjectId(id))).first();

        if (emp != null) {
            Map<String, Object> items = (Map<String, Object>)emp.get("items");
            Iterator<String> keys = items.keySet().iterator();

            while (keys.hasNext()){
                String key = keys.next();
                Map<String, Object> map = new HashMap<String, Object>(){
                    {
                        put("ItemName", key);
                        put("ItemValue", items.get(key));
                    }
                };
                records.add(map);
            }
        }

        return new HashMap<String, Object>(){
            {
                put("SalaryID", id);
                put("ReturnObject", records);
                put("ErrorCode", 0);
                put("IsSuccess", true);
                put("Message", "获取成功");
            }
        };
    }


}
