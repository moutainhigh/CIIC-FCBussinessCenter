package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForSubSupplierDetail;
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
 * @author yuantongqing  on 2018/02/11
 */
@RestController
public class TaskSubSupplierDetailController extends BaseController {

    @Autowired
    private TaskSubSupplierDetailService taskSubSupplierDetailService;

    /**
     * 查询供应商明细
     *
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @PostMapping(value = "querySubSupplierDetailsByParams")
    public JsonResult<ResponseForSubSupplierDetail> querySubSupplierDetailsByParams(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {
        JsonResult<ResponseForSubSupplierDetail> jr = new JsonResult<>();
        try {
            RequestForSubSupplierDetail requestForSubSupplierDetail = new RequestForSubSupplierDetail();
            BeanUtils.copyProperties(taskSubSupplierDetailDTO, requestForSubSupplierDetail);
            ResponseForSubSupplierDetail responseForSubSupplierDetail = taskSubSupplierDetailService.querySubSupplierDetailsByParams(requestForSubSupplierDetail);
            jr.fill(responseForSubSupplierDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subSupplierId", taskSubSupplierDetailDTO.getSubSupplierId().toString());
            tags.put("employeeNo", taskSubSupplierDetailDTO.getEmployeeNo());
            tags.put("employeeName", taskSubSupplierDetailDTO.getEmployeeName());
            tags.put("idType", taskSubSupplierDetailDTO.getIdType());
            tags.put("idNo", taskSubSupplierDetailDTO.getIdNo());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierDetailController.querySubSupplierDetailsByParams", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
