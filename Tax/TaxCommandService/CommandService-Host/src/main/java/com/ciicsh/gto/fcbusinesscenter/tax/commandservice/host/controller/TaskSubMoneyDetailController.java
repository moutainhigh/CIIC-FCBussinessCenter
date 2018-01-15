package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyDetailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubMoneyDetailController.class);

    @Autowired
    private TaskSubMoneyDetailService taskSubMoneyDetailService;


    /**
     * 查询划款明细
     * @param taskSubMoneyDetailDTO
     * @return
     */
    @PostMapping(value = "querySubMoneyDetailsByParams")
    public JsonResult querySubMoneyDetailsByParams(@RequestBody TaskSubMoneyDetailDTO taskSubMoneyDetailDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForSubMoneyDetail requestForSubMoneyDetail = new RequestForSubMoneyDetail();
            BeanUtils.copyProperties(taskSubMoneyDetailDTO,requestForSubMoneyDetail);
            ResponseForSubMoneyDetail responseForSubMoneyDetail = taskSubMoneyDetailService.querySubMoneyDetailsByParams(requestForSubMoneyDetail);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubMoneyDetail);
        } catch (Exception e) {
            logger.error("querySubMoneyDetailsByParams error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }
}
