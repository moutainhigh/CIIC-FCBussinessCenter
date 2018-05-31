package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyDetailController extends BaseController {

    @Autowired
    private TaskSubMoneyDetailService taskSubMoneyDetailService;


    /**
     * 查询划款明细
     *
     * @param taskSubMoneyDetailDTO
     * @return
     */
    @GetMapping(value = "querySubMoneyDetailsByParams")
    public JsonResult<ResponseForSubMoneyDetail> querySubMoneyDetailsByParams(TaskSubMoneyDetailDTO taskSubMoneyDetailDTO) {
        JsonResult<ResponseForSubMoneyDetail> jr = new JsonResult<>();

        RequestForSubMoneyDetail requestForSubMoneyDetail = new RequestForSubMoneyDetail();
        BeanUtils.copyProperties(taskSubMoneyDetailDTO, requestForSubMoneyDetail);
        ResponseForSubMoneyDetail responseForSubMoneyDetail = taskSubMoneyDetailService.querySubMoneyDetailsByParams(requestForSubMoneyDetail);
        jr.fill(responseForSubMoneyDetail);

        return jr;
    }
}
