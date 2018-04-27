package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryTaskDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryTaskHandleDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.transform.CommonTransform;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            Page page = salaryGrantTaskQueryService.salaryGrantList(bo);
            List<SalaryTaskDTO> list = CommonTransform.convertToDTOs(page.getRecords(), SalaryTaskDTO.class);
            page.setRecords(list);
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览").setContent(JSON.toJSONString(page)));
            return ResultGenerator.genSuccessResult(page);
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("薪资发放一览查询失败");
        }
    }

    /**
     * 薪资发放任务单提交
     * @author chenpb
     * @date 2018-04-26
     * @param
     * @return
     */
    @RequestMapping(value="/submit", method = RequestMethod.POST)
    public Result submit(@RequestBody SalaryTaskHandleDTO salaryTaskHandleDTO) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交").setContent(JSON.toJSONString(salaryTaskHandleDTO)));
        try {
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交").setContent("成功"));
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("提交失败");
        }
    }

    /**
     * 薪资发放任务单批量提交
     * @author chenpb
     * @date 2018-04-26
     * @param
     * @return
     */
    @RequestMapping(value="/batchSubmit", method = RequestMethod.POST)
    public Result batchSubmit(@RequestBody List<SalaryTaskHandleDTO> list) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交").setContent(JSON.toJSONString(list)));
        try {
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交").setContent("成功"));
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("批量提交失败");
        }
    }

    /**
     * 失效
     * @author chenpb
     * @date 2018-04-24
     * @param
     * @return
     */
    @RequestMapping(value="/cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody SalaryTaskHandleDTO dto) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("失效").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("失效异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("失效处理失败");
        }
    }

    /**
     * 撤回
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/retract", method = RequestMethod.POST)
    public Result retract(@RequestBody SalaryTaskHandleDTO dto) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("撤回").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("撤回异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("撤回处理失败");
        }
    }

    /**
     * 审批
     * @author chenpb
     * @date 2018-04-26
     * @param
     * @return
     */
    @RequestMapping(value="/approve", method = RequestMethod.POST)
    public Result approve(@RequestBody SalaryTaskHandleDTO dto) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("审批失败");
        }
    }

    /**
     * 雇员信息变更记录
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/empInfoChange", method = RequestMethod.POST)
    public Result empInfoChange(@RequestBody SalaryTaskHandleDTO dto) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("雇员信息变更查询").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("雇员信息变更查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("雇员信息变更查询失败");
        }
    }

    /**
     * 业务明细
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/businessDetail", method = RequestMethod.POST)
    public Result businessDetail(@RequestBody SalaryTaskHandleDTO dto) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("业务明细查询").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("业务明细查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("业务明细查询失败");
        }
    }

    /**
     * 财务明细
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/financeDetail", method = RequestMethod.POST)
    public Result financeDetail(@RequestBody SalaryTaskHandleDTO dto) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("财务明细查询").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("财务明细查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("财务明细查询失败");
        }
    }

    /**
     * 薪资发放明细
     * @author chenpb
     * @date 2018-04-25
     * @param
     * @return
     */
    @RequestMapping(value="/detail", method = RequestMethod.POST)
    public Result<SalaryTaskDetailDTO> detail(@RequestBody SalaryTaskDetailDTO dto) {
        Map<String, String> tags = new HashMap<>();
        tags.put("taskCode", dto.getTaskCode());
        tags.put("taskType", dto.getTaskType().toString());
        tags.put("taskStatus", dto.getTaskStatus().toString());
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询明细").setContent("条件").setTags(tags));
        try {
            SalaryTaskDetailDTO salaryTaskDetailDTO = new SalaryTaskDetailDTO();
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo = salaryGrantTaskQueryService.selectTaskByTaskCode(bo);
            SalaryGrantDetailDTO salaryGrantDetailDTO = CommonTransform.convertToDTO(bo, SalaryGrantDetailDTO.class);
            salaryTaskDetailDTO.setTask(salaryGrantDetailDTO);
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("明细").setContent(JSON.toJSONString(salaryTaskDetailDTO)));
            return ResultGenerator.genSuccessResult(salaryTaskDetailDTO);
        } catch (Exception e) {
            logService.error(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询明细异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询明细失败");
        }
    }

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
