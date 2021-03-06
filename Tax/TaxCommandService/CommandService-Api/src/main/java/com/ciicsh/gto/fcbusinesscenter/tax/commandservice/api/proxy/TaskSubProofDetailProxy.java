package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.SubProofDetailDTO;
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
public interface TaskSubProofDetailProxy {

    /**
     * 批量保存完税凭证申请明细
     *
     * @param subProofDetailDTO
     * @return
     */
    @PostMapping(value = "/api/saveSubProofDetail")
    JsonResult saveSubProofDetail(@RequestBody SubProofDetailDTO subProofDetailDTO);

    /**
     * 查询完税申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/api/queryTaskSubProofDetail")
    JsonResult queryTaskSubProofDetail(@RequestBody TaskProofDTO taskProofDTO);

    /**
     * 查询雇员信息(测试接口)
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/api/queryEmployee")
    JsonResult queryEmployee(@RequestBody TaskProofDTO taskProofDTO);
}
