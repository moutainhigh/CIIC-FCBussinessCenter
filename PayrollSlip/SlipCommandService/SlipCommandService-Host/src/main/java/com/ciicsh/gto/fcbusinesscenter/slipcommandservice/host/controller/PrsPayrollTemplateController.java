package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.util.result.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollTemplateService;
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
 * @since 2018-01-29
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class PrsPayrollTemplateController {

    @Autowired
    private PrsPayrollTemplateService prsPayrollTemplateService;

    @RequestMapping(value = "/listPrsPayrollTemplates")
    public JsonResult list(Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.listPrsPayrollTemplates(params));
    }

    @RequestMapping(value = "/pagePrsPayrollTemplates")
    public JsonResult page(Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.pagePrsPayrollTemplates(params));
    }

    @RequestMapping(value = "/getPrsPayrollTemplate")
    public JsonResult get(Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.getPrsPayrollTemplate(params));
    }

    @RequestMapping(value = "/addPrsPayrollTemplate")
    public JsonResult add(Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.addPrsPayrollTemplate(params));
    }

    @RequestMapping(value = "/updatePrsPayrollTemplate")
    public JsonResult update(Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.updatePrsPayrollTemplate(params));
    }

    @PostMapping(value = "/uploadPrsPayrollTemplate")
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
