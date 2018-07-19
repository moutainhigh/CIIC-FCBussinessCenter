package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy.SalaryGrantProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.ReprieveEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantSubTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.transform.CommonTransform;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Result<ReponseSubTaskDTO> getSubTask(RequestSubTaskDTO dto) {
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

    @Override
    public Result<Boolean> toCreateRefundTask(SalaryGrantRefundDTO dto) {
        return null;
    }

    @Override
    public Result<Boolean> toRejectTask(SalaryGrantTaskDTO dto) {
        return null;
    }

}