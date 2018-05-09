package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 供应商暂缓处理 Controller</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/23 0023
 */
@RestController
public class SupplierDeferController {

    @Autowired
    private SalaryGrantSupplierSubTaskService supplierSubTaskService;

    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/submit")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForSubmitPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = supplierSubTaskService.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO);

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }

    /**
     * 查询供应商任务单列表
     * 待审批:1-审批中 角色=审核员
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/approve")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForApprovePage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = supplierSubTaskService.querySupplierSubTaskForApprovePage(page, salaryGrantTaskBO);

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }

    /**
     * 查询供应商任务单列表
     * 已处理:1-审批中 角色=操作员
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/haveApproved")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForHaveApprovedPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = supplierSubTaskService.querySupplierSubTaskForHaveApprovedPage(page, salaryGrantTaskBO);

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }

    /**
     * 查询供应商任务单列表
     * 已处理:审批通过 2-审批通过、10-待合并、11-已合并 角色=操作员、审核员
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/pass")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForPassPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = supplierSubTaskService.querySupplierSubTaskForPassPage(page, salaryGrantTaskBO);

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }

    /**
     * 查询供应商任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回 角色=操作员、审核员（查历史表）
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/reject")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForRejectPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = supplierSubTaskService.querySupplierSubTaskForRejectPage(page, salaryGrantTaskBO);

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }

    /**
     * 审批通过供应商任务单
     *
     * @return
     */
    @RequestMapping("/supplierDefer/approveSubTask")
    public Result approveSupplierSubTask() {
        return new Result();
    }

    /**
     * 审批退回供应商任务单
     *
     * @return
     */
    @RequestMapping("/supplierDefer/rejectSubTask")
    public Result rejectSupplierSubTask() {
        return new Result();
    }
}
