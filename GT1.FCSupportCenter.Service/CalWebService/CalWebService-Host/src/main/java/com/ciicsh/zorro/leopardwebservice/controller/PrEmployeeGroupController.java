package com.ciicsh.zorro.leopardwebservice.controller;

import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeEntity;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeGroupEntity;
import com.ciicsh.zorro.leopardwebservice.service.PrEmployeeGroupService;
import com.ciicsh.zorro.leopardwebservice.service.PrEmployeeService;
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

import javax.websocket.server.PathParam;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@RestController
@RequestMapping(value = "/")
public class PrEmployeeGroupController {

    @Autowired
    private PrEmployeeGroupService prEmployeeGroupService;

    @Autowired
    private PrEmployeeService prEmployeeService;

    @GetMapping(value = "/prEmployeeGroup")
    public List<PrEmployeeGroupEntity> getEmpGroupList(@RequestParam String managementId,
                                                       @RequestParam String companyId,
                                                       @RequestParam(required = false, defaultValue = "1") Integer pageNum) {

        PrEmployeeGroupEntity paramItem = new PrEmployeeGroupEntity();
        setCommonParam(paramItem, managementId, companyId);
        PageInfo<PrEmployeeGroupEntity> pageInfo =  prEmployeeGroupService.getList(paramItem, pageNum);
        List<PrEmployeeGroupEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @GetMapping(value = "/prEmployeeGroupName")
    public List<String> getEmployeeNameList(@RequestParam String managementId,
                                            @RequestParam String companyId) {

        List<String> resultList = prEmployeeGroupService.getNameList(managementId, companyId);
        return resultList;
    }

    @PostMapping(value = "/prEmployeeGroup")
    public String newEmployeeGroup(@RequestBody PrEmployeeGroupEntity prEmployeeGroupEntity){

        Random random = new Random();
        DecimalFormat df6 = new DecimalFormat("000000");
        DecimalFormat df5 = new DecimalFormat("00000");
        Integer tempID = random.nextInt(999999);
        prEmployeeGroupEntity.setEntityId("PEG" + df6.format(tempID));
        Integer tempCode = random.nextInt(9999);
        prEmployeeGroupEntity.setCode("GYZ-CMY000001-" + df5.format(tempCode));
        prEmployeeGroupEntity.setCreatedBy("jiang");
        prEmployeeGroupEntity.setModifiedBy("jiang");
        int result = prEmployeeGroupService.addItem(prEmployeeGroupEntity);
        return "" + result;
    }

    @GetMapping(value = "/prEmployeeGroup/{id}")
    public PrEmployeeGroupEntity getEmployeeGroup(@PathVariable("id") String id,
                                   @RequestParam String managementId,
                                   @RequestParam String companyId){

        PrEmployeeGroupEntity paramEntity = new PrEmployeeGroupEntity();
        setCommonParam(paramEntity, managementId, companyId);
        paramEntity.setEntityId(id);
        PrEmployeeGroupEntity result = prEmployeeGroupService.getItem(paramEntity);
        return result;
    }

    @GetMapping(value = "/prEmployee")
    public List<PrEmployeeEntity> getEmployeeList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                  @RequestParam(required = false, defaultValue = "") String depart,
                                                  @RequestParam(required = false, defaultValue = "") String position){

        PrEmployeeEntity paramEntity = new PrEmployeeEntity();
        PageInfo<PrEmployeeEntity> pageInfo = prEmployeeService.getEmployeeList(paramEntity, pageNum);
        List<PrEmployeeEntity> resultList = pageInfo.getList();
        return resultList;
    }

    @PutMapping(value = "/prEmployeeGroup/{id}")
    public int updateEmployeeGroup(@PathVariable("id") String id,
                                                     @RequestBody PrEmployeeGroupEntity prEmployeeGroupEntity){

        prEmployeeGroupEntity.setEntityId(id);
        prEmployeeGroupEntity.setCreatedBy("jiang");
        prEmployeeGroupEntity.setModifiedBy("jiang");
        int result = prEmployeeGroupService.updateItem(prEmployeeGroupEntity);
        return result;
    }

    @PostMapping(value = "/prEmployeeGroupQuery")
    public List<PrEmployeeGroupEntity> prItemTemplateListQuery(@RequestBody PrEmployeeGroupEntity paramItem,
                                                              @RequestParam(required = false, defaultValue = "1") Integer pageNum) {

        PageInfo<PrEmployeeGroupEntity> pageInfo =  prEmployeeGroupService.getList(paramItem, pageNum);
        List<PrEmployeeGroupEntity> resultList = pageInfo.getList();
        return resultList;
    }

    private void setCommonParam(PrEmployeeGroupEntity paramModel, String managementId, String companyId) {
        if (paramModel != null) {
            paramModel.setManagementId(managementId);
            paramModel.setCompanyId(companyId);
        } else {

        }
    }
}
