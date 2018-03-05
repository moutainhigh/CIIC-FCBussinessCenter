package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CalculationBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.EmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.CalculationBatchServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhua
 */
@RestController
public class CalculationBatchController extends BaseController {

    @Autowired
    public CalculationBatchServiceImpl calculationBatchService;

    /**
     * 查询计算批次列表
     *
     * @param calculationBatchDTO
     * @return
     */
    @RequestMapping(value = "/queryCalculationBatchs")
    public JsonResult queryCalculationBatchs(@RequestBody CalculationBatchDTO calculationBatchDTO) {

        JsonResult jr = new JsonResult();

        try {
            RequestForCalBatch requestForCalBatch = new RequestForCalBatch();
            BeanUtils.copyProperties(calculationBatchDTO, requestForCalBatch);
            ResponseForCalBatch responseForCalBatch = calculationBatchService.queryCalculationBatchs(requestForCalBatch);
            jr.success(responseForCalBatch);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", calculationBatchDTO.getManagerName());
            tags.put("batchNo", calculationBatchDTO.getBatchNo());
            //日志工具类返回
            logService.error(e, "CalculationBatchController.queryCalculationBatchs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "00"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 查询计算批次详情列表
     *
     * @param employeeDTO
     * @return
     */
    @RequestMapping(value = "/queryCalculationBatchDetails")
    public JsonResult queryCalculationBatchDetails(@RequestBody EmployeeDTO employeeDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForEmployees requestForEmployees = new RequestForEmployees();
            BeanUtils.copyProperties(employeeDTO, requestForEmployees);
            ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchService.queryCalculationBatchDetails(requestForEmployees);
            jr.success(responseForCalBatchDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("calculationBatchId", employeeDTO.getCalculationBatchId().toString());
            //日志工具类返回
            logService.error(e, "CalculationBatchController.queryCalculationBatchDetails", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "00"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 创建主任务
     *
     * @param calculationBatchDTO
     * @return
     */
    @RequestMapping(value = "/createMainTask")
    public JsonResult createMainTask(@RequestBody CalculationBatchDTO calculationBatchDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(calculationBatchDTO, requestForMainTaskMain);
            calculationBatchService.createMainTask(requestForMainTaskMain);
            jr.success(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("batchIds", calculationBatchDTO.getBatchIds().toString());
            tags.put("batchNos", calculationBatchDTO.getBatchNos().toString());
            //日志工具类返回
            logService.error(e, "CalculationBatchController.createMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

}
