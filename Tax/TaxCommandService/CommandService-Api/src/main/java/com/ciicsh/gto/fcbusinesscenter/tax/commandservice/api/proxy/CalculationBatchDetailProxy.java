package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy;

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
public interface CalculationBatchDetailProxy {
    /**
     * 查询申报记录
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaxBatchDetail")
    JsonResult queryTaxBatchDetail(@RequestBody TaskProofDTO taskProofDTO);
}
