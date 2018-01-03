package com.ciicsh.gto.fcsupportcenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcsupportcenter.tax.commandservice.api.dto.TaskSubPaymentDetailDTO;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.business.TaskSubPaymentDetailService;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.payment.RequestForSubPaymentDetail;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.payment.ResponseForSubPaymentDetail;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing create on 2018/1/3
 */
@RestController
public class TaskSubPaymentDetailController {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubPaymentDetailController.class);

    @Autowired
    private TaskSubPaymentDetailService taskSubPaymentDetailService;

    /**
     * 查询缴纳明细
     * @param taskSubPaymentDetailDTO
     * @return
     */
    @PostMapping(value = "querySubPaymentDetailsByParams")
    public JsonResult querySubPaymentDetailsByParams(@RequestBody TaskSubPaymentDetailDTO taskSubPaymentDetailDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubPaymentDetail requestForSubPaymentDetail = new RequestForSubPaymentDetail();
            BeanUtils.copyProperties(taskSubPaymentDetailDTO,requestForSubPaymentDetail);
            ResponseForSubPaymentDetail responseForSubPaymentDetail = taskSubPaymentDetailService.querySubPaymentDetailsByParams(requestForSubPaymentDetail);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubPaymentDetail);
        } catch (BeansException e) {
            logger.error("querySubPaymentDetailsByParams error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

}
