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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuhua
 */
@RestController
public class CalculationBatchController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchController.class);

    @Autowired
    public CalculationBatchServiceImpl calculationBatchService;

    /**
     * 查询计算批次列表
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
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForCalBatch);

        } catch (Exception e) {
            logger.error(e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
            throw e;
        }

        return jr;
    }

    /**
     * 查询计算批次详情列表
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
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForCalBatchDetail);
        } catch (Exception e) {
            logger.error(e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
            throw e;
        }

        return jr;
    }

    /**
     * 创建主任务
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
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            //jr.setData();
        } catch (Exception e) {
            logger.error(e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
            throw e;
        }

        return jr;
    }

}
