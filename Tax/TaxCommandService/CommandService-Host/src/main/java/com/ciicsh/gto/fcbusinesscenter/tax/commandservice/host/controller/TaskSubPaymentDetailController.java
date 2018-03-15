package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubPaymentDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPaymentDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPaymentDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    @PostMapping(value = "querySubPaymentDetailsByParams")
    public JsonResult<ResponseForSubPaymentDetail> querySubPaymentDetailsByParams(@RequestBody TaskSubPaymentDetailDTO taskSubPaymentDetailDTO) {
        JsonResult<ResponseForSubPaymentDetail> jr = new JsonResult<>();
        try {
            RequestForSubPaymentDetail requestForSubPaymentDetail = new RequestForSubPaymentDetail();
            BeanUtils.copyProperties(taskSubPaymentDetailDTO, requestForSubPaymentDetail);
            ResponseForSubPaymentDetail responseForSubPaymentDetail = taskSubPaymentDetailService.querySubPaymentDetailsByParams(requestForSubPaymentDetail);
            jr.fill(responseForSubPaymentDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskSubPaymentId", taskSubPaymentDetailDTO.getTaskSubPaymentId().toString());
            tags.put("employeeNo", taskSubPaymentDetailDTO.getEmployeeNo());
            tags.put("employeeName", taskSubPaymentDetailDTO.getEmployeeName());
            tags.put("idType", taskSubPaymentDetailDTO.getIdType());
            tags.put("idNo", taskSubPaymentDetailDTO.getIdNo());
            //日志工具类返回
            logService.error(e, "TaskSubPaymentDetailController.querySubPaymentDetailsByParams", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "04"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
