package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.GroupExampleChangeService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.CmyFcConfigureTaskTranslator;
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
public class GroupExampleChangeController {

    @Autowired
    private GroupExampleChangeService groupExampleChangeService;

    @PostMapping(value = "/selectPrGroupExampleList")
    public JsonResult selectPrGroupExampleList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                               @RequestBody CmyFcConfigureTaskDTO cmyFcConfigureTaskDTO) {
        CmyFcConfigureTaskPO cmyFcConfigureTaskPO = new CmyFcConfigureTaskPO();
        BeanUtils.copyProperties(cmyFcConfigureTaskDTO, cmyFcConfigureTaskPO);
        PageInfo<CmyFcConfigureTaskPO> pageInfo = groupExampleChangeService.selectPrGroupExampleList(
                cmyFcConfigureTaskPO, pageNum, pageSize);
        List<CmyFcConfigureTaskDTO> resultList = pageInfo.getList()
                .stream()
                .map(CmyFcConfigureTaskTranslator::toCmyFcConfigureTaskDTO)
                .collect(Collectors.toList());
        PageInfo<CmyFcConfigureTaskDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");

        return JsonResult.success(resultPage);
    }


}
