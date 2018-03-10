package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuantongqing  on 2018/02/09
 */
@RestController
public class TaskSubSupplierController extends BaseController {

    @Autowired
    private TaskSubSupplierService taskSubSupplierService;

    /**
     * 查询供应商子任务列表
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubSupplier")
    public JsonResult<ResponseForTaskSubSupplier> queryTaskSubSupplier(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<ResponseForTaskSubSupplier> jr = new JsonResult<>();
        try {
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            ResponseForTaskSubSupplier responseForTaskSubSupplier = taskSubSupplierService.queryTaskSubSupplier(requestForTaskSubSupplier);
            jr.success(responseForTaskSubSupplier);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("declareAccount", taskSubSupplierDTO.getDeclareAccount());
            tags.put("managerName", taskSubSupplierDTO.getManagerName());
            tags.put("period", taskSubSupplierDTO.getPeriod());
            tags.put("periodType", taskSubSupplierDTO.getPeriodType());
            tags.put("supportName", taskSubSupplierDTO.getSupportName());
            tags.put("statusType", taskSubSupplierDTO.getStatusType());
            //日志工具类返回
            logService.error(e, "TaskSubSupplierController.queryTaskSubSupplier", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
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
    public JsonResult<TaskSubSupplierPO> querySupplierDetailsById(@PathVariable(value = "subSupplierId") Long subSupplierId) {
        JsonResult<TaskSubSupplierPO> jr = new JsonResult();
        try {
            //根据供应商子任务ID查询供应商信息
            TaskSubSupplierPO taskSubSupplierPO = taskSubSupplierService.querySupplierDetailsById(subSupplierId);
            jr.success(taskSubSupplierPO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subSupplierId", subSupplierId.toString());
            //日志工具类返回
            logService.error(e, "TaskSubSupplierController.querySupplierDetailsById", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
}
