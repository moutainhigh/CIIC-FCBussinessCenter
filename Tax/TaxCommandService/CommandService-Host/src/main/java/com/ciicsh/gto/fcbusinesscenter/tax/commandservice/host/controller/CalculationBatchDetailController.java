package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.CalculationBatchDetailProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing on 2017/12/19
 */
@RestController
public class CalculationBatchDetailController extends BaseController implements CalculationBatchDetailProxy {

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchDetailController.class);

    @Autowired
    private CalculationBatchDetailService calculationBatchDetailService;

    /**
     * 查询申报记录
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    public JsonResult queryTaxBatchDetail(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForBatchDetail responseForBatchDetail = calculationBatchDetailService.queryCalculationBatchDetail(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForBatchDetail);
        } catch (Exception e) {
            logger.error("queryTaxBatchDetail error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }
}
