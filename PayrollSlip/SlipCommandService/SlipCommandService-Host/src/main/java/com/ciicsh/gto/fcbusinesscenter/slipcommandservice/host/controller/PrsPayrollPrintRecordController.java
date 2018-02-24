package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollPrintRecordService;
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
 * @since 2018-02-24
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class PrsPayrollPrintRecordController {

    @Autowired
    private PrsPayrollPrintRecordService prsPayrollPrintRecordService;

    @RequestMapping(value = "/listPrsPayrollPrintRecords")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollPrintRecordService.listPrsPayrollPrintRecords(params));
    }

    @RequestMapping(value = "/pagePrsPayrollPrintRecords")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollPrintRecordService.pagePrsPayrollPrintRecords(params));
    }

    @RequestMapping(value = "/getPrsPayrollPrintRecord")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollPrintRecordService.getPrsPayrollPrintRecord(params));
    }

    @RequestMapping(value = "/addPrsPayrollPrintRecord")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollPrintRecordService.addPrsPayrollPrintRecord(params));
    }

    @RequestMapping(value = "/updatePrsPayrollPrintRecord")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollPrintRecordService.updatePrsPayrollPrintRecord(params));
    }

    

}
