package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantEmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util.CommonTransform;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: 薪资发放雇员信息 Controller</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/26 0026
 */
@RestController
public class SalaryGrantEmployeeController {
    @Autowired
    LogClientService logClientService;
    @Autowired
    private SalaryGrantEmployeeQueryService employeeQueryService;
    @Autowired
    private SalaryGrantEmployeeCommandService employeeCommandService;

    /**
     * 查询子表的雇员信息
     * 主要根据子表任务单编号查询
     *
     * @param salaryGrantEmployeeDTO
     * @return Page<SalaryGrantEmployeeDTO>
     */
    @RequestMapping("/SalaryGrantEmployee/SubTask")
    public Result<Page<SalaryGrantEmployeeDTO>> queryEmployeeForSubTask(@RequestBody SalaryGrantEmployeeDTO salaryGrantEmployeeDTO) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询子任务单雇员一览").setContent(JSON.toJSONString(salaryGrantEmployeeDTO)));
            Page<SalaryGrantEmployeeBO> page = new Page<SalaryGrantEmployeeBO>(salaryGrantEmployeeDTO.getCurrent(), salaryGrantEmployeeDTO.getSize());
            SalaryGrantEmployeeBO grantEmployeeBO = CommonTransform.convertToEntity(salaryGrantEmployeeDTO, SalaryGrantEmployeeBO.class);
            page = employeeQueryService.queryEmployeeTask(page, grantEmployeeBO);

            // BO PAGE 转换为 DTO PAGE
            String boJSONStr = JSONObject.toJSONString(page);
            Page<SalaryGrantEmployeeDTO> employeeDTOPage = JSONObject.parseObject(boJSONStr, Page.class);
            return ResultGenerator.genSuccessResult(employeeDTOPage);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询子任务单雇员一览异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }

    /**
     * 查询发放变化的雇员信息
     *
     * @param salaryGrantEmployeeDTO
     * @return
     */
    @RequestMapping("/SalaryGrantEmployee/infoChanged")
    public Result<Page<SalaryGrantEmployeeDTO>> queryEmployeeInfoChanged(@RequestBody SalaryGrantEmployeeDTO salaryGrantEmployeeDTO) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询信息变化雇员").setContent(JSON.toJSONString(salaryGrantEmployeeDTO)));

            Page<SalaryGrantEmployeeBO> page = new Page<>(salaryGrantEmployeeDTO.getCurrent(), salaryGrantEmployeeDTO.getSize());
            SalaryGrantEmployeeBO grantEmployeeBO = CommonTransform.convertToEntity(salaryGrantEmployeeDTO, SalaryGrantEmployeeBO.class);
            page = employeeQueryService.queryEmployeeInfoChanged(page, grantEmployeeBO);

            // BO PAGE 转换为 DTO PAGE
            String boJSONStr = JSONObject.toJSONString(page);
            Page<SalaryGrantEmployeeDTO> employeeDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

            return ResultGenerator.genSuccessResult(employeeDTOPage);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询信息变化雇员异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }

    /**
     * 根据grantStatus进行不同的操作
     *
     * grantStatus = 1:
     * 暂缓雇员操作
     * 暂缓单个雇员信息，修改雇员的发放状态为手工暂缓。
     *
     * grantStatus = 0:
     * 恢复雇员操作
     * 恢复单个雇员信息，修改雇员的发放状态为正常。
     *
     * @param salaryGrantEmployeeDTO
     * @return Result
     */
    @RequestMapping("/SalaryGrantEmployee/reprieveEmployee")
    public Result toReprieveEmployee(@RequestBody SalaryGrantEmployeeDTO salaryGrantEmployeeDTO) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("暂缓或恢复雇员发放状态").setContent(JSON.toJSONString(salaryGrantEmployeeDTO)));

            SalaryGrantEmployeePO employeePO = new SalaryGrantEmployeePO();
            employeePO.setSalaryGrantEmployeeId(salaryGrantEmployeeDTO.getSalaryGrantEmployeeId());
            employeePO.setGrantStatus(salaryGrantEmployeeDTO.getGrantStatus());
            employeePO.setModifiedBy(UserContext.getUserId());
            employeePO.setModifiedTime(new Date());

            employeeCommandService.updateById(employeePO);
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("暂缓或恢复雇员发放状态异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }

    /**
     * 批量暂缓雇员操作
     * 循环遍历雇员信息，调用单个暂缓雇员操作
     *
     * @param salaryGrantEmployeeIdList
     * @return Result
     */
    @RequestMapping("/SalaryGrantEmployee/batchReprieveEmployee")
    public Result toBatchReprieveEmployee(@RequestBody List<Long> salaryGrantEmployeeIdList) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("批量暂缓雇员").setContent(JSON.toJSONString(salaryGrantEmployeeIdList)));
            if (!CollectionUtils.isEmpty(salaryGrantEmployeeIdList)) {
                List<SalaryGrantEmployeePO> employeePOList = new ArrayList<>(salaryGrantEmployeeIdList.size());
                String userId = UserContext.getUserId();
                salaryGrantEmployeeIdList.stream().forEach( x -> {
                    SalaryGrantEmployeePO employeePO = new SalaryGrantEmployeePO();
                    employeePO.setSalaryGrantEmployeeId(x);
                    //发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
                    employeePO.setGrantStatus(1);
                    employeePO.setModifiedBy(userId);
                    employeePO.setModifiedTime(new Date());

                    employeePOList.add(employeePO);
                });
                employeeCommandService.updateBatchById(employeePOList, salaryGrantEmployeeIdList.size());
            }
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("批量暂缓雇员异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }

    /**
     * 批量恢复雇员操作
     * 循环遍历雇员信息，调用单个恢复雇员操作
     *
     * @param salaryGrantEmployeeIdList
     * @return Result
     */
    @RequestMapping("/SalaryGrantEmployee/batchRecoverEmployee")
    public Result toBatchRecoverEmployee(@RequestBody List<Long> salaryGrantEmployeeIdList) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("批量恢复雇员").setContent(JSON.toJSONString(salaryGrantEmployeeIdList)));
            if (!CollectionUtils.isEmpty(salaryGrantEmployeeIdList)) {
                List<SalaryGrantEmployeePO> employeePOList = new ArrayList<>(salaryGrantEmployeeIdList.size());
                String userId = UserContext.getUserId();
                salaryGrantEmployeeIdList.stream().forEach( x -> {
                    SalaryGrantEmployeePO employeePO = new SalaryGrantEmployeePO();
                    employeePO.setSalaryGrantEmployeeId(x);
                    //发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
                    employeePO.setGrantStatus(0);
                    employeePO.setModifiedBy(userId);
                    employeePO.setModifiedTime(new Date());

                    employeePOList.add(employeePO);
                });
                employeeCommandService.updateBatchById(employeePOList, salaryGrantEmployeeIdList.size());
            }
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("批量恢复雇员异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }

    /**
     * 查询雇员调整信息
     *
     * @param salaryGrantEmployeeId
     * @return
     */
    @RequestMapping("/SalaryGrantEmployee/selectAdjustCompareInfo/{salaryGrantEmployeeId}")
    public Result<List<SalaryGrantEmployeeDTO>> selectAdjustCompareInfo(@PathVariable Long salaryGrantEmployeeId) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询雇员调整信息").setContent("雇员表ID: " + salaryGrantEmployeeId));
            List<SalaryGrantEmployeeDTO> retEmployeePOList = new ArrayList<>();
            SalaryGrantEmployeePO employeePO = employeeQueryService.selectById(salaryGrantEmployeeId);
            String adjustCompareInfo = employeePO.getAdjustCompareInfo();
            if (!StringUtils.isEmpty(adjustCompareInfo)) {
                retEmployeePOList = JSONObject.parseArray(adjustCompareInfo, SalaryGrantEmployeeDTO.class);
            }
            return ResultGenerator.genSuccessResult(retEmployeePOList);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询雇员调整信息异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }

    /**
     * 查询雇员变更信息
     *
     * @param salaryGrantEmployeeId
     * @return
     */
    @RequestMapping("/SalaryGrantEmployee/selectChangeLog/{salaryGrantEmployeeId}")
    public Result<List<SalaryGrantEmployeeDTO>> selectChangeLog(@PathVariable Long salaryGrantEmployeeId) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询雇员变更信息").setContent("雇员表ID: " + salaryGrantEmployeeId));
            List<SalaryGrantEmployeeDTO> employeeDTOList = new ArrayList<>();
            List<SalaryGrantEmployeeBO> employeeBOList = employeeQueryService.selectChangeLog(salaryGrantEmployeeId);
            if (!CollectionUtils.isEmpty(employeeBOList)) {
                String employeeBOListJsonStr = JSONObject.toJSONString(employeeBOList);
                employeeDTOList = JSONObject.parseArray(employeeBOListJsonStr, SalaryGrantEmployeeDTO.class);
            }
            return ResultGenerator.genSuccessResult(employeeDTOList);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询雇员变更信息异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常，请稍后重试！");
        }
    }
}
