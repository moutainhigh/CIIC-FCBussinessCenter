package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yuantongqing
 * on create 2018/1/9
 */
@FeignClient("fc-business-center-command-service-tax")
@RequestMapping("/tax")
public interface TaskMainProofProxy {

    /**
     * 对外查询完税凭证主任务API借口
     * @param taskMainProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskMainProofByRes")
    JsonResult queryTaskMainProofByRes(@RequestBody TaskMainProofDTO taskMainProofDTO);
}
