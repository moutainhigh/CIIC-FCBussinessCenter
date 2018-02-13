package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForSubSupplierDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing  on 2018/02/11
 */
@RestController
public class TaskSubSupplierDetailController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskSubSupplierDetailController.class);

    @Autowired
    private TaskSubSupplierDetailService taskSubSupplierDetailService;

    /**
     * 查询供应商明细
     *
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @PostMapping(value = "querySubSupplierDetailsByParams")
    public JsonResult querySubSupplierDetailsByParams(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubSupplierDetail requestForSubSupplierDetail = new RequestForSubSupplierDetail();
            BeanUtils.copyProperties(taskSubSupplierDetailDTO, requestForSubSupplierDetail);
            ResponseForSubSupplierDetail responseForSubSupplierDetail = taskSubSupplierDetailService.querySubSupplierDetailsByParams(requestForSubSupplierDetail);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubSupplierDetail);
        } catch (Exception e) {
            logger.error("querySubSupplierDetailsByParams error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }

}
