package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.FcPayrollCalcResultService;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
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
public class FcPayrollCalcResultController {

    @Autowired
    private FcPayrollCalcResultService fcPayrollCalcResultService;

    @RequestMapping(value = "/listFcPayrollCalcResults")
    public JsonResult list(@RequestBody Map<String, Object> params) {
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
        ArrayList<Map<String, Object>> list = new ArrayList();

        String empId = request.getParameter("EmployeeID");
        if (empId == null || empId.equals("")) {
            return new HashMap<String, Object>(){
                {
                    put("EmployeeID", empId);
                    put("SalaryInfos", list);
                    put("IsSuccess", false);
                    put("ErrorCode", "缺少参数EmployeeID");
                }
            };
        }

        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("empId", empId);
            }
        };


        for (FcPayrollCalcResultPO po : fcPayrollCalcResultService.listFcPayrollCalcResults(params)) {

            Map<String, Object> ele = new HashMap<String, Object>(){
                {
                    put("SalaryID", po.getFcPayrollCalcResultId());
                    put("SalaryMonth", po.getPersonnelIncomeYearMonth());
                    put("Salary", po.getPersonnelIncomeNetPay());
                }
            };

            list.add(ele);
        }

        return new HashMap<String, Object>(){
            {
                put("EmployeeID", empId);
                put("SalaryInfos", list);
                put("IsSuccess", true);
                put("ErrorCode", "0");
            }
        };
    }

    @RequestMapping(value = "/DetailsSalaryQuery")
    public Map<String, Object> DetailsSalaryQuery(HttpServletRequest request) {
        ArrayList<Map<String, Object>> list = new ArrayList();

        String fcPayrollCalcResultId = request.getParameter("SalaryID");
        if (fcPayrollCalcResultId == null || fcPayrollCalcResultId.equals("")) {
            return new HashMap<String, Object>(){
                {
                    put("SalaryID", fcPayrollCalcResultId);
                    put("SalaryItems", list);
                    put("IsSuccess", false);
                    put("ErrorCode", "缺少参数SalaryID");
                }
            };
        }

        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("fcPayrollCalcResultId", fcPayrollCalcResultId);
            }
        };

        FcPayrollCalcResultPO po = fcPayrollCalcResultService.getFcPayrollCalcResult(params);

        JSONObject hash = JSON.parseObject(po.getSalaryCalcResultItems());

        for (String key : hash.keySet()) {
            Map<String, Object> item = new HashMap<String, Object>(){
                {
                    put("ItemName", key);
                    put("ItemValue", hash.get(key));
                }
            };
            list.add(item);
        }

        return new HashMap<String, Object>(){
            {
                put("SalaryID", fcPayrollCalcResultId);
                put("SalaryItems", list);
                put("IsSuccess", true);
                put("ErrorCode", "0");
            }
        };
    }




}
