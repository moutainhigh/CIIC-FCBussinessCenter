package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;
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
    @PostMapping(value = "querySubMoneyDetailsByParams")
    public JsonResult<ResponseForSubMoneyDetail> querySubMoneyDetailsByParams(@RequestBody TaskSubMoneyDetailDTO taskSubMoneyDetailDTO) {
        JsonResult<ResponseForSubMoneyDetail> jr = new JsonResult<>();
        try {
            RequestForSubMoneyDetail requestForSubMoneyDetail = new RequestForSubMoneyDetail();
            BeanUtils.copyProperties(taskSubMoneyDetailDTO, requestForSubMoneyDetail);
            ResponseForSubMoneyDetail responseForSubMoneyDetail = taskSubMoneyDetailService.querySubMoneyDetailsByParams(requestForSubMoneyDetail);
            jr.fill(responseForSubMoneyDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskSubMoneyId", taskSubMoneyDetailDTO.getTaskSubMoneyId().toString());
            tags.put("employeeNo", taskSubMoneyDetailDTO.getEmployeeNo());
            tags.put("employeeName", taskSubMoneyDetailDTO.getEmployeeName());
            tags.put("idType", taskSubMoneyDetailDTO.getIdType());
            tags.put("idNo", taskSubMoneyDetailDTO.getIdNo());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyDetailController.querySubMoneyDetailsByParams", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
}
