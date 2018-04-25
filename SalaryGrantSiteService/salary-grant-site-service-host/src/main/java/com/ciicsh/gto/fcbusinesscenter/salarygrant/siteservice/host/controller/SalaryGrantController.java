package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryTaskDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.transform.CommonTransform;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 薪资发放任务单 控制类
 * </p>
 *
 * @author gaoyang
 * @since 2018-1-16
 */
@RestController
@RequestMapping(value="/api/sg")
public class SalaryGrantController {

    /**
     * 记录日志
     */
    @Autowired
    LogServiceProxy logService;

    @Autowired
    private SalaryGrantService salaryGrantService;

    @Autowired
    private SalaryGrantTaskQueryService salaryGrantTaskQueryService;

    @Autowired
    private SheetServiceProxy sheetServiceProxy;

    /**
     * 薪资发放任务单一览
     * @author chenpb
     * @date 2018-04-20
     * @param
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Result<SalaryTaskDTO> list(@RequestBody SalaryTaskDTO dto) {
        Map<String, String> tags = new HashMap<>();
        tags.put("taskCode", dto.getTaskCode());
        tags.put("batchCode", dto.getBatchCode());
        tags.put("managementId", dto.getManagementId());
        tags.put("managementName", dto.getManagementName());
        tags.put("grantMode", dto.getGrantMode());
        tags.put("grantCycle", dto.getGrantCycle());
        tags.put("taskStatus", dto.getTaskStatus());
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询").setContent("条件").setTags(tags));
        try {
            Page page = null;
            Page<SalaryGrantTaskBO> paging = new Page<SalaryGrantTaskBO>(dto.getCurrent(), dto.getSize());
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            System.out.println("状态：" + bo.getTaskStatus());
            if (SalaryGrantBizConsts.TASK_STATUS_DRAFT.equals(bo.getTaskStatus())) {
                page = salaryGrantTaskQueryService.queryTaskForSubmitPage(paging, bo);
            } else if (SalaryGrantBizConsts.TASK_STATUS_APPROVAL.equals(bo.getTaskStatus())) {
                page = salaryGrantTaskQueryService.queryTaskForApprovePage(paging, bo);
            } else if (SalaryGrantBizConsts.TASK_STATUS_PASS.equals(bo.getTaskStatus())) {
                page = salaryGrantTaskQueryService.queryTaskForPassPage(paging, bo);
            } else if (SalaryGrantBizConsts.TASK_STATUS_REFUSE.equals(bo.getTaskStatus())) {
                page = salaryGrantTaskQueryService.queryTaskForRejectPage(paging, bo);
            } else if (SalaryGrantBizConsts.TASK_STATUS_CANCEL.equals(bo.getTaskStatus())) {
                page = salaryGrantTaskQueryService.queryTaskForInvalidPage(paging, bo);
            }
            List<SalaryTaskDTO> list = CommonTransform.convertToDTOs(page.getRecords(), SalaryTaskDTO.class);
            page.setRecords(list);
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询结果").setContent(JSON.toJSONString(page)));
            return ResultGenerator.genSuccessResult(page);
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询 -> 异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("薪资发放一览查询失败");
        }
    }

    /**
     * 薪资发放任务单提交（点击提交按钮）
     * @author chenpb
     * @date 2018-04-20
     * @param
     * @return
     */
    @RequestMapping(value="/submit", method = RequestMethod.POST)
    public Result completeTask(@RequestBody SalaryGrantTaskRequestDTO salaryGrantMainTaskTaskRequestDTO) throws Exception {
        // logger.info("customer系统调用完成任务接口："+custTaskRequestDTO.toString());
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTaskId(salaryGrantMainTaskTaskRequestDTO.getTaskId());
        taskRequestDTO.setAssignee(salaryGrantMainTaskTaskRequestDTO.getAssignee());
        taskRequestDTO.setVariables(salaryGrantMainTaskTaskRequestDTO.getVariables());
        //
        com.ciicsh.gto.commonservice.util.dto.Result restResult = sheetServiceProxy.completeTask(taskRequestDTO);
        //logger.info("customer系统收到完成任务接口返回："+String.valueOf("code:"+restResult.getCode()+"message:")+restResult.getMessage());
        return ResultGenerator.genSuccessResult(true);

    }

    /**
     * 失效
     * @author chenpb
     * @date 2018-04-24
     * @param
     * @return
     */
    @RequestMapping(value="/cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody SalaryTaskDTO dto) {
        try {
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            List<SalaryTaskDTO> list = CommonTransform.convertToDTOs(null, SalaryTaskDTO.class);
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询 -> 异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("薪资发放失效处理失败");
        }
    }

    /**
     * 薪资发放详情
     * @author chenpb
     * @date 2018-04-24
     * @param
     * @return
     */
    @RequestMapping(value="/detail", method = RequestMethod.POST)
    public Result detail(@RequestBody SalaryTaskDetailDTO dto) {
        Map<String, String> tags = new HashMap<>();
        tags.put("taskCode", dto.getTaskCode());
        tags.put("taskType", dto.getTaskType().toString());
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("详情").setContent("条件").setTags(tags));
        try {
            SalaryTaskDetailDTO salaryTaskDetailDTO = new SalaryTaskDetailDTO();
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo = salaryGrantTaskQueryService.selectTaskByTaskCode(bo);
            SalaryGrantDetailDTO salaryGrantDetailDTO = CommonTransform.convertToDTO(bo, SalaryGrantDetailDTO.class);
            salaryTaskDetailDTO.setTask(salaryGrantDetailDTO);
            return ResultGenerator.genSuccessResult(salaryTaskDetailDTO);
        } catch (Exception e) {
            return ResultGenerator.genServerFailResult("薪资发放详情查询失败");
        }
    }

    //todo
    //薪资发放任务单失效（点击失效按钮）

    //todo
    //查询薪资发放任务单明细信息（点击任务单编号链接）
    //包括查询雇员列表信息

    //todo
    //查询薪资发放任务单审批信息（点击审批按钮）
    //包括查询雇员列表信息

    //todo
    //创建薪资发放任务单信息（订阅计算引擎消息）
    //1、创建薪资发放任务单 2、拆分雇员数据插入到雇员信息表

    //todo
    //薪资发放任务单批量提交（点击批量提交按钮）

    //todo
    //查询任务单列表中雇员信息变更记录（点击任务单备注链接）

    //todo
    //生成工资清单：业务明细

    //todo
    //生成工资清单：财务明细

    //todo
    //生成工资清单：打印预览

    //todo
    //生成工资清单：打印操作

    //todo
    //查询薪资发放任务单主表拆分的子任务单

    //todo
    //薪资发放任务单主表撤回操作

    //todo
    //薪资发放任务单子表撤回操作

    //todo
    //导出雇员信息

    //todo
    //导入暂缓名单

    //todo
    //暂缓雇员操作

    //todo
    //恢复雇员操作

    //todo
    //批量暂缓雇员操作

    //todo
    //批量恢复雇员操作

    //todo
    // 任务单流程--查看流程日志

    //todo
    //调整信息-对于发放类型是调整发放。查询调整/回溯批次、基于的计算批次，查询2条计算结果数据，再进行数据合并生成第三条合并数据。

    //todo
    //查询任务单中雇员信息变更历史记录（点击雇员备注链接）
}
