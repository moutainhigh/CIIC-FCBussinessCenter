package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantEmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.WorkFlowResultDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantWorkFlowEnums;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowResultBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util.CommonTransform;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private SalaryGrantTaskQueryService salaryGrantTaskQueryService;
    @Autowired
    LogClientService logClientService;

    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/submit")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForSubmitPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("查询供应商任务单列表 待提交").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

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
     * 比较雇员最新信息之后
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/supplierDefer/submitUpdateEmployee")
    public Page<SalaryGrantTaskDTO> querySupplierSubTaskForSubmitPageUpdateEmployee(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("查询供应商任务单列表 待提交").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = supplierSubTaskService.querySupplierSubTaskForSubmitPageUpdateEmployee(page, salaryGrantTaskBO);

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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("查询供应商任务单列表 待审批").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("查询供应商任务单列表 已处理:审批中").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("查询供应商任务单列表 已处理:审批通过").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("查询供应商任务单列表 已处理:审批拒绝").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

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
     * 审批通过
     *
     * @return
     */
    @RequestMapping("/supplierDefer/approveSubTask")
    public Result approveSupplierSubTask(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("审批通过").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

        try {
            SalaryGrantTaskBO salaryGrantTaskBO = CommonTransform.convertToEntity(salaryGrantTaskDTO, SalaryGrantTaskBO.class);
            salaryGrantTaskBO.setUserId(UserContext.getUserId());
            salaryGrantTaskBO.setUserName(UserContext.getLoginName());

            if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskQueryService.lockMainTask(salaryGrantTaskBO) > 0) {
                salaryGrantTaskQueryService.approvalPass(true, salaryGrantTaskBO);
            } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskQueryService.lockSubTask(salaryGrantTaskBO) > 0) {
                salaryGrantTaskQueryService.approvalPass(false, salaryGrantTaskBO);
            } else {
                return new Result(1, "任务单已通过审批！", null, null);
            }

            return new Result(0, null, null, null);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("审批通过异常").setContent(e.getMessage()));
            return new Result(2, "系统异常，请稍后重试！", null, null);
        }
    }

    /**
     * 审批退回
     *
     * @return
     */
    @RequestMapping("/supplierDefer/rejectSubTask")
    public Result rejectSupplierSubTask(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("审批退回").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

        try {
            SalaryGrantTaskBO salaryGrantTaskBO = CommonTransform.convertToEntity(salaryGrantTaskDTO, SalaryGrantTaskBO.class);
            salaryGrantTaskBO.setUserId(UserContext.getUserId());
            salaryGrantTaskBO.setUserName(UserContext.getLoginName());

            if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskQueryService.lockMainTask(salaryGrantTaskBO) > 0) {
                salaryGrantTaskQueryService.approvalReject(true, salaryGrantTaskBO);
            } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskQueryService.lockSubTask(salaryGrantTaskBO) > 0) {
                salaryGrantTaskQueryService.approvalReject(false, salaryGrantTaskBO);
            } else {
                return new Result(1, "任务单已退回！", null, null);
            }

            return new Result(0, null, null, null);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("审批退回异常").setContent(e.getMessage()));
            return new Result(2, "系统异常，请稍后重试！", null, null);
        }
    }

    /**
     * 提交子任务单
     *
     * @param salaryGrantTaskDTO
     * @return
     */
    @RequestMapping("/supplierDefer/submitSupplierSubTask")
    public Result<WorkFlowResultDTO> submitSupplierSubTask(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("提交子任务单").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

        try {
            WorkFlowResultBO resultBo = BeanUtils.instantiate(WorkFlowResultBO.class);
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(salaryGrantTaskDTO, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            bo.setUserName(UserContext.getLoginName());

            if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType()) && salaryGrantTaskQueryService.lockMainTask(bo) > 0) {
                resultBo = salaryGrantTaskQueryService.submit(true, bo);
            } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType()) && salaryGrantTaskQueryService.lockSubTask(bo) > 0) {
                resultBo = salaryGrantTaskQueryService.submit(false, bo);
            } else {
                resultBo.setResult(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getResult());
                resultBo.setMessage(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getExtension());
            }

            WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(resultBo, WorkFlowResultDTO.class);

            return new Result(0, null, resultDto, null);
        } catch (Exception e) {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("提交子任务单").setContent("异常"));
            return new Result(1, "系统异常，请稍后重试！", null, null);
        }
    }

    /**
     * 批量提交子任务单
     *
     * @param salaryGrantTaskDTOList
     * @return
     */
    @RequestMapping("/supplierDefer/batchSubmitSupplierSubTask")
    public Result<List<WorkFlowResultDTO>> batchSubmitSupplierSubTask(@RequestBody List<SalaryGrantTaskDTO> salaryGrantTaskDTOList) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("批量提交子任务单").setContent(JSON.toJSONString(salaryGrantTaskDTOList)));

        List<WorkFlowResultDTO> failList = new ArrayList<>();
        try {
            if (!salaryGrantTaskDTOList.isEmpty()) {
                salaryGrantTaskDTOList.stream().forEach(salaryGrantTaskDTO -> {
                    WorkFlowResultBO workFlowResultBO = BeanUtils.instantiate(WorkFlowResultBO.class);
                    workFlowResultBO.setTaskCode(salaryGrantTaskDTO.getTaskCode());
                    workFlowResultBO.setTaskType(salaryGrantTaskDTO.getTaskType());

                    SalaryGrantTaskBO salaryGrantTaskBO = CommonTransform.convertToEntity(salaryGrantTaskDTO, SalaryGrantTaskBO.class);
                    salaryGrantTaskBO.setUserId(UserContext.getUserId());
                    salaryGrantTaskBO.setUserName(UserContext.getLoginName());

                    try {
                        if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskQueryService.lockMainTask(salaryGrantTaskBO) > 0) {
                            workFlowResultBO = salaryGrantTaskQueryService.submit(true, salaryGrantTaskBO);
                        } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskQueryService.lockSubTask(salaryGrantTaskBO) > 0) {
                            workFlowResultBO = salaryGrantTaskQueryService.submit(false, salaryGrantTaskBO);
                        } else {
                            workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getResult());
                            workFlowResultBO.setMessage(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getExtension());
                        }

                        WorkFlowResultDTO workFlowResultDTO = CommonTransform.convertToDTO(workFlowResultBO, WorkFlowResultDTO.class);

                        failList.add(workFlowResultDTO);
                    } catch (Exception e) {
                        workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.EXCEPTION.getResult());
                        workFlowResultBO.setMessage(SalaryGrantWorkFlowEnums.TaskResult.EXCEPTION.getExtension());

                        WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(workFlowResultBO, WorkFlowResultDTO.class);

                        failList.add(resultDto);

                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("供应商暂缓处理").setTitle("批量提交子任务单").setContent("异常"));
                    }
                });
            }
        } catch (Exception e) {
            return new Result(1, "系统异常，请稍后重试！", null, null);
        }

        return new Result(0, null, null, failList);
    }
}
