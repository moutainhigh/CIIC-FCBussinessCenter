package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.SalManagementService;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.PayrollAccountProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @author taka
 * @since 2018-02-05
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class SalManagementController {

    @Autowired
    private PayrollAccountProxy payrollAccountProxy;

    @Autowired
    private BatchProxy batchProxy;

    @Autowired
    private SalManagementService salManagementService;

    @RequestMapping(value = "/listSalManagements")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(salManagementService.listSalManagements(params));
    }

    @RequestMapping(value = "/pageSalManagements")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(salManagementService.pageSalManagements(params));
    }

    @RequestMapping(value = "/getSalManagement")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(salManagementService.getSalManagement(params));
    }

    @RequestMapping(value = "/addSalManagement")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(salManagementService.addSalManagement(params));
    }

    @RequestMapping(value = "/updateSalManagement")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(salManagementService.updateSalManagement(params));
    }

    @RequestMapping(value = "/listAccountSets")
    public JsonResult listAccountSets(@RequestBody Map<String, Object> params) {
        return JsonResult.success(payrollAccountProxy.getAccountSetsByManagementId((String)params.get("managementId")));

    }

    @RequestMapping(value = "/listBatches")
    public JsonResult listBatches(@RequestBody Map<String, Object> params) {
        return JsonResult.success(batchProxy.getBatchListByManagementId((String)params.get("managementId"), (String)params.get("batchCode"), (int)params.get("pageNum"), (int)params.get("pageSize")));

    }



}
