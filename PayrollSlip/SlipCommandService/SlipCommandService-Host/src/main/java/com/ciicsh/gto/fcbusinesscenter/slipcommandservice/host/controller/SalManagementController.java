package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.SalManagementService;
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
 * @since 2018-02-05
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class SalManagementController {

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

    @PostMapping(value = "/uploadSalManagement")
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
