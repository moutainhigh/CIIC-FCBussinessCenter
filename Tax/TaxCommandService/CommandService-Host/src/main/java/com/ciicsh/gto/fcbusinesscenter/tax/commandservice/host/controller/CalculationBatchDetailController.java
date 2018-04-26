package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CalculationBatchDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.CalculationBatchDetailProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuantongqing on 2017/12/19
 */
@RestController
public class CalculationBatchDetailController extends BaseController implements CalculationBatchDetailProxy {

    @Autowired
    private CalculationBatchDetailService calculationBatchDetailService;

    /**
     * 查询申报记录
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    public JsonResult<ResponseForBatchDetail> queryTaxBatchDetail(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForBatchDetail> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForBatchDetail responseForBatchDetail = calculationBatchDetailService.queryCalculationBatchDetail(requestForProof);
            jr.fill(responseForBatchDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("idType", taskProofDTO.getIdType());
            tags.put("idNo", taskProofDTO.getIdNo());
            tags.put("incomeSubject", taskProofDTO.getIncomeSubject());
            tags.put("submitTimeStart", taskProofDTO.getSubmitTimeStart());
            tags.put("submitTimeEnd", taskProofDTO.getSubmitTimeEnd());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CalculationBatchDetailController.queryTaxBatchDetail", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 条件查询计算批次明细
     *
     * @param calculationBatchDetailDTO
     * @return
     */
    @PostMapping(value = "queryTaxBatchDetailByRes")
    public JsonResult<ResponseForCalBatchDetail> queryTaxBatchDetailByRes(@RequestBody CalculationBatchDetailDTO calculationBatchDetailDTO) {
        JsonResult<ResponseForCalBatchDetail> jr = new JsonResult<>();
        try {
            RequestForCalBatchDetail requestForCalBatchDetail = new RequestForCalBatchDetail();
            BeanUtils.copyProperties(calculationBatchDetailDTO, requestForCalBatchDetail);
            Optional.ofNullable(UserContext.getManagementInfo()).ifPresent(managementInfo -> {
                //设置request请求管理方名称数组
                requestForCalBatchDetail.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
            });
            ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchDetailService.queryTaxBatchDetailByRes(requestForCalBatchDetail);
            jr.fill(responseForCalBatchDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("employeeNo", calculationBatchDetailDTO.getEmployeeNo());
            tags.put("employeeName", calculationBatchDetailDTO.getEmployeeName());
            tags.put("managerName", calculationBatchDetailDTO.getManagerName());
            tags.put("batchNo", calculationBatchDetailDTO.getBatchNo());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CalculationBatchDetailController.queryTaxBatchDetailByRes", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量恢复计算批次明细
     *
     * @param calculationBatchDetailDTO
     * @return
     */
    @PostMapping(value = "recoveryCalBatchDetail")
    public JsonResult<Boolean> recoveryCalBatchDetail(@RequestBody CalculationBatchDetailDTO calculationBatchDetailDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            if (calculationBatchDetailDTO.getIds() != null && calculationBatchDetailDTO.getIds().length > 0) {
                calculationBatchDetailService.queryCalculationBatchDetail(calculationBatchDetailDTO.getIds());
            }
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("ids", calculationBatchDetailDTO.getIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CalculationBatchDetailController.recoveryCalBatchDetail", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "00"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
