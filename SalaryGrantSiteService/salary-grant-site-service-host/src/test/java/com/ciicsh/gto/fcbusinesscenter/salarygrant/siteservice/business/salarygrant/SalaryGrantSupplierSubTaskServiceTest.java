package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/23 0023
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantSupplierSubTaskServiceTest {
    @Autowired
    private SalaryGrantSupplierSubTaskService supplierSubTaskService;

    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     */
    @Test
    public void querySupplierSubTaskForSubmitPage() {
        Page<SalaryGrantTaskBO> page = new Page<>();
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
    }

    /**
     * 查询供应商任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回 角色=操作员、审核员（查历史表）
     */
    @Test
    public void querySupplierSubTaskForRejectPage() {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(3);
        page.setSize(2);

        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
//        salaryGrantTaskBO.setCurrentUserId("001");
//        salaryGrantTaskBO.setTaskCode("ST201803280000000001");
//        salaryGrantTaskBO.setManagementId("001");
//        salaryGrantTaskBO.setManagementName("星巴克");
//        salaryGrantTaskBO.setBatchCode("CMY000001-20180328-0001");
//        salaryGrantTaskBO.setGrantAccountCode("sp001");

        Page<SalaryGrantTaskBO> taskBOPage = supplierSubTaskService.querySupplierSubTaskForRejectPage(page, salaryGrantTaskBO);
        System.out.println("查询供应商任务单列表 page: " + taskBOPage + " 记录: " + taskBOPage.getRecords());
    }

    /**
     * BO PAGE TO DTO PAGE
     */
    @Test
    public void boPageToDTOPage() {
        SalaryGrantTaskDTO salaryGrantTaskDTO = new SalaryGrantTaskDTO();
        salaryGrantTaskDTO.setCurrent(1);
        salaryGrantTaskDTO.setSize(2);

        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);

        page = supplierSubTaskService.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO);
        System.out.println("BO PAGE TO DTO PAGE BO page: " + page + " 记录: " + page.getRecords());

//        String boJsonStr = JSONArray.toJSONString(page.getRecords());
//
//        // 复制分页信息
//        Page<SalaryGrantTaskDTO> taskDTOPage = new Page<>();
//        BeanUtils.copyProperties(page, taskDTOPage);
//        taskDTOPage.setRecords(JSONArray.parseArray(boJsonStr, SalaryGrantTaskDTO.class));
//        System.out.println("BO PAGE TO DTO PAGE DTO page: " + taskDTOPage + " 记录: " + taskDTOPage.getRecords());

        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);
        System.out.println("BO PAGE TO DTO PAGE DTO page: " + taskDTOPage + " 记录: " + taskDTOPage.getRecords());

    }
}
