package com.ciicsh.zorro.leopardwebservice.controller;

import com.ciicsh.zorro.leopardwebservice.entity.PrGroupEntity;
import com.ciicsh.zorro.leopardwebservice.service.IPrGroupService;
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
 * Created by jiangtianning on 2017/10/24.
 */
@RestController
@RequestMapping(value = "/")
public class PrGroupController {

    @Autowired
    private IPrGroupService prGroupService;

    @GetMapping(value = "/prGroup")
    @ResponseBody
    public List<PrGroupEntity> getPrGroupList(@RequestParam String managementId,
                                              @RequestParam(required = false, defaultValue = "1") Integer pageNum){

        PrGroupEntity paramItem = new PrGroupEntity();
        setCommonParam(paramItem, managementId,null);
        PageInfo<PrGroupEntity> pageInfo =  prGroupService.getList(paramItem, pageNum);
        List<PrGroupEntity> resultList = pageInfo.getList();
        return resultList;
    }

    /**
     * 薪资组条件查询
     * @param paramItem
     * @param pageNum
     * @return
     */
    @PostMapping(value = "/prGroupQuery")
    @ResponseBody
    public List<PrGroupEntity> getPrGroupQueryList(@RequestBody PrGroupEntity paramItem,
                                              @RequestParam(required = false, defaultValue = "1") Integer pageNum){

        PageInfo<PrGroupEntity> pageInfo =  prGroupService.getQueryList(paramItem, pageNum);
        List<PrGroupEntity> resultList = pageInfo.getList();
        return resultList;
    }

    /**
     * 获取薪资组名列表
     * @param managementId
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/prGroupName")
    @ResponseBody
    public List<String> getPrGroupNameList(@RequestParam String managementId,
                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum){
        PrGroupEntity paramItem = new PrGroupEntity();
        setCommonParam(paramItem, managementId, null);
        List<String> resultList = prGroupService.getNameList(managementId);
        return resultList;
    }


    /**
     * 更新薪资组
     * @return
     */
    @PutMapping(value = "/prGroup/{id}")
    @ResponseBody
    public String updatePrGroup(@PathVariable("id") String id,@RequestBody PrGroupEntity prGroupEntity){

        int count = prGroupService.updateItem(prGroupEntity);
        System.out.println("修改行数："+count);
        if(count==1){
            return "success";
        }else {
            return "false";
        }
    }


    @GetMapping(value = "/prGroup/{id}")
    @ResponseBody
    public PrGroupEntity getPrGroup(@PathVariable("id") String id,
                                    @RequestParam String managementId) {

        PrGroupEntity paramItem = new PrGroupEntity();
        setCommonParam(paramItem, managementId, null);
        paramItem.setEntityId(id);
        PrGroupEntity result =  prGroupService.getItem(paramItem);
        return result;
    }

    @PostMapping(value = "/prGroup")
    @ResponseBody
    public String newPrGroup(@RequestBody PrGroupEntity paramItem){

//        setCommonParam(paramItem, managementId);
        // TODO 获取EntityID
        Random random = new Random();
        DecimalFormat df6 = new DecimalFormat("000000");
        DecimalFormat df3 = new DecimalFormat("000");
        Integer tempID = random.nextInt(999999);
        paramItem.setEntityId("PRG" + df6.format(tempID));
        Integer tempCode = random.nextInt(999);
        paramItem.setCode("XZZ-001-" + df3.format(tempCode));
        paramItem.setCreatedBy("jiang");
        paramItem.setModifiedBy("jiang");
        // Version 生成
        paramItem.setVersion("1.0.0");
        // 成功返回: 薪资项模板编码
        // 失败返回: 空
        String resultCode = prGroupService.addItem(paramItem);
        return resultCode;
    }

    private void setCommonParam(PrGroupEntity paramModel, String managementId, String name) {
        if (paramModel != null) {
            paramModel.setManagementId(managementId);
        }
        if (name != null) {
            paramModel.setName(name);
        }
    }
}
