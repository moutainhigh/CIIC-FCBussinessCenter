package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Pagination;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantWorkFlowEnums;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.excel.ReprieveEmpImportExcelDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.excel.SalaryTaskEmpExcelDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util.CommonTransform;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util.ExcelUtil;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util.PageUtil;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    CommonService commonService;

    @Autowired
    private SalaryGrantTaskQueryService salaryGrantTaskQueryService;

    @Autowired
    private SalaryGrantSupplierSubTaskService salaryGrantSupplierSubTaskService;

    @Autowired
    private SalaryGrantEmployeeQueryService salaryGrantEmployeeQueryService;

    @Autowired
    private SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;

    @Autowired
    private SalaryGrantReprieveEmployeeImportService salaryGrantReprieveEmployeeImportService;

    @Autowired
    private SalaryGrantPayrollService salaryGrantPayrollService;

    @Autowired
    private SalaryGrantWorkFlowService salaryGrantWorkFlowService;

    @Autowired
    private SalaryGrantSubTaskWorkFlowService salaryGrantSubTaskWorkFlowService;

    /**
     * 刷新数据
     * @author chenpb
     * @date 2018-07-04
     * @return
     */
    @RequestMapping(value="/refresh", method = RequestMethod.POST)
    public Result<Pagination<SalaryTaskDTO>> refresh(@RequestBody SalaryTaskDTO dto) {
        try {
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setManagementIds(CommonHelper.getManagementIDs());
            bo.setUserId(UserContext.getUserId());
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步待提交雇员信息").setContent(JSON.toJSONString(bo)));
            Page<SalaryGrantTaskBO> page = salaryGrantTaskQueryService.refreshDraftTask(bo);
            return ResultGenerator.genSuccessResult(page);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步待提交雇员信息异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("同步待提交雇员信息失败！");
        }
    }

    /**
     * 薪资发放任务单一览
     * @author chenpb
     * @date 2018-04-20
     * @param
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Result<Pagination<SalaryTaskDTO>> list(@RequestBody SalaryTaskDTO dto) {
        try {
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setManagementIds(CommonHelper.getManagementIDs());
            Map<String, String> tags = new HashMap<>();
            tags.put("taskCode", dto.getTaskCode());
            tags.put("batchCode", dto.getBatchCode());
            tags.put("managementIds", String.valueOf(bo.getManagementIds()));
            tags.put("grantMode", dto.getGrantMode());
            tags.put("grantCycle", dto.getGrantCycle());
            tags.put("taskStatusEn", dto.getTaskStatusEn());
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("一览查询").setTags(tags));
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
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单查询").setContent("查询条件 -> taskCode: " + dto.getTaskCode()));
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
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
     * @date 2018-05-23
     * @param
     * @return
     */
    @RequestMapping(value="/submit", method = RequestMethod.POST)
    public Result<WorkFlowResultDTO> submit(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交").setContent(JSON.toJSONString(dto)));
            WorkFlowResultBO resultBo = BeanUtils.instantiate(WorkFlowResultBO.class);
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            bo.setUserName(UserContext.getName());
            if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                resultBo = salaryGrantTaskQueryService.submit(true, bo);
                SalaryGrantMainTaskPO mainPo = salaryGrantTaskQueryService.selectById(bo.getTaskId());
                resultBo.setModifiedTime(mainPo.getModifiedTime());
            } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                resultBo = salaryGrantTaskQueryService.submit(false, bo);
                SalaryGrantSubTaskPO subPo = salaryGrantSupplierSubTaskService.selectById(bo.getTaskId());
                resultBo.setModifiedTime(subPo.getModifiedTime());
            }
            WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(resultBo, WorkFlowResultDTO.class);
            return ResultGenerator.genSuccessResult(resultDto);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("提交异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常！");
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
    public Result<List<WorkFlowResultDTO>> batchSubmit(@RequestBody List<SalaryTaskHandleDTO> list) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交").setContent(JSON.toJSONString(list)));
        List<WorkFlowResultDTO> resultList = new ArrayList<>();
        if (!list.isEmpty()) {
            list.stream().forEach(x -> {
                WorkFlowResultBO resultBo = BeanUtils.instantiate(WorkFlowResultBO.class);
                resultBo.setTaskCode(x.getTaskCode());
                resultBo.setTaskType(x.getTaskType());
                SalaryGrantTaskBO bo = CommonTransform.convertToEntity(x, SalaryGrantTaskBO.class);
                bo.setUserId(UserContext.getUserId());
                bo.setUserName(UserContext.getName());
                try {
                    if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                        resultBo = salaryGrantTaskQueryService.submit(true, bo);
                    } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                        resultBo = salaryGrantTaskQueryService.submit(false, bo);
                    }
                    WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(resultBo, WorkFlowResultDTO.class);
                    resultList.add(resultDto);
                } catch (Exception e) {
                    resultBo.setResult(SalaryGrantWorkFlowEnums.TaskResult.EXCEPTION.getResult());
                    resultBo.setMessage(SalaryGrantWorkFlowEnums.TaskResult.EXCEPTION.getExtension());
                    WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(resultBo, WorkFlowResultDTO.class);
                    resultList.add(resultDto);
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("系统异常").setContent(JSON.toJSONString(x)));
                }
            });
        }
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("批量提交结果").setContent(JSON.toJSONString(resultList)));
        return ResultGenerator.genSuccessResult(resultList);
    }

    /**
     * 审批通过
     * @author chenpb
     * @date 2018-05-14
     * @param
     * @return
     */
    @RequestMapping(value="/pass", method = RequestMethod.POST)
    public Result<WorkFlowResultDTO> pass(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批通过").setContent(JSON.toJSONString(dto)));
            WorkFlowResultBO resultBo = BeanUtils.instantiate(WorkFlowResultBO.class);
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            bo.setUserName(UserContext.getName());
            if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                resultBo = salaryGrantTaskQueryService.approvalPass(true, bo);
            } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                resultBo = salaryGrantTaskQueryService.approvalPass(false, bo);
            }
            WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(resultBo, WorkFlowResultDTO.class);
            return ResultGenerator.genSuccessResult(resultDto);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批通过异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常！");
        }
    }

    /**
     * 审批退回
     * @author chenpb
     * @date 2018-05-14
     * @param
     * @return
     */
    @RequestMapping(value="/reject", method = RequestMethod.POST)
    public Result<WorkFlowResultDTO> reject(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批退回").setContent(JSON.toJSONString(dto)));
            WorkFlowResultBO resultBo = BeanUtils.instantiate(WorkFlowResultBO.class);
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            bo.setUserName(UserContext.getName());
            if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                resultBo = salaryGrantTaskQueryService.approvalReject(true, bo);
            } else if (!SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(bo.getTaskType())) {
                resultBo = salaryGrantTaskQueryService.approvalReject(false, bo);
            }
            WorkFlowResultDTO resultDto = CommonTransform.convertToDTO(resultBo, WorkFlowResultDTO.class);
            return ResultGenerator.genSuccessResult(resultDto);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("审批退回异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常！");
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
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("失效").setContent(JSON.toJSONString(dto)));
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("失效异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("失效处理失败");
        }
    }

    /**
     * 主任务单撤回
     * @author chenpb
     * @date 2018-04-27
     * @param
     * @return
     */
    @RequestMapping(value="/retract", method = RequestMethod.POST)
    public Result retract(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("主任务单撤回").setContent(JSON.toJSONString(dto)));
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            salaryGrantWorkFlowService.doRetreatTask(bo);
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("主任务单撤回异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("主任务单撤回失败！");
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
    public Result<Boolean> subRetract(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单撤回").setContent(JSON.toJSONString(dto)));
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo.setUserId(UserContext.getUserId());
            Boolean result = salaryGrantSubTaskWorkFlowService.retreatSubTask(bo);
            return ResultGenerator.genSuccessResult(result);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("子任务单撤回异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("子任务单撤回失败！");
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
    public Result<ChangedEmpInfoDTO> empInfoChange(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("信息变更雇员查询").setContent(JSON.toJSONString(dto)));
            Page<SalaryGrantEmployeeBO> page = new Page<>(dto.getCurrent(), dto.getSize());
            SalaryGrantEmployeeBO bo = CommonTransform.convertToEntity(dto, SalaryGrantEmployeeBO.class);
            page = salaryGrantEmployeeQueryService.queryEmployeeInfoChanged(page, bo);
            if (!page.getRecords().isEmpty()) {
                page.getRecords().parallelStream().forEach(x -> {if(StringUtils.isNotBlank(x.getCountryCode())){x.setCountryName(commonService.getCountryName(x.getCountryCode()));}});
            }
            Pagination<ChangedEmpInfoDTO> pagination = PageUtil.changeWapper(page, ChangedEmpInfoDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("信息变更雇员").setContent(JSON.toJSONString(pagination)));
            return ResultGenerator.genSuccessResult(pagination);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("信息变更雇员查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("信息变更雇员查询失败");
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
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("业务明细查询").setContent(JSON.toJSONString(dto)));
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
    public Result<SalaryGrantFinanceDTO> financeDetail(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("生成财务明细").setContent(JSON.toJSONString(dto)));
            SalaryGrantFinanceDTO financeDto = BeanUtils.instantiate(SalaryGrantFinanceDTO.class);
            SalaryGrantFinanceBO financeBo = salaryGrantPayrollService.createFinanceDetail(dto.getTaskCode());
            financeDto.setFinanceTask(CommonTransform.convertToDTO(financeBo.getTask(), FinanceTaskDTO.class));
            financeDto.setEmpList(CommonTransform.convertToDTOs(financeBo.getEmpList(), FinanceEmployeeDTO.class));
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("财务明细").setContent(JSON.toJSONString(financeDto)));
            return ResultGenerator.genSuccessResult(financeDto);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("生成财务明细异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("生成财务明细失败");
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
        try {
            Map<String, String> tags = new HashMap<>();
            tags.put("taskId", String.valueOf(dto.getTaskId()));
            tags.put("taskCode", dto.getTaskCode());
            tags.put("taskType", String.valueOf(dto.getTaskType()));
            tags.put("taskStatus", dto.getTaskStatus());
            tags.put("grantStatus", String.valueOf(dto.getGrantStatus()));
            tags.put("grantMode", String.valueOf(dto.getGrantMode()));
            tags.put("employeeId", dto.getEmployeeId());
            tags.put("employeeName", dto.getEmployeeName());
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("明细查询").setTags(tags));
            SalaryTaskDetailDTO salaryTaskDetailDTO = new SalaryTaskDetailDTO();
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            bo = salaryGrantTaskQueryService.selectTaskByTaskCode(bo);
            SalaryGrantDetailDTO salaryGrantDetailDTO = CommonTransform.convertToDTO(bo, SalaryGrantDetailDTO.class);
            salaryGrantDetailDTO.setGrantTimeStr(String.valueOf(bo.getGrantTime()));

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
            return ResultGenerator.genServerFailResult("查询明细失败！");
        }
    }

    /**
     * 薪资调整信息
     * @author chenpb
     * @date 2018-07-02
     * @param dto
     * @return
     */
    @RequestMapping(value="/adjustSgInfo", method = RequestMethod.POST)
    public Result<List<SalaryGrantEmployeeDTO>> adjustSgInfo(@RequestBody EmployeeHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员薪资调整信息").setContent(JSON.toJSONString(dto)));
            List<SalaryGrantEmployeeDTO> dtoList =new ArrayList<>();
            SalaryGrantEmployeePO employeePO = salaryGrantEmployeeQueryService.selectById(dto.getSalaryGrantEmployeeId());
            if (!ObjectUtils.isEmpty(employeePO) && !StringUtils.isEmpty(employeePO.getAdjustCompareInfo())) {
                dtoList = JSONObject.parseArray(employeePO.getAdjustCompareInfo(), SalaryGrantEmployeeDTO.class);
            }
            return ResultGenerator.genSuccessResult(dtoList);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员薪资调整信息异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询雇员薪资调整信息失败！");
        }
    }

    /**
     * 雇员变化信息
     * @author chenpb
     * @date 2018-07-04
     * @param
     * @return
     */
    @RequestMapping(value="/empInfo", method = RequestMethod.POST)
    public Result<List<SalaryGrantEmployeeDTO>> empInfo(@RequestBody EmployeeHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员变化信息").setContent(JSON.toJSONString(dto)));
            List<SalaryGrantEmployeeBO> boList = salaryGrantEmployeeQueryService.selectChangeLog(dto.getSalaryGrantEmployeeId());
            List<SalaryGrantEmployeeDTO> list = CommonTransform.convertToDTOs(boList, SalaryGrantEmployeeDTO.class);
            return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员变化信息异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询雇员变化信息失败！");
        }
    }

    /**
     * 恢复
     * @author chenpb
     * @date 2018-05-16
     * @param
     * @return
     */
    @RequestMapping(value="/recover", method = RequestMethod.POST)
    public Result recover(@RequestBody List<EmployeeHandleDTO> dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("恢复").setContent(JSON.toJSONString(dto)));
            List<SalaryGrantEmployeePO> list = CommonTransform.convertToEntities(dto, SalaryGrantEmployeePO.class);
            list.parallelStream().forEach(x -> {x.setModifiedBy(UserContext.getUserId()); x.setModifiedTime(new Date());});
            salaryGrantEmployeeCommandService.updateBatchById(list);
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("恢复异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("恢复失败");
        }
    }

    /**
     * 暂缓
     * @author chenpb
     * @date 2018-05-15
     * @param
     * @return
     */
    @RequestMapping(value="/defer", method = RequestMethod.POST)
    public Result defer(@RequestBody List<EmployeeHandleDTO> dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓").setContent(JSON.toJSONString(dto)));
            List<SalaryGrantEmployeePO> list = CommonTransform.convertToEntities(dto, SalaryGrantEmployeePO.class);
            list.parallelStream().forEach(x -> {x.setModifiedBy(UserContext.getUserId()); x.setModifiedTime(new Date());});
            salaryGrantEmployeeCommandService.updateBatchById(list);
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("暂缓失败");
        }
    }

    /**
     * 薪资项
     * @author chenpb
     * @date 2018-05-22
     * @param
     * @return
     */
    @RequestMapping(value="/itemsInfo", method = RequestMethod.POST)
    public Result<SalaryTaskItemDTO> itemsInfo(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询薪资项").setContent(JSON.toJSONString(dto)));
            Map map = new HashMap();
            map.put("batchCode", dto.getBatchCode());
            map.put("batchType", dto.getGrantType());
            List<CalcResultItemBO> bo = salaryGrantEmployeeQueryService.getSalaryCalcResultItemsList(map);
            List<SalaryTaskItemDTO> items = CommonTransform.convertToDTOs(bo, SalaryTaskItemDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("薪资项").setContent(JSON.toJSONString(items)));
            return ResultGenerator.genSuccessResult(items);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询薪资项异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("查询薪资项失败");
        }
    }

    /**
     * 薪资
     * @author chenpb
     * @date 2018-05-22
     * @param
     * @return
     */
    @RequestMapping(value="/itemsData", method = RequestMethod.POST)
    public Result<EmpCalcResultBO> itemsData(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询薪资").setContent(JSON.toJSONString(dto)));
            List<CalcResultItemBO> paramList = CommonTransform.convertToEntities(new ArrayList<T>(), CalcResultItemBO.class);
            SalaryGrantTaskBO paramBo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            List<EmpCalcResultBO> bo = salaryGrantEmployeeQueryService.getEmployeeForBizList(paramList, paramBo);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("薪资").setContent(JSON.toJSONString(bo)));
            return ResultGenerator.genSuccessResult(bo);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询薪资异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult(e.getMessage());
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
    public Result<SalaryGrantOperationDTO> logInfo(@RequestBody SalaryTaskHandleDTO dto) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询操作记录").setContent(JSON.toJSONString(dto)));
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

    /**
     * 导出雇员信息
     * @author chenpb
     * @date 2018-05-15
     * @param salaryTaskDetailDTO
     * @param response
     * @return
     */
    @GetMapping(value = "/exportEmpInfo")
    public void exportEmpInfo(SalaryTaskDetailDTO salaryTaskDetailDTO, HttpServletResponse response) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导出雇员信息").setContent(JSON.toJSONString(salaryTaskDetailDTO)));
            SalaryGrantEmployeeBO empBO = CommonTransform.convertToEntity(salaryTaskDetailDTO, SalaryGrantEmployeeBO.class);
            Page<SalaryGrantEmployeeBO> page = new Page<SalaryGrantEmployeeBO>(salaryTaskDetailDTO.getCurrent(), Page.NO_ROW_LIMIT);
            page = salaryGrantEmployeeQueryService.queryEmployeeTask(page, empBO);
            List<SalaryTaskEmpExcelDTO> lists = new ArrayList<>();
            page.getRecords().stream().forEach(i -> {
                SalaryTaskEmpExcelDTO excelDTO =  CommonTransform.convertToDTO(i, SalaryTaskEmpExcelDTO.class);
                lists.add(excelDTO);
            });
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("雇员信息").setContent(JSON.toJSONString(lists)));
            ExcelUtil.exportExcel(lists, "","雇员信息", SalaryTaskEmpExcelDTO.class, "薪资发放雇员信息.xls", response);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导出雇员信息异常").setContent(e.getMessage()));
        }
    }

    /**
     * 导入暂缓雇员
     * @author chenpb
     * @date 2018-05-16
     * @param file
     * @return
     */
    @PostMapping("/importDeferList")
    public Result<List<ReprieveEmpImportExcelDTO>> importDeferList(@RequestParam("file") MultipartFile file, @RequestParam("taskCode") String taskCode, @RequestParam("taskType") Integer taskType) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导入暂缓雇员"));
            List<ReprieveEmpImportExcelDTO> list = ExcelUtil.importExcel(file, 0,1, ReprieveEmpImportExcelDTO.class, true);
            if (list.isEmpty()) {
                return ResultGenerator.genServerFailResult("无暂缓雇员");
            }
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓雇员").setContent(JSON.toJSONString(list)));
            List<SalaryGrantEmployeeBO> bos = CommonTransform.convertToEntities(list, SalaryGrantEmployeeBO.class);
            bos = salaryGrantReprieveEmployeeImportService.deferEmployee(bos, taskCode, taskType, UserContext.getUserId());
            List<ReprieveEmpImportExcelDTO> failList = CommonTransform.convertToEntities(bos, ReprieveEmpImportExcelDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导入暂缓 -> 失败雇员").setContent(JSON.toJSONString(list)));
            return ResultGenerator.genSuccessResult(failList);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导入暂缓名单异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("导入暂缓名单失败");
        }
    }

    /**
     * 导出暂缓失败雇员
     * @author chenpb
     * @date 2018-05-24
     * @param
     * @param response
     */
    @GetMapping(value = "/exportFailList")
    public void exportFailList(SalaryTaskHandleDTO dto, HttpServletResponse response) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导出暂缓失败雇员").setContent(JSON.toJSONString(dto)));
            SalaryGrantReprieveEmployeeImportBO bo = CommonTransform.convertToEntity(dto, SalaryGrantReprieveEmployeeImportBO.class);
            List<SalaryGrantReprieveEmployeeImportBO> bos = salaryGrantReprieveEmployeeImportService.selectDeferEmployee(bo);
            List<ReprieveEmpImportExcelDTO> list = CommonTransform.convertToDTOs(bos, ReprieveEmpImportExcelDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓失败雇员").setContent(JSON.toJSONString(list)));
            ExcelUtil.exportExcel(list, "","暂缓失败雇员", ReprieveEmpImportExcelDTO.class, "暂缓失败雇员名单.xls", response);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("导出暂缓失败雇员异常").setContent(e.getMessage()));
        }
    }

}
























