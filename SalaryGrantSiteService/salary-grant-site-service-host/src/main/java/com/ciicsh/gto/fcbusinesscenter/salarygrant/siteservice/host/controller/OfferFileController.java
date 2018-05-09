package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: 报盘文件 Controller</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/24 0024
 */
@RestController
public class OfferFileController {
    @Autowired
    private SalaryGrantOfferDocumentTaskService offerDocumentTaskService;
    @Autowired
    private CommonService commonService;

    /**
     * 查询薪资发放报盘任务单列表
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/offerFile/DocumentTask")
    public Page<SalaryGrantTaskDTO> queryOfferDocumentTaskPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = offerDocumentTaskService.queryOfferDocumentTaskPage(page, salaryGrantTaskBO);

        //字段转换
        List<SalaryGrantTaskBO> grantTaskBOList = page.getRecords();
        if (!CollectionUtils.isEmpty(grantTaskBOList)) {
            int size = grantTaskBOList.size();
            SalaryGrantTaskBO grantTaskBO;
            for (int i = 0; i < size; i++) {
                grantTaskBO = grantTaskBOList.get(i);
                //发放类型不为空时设置发放类型名称
                if (!ObjectUtils.isEmpty(grantTaskBO.getGrantType())) {
                    grantTaskBO.setGrantTypeName(commonService.getNameByValue("sgmGrantType", String.valueOf(grantTaskBO.getGrantType())));
                }
            }
        }

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }
}
