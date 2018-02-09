package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.FcPayrollCalcResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(value = "/uploadFcPayrollCalcResult")
    public JsonResult upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String dist = FileHandler.uploadFile(multipartFile.getInputStream());
            return JsonResult.success(dist);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.faultMessage();
        }
    }

}
