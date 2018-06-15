package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsMainTaskService;
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
public class PrsMainTaskController {

    @Autowired
    private PrsMainTaskService prsMainTaskService;

    @RequestMapping(value = "/listPrsMainTasks")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.listPrsMainTasks(params));
    }

    @RequestMapping(value = "/pagePrsMainTasks")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.pagePrsMainTasks(params));
    }

    @RequestMapping(value = "/getPrsMainTask")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.getPrsMainTask(params));
    }

    @RequestMapping(value = "/getTaskEmps")
    public JsonResult getTaskEmps(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.getTaskEmps(params));
    }

    @RequestMapping(value = "/listTaskEmps")
    public JsonResult listTaskEmps(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.listTaskEmps(params));
    }

    @RequestMapping(value = "/pageTaskEmps")
    public JsonResult pageTaskEmps(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.pageTaskEmps(params));
    }

    @RequestMapping(value = "/deleteTaskEmps")
    public JsonResult deleteTaskEmps(@RequestBody Map<String, Object> query) {
        return JsonResult.success(prsMainTaskService.deleteTaskEmps(query));
    }

    @RequestMapping(value = "/addPrsMainTask")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.addPrsMainTask(params));
    }

    @RequestMapping(value = "/updatePrsMainTask")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsMainTaskService.updatePrsMainTask(params));
    }



}
