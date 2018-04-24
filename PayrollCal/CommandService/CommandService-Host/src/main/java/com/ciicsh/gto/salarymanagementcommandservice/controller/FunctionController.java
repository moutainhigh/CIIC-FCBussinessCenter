package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.service.FunctionService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NeoJiang on 2018/4/10.
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    @GetMapping("/functionName")
    public JsonResult getPayrollGroupNameList(){
        List<HashMap<String, String>> nameList = functionService.getFunctionNameList();
        return JsonResult.success(nameList);
    }
}
