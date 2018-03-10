package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;
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
 * @author yuantongqing  on 2018/02/08
 */
@RestController
public class TaskSubDeclareDetailController extends BaseController {

    @Autowired
    public TaskSubDeclareDetailService taskSubDeclareDetailService;

    /**
     * 查询申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "querySubDeclareDetailsByParams")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsByParams(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();
        try {
            RequestForSubDeclareDetail requestForSubDeclareDetail = new RequestForSubDeclareDetail();
            BeanUtils.copyProperties(taskSubDeclareDetailDTO, requestForSubDeclareDetail);
            ResponseForSubDeclareDetail responseForSubDeclareDetail = taskSubDeclareDetailService.querySubDeclareDetailsByParams(requestForSubDeclareDetail);
            jr.success(responseForSubDeclareDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", taskSubDeclareDetailDTO.getSubDeclareId().toString());
            tags.put("employeeNo", taskSubDeclareDetailDTO.getEmployeeNo());
            tags.put("employeeName", taskSubDeclareDetailDTO.getEmployeeName());
            tags.put("idType", taskSubDeclareDetailDTO.getIdType());
            tags.put("idNo", taskSubDeclareDetailDTO.getIdNo());
            //日志工具类返回
            logService.error(e, "TaskSubDeclareDetailController.querySubDeclareDetailsByParams", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
