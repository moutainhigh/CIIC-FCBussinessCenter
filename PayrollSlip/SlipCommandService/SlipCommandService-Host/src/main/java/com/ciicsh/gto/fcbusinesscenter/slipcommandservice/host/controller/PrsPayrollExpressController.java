package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollExpressService;
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
public class PrsPayrollExpressController {

    @Autowired
    private PrsPayrollExpressService prsPayrollExpressService;

    @RequestMapping(value = "/listPrsPayrollExpresss")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollExpressService.listPrsPayrollExpresss(params));
    }

    @RequestMapping(value = "/pagePrsPayrollExpresss")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollExpressService.pagePrsPayrollExpresss(params));
    }

    @RequestMapping(value = "/getPrsPayrollExpress")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollExpressService.getPrsPayrollExpress(params));
    }

    @RequestMapping(value = "/addPrsPayrollExpress")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollExpressService.addPrsPayrollExpress(params));
    }

    @RequestMapping(value = "/updatePrsPayrollExpress")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollExpressService.updatePrsPayrollExpress(params));
    }

    

}
