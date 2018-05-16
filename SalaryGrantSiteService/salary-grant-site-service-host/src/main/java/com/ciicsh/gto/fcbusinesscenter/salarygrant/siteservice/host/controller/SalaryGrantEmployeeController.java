package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantEmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
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
    private SalaryGrantEmployeeQueryService employeeQueryService;
    @Autowired
    private SalaryGrantEmployeeCommandService employeeCommandService;
    @Autowired
    private CommonService commonService;
    @Autowired
    LogServiceProxy logService;

    /**
     * 查询子表的雇员信息
     * 主要根据子表任务单编号查询
     *
     * @param salaryGrantEmployeeDTO
     * @return Page<SalaryGrantEmployeeDTO>
     */
    @RequestMapping("/SalaryGrantEmployee/SubTask")
    public Page<SalaryGrantEmployeeDTO> queryEmployeeForSubTask(@RequestBody SalaryGrantEmployeeDTO salaryGrantEmployeeDTO){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询子表的雇员信息").setContent(JSON.toJSONString(salaryGrantEmployeeDTO)));

        Page<SalaryGrantEmployeeBO> page = new Page<>();
        page.setCurrent(salaryGrantEmployeeDTO.getCurrent());
        page.setSize(salaryGrantEmployeeDTO.getSize());
        SalaryGrantEmployeeBO grantEmployeeBO = new SalaryGrantEmployeeBO();
        BeanUtils.copyProperties(salaryGrantEmployeeDTO, grantEmployeeBO);

        page = employeeQueryService.queryEmployeeForSubTask(page, grantEmployeeBO);

        //字段转换
        List<SalaryGrantEmployeeBO> employeeBOList = page.getRecords();
        if (!CollectionUtils.isEmpty(employeeBOList)) {
            int size = employeeBOList.size();
            SalaryGrantEmployeeBO employeeBO;
            for (int i = 0; i < size; i++) {
                employeeBO = employeeBOList.get(i);
                //国籍转码
                if (!StringUtils.isEmpty(employeeBO.getCountryCode())){
                    employeeBO.setCountryName(commonService.getCountryName(employeeBO.getCountryCode()));
                }

                //发放状态
                if (!ObjectUtils.isEmpty(employeeBO.getGrantStatus())){
                    employeeBO.setGrantStatusName(commonService.getNameByValue("sgGrantStatus", String.valueOf(employeeBO.getGrantStatus())));
                }
            }
        }

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantEmployeeDTO> employeeDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return employeeDTOPage;
    }

    /**
     * 查询发放变化的雇员信息
     *
     * @param salaryGrantEmployeeDTO
     * @return
     */
    @RequestMapping("/SalaryGrantEmployee/infoChanged")
    public Page<SalaryGrantEmployeeDTO> queryEmployeeInfoChanged(@RequestBody SalaryGrantEmployeeDTO salaryGrantEmployeeDTO){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("查询发放变化的雇员信息").setContent(JSON.toJSONString(salaryGrantEmployeeDTO)));

        Page<SalaryGrantEmployeeBO> page = new Page<>();
        page.setCurrent(salaryGrantEmployeeDTO.getCurrent());
        page.setSize(salaryGrantEmployeeDTO.getSize());
        SalaryGrantEmployeeBO grantEmployeeBO = new SalaryGrantEmployeeBO();
        BeanUtils.copyProperties(salaryGrantEmployeeDTO, grantEmployeeBO);

        page = employeeQueryService.queryEmployeeInfoChanged(page, grantEmployeeBO);

        //字段转换
        List<SalaryGrantEmployeeBO> employeeBOList = page.getRecords();
        if (!CollectionUtils.isEmpty(employeeBOList)) {
            int size = employeeBOList.size();
            SalaryGrantEmployeeBO employeeBO;
            for (int i = 0; i < size; i++) {
                employeeBO = employeeBOList.get(i);
                //国籍转码
                if (!StringUtils.isEmpty(employeeBO.getCountryCode())){
                    employeeBO.setCountryName(commonService.getCountryName(employeeBO.getCountryCode()));
                }

                //发放状态
                if (!ObjectUtils.isEmpty(employeeBO.getGrantStatus())){
                    employeeBO.setGrantStatusName(commonService.getNameByValue("sgGrantStatus", String.valueOf(employeeBO.getGrantStatus())));
                }
            }
        }

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantEmployeeDTO> employeeDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return employeeDTOPage;
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
    public Result toReprieveEmployee(@RequestBody SalaryGrantEmployeeDTO salaryGrantEmployeeDTO){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("根据grantStatus进行不同的操作").setContent(JSON.toJSONString(salaryGrantEmployeeDTO)));

        SalaryGrantEmployeePO employeePO = new SalaryGrantEmployeePO();
        employeePO.setSalaryGrantEmployeeId(salaryGrantEmployeeDTO.getSalaryGrantEmployeeId());
        employeePO.setGrantStatus(salaryGrantEmployeeDTO.getGrantStatus());
        employeePO.setModifiedBy(UserContext.getUserId());
        employeePO.setModifiedTime(new Date());

        try {
            employeeCommandService.updateById(employeePO);
        } catch (Exception e) {
            //异常返回
            return new Result(1, "", null, null);
        }

        return new Result();
    }

    /**
     * 批量暂缓雇员操作
     * 循环遍历雇员信息，调用单个暂缓雇员操作
     *
     * @param salaryGrantEmployeeIdList
     * @return Result
     */
    @RequestMapping("/SalaryGrantEmployee/batchReprieveEmployee")
    public Result toBatchReprieveEmployee(@RequestBody List<Long> salaryGrantEmployeeIdList){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("批量暂缓雇员操作").setContent(JSON.toJSONString(salaryGrantEmployeeIdList)));

        if (CollectionUtils.isEmpty(salaryGrantEmployeeIdList)) {
            return new Result();
        }

        List<SalaryGrantEmployeePO> employeePOList = new ArrayList<>(salaryGrantEmployeeIdList.size());
        SalaryGrantEmployeePO employeePO;
        String userId = UserContext.getUserId();
        for (Long salaryGrantEmployeeId : salaryGrantEmployeeIdList) {
            employeePO = new SalaryGrantEmployeePO();
            employeePO.setSalaryGrantEmployeeId(salaryGrantEmployeeId);
            //发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
            employeePO.setGrantStatus(1);
            employeePO.setModifiedBy(userId);
            employeePO.setModifiedTime(new Date());

            employeePOList.add(employeePO);
        }

        try {
            employeeCommandService.updateBatchById(employeePOList, salaryGrantEmployeeIdList.size());
        } catch (Exception e) {
            //异常返回
            return new Result(1, "", null, null);
        }

        return new Result();
    }

    /**
     * 批量恢复雇员操作
     * 循环遍历雇员信息，调用单个恢复雇员操作
     *
     * @param salaryGrantEmployeeIdList
     * @return Result
     */
    @RequestMapping("/SalaryGrantEmployee/batchRecoverEmployee")
    public Result toBatchRecoverEmployee(@RequestBody List<Long> salaryGrantEmployeeIdList){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放雇员信息").setTitle("批量恢复雇员操作").setContent(JSON.toJSONString(salaryGrantEmployeeIdList)));

        if (CollectionUtils.isEmpty(salaryGrantEmployeeIdList)) {
            return new Result();
        }

        List<SalaryGrantEmployeePO> employeePOList = new ArrayList<>(salaryGrantEmployeeIdList.size());
        SalaryGrantEmployeePO employeePO;
        String userId = UserContext.getUserId();
        for (Long salaryGrantEmployeeId : salaryGrantEmployeeIdList) {
            employeePO = new SalaryGrantEmployeePO();
            employeePO.setSalaryGrantEmployeeId(salaryGrantEmployeeId);
            //发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
            employeePO.setGrantStatus(0);
            employeePO.setModifiedBy(userId);
            employeePO.setModifiedTime(new Date());

            employeePOList.add(employeePO);
        }

        try {
            employeeCommandService.updateBatchById(employeePOList, salaryGrantEmployeeIdList.size());
        } catch (Exception e) {
            //异常返回
            return new Result(1, "", null, null);
        }
        return new Result();
    }



    /**
     * 导出雇员信息
     *
     * @param salaryGrantMainTaskCode
     * @return
     */
    void toExportEmployee(String salaryGrantMainTaskCode){
        
    }

}
