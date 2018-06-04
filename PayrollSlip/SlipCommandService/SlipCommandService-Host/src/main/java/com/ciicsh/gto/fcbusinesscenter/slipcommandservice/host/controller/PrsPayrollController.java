package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public JsonResult page(@RequestBody Map<String, Object> params) {
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


}
