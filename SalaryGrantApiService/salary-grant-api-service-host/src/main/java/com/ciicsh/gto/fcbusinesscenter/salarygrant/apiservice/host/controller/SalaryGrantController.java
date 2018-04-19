package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.ReponseTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.RequestTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy.SalaryGrantProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private SalaryGrantService salaryGrantService;

    /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param dto
     * @return
     */
    @Override
    @RequestMapping(value = "/getTask", method = RequestMethod.POST)
    public Result<ReponseTaskDTO> getTask(@RequestBody RequestTaskDTO dto) {
        salaryGrantService.getTask(null);
        return null;
    }

}
