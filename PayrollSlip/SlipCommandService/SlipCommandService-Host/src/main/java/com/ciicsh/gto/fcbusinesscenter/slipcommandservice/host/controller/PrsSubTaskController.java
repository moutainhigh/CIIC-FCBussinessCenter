package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsSubTaskService;
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
 * @since 2018-02-11
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class PrsSubTaskController {

    @Autowired
    private PrsSubTaskService prsSubTaskService;

    @RequestMapping(value = "/listPrsSubTasks")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsSubTaskService.listPrsSubTasks(params));
    }

    @RequestMapping(value = "/pagePrsSubTasks")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsSubTaskService.pagePrsSubTasks(params));
    }

    @RequestMapping(value = "/getPrsSubTask")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsSubTaskService.getPrsSubTask(params));
    }

    @RequestMapping(value = "/addPrsSubTask")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsSubTaskService.addPrsSubTask(params));
    }

    @RequestMapping(value = "/updatePrsSubTask")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsSubTaskService.updatePrsSubTask(params));
    }



}
