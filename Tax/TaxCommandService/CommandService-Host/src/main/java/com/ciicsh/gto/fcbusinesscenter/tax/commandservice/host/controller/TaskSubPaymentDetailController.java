package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubPaymentDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPaymentDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPaymentDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing create on 2018/1/3
 */
@RestController
public class TaskSubPaymentDetailController extends BaseController {

    @Autowired
    private TaskSubPaymentDetailService taskSubPaymentDetailService;

    /**
     * 查询缴纳明细
     *
     * @param taskSubPaymentDetailDTO
     * @return
     */
    @GetMapping(value = "querySubPaymentDetailsByParams")
    public JsonResult<ResponseForSubPaymentDetail> querySubPaymentDetailsByParams(TaskSubPaymentDetailDTO taskSubPaymentDetailDTO) {
        JsonResult<ResponseForSubPaymentDetail> jr = new JsonResult<>();

        RequestForSubPaymentDetail requestForSubPaymentDetail = new RequestForSubPaymentDetail();
        BeanUtils.copyProperties(taskSubPaymentDetailDTO, requestForSubPaymentDetail);
        ResponseForSubPaymentDetail responseForSubPaymentDetail = taskSubPaymentDetailService.querySubPaymentDetailsByParams(requestForSubPaymentDetail);
        jr.fill(responseForSubPaymentDetail);

        return jr;
    }

}
