package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy.SalaryGrantProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantTaskProcessService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.transform.CommonTransform;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 薪资发放 前端控制器
 * </p>
 *
 * @author chenpb
 * @since 2018-04-18
 */
@RestController
@RequestMapping(value = "/api/sg")
public class SalaryGrantController implements SalaryGrantProxy {
    @Autowired
    LogClientService logClientService;
    @Autowired
    private SalaryGrantService salaryGrantService;
    @Autowired
    SalaryGrantTaskProcessService salaryGrantTaskProcessService;

    /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param dto
     * @return
     */
    @Override
    public Result<ReponseTaskDTO> getTask(@RequestBody RequestTaskDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询任务单主表").setContent(JSON.toJSONString(dto)));
        try {
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            List<SalaryGrantTaskBO> list = salaryGrantService.getTask(bo);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("任务单主表列表信息").setContent(JSON.toJSONString(list)));
            return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("任务单主表查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

    /**
     *  根据主表任务单编号查询薪资发放任务单子表
     * @author gaoyang
     * @date 2018-04-19
     * @param dto
     * @return Result<ReponseSubTaskDTO>
     */
    @Override
    public Result<ReponseSubTaskDTO> getSubTask(@RequestBody RequestSubTaskDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询任务单子表").setContent(JSON.toJSONString(dto)));
        try {
            SalaryGrantSubTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantSubTaskBO.class);
            List<SalaryGrantSubTaskBO> list = salaryGrantService.getSubTask(bo);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("任务单子表列表信息").setContent(JSON.toJSONString(list)));
            return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("任务单子表查询异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

    /**
     *  修改雇员发放状态
     * @author chenpb
     * @date 2018-06-19
     * @param dto
     * @return Result<ReprieveEmployeeDTO>
     */
    @Override
    public Result<ReprieveEmployeeDTO> updateForReprieveEmployee(@RequestBody ReprieveEmployeeDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("修改雇员发放状态").setContent(JSON.toJSONString(dto)));
        try {
            ReprieveEmployeeBO bo = CommonTransform.convertToEntity(dto, ReprieveEmployeeBO.class);
            bo = salaryGrantService.updateForReprieveEmployee(bo);
            ReprieveEmployeeDTO responseDto = CommonTransform.convertToDTO(bo, ReprieveEmployeeDTO.class);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("结果").setContent(JSON.toJSONString(responseDto)));
            return ResultGenerator.genSuccessResult(responseDto);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("修改雇员发放状态异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

    /**
     *  根据退票雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-05-22
     * @param dto
     * @return Result<ResponseRefundDTO>
     */
    @Override
    public Result<ResponseRefundDTO> toCreateRefundTask(@RequestBody SalaryGrantRefundDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("退票发放").setContent(JSON.toJSONString(dto)));
        try{
            if(StringUtils.isEmpty(dto.getBatchCode())){
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("退票发放").setContent("传入参数计算批次号为空，生成薪资发放任务单失败！"));
                ResponseRefundDTO responseDto = new ResponseRefundDTO();
                responseDto.setProcessResult(false);
                responseDto.setProcessMessage("传入参数计算批次号为空，生成薪资发放任务单失败！");
                return ResultGenerator.genSuccessResult(responseDto);
            }else{
                String batchCode = dto.getBatchCode();
                List<SalaryGrantEmployeeRefundBO> employeeRefundList =  CommonTransform.convertToDTOs(dto.getEmployeeList(), SalaryGrantEmployeeRefundBO.class);
                ResponseRefundBO responseRefundBO = salaryGrantTaskProcessService.createRefundTask(employeeRefundList, batchCode);
                ResponseRefundDTO responseDto = CommonTransform.convertToDTO(responseRefundBO, ResponseRefundDTO.class);
                return ResultGenerator.genSuccessResult(responseDto);
            }
        } catch (Exception e){
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("退票发放异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

    /**
     *  根据暂缓雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-08-07
     * @param dto
     * @return Result<ResponseReprieveDTO>
     */
    @Override
    public Result<ResponseReprieveDTO> toCreateReprieveTask(@RequestBody SalaryGrantReprieveDTO dto) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓发放").setContent(JSON.toJSONString(dto)));
        try{
            if(StringUtils.isEmpty(dto.getBatchCode()) || StringUtils.isEmpty(dto.getTaskCode())){
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓发放").setContent("传入参数计算批次号或任务单编号为空，生成薪资发放任务单失败！"));
                ResponseReprieveDTO responseDto = new ResponseReprieveDTO();
                responseDto.setProcessResult(false);
                responseDto.setProcessMessage("传入参数计算批次号或任务单编号为空，生成薪资发放任务单失败！");
                return ResultGenerator.genSuccessResult(responseDto);
            }else{
                String batchCode = dto.getBatchCode();
                String taskCode = dto.getTaskCode();
                List<SalaryGrantEmployeeReprieveBO> employeeReprieveList =  CommonTransform.convertToDTOs(dto.getEmployeeList(), SalaryGrantEmployeeReprieveBO.class);
                ResponseReprieveBO responseReprieveBO = salaryGrantTaskProcessService.createReprieveTask(employeeReprieveList, batchCode, taskCode);
                ResponseReprieveDTO responseDto = CommonTransform.convertToDTO(responseReprieveBO, ResponseReprieveDTO.class);
                return ResultGenerator.genSuccessResult(responseDto);
            }
        } catch (Exception e){
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("暂缓发放异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

    /**
     *  根据任务单信息进行驳回处理
     * @author gaoyang
     * @date 2018-05-22
     * @param dto
     * @return Result<Boolean>
     */
    @Override
    public Result<Boolean> toRejectTask(@RequestBody SalaryGrantTaskDTO dto) {
        return null;
    }

}
