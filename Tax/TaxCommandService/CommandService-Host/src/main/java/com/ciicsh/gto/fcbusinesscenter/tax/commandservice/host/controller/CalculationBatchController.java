package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CalculationBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.EmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
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
//@RequestMapping("/tax")
public class CalculationBatchController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchController.class);

    @Autowired
    public CalculationBatchService calculationBatchService;

    @Autowired
    public CalculationBatchDetailService calculationBatchDetailService;

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
            ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchDetailService.queryCalculationBatchDetails(requestForEmployees);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForCalBatchDetail);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }

        return jr;
    }

}
