package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CalculationBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.EmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.CalculationBatchServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
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

    @Autowired
    public TaskNoService taskNoService;

    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.ciicsh.gt1.fcbusinesscenter.tax", artifactId = "fcbusinesscenter_tax_commandservice_drools", version = "1.0.0-SNAPSHOT")
    private KieSession kSession;

    /**
     * 查询计算批次列表
     *
     * @param calculationBatchDTO
     * @return
     */
    @RequestMapping(value = "/queryCalculationBatchs")
    public JsonResult<ResponseForCalBatch> queryCalculationBatchs(@RequestBody CalculationBatchDTO calculationBatchDTO) {

        JsonResult<ResponseForCalBatch> jr = new JsonResult<>();

        /*try {

            ReleaseIdImpl releaseId = new ReleaseIdImpl("com.ciicsh.gt1.fcbusinesscenter.tax", "fcbusinesscenter-tax-commandservice-drools","LATEST");
            KieServices ks = KieServices.Factory.get();
            KieContainer container = ks.newKieContainer(releaseId);
            KieScanner scanner = ks.newKieScanner(container);
            scanner.start(1000);
            StatelessKieSession session = container.newStatelessKieSession();

            FactType factType = session.getKieBase().getFactType("com.ciicsh.gt1.fcbusinesscenter.tax.fcbusinesscenter-tax-commandservice-drools", "CalculationContext");
            Object cc = factType.newInstance();
            //factType.set(cc, "name", "张三");
            //factType.set(cc, "age", 17);
            session.execute(cc);
            Object o = factType.get(cc,"incomeForTax");

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        /*try {
            RequestForCalBatch requestForCalBatch = new RequestForCalBatch();
            BeanUtils.copyProperties(calculationBatchDTO, requestForCalBatch);
            ResponseForCalBatch responseForCalBatch = calculationBatchService.queryCalculationBatchs(requestForCalBatch);
            jr.fill(responseForCalBatch);
            LogTaskFactory.getLogger().info("", "CalculationBatchController.queryCalculationBatchs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "00"), LogType.APP, null);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", calculationBatchDTO.getManagerName());
            tags.put("batchNo", calculationBatchDTO.getBatchNo());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CalculationBatchController.queryCalculationBatchs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "00"), LogType.APP, tags);
            jr.error();
        }*/

        return jr;
    }

    /**
     * 查询计算批次详情列表
     *
     * @param employeeDTO
     * @return
     */
    @RequestMapping(value = "/queryCalculationBatchDetails")
    public JsonResult<ResponseForCalBatchDetail> queryCalculationBatchDetails(@RequestBody EmployeeDTO employeeDTO) {

        JsonResult<ResponseForCalBatchDetail> jr = new JsonResult<>();
        try {
            RequestForEmployees requestForEmployees = new RequestForEmployees();
            BeanUtils.copyProperties(employeeDTO, requestForEmployees);
            ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchService.queryCalculationBatchDetails(requestForEmployees);
            jr.fill(responseForCalBatchDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("calculationBatchId", employeeDTO.getCalculationBatchId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CalculationBatchController.queryCalculationBatchDetails", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "00"), LogType.APP, tags);
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
    public JsonResult<Boolean> createMainTask(@RequestBody CalculationBatchDTO calculationBatchDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(calculationBatchDTO, requestForMainTaskMain);
            calculationBatchService.createMainTask(requestForMainTaskMain);
//            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("batchIds", calculationBatchDTO.getBatchIds().toString());
            tags.put("batchNos", calculationBatchDTO.getBatchNos().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CalculationBatchController.createMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

}
