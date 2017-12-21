package com.ciicsh.gto.fcsupportcenter.tax.queryservice.host.controller;

import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForBatchDetail;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing on 2017/12/19
 */
@RestController
public class CalculationBatchDetailController {

    @Autowired
    private CalculationBatchDetailService calculationBatchDetailService;

    @RequestMapping(value = "/queryTaxBatchDetail")
    public JsonResult queryTaxBatchDetail(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForBatchDetail responseForBatchDetail = calculationBatchDetailService.queryCalculationBatchDetail(requestForProof);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(responseForBatchDetail);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }
}
