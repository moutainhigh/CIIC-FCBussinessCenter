package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy;


import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuantongqing on 20180116
 */
@FeignClient(name="fcbusiness-center-tax-command-service")
@RequestMapping(value = "/tax")
public interface TaskSubProofProxy {

    /**
     * 根据主任务ID查询子任务
     *
     * @param taskMainProofId
     * @return
     */
    @GetMapping(value = "/api/queryTaskSubProofByMainId/{taskMainProofId}")
    JsonResult queryTaskSubProofByMainId(@PathVariable(value = "taskMainProofId")  Long taskMainProofId);

    /**
     * 根据子任务ID复制其关联数据
     *
     * @param taskSubProofId
     * @return
     */
    @PostMapping(value = "/api/copyProofInfoBySubId/{taskSubProofId}")
    JsonResult copyProofInfoBySubId(@PathVariable(value = "taskSubProofId") Long taskSubProofId);

    /**
     * 查询子任务信息
     *
     * @param taskSubProofDTO
     * @return
     */
    @GetMapping(value = "/api/queryTaskSubProofByRes")
    JsonResult queryTaskSubProofByRes(TaskSubProofDTO taskSubProofDTO);

}
