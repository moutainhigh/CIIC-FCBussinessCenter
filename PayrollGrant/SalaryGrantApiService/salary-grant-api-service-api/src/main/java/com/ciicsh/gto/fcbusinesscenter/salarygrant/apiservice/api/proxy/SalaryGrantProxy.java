package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("fcbusiness-center-salarygrant-api-service")
@RequestMapping(value = "/api/sg")
public interface SalaryGrantProxy {

    /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param dto
     * @return
     */
    @RequestMapping(value = "/getTask", method = RequestMethod.POST)
    Result<ReponseTaskDTO> getTask(@RequestBody RequestTaskDTO dto);

    /**
     *  根据主表任务单编号查询薪资发放任务单子表
     * @author gaoyang
     * @date 2018-04-19
     * @param dto
     * @return Result<ReponseSubTaskDTO>
     */
    @PostMapping("/getSubTask")
    Result<ReponseSubTaskDTO> getSubTask(@RequestBody RequestSubTaskDTO dto);

    /**
     *  修改雇员发放状态
     * @author chenpb
     * @date 2018-06-19
     * @param dto
     * @return Result<ReprieveEmployeeDTO>
     */
    @PostMapping("/updateGrantStatus")
    Result<ReprieveEmployeeDTO> updateForReprieveEmployee(@RequestBody ReprieveEmployeeDTO dto);

    /**
     *  根据退票雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-05-22
     * @param dto
     * @return Result<ResponseRefundDTO>
     */
    @PostMapping("/toCreateRefundTask")
    Result<ResponseRefundDTO> toCreateRefundTask(@RequestBody SalaryGrantRefundDTO dto);

    /**
     *  根据暂缓雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-08-07
     * @param dto
     * @return Result<ResponseReprieveDTO>
     */
    @PostMapping("/toCreateReprieveTask")
    Result<ResponseReprieveDTO> toCreateReprieveTask(@RequestBody SalaryGrantReprieveDTO dto);

    /**
     *  根据任务单信息进行驳回处理
     * @author gaoyang
     * @date 2018-05-22
     * @param dto
     * @return Result<Boolean>
     */
    @PostMapping("/toRejectTask")
    Result<Boolean> toRejectTask(@RequestBody SalaryGrantTaskDTO dto);
}
