package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author yuantongqing  on 2018/02/08
 */
@RestController
public class TaskSubDeclareDetailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubDeclareDetailController.class);

    @Autowired
    public TaskSubDeclareDetailService taskSubDeclareDetailService;

    /**
     * 查询申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "querySubDeclareDetailsByParams")
    public JsonResult querySubDeclareDetailsByParams(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubDeclareDetail requestForSubDeclareDetail = new RequestForSubDeclareDetail();
            BeanUtils.copyProperties(taskSubDeclareDetailDTO, requestForSubDeclareDetail);
            ResponseForSubDeclareDetail responseForSubDeclareDetail = taskSubDeclareDetailService.querySubDeclareDetailsByParams(requestForSubDeclareDetail);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubDeclareDetail);
        } catch (Exception e) {
            logger.error("querySubDeclareDetailsByParams error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }

}
