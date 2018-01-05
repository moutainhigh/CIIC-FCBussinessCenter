package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.PayrollGroupTemplateProxy;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupTemplateDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupTemplateHistoryDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplateHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrGroupTemplateServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.translator.GroupTemplateTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.ItemTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.TranslatorUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.constants.ResultMessages;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/2.
 * @author jiangtianning
 */
@RestController
public class GroupTemplateController extends BaseController implements PayrollGroupTemplateProxy{

    @Autowired
    private PrGroupTemplateServiceImpl prGroupTemplateService;

    @PostMapping(value = "/prGroupTemplateQuery")
    public JsonResult searchPrGroupTemplateList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
                                                @RequestBody PrPayrollGroupTemplateDTO param) {
        PrPayrollGroupTemplatePO prPayrollGroupTemplatePO = new PrPayrollGroupTemplatePO();
        TranslatorUtils.copyNotNullProperties(param, prPayrollGroupTemplatePO);
        PageInfo<PrPayrollGroupTemplatePO> pageInfo = prGroupTemplateService.getList(prPayrollGroupTemplatePO, pageNum, pageSize);
        List<PrPayrollGroupTemplateDTO> resultList = pageInfo.getList()
                .stream()
                .map(GroupTemplateTranslator::toPrPayrollGroupTemplateDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollGroupTemplateDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }

    @GetMapping(value = "/prGroupTemplate/{code}")
    public JsonResult getPrGroupTemplateByCode(@PathVariable("code") String code) {
        PrPayrollGroupTemplatePO resultPO = prGroupTemplateService.getItemByCode(code);
        PrPayrollGroupTemplateDTO resultDTO = GroupTemplateTranslator.toPrPayrollGroupTemplateDTO(resultPO);
        return JsonResult.success(resultDTO);
    }

    @PutMapping(value = "/prGroupTemplate/{code}")
    public JsonResult updatePrGroupTemplateByCode(@PathVariable("code") String code,
                                             @RequestBody PrPayrollGroupTemplateDTO param) {

        PrPayrollGroupTemplatePO updateParam = new PrPayrollGroupTemplatePO();
        TranslatorUtils.copyNotNullProperties(param, updateParam);
        updateParam.setGroupTemplateCode(code);
        updateParam.setModifiedBy("jiang");
        Integer result = prGroupTemplateService.updateItemByCode(updateParam);
        return JsonResult.success(result);
    }

    @PostMapping(value = "/prGroupTemplate")
    public JsonResult newPrGroupTemplate(@RequestBody PrPayrollGroupTemplateDTO param) {
        PrPayrollGroupTemplatePO newParam = GroupTemplateTranslator.toPrPayrollGroupTemplatePO(param);
        newParam.setGroupTemplateCode(codeGenerator.genPrGroupTemplateCode());
        // Version 生成
        newParam.setVersion("1.0");
        newParam.setCreatedBy("jiang");
        newParam.setModifiedBy("jiang");
        int result = prGroupTemplateService.newItem(newParam);
        return result > 0 ? JsonResult.success(newParam.getGroupTemplateCode()) : JsonResult.faultMessage("新建薪资组模板失败");
    }

    @GetMapping(value = "/prGroupTemplateName")
    public JsonResult getPrGroupTemplateNameList() {
        List<HashMap<String, String>> resultList = prGroupTemplateService.getNameList();
        return JsonResult.success(resultList);
    }

    @GetMapping("/getPayrollGroupTemplateNames")
    public JsonResult getPayrollGroupTemplateNames(@RequestParam String query){
        List<KeyValuePO> resultList = prGroupTemplateService.getPayrollGroupTemplateNames();
        List<KeyValuePO> results = new ArrayList<>();
        results.clear();
        if(resultList.size() > 0){
            if(CommonUtils.isContainChinese(query)){
                resultList.forEach(item->{
                    if(item.getValue().contains(query)){
                        results.add(item);
                    }
                });
            }else {
                resultList.forEach(item ->{
                    if(item.getKey().contains(query)){
                        results.add(item);
                    }
                });
            }
        }
        return JsonResult.success(results);
    }

    @DeleteMapping(value = "/prGroupTemplate/{code}")
    public JsonResult deleteItem(@PathVariable("code") String code){
        String[] codes = code.split(",");
        int i = prGroupTemplateService.deleteByCodes(Arrays.asList(codes));
        if (i >= 1){
            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

    /**
     * 获取薪资租模板历史数据
     * @param code
     * @return
     */
    @GetMapping("/lastVersionPayrollGroupTemplateItems")
    public JsonResult getLastVersion(@RequestParam("code") String code) {
//        JSONObject result = new JSONObject();
        PrPayrollGroupTemplateHistoryPO lastVersionData = prGroupTemplateService.getLastVersion(code);
        if (lastVersionData == null) {
            return JsonResult.faultMessage("该薪资组模板没有历史版本");
        }
        List<PrPayrollItemPO> lastVersionItems = JSON.parseObject(lastVersionData.getPayrollGroupTemplateHistory(),
                new TypeReference<List<PrPayrollItemPO>>() {});
        List<PrPayrollItemDTO> lastVersionItemsDTO = lastVersionItems
                .stream()
                .map(ItemTranslator::toPrPayrollItemDTO)
                .collect(Collectors.toList());
        PrPayrollGroupTemplateHistoryDTO resultObject = new PrPayrollGroupTemplateHistoryDTO();
        BeanUtils.copyProperties(lastVersionData, resultObject);
        resultObject.setItemDTOList(lastVersionItemsDTO);
        return JsonResult.success(resultObject);
    }

    /**
     * 审核薪资租模板
     * @param code
     * @param paramItem
     * @return
     */
    @PutMapping("/approvePayrollGroupTemplate/{code}")
    public JsonResult approvePayrollGroupTemplate(@PathVariable("code") String code, @RequestBody PrPayrollGroupTemplateDTO paramItem){

        PrPayrollGroupTemplatePO updateParam = new PrPayrollGroupTemplatePO();
        TranslatorUtils.copyNotNullProperties(paramItem, updateParam);
        updateParam.setGroupTemplateCode(code);
        updateParam.setModifiedBy("system");
        boolean result = prGroupTemplateService.approvePrGroupTemplate(updateParam);
        return result ? JsonResult.success(null, ResultMessages.PAYROLL_GROUP_TEMPLATE_APPROVE_SUCCESS)
                : JsonResult.faultMessage(ResultMessages.PAYROLL_GROUP_TEMPLATE_APPROVE_FAIL);
    }
}
