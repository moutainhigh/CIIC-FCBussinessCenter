package com.ciicsh.gto.fcsupportcenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcsupportcenter.tax.commandservice.api.dto.CalculationBatchDTO;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuhua
 */
@RestController
public class CalculationBatchController {

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchController.class);

    @Autowired
    public CalculationBatchService calculationBatchService;

    /**
     * 查询计算批次列表
     * @param calculationBatchDTO
     * @return
     */
    @RequestMapping(value = "/queryCalculationBatchs")
    public JsonResult queryCalculationBatchs(/*@RequestBody CalculationBatchDTO calculationBatchDTO*/) {
        logger.info("$$$$$$$$$$$$$$");
        CalculationBatchDTO calculationBatchDTO = new CalculationBatchDTO();
        JsonResult jr = new JsonResult();
        try {
            RequestForCalBatch requestForCalBatch = new RequestForCalBatch();
            BeanUtils.copyProperties(calculationBatchDTO, requestForCalBatch);
            ResponseForCalBatch responseForCalBatch = calculationBatchService.queryCalculationBatchs(requestForCalBatch);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForCalBatch);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

}
