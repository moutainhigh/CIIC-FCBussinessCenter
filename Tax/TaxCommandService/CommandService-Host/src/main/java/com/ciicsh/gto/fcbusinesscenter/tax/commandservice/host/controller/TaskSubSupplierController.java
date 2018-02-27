package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing  on 2018/02/09
 */
@RestController
public class TaskSubSupplierController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskSubSupplierController.class);

    @Autowired
    private TaskSubSupplierService taskSubSupplierService;

    /**
     * 查询供应商子任务列表
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubSupplier")
    public JsonResult queryTaskSubSupplier(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            ResponseForTaskSubSupplier responseForTaskSubSupplier = taskSubSupplierService.queryTaskSubSupplier(requestForTaskSubSupplier);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForTaskSubSupplier);
        } catch (Exception e) {
            logger.error("queryTaskSubSupplier error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }


    /**
     * 根据供应商子任务ID查询供应商信息
     *
     * @param subSupplierId
     * @return
     */
    @PostMapping(value = "/querySupplierDetailsById/{subSupplierId}")
    public JsonResult querySupplierDetailsById(@PathVariable(value = "subSupplierId") Long subSupplierId) {
        JsonResult jr = new JsonResult();
        try {
            //根据供应商子任务ID查询供应商信息
            TaskSubSupplierPO taskSubSupplierPO = taskSubSupplierService.querySupplierDetailsById(subSupplierId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubSupplierPO);
        } catch (Exception e) {
            logger.error("querySupplierDetailsById error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }
}
