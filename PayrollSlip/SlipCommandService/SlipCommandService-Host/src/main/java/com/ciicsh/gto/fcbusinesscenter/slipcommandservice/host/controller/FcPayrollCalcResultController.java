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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import kafka.controller.LeaderIsrAndControllerEpoch;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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






}
