package com.ciicsh.zorro.leopardwebservice.controller;

import com.ciicsh.zorro.leopardwebservice.entity.PrGroupTemplateEntity;
import com.ciicsh.zorro.leopardwebservice.service.PrGroupTemplateService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by jiangtianning on 2017/11/2.
 */
@RestController
public class PrGroupTemplateController {

    @Autowired
    private PrGroupTemplateService prGroupTemplateService;

    @GetMapping(value = "/prGroupTemplate")
    public List<PrGroupTemplateEntity> getPrGroupTemplateList(@RequestParam Integer pageNum) {
        PageInfo<PrGroupTemplateEntity> pageInfo = prGroupTemplateService.getList(new PrGroupTemplateEntity(), pageNum);
        List<PrGroupTemplateEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @PostMapping(value = "/prGroupTemplateQuery")
    public List<PrGroupTemplateEntity> searchPrGroupTemplateList(@RequestParam Integer pageNum,
                                                                 @RequestBody PrGroupTemplateEntity param) {
        PageInfo<PrGroupTemplateEntity> pageInfo = prGroupTemplateService.getList(param, pageNum);
        List<PrGroupTemplateEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @GetMapping(value = "/prGroupTemplate/{id}")
    public PrGroupTemplateEntity getPrGroupTemplateById(@PathVariable("id") String id) {
        PrGroupTemplateEntity result = prGroupTemplateService.getItemById(id);
        return result;
    }

    @PutMapping(value = "/prGroupTemplate/{id}")
    public Integer updatePrGroupTemplateById(@PathVariable("id") String id,
                                             @RequestBody PrGroupTemplateEntity param) {
        param.setEntityId(id);
        param.setModifiedBy("jiang");
        Integer result = prGroupTemplateService.updateItemById(param);
        return result;
    }

    @PostMapping(value = "/prGroupTemplate")
    public Integer newPrGroupTemplate(@RequestBody PrGroupTemplateEntity param) {
        // TODO 获取EntityID
        Random random = new Random();
        DecimalFormat df6 = new DecimalFormat("000000");
        Integer tempID = random.nextInt(999999);
        param.setEntityId("PRGT" + df6.format(tempID));
        Integer tempCode = random.nextInt(999999);
        param.setCode("XZZMB-" + df6.format(tempCode));
        param.setCreatedBy("jiang");
        param.setModifiedBy("jiang");
        // Version 生成
        param.setVersion("1.0.0");
        int result = prGroupTemplateService.addItem(param);
        return result;
    }

    @GetMapping(value = "/prGroupTemplateName")
    public List<String> getPrGroupTemplateNameList() {
        List<String> resultList = prGroupTemplateService.getNameList();
        return resultList;
    }
}
