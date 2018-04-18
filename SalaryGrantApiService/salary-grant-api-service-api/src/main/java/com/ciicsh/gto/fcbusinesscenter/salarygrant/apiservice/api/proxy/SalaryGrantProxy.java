package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.requestTaskDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.core.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
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
    public JsonResult<Object> getTask(@RequestBody requestTaskDTO dto);

}
