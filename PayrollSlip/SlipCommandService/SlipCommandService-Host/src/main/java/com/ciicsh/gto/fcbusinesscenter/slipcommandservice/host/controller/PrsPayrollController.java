package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollService;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/listPrsPayrolls")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollService.listPrsPayrolls(params));
    }

    @RequestMapping(value = "/pagePrsPayrolls")
    public JsonResult page(@RequestBody String params) {
        return JsonResult.success(prsPayrollService.pagePrsPayrolls(params));
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

        HashMap<String, Object> params = new HashMap<String, Object>(){
            {
                put("employeeId", empId);
            }
        };

        for (PrsPayrollPO payroll : prsPayrollService.listPrsPayrolls(params)) {
            Map<String, Object> ele = new HashMap<String, Object>(){
                {
                    put("SalaryID", payroll.getId());
                    put("SalaryMonth", payroll.getPersonnelIncomeYearMonth());
                    put("Salary", payroll.getNetPay());
                }
            };
            records.add(ele);
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

        HashMap<String, Object> params = new HashMap<String, Object>(){
            {
                put("id", id);
            }
        };

        PrsPayrollPO payroll = prsPayrollService.getPrsPayroll(params);

        JSONArray items = JSON.parseArray(payroll.getItems());

        ArrayList<Map<String, Object>> records = new ArrayList();

        for (Object objItemAndVal : items) {
            JSONArray itemAndVal = (JSONArray) objItemAndVal;
            Map<String, Object> map = new HashMap<String, Object>(){
                {
                    put("ItemName", itemAndVal.get(0));
                    put("ItemValue", itemAndVal.get(1));
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
