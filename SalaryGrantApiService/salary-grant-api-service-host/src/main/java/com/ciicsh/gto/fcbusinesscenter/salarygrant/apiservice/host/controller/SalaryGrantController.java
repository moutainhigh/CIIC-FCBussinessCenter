package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.ReponseSubTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.ReponseTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.RequestSubTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant.RequestTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.proxy.SalaryGrantProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantSubTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.transform.CommonTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        try {
            SalaryGrantTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantTaskBO.class);
            List<SalaryGrantTaskBO> list = salaryGrantService.getTask(bo);
            return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

    @Override
    public Result<ReponseSubTaskDTO> getSubTask(RequestSubTaskDTO dto) {
        try {
            SalaryGrantSubTaskBO bo = CommonTransform.convertToEntity(dto, SalaryGrantSubTaskBO.class);
            List<SalaryGrantSubTaskBO> list = salaryGrantService.getSubTask(bo);
            return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            return ResultGenerator.genServerFailResult("处理失败");
        }
    }

}
