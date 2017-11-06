package com.ciicsh.zorro.leopardwebservice.controller;

import com.ciicsh.zorro.leopardwebservice.entity.PrItemTemplateEntity;
import com.ciicsh.zorro.leopardwebservice.service.PrItemTemplateService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by jiangtianning on 2017/10/20.
 */
@RestController
@RequestMapping(path = "/")
public class PrItemTemplateController {

    @Autowired
    private PrItemTemplateService prItemTemplateService;

    @GetMapping(value = "/prItemTemplate")
    @ResponseBody
    public List<PrItemTemplateEntity> getPrItemTemplateList(@RequestParam String managementId,
                                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum){

        PrItemTemplateEntity paramItem = new PrItemTemplateEntity();
        setCommonParam(paramItem, managementId);
//        List<PrItemTemplateEntity> resultList = prItemTemplateService.getList(paramItem);
        PageInfo<PrItemTemplateEntity> pageInfo =  prItemTemplateService.getList(paramItem, pageNum);
        List<PrItemTemplateEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @GetMapping(value = "/prItemTemplate/{id}")
    @ResponseBody
    public PrItemTemplateEntity getPrItemTemplate(@PathVariable("id") String code,
                                                  @RequestParam String managementId) {

        PrItemTemplateEntity paramItem = new PrItemTemplateEntity();
        setCommonParam(paramItem, managementId);
        paramItem.setCode(code);
        PrItemTemplateEntity result = prItemTemplateService.getItem(paramItem);
        return result;
    }

    @GetMapping(value = "/prItemTemplateName")
    @ResponseBody
    public List<String> getPrItemTemplateNameList(@RequestParam String managementId) {

        PrItemTemplateEntity paramItem = new PrItemTemplateEntity();
        setCommonParam(paramItem, managementId);
        List<String> resultList = prItemTemplateService.getNameList(managementId);
        return resultList;
    }

    @PostMapping(value = "/prItemTemplate")
    public String addPrItemTemplate(@RequestParam String managementId,
                                        @RequestBody PrItemTemplateEntity paramItem) {

        setCommonParam(paramItem, managementId);
        // TODO 获取EntityID
        Random random = new Random();
        DecimalFormat df6 = new DecimalFormat("000000");
        DecimalFormat df5 = new DecimalFormat("00000");
        Integer tempID = random.nextInt(999999);
        paramItem.setEntityId("PRIT" + df6.format(tempID));
        Integer tempCode = random.nextInt(9999);
        paramItem.setCode("XZXMB-" + df5.format(tempCode));
        paramItem.setCreatedBy("jiang");
        paramItem.setModifiedBy("jiang");
        // 成功返回: 薪资项模板编码
        // 失败返回: 空
        String resultCode = prItemTemplateService.addItem(paramItem);
        return resultCode;
    }

    @PutMapping(value = "/prItemTemplate/{id}")
    public int updatePrItemTemplate(@PathVariable("id") String code,
                                            @RequestParam String managementId,
                                            @RequestBody PrItemTemplateEntity paramItem) {

        setCommonParam(paramItem, managementId);
        paramItem.setCode(code);
        paramItem.setModifiedBy("jiang");
        int result = prItemTemplateService.updateItem(paramItem);
        return result;
    }

    @PostMapping(value = "/prItemTemplateQuery")
    public List<PrItemTemplateEntity> prItemTemplateListQuery(@RequestParam String managementId,
                                                              @RequestBody PrItemTemplateEntity paramItem,
                                                              @RequestParam(required = false, defaultValue = "1") Integer pageNum) {

        setCommonParam(paramItem, managementId);
        PageInfo<PrItemTemplateEntity> pageInfo =  prItemTemplateService.getList(paramItem, pageNum);
        List<PrItemTemplateEntity> resultList = pageInfo.getList();
        return resultList;
    }

    private void setCommonParam(PrItemTemplateEntity paramModel, String managementId) {
        if (paramModel != null) {
            paramModel.setManagementId(managementId);
        } else {

        }
    }
}
