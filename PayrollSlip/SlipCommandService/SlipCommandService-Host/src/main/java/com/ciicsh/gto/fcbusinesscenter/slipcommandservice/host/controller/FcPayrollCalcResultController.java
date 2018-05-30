package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gt1.config.MongoConfig;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.FcPayrollCalcResultService;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
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
public class FcPayrollCalcResultController {

    @Autowired
    private FcPayrollCalcResultService fcPayrollCalcResultService;

    @Autowired
    private MongoConfig mongoConfig;

    @RequestMapping(value = "/listFcPayrollCalcResults")
    public JsonResult list(@RequestBody String params) {
        return JsonResult.success(fcPayrollCalcResultService.listFcPayrollCalcResults(params));
    }

    @RequestMapping(value = "/pageFcPayrollCalcResults")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(fcPayrollCalcResultService.pageFcPayrollCalcResults(params));
    }

    @RequestMapping(value = "/getFcPayrollCalcResult")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(fcPayrollCalcResultService.getFcPayrollCalcResult(params));
    }

    @RequestMapping(value = "/addFcPayrollCalcResult")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(fcPayrollCalcResultService.addFcPayrollCalcResult(params));
    }

    @RequestMapping(value = "/updateFcPayrollCalcResult")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(fcPayrollCalcResultService.updateFcPayrollCalcResult(params));
    }

    @RequestMapping(value = "/listBatchIds")
    public JsonResult listBatchIds(@RequestBody Map<String, Object> params) {
        return JsonResult.success(fcPayrollCalcResultService.listBatchIds(params));
    }

    @RequestMapping(value = "/listPayrollTypes")
    public JsonResult listPayrollTypes(@RequestBody Map<String, Object> params) {
        return JsonResult.success(fcPayrollCalcResultService.listPayrollTypes(params));
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

//        BasicDBList condList = new BasicDBList();
//        condList.add(new BasicDBObject("empId", empId));
        BasicDBObject condition= new BasicDBObject("emp_id", empId);

        MongoCursor<Document> cursor = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("fc_payroll_calc_result_table").find(condition).iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Map<String, Object> ele = new HashMap<String, Object>(){
                    {
                        put("SalaryID", doc.get("income_year_month"));
                        put("SalaryMonth", doc.get("income_year_month"));
                        put("Salary", doc.get("net_pay"));
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

//        BasicDBList condList = new BasicDBList();
//        condList.add(new BasicDBObject("_id", id));
        BasicDBObject condition= new BasicDBObject("income_year_month", id);

        Document doc = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("fc_payroll_calc_result_table").find(condition).first();

        JSONArray arr = JSON.parseArray((String)doc.get("salary_calc_result_items"));

        ArrayList<Map<String, Object>> records = new ArrayList();

        for (Object obj : arr) {
            Map<String, Object> item = (Map<String, Object>) obj;
            Map<String, Object> map = new HashMap<String, Object>(){
                {
                    put("ItemName", item.get("item_name"));
                    put("ItemValue", item.get("item_value"));
                }
            };
            records.add(map);
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
