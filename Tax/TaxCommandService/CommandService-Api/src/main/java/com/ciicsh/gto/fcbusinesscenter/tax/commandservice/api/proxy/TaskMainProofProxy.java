package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yuantongqing on 20180116
 */
@FeignClient(name="fcbusiness-center-tax-command-service")
@RequestMapping(value = "/tax")
public interface TaskMainProofProxy {
    /**
     * 条件查询完税凭证主任务
     *
     * @param taskMainProofDTO
     * @return
     */
    @PostMapping(value = "/api/queryTaskMainProofByRes")
    JsonResult queryTaskMainProofByRes(@RequestBody  TaskMainProofDTO taskMainProofDTO);

    /**
     * 新建任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/api/addTaskProof")
    JsonResult addTaskProof(@RequestBody TaskProofDTO taskProofDTO);

    /**
     * 修改（即：提交）完税凭证状态
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/api/updateTaskProof")
    JsonResult updateTaskProof(@RequestBody TaskProofDTO taskProofDTO);

    /**
     * 将完税凭证任务置为失效
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/api/invalidTaskProof")
    JsonResult invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO);

}
