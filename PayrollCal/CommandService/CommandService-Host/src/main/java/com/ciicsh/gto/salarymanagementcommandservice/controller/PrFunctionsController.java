package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagement.entity.po.PrFunctionsPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrFunctionsDTO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrFunctionsService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PrFunctionsTranslator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-08-10 16:57
 * @description 标准函数库管理Controller
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class PrFunctionsController extends BaseController {

    @Autowired
    private PrFunctionsService prFunctionsService;

    @PostMapping("/getFunctionsList")
    public JsonResult getFunctionsList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                       @RequestBody PrFunctionsDTO prFunctionsDTO) {
        PrFunctionsPO prFunctionsPO = new PrFunctionsPO();
        BeanUtils.copyProperties(prFunctionsDTO, prFunctionsPO);
        PageInfo<PrFunctionsPO> pageInfo = prFunctionsService.getFunctionsListByName(prFunctionsPO, pageNum, pageSize);
        List<PrFunctionsDTO> resultList = pageInfo.getList()
                .stream()
                .map(PrFunctionsTranslator::toPrFunctionsDTO)
                .collect(Collectors.toList());

        PageInfo<PrFunctionsDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");

        return JsonResult.success(resultPage);
    }
}
