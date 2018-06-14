package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CalculationBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.EmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ConstraintService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.CalculationBatchServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@RestController
public class CalculationBatchController extends BaseController {

    @Autowired
    private CalculationBatchServiceImpl calculationBatchService;

    @Autowired
    private TaskNoService taskNoService;

    @Autowired
    private ConstraintService constraintService;

    /**
     * 查询计算批次列表
     *
     * @param calculationBatchDTO
     * @return
     */
    @GetMapping(value = "/queryCalculationBatchs")
    public JsonResult<ResponseForCalBatch> queryCalculationBatchs(CalculationBatchDTO calculationBatchDTO) {

        JsonResult<ResponseForCalBatch> jr = new JsonResult<>();

        RequestForCalBatch requestForCalBatch = new RequestForCalBatch();
        BeanUtils.copyProperties(calculationBatchDTO, requestForCalBatch);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方名称数组
            requestForCalBatch.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForCalBatch responseForCalBatch = calculationBatchService.queryCalculationBatchs(requestForCalBatch);
        jr.fill(responseForCalBatch);

        return jr;
    }

    /**
     * 查询计算批次详情列表
     *
     * @param employeeDTO
     * @return
     */
    @GetMapping(value = "/queryCalculationBatchDetails")
    public JsonResult<ResponseForCalBatchDetail> queryCalculationBatchDetails(EmployeeDTO employeeDTO) {

        JsonResult<ResponseForCalBatchDetail> jr = new JsonResult<>();

        RequestForEmployees requestForEmployees = new RequestForEmployees();
        BeanUtils.copyProperties(employeeDTO, requestForEmployees);
        ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchService.queryCalculationBatchDetails(requestForEmployees);
        jr.fill(responseForCalBatchDetail);

        return jr;
    }

    /**
     * 创建主任务
     *
     * @param calculationBatchDTO
     * @return
     */
    @RequestMapping(value = "/createMainTask")
    public JsonResult<Boolean> createMainTask(@RequestBody CalculationBatchDTO calculationBatchDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        //检查约束
        int i = this.constraintService.checkBatch(calculationBatchDTO.getBatchIds());
        if(i == ConstraintService.C1){
            jr.fill(JsonResult.ReturnCode.CONSTRAINTS_1);
            return jr;
        }

        RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(calculationBatchDTO, requestForMainTaskMain);
        calculationBatchService.createMainTask(requestForMainTaskMain);

        return jr;
    }

}
