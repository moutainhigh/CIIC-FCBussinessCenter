package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Pagination;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskProcessService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.transform.CommonTransform;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.transform.PageUtil;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    LogClientService logClientService;

    @Autowired
    private SalaryGrantTaskProcessService salaryGrantTaskProcessService;

    @Autowired
    private SalaryGrantTaskQueryService salaryGrantTaskQueryService;

    @Autowired
    private SalaryGrantEmployeeQueryService salaryGrantEmployeeQueryService;

    /**
     * 薪资发放任务单一览
     * @author chenpb
     * @date 2018-04-20
     * @param
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Result<SalaryTaskDTO> list(@RequestBody SalaryTaskDTO dto) {
        SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
        bo.setManagementIds(CommonHelper.getManagementIDs());
        Map<String, String> tags = new HashMap<>();
        tags.put("taskCode", dto.getTaskCode());
        tags.put("batchCode", dto.getBatchCode());
        tags.put("managementIds", String.valueOf(bo.getManagementIds()));
        tags.put("grantMode", dto.getGrantMode());
        tags.put("grantCycle", dto.getGrantCycle());
        tags.put("taskStatus", dto.getTaskStatus());
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询").setContent("条件").setTags(tags));
        try {
            bo.setUserId(UserContext.getUserId());
            Page<SalaryGrantTaskBO> page = salaryGrantTaskQueryService.salaryGrantList(bo);
            Pagination<SalaryTaskDTO> pagination = PageUtil.changeWapper(page, SalaryTaskDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览").setContent(JSON.toJSONString(pagination)));
            return ResultGenerator.genSuccessResult(pagination);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("薪资发放一览查询失败");
        }
    }

    /**
     * 根据主表任务单编号查询子表任务单
     * @author chenpb
     * @date 2018-05-10
     * @param
     * @return
     */
    @RequestMapping(value="/subList", method = RequestMethod.POST)
    public Result<SalaryTaskDTO> subList(@RequestBody SalaryTaskDTO dto) {
        SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单查询").setContent("条件：" + dto.getTaskCode()));
        try {
            List<SalaryGrantTaskBO> boList = salaryGrantTaskQueryService.querySubTask(bo);
            List<SalaryTaskDTO> list = CommonTransform.convertToDTOs(boList, SalaryTaskDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单一览").setContent(JSON.toJSONString(list)));
            return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("薪资发放子任务单查询失败");
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交").setContent(JSON.toJSONString(salaryTaskHandleDTO)));
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交").setContent("成功"));
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交异常").setContent(e.getMessage()));
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交").setContent(JSON.toJSONString(list)));
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交").setContent("成功"));
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交异常").setContent(e.getMessage()));
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("失效").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("失效异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("失效处理失败");
        }
    }

    /**
     * 任务单撤回
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/retract", method = RequestMethod.POST)
    public Result retract(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("撤回").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("撤回异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("撤回处理失败");
        }
    }

    /**
     * 子任务单撤回
     * @author chenpb
     * @date 2018-05-11
     * @param
     * @return
     */
    @RequestMapping(value="/subRetract", method = RequestMethod.POST)
    public Result subRetract(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单撤回").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单撤回异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("子任务单撤回处理失败");
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批异常").setContent(e.getMessage()));
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("雇员信息变更查询").setContent(JSON.toJSONString(dto)));
        try {
            Page<SalaryGrantEmployeeBO> page = new Page<SalaryGrantEmployeeBO>(dto.getCurrent(), dto.getSize());
            Pagination<SalaryTaskDTO> pagination = PageUtil.changeWapper(page, SalaryTaskDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览").setContent(JSON.toJSONString(pagination)));
            return ResultGenerator.genSuccessResult(pagination);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("雇员信息变更查询异常").setContent(e.getMessage()));
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("业务明细查询").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("业务明细查询异常").setContent(e.getMessage()));
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
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("财务明细查询").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("财务明细查询异常").setContent(e.getMessage()));
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
        tags.put("taskType", String.valueOf(dto.getTaskType()));
        tags.put("taskStatus", dto.getTaskStatus());
        tags.put("grantStatus", String.valueOf(dto.getGrantStatus()));
        tags.put("employeeId", dto.getEmployeeId());
        tags.put("employeeName", dto.getEmployeeName());
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("明细查询").setContent("条件").setTags(tags));
        try {
            SalaryTaskDetailDTO salaryTaskDetailDTO = new SalaryTaskDetailDTO();
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo = salaryGrantTaskQueryService.selectTaskByTaskCode(bo);
            SalaryGrantDetailDTO salaryGrantDetailDTO = CommonTransform.convertToDTO(bo, SalaryGrantDetailDTO.class);

            SalaryGrantEmployeeBO empBO = CommonTransform.convertToEntity(dto, SalaryGrantEmployeeBO.class);
            Page<SalaryGrantEmployeeBO> page = new Page<SalaryGrantEmployeeBO>(dto.getCurrent(), dto.getSize());
            page = salaryGrantEmployeeQueryService.queryEmployeeTask(page, empBO);
            Pagination<SalaryGrantEmpDTO> pagination = PageUtil.changeWapper(page, SalaryGrantEmpDTO.class);

            salaryTaskDetailDTO.setEmpSgInfo(pagination);
            salaryTaskDetailDTO.setTask(salaryGrantDetailDTO);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("明细").setContent(JSON.toJSONString(salaryTaskDetailDTO)));
            return ResultGenerator.genSuccessResult(salaryTaskDetailDTO);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询明细异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询明细失败");
        }
    }

    /**
     * 发放账户变化查询
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/selectPayrollAccount", method = RequestMethod.POST)
    public Result selectPayrollAccount(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询变化发放账户").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询变化发放账户异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询变化发放账户失败");
        }
    }

    /**
     * 收款账户变化查询
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/selectPayeeAccount", method = RequestMethod.POST)
    public Result selectPayeeAccount(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询变化收款账户").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询变化收款账户异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询变化收款账户失败");
        }
    }

    /**
     * 暂缓
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/defer", method = RequestMethod.POST)
    public Result defer(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("暂缓失败");
        }
    }

    /**
     * 批量暂缓
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/batchDefer", method = RequestMethod.POST)
    public Result batchDefer(@RequestBody List<SalaryTaskHandleDTO> list) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量暂缓").setContent(JSON.toJSONString(list)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量暂缓异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("批量暂缓失败");
        }
    }

    /**
     * 恢复
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/recover", method = RequestMethod.POST)
    public Result recover(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("恢复").setContent(JSON.toJSONString(dto)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("恢复异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("恢复失败");
        }
    }

    /**
     * 批量恢复
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/batchRecover", method = RequestMethod.POST)
    public Result batchRecover(@RequestBody List<SalaryTaskHandleDTO> list) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量恢复").setContent(JSON.toJSONString(list)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量恢复异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("批量恢复失败");
        }
    }

    /**
     * 薪资发放项金额
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/itemsData", method = RequestMethod.POST)
    public Result itemsData(@RequestBody List<SalaryTaskHandleDTO> list) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询薪资发放项金额").setContent(JSON.toJSONString(list)));
        try {
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询薪资发放项金额异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询薪资发放项金额失败");
        }
    }

    /**
     * 日志信息
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/logInfo", method = RequestMethod.POST)
    public Result logInfo(@RequestBody SalaryTaskHandleDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询操作记录").setContent(JSON.toJSONString(dto)));
        try {
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            Page<WorkFlowTaskInfoBO> page = salaryGrantTaskQueryService.operation(bo);
            Pagination<SalaryGrantOperationDTO> pagination = PageUtil.changeWapper(page, SalaryGrantOperationDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("操作记录").setContent(JSON.toJSONString(pagination)));
            return ResultGenerator.genSuccessResult(pagination);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询操作记录异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询日志信息失败");
        }
    }

//    /**
//     * @description 导出雇员信息
//     * @author chenpb
//     * @since 2018-05-10
//     * @param jsonData 查询条件
//     * @return 导出结果
//     */
//    @RequestMapping(value="/exportEmpInfo", method = RequestMethod.GET)
//    public void exportEmpInfo(@RequestParam("jsonData")String jsonData, HttpServletResponse response) throws IOException {
//        SalaryTaskHandleDTO dto = JSON.parseObject(jsonData, new TypeReference<SalaryTaskHandleDTO>() {});
//        SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
//        Page<WorkFlowTaskInfoBO> page = salaryGrantTaskQueryService.operation(bo);
//        SheetSettings sheet = getSheetSettings(page.getRecords());
//        OfficeIoResult result = OfficeIoUtils.exportXlsx(sheet);
//        OfficeIoUtils.exportErrorRecord(result.getSheetSettings(),result.getErrRecordRows());
//        String fileName = "EmployeePayment.xlsx";
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
//        result.getResultWorkbook().write(outputStream);
//        outputStream.flush();
//        outputStream.close();
//    }
//
//    private static SheetSettings getSheetSettings(List<WorkFlowTaskInfoBO> list) {
//        Map applyTypeMap = new HashMap();
//        applyTypeMap.put(1, "支付个人");
//        applyTypeMap.put(2, "其他");
//
//        Map statusMap = new HashMap();
//        statusMap.put(1, "未审核");
//        statusMap.put(2, "已批退");
//        statusMap.put(3, "已审核未同步");
//        statusMap.put(4, "已同步");
//        statusMap.put(5, "已支付");
//        statusMap.put(6, "财务退回");
//        statusMap.put(7, "银行退票");
//        statusMap.put(8, "已完成");
//
//        SheetSettings sheet = new SheetSettings("雇员付款",AfEmpPaymentInfoDTO.class);
//        sheet.setCellSettings(new CellSettings[] {
//                new CellSettings("paymentApplyId","序号"),
//                new CellSettings("companyId","客户编号"),
//                new CellSettings("employeeId","雇员编号"),
//                new CellSettings("employeeName","雇员姓名"),
//                new CellSettings("modifiedBy","业务经办人"),
//                new CellSettings("payTotalAmount","付款金额"),
//                new CellSettings("applyTypeId","申请类型").addFixedMap(applyTypeMap),
//                new CellSettings("createdTime","申请日期").addCellDataType(CellDataType.DATE).addPattern(DatePattern.DATE_FORMAT_DAY),
//                new CellSettings("bankAccount","卡号"),
//                //new CellSettings("modifiedBy","客服"),
//                new CellSettings("status","状态").addFixedMap(statusMap)
//        });
//        sheet.setExportData(list);
//        return sheet;
//    }

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
    //调整信息-对于发放类型是调整发放。查询调整/回溯批次、基于的计算批次，查询2条计算结果数据，再进行数据合并生成第三条合并数据。

    //todo
    //查询任务单中雇员信息变更历史记录（点击雇员备注链接）
}
