package com.ciicsh.gto.fcbusinesscenter.site.service.host.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.site.service.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.site.service.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.site.service.api.dto.SalaryGrantMainTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.site.service.api.dto.SalaryGrantMainTaskTaskRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.site.service.business.SalaryGrantMainTaskService;
import com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo.SalaryGrantMainTaskBO;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonTransform;
import com.ciicsh.gto.fcbusinesscenter.util.common.PageUtil;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 薪资发放任务单主表 控制类
 * </p>
 *
 * @author gaoyang
 * @since 2018-1-16
 */
@RestController
@RequestMapping(value = "/salarygrantMainTask")
public class SalaryGrantMainTaskController {

    @Autowired
    private SalaryGrantMainTaskService salaryGrantMainTaskService;

    @Autowired
    private SheetServiceProxy sheetServiceProxy;

    //todo
    //查询薪资发放任务单列表（待提交、已处理-审批中、已处理-审批通过、已处理-审批拒绝、已处理-审批失效）
    //包括查询雇员列表信息
    public Result querySalaryGrantMainTaskPage(@RequestBody SalaryGrantMainTaskDTO salaryGrantMainTaskDTO){
        try {
            Page<SalaryGrantMainTaskBO> page = new Page<>(salaryGrantMainTaskDTO.getCurrentPage(), salaryGrantMainTaskDTO.getSize());
            SalaryGrantMainTaskBO salaryGrantMainTaskBO = CommonTransform.convertToEntity(salaryGrantMainTaskDTO, SalaryGrantMainTaskBO.class);
            page = salaryGrantMainTaskService.querySalaryGrantMainTaskPage(page, salaryGrantMainTaskBO);
            Page<SalaryGrantMainTaskDTO> dtoPage = PageUtil.changeWapper(page, SalaryGrantMainTaskDTO.class);
            return ResultGenerator.genSuccessResult(dtoPage);
        }catch (Exception e){
            return ResultGenerator.genServerFailResult();
        }
    }

    //todo
    //查询薪资发放任务单明细信息（点击任务单编号链接）
    //包括查询雇员列表信息

    //todo
    //查询薪资发放任务单审批信息（点击审批按钮）
    //包括查询雇员列表信息

    //todo
    //查询薪资发放任务单编辑信息（点击处理按钮）
    //包括查询雇员列表信息

    //todo
    //创建薪资发放任务单信息（订阅计算引擎消息）
    //1、创建薪资发放任务单 2、拆分雇员数据插入到雇员信息表

    //todo
    //薪资发放任务单批量提交（点击批量提交按钮）

    //todo
    //薪资发放任务单提交（点击提交按钮）
    @PostMapping(value="/task/complete",consumes = {"application/json"})
    public Result completeTask(@RequestBody SalaryGrantMainTaskTaskRequestDTO salaryGrantMainTaskTaskRequestDTO) throws Exception {
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
    //todo
    //薪资发放任务单失效（点击失效按钮）

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
