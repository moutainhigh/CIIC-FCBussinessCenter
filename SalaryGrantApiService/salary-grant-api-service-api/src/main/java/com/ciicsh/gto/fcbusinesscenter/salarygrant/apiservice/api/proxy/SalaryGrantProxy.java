package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.ReponseSubTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.ReponseTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.RequestSubTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.RequestTaskDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("fc-business-center-sg-api-service")
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

}
