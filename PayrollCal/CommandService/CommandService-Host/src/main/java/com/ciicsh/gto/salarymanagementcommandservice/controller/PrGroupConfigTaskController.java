package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupConfigTaskService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PrPayrollConfigTaskTranslator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-07-10 20:02
 * @description 薪资组实例变更任务单Controller
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class PrGroupConfigTaskController {

    @Autowired
    private PrGroupConfigTaskService prGroupConfigTaskService;

    @PostMapping(value = "/selectPrGroupConfigTaskList")
    public JsonResult selectPrGroupConfigTaskList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                               @RequestBody PrPayrollConfigTaskDTO prPayrollConfigTaskDTO) {
        PrPayrollConfigTaskPO prPayrollConfigTaskPO = new PrPayrollConfigTaskPO();
        BeanUtils.copyProperties(prPayrollConfigTaskDTO, prPayrollConfigTaskPO);
        PageInfo<PrPayrollConfigTaskPO> pageInfo = prGroupConfigTaskService.selectPrGroupConfigTaskList(
                prPayrollConfigTaskPO, pageNum, pageSize);
        List<PrPayrollConfigTaskDTO> resultList = pageInfo.getList()
                .stream()
                .map(PrPayrollConfigTaskTranslator::toPrPayrollConfigTaskDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollConfigTaskDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");

        return JsonResult.success(resultPage);
    }


}
