package com.ciicsh.zorro.leopardwebservice.controller;

import com.ciicsh.zorro.leopardwebservice.entity.PrAccountSetEntity;
import com.ciicsh.zorro.leopardwebservice.service.IPrAccountSetService;
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
 * Created by jiangtianning on 2017/11/1.
 */
@RestController
public class PrAccountSetController {

    @Autowired
    private IPrAccountSetService prAccountSetService;

    @GetMapping(value = "/prAccountSet")
    public List<PrAccountSetEntity> getPrAccountSetList(@RequestParam String managementId,
                                                        @RequestParam Integer pageNum) {

        PrAccountSetEntity param = new PrAccountSetEntity();
        param.setManagementId(managementId);
        PageInfo<PrAccountSetEntity> pageInfo = prAccountSetService.getList(param, pageNum);
        List<PrAccountSetEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @PostMapping(value = "/prAccountSetQuery")
    public List<PrAccountSetEntity> searchPrAccountSetList(@RequestBody PrAccountSetEntity param,
                                                           @RequestParam Integer pageNum) {

        PageInfo<PrAccountSetEntity> pageInfo = prAccountSetService.getList(param, pageNum);
        List<PrAccountSetEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @PostMapping(value = "/prAccountSet")
    public Integer newPrAccountSet(@RequestBody PrAccountSetEntity param) {

        Random random = new Random();
        DecimalFormat df6 = new DecimalFormat("000000");
        DecimalFormat df5 = new DecimalFormat("00000");
        Integer tempID = random.nextInt(999999);
        Integer tempCode = random.nextInt(999999);
        param.setEntityId("PRAS" + df6.format(tempID));
        param.setCode("XZZT-CMY" + df6.format(tempCode) + "-001-01");
        param.setCreatedBy("jiang");
        param.setModifiedBy("jiang");
        int result = prAccountSetService.addItem(param);
        return result;
    }

    @GetMapping(value = "/prAccountSet/{id}")
    public PrAccountSetEntity getPrAccountSet(@PathVariable("id") String id) {

        PrAccountSetEntity result = prAccountSetService.getItemById(id);
        return result;
    }

    @PutMapping(value = "/prAccountSet/{id}")
    public int updatePrAccountSet(@PathVariable("id") String id,
                                  @RequestBody PrAccountSetEntity param) {

        param.setEntityId(id);
        param.setModifiedBy("jiang");
        param.setDataChangeLastTime(new Date());
        int result = prAccountSetService.updateItemById(param);
        return result;
    }

    @GetMapping(value = "/prAccountSetName")
    public List<String> getPrAccountSetNameList(@RequestParam String managementId) {

        List<String> resultList = prAccountSetService.getNameList(managementId);
        return resultList;
    }
}
