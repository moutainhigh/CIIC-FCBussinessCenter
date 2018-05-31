package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CalculationBatchDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.CalculationBatchDetailProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        ResponseForBatchDetail responseForBatchDetail = calculationBatchDetailService.queryCalculationBatchDetail(requestForProof);
        jr.fill(responseForBatchDetail);

        return jr;
    }

    /**
     * 条件查询计算批次明细
     *
     * @param calculationBatchDetailDTO
     * @return
     */
    @GetMapping(value = "queryTaxBatchDetailByRes")
    public JsonResult<ResponseForCalBatchDetail> queryTaxBatchDetailByRes(CalculationBatchDetailDTO calculationBatchDetailDTO) {
        JsonResult<ResponseForCalBatchDetail> jr = new JsonResult<>();

        RequestForCalBatchDetail requestForCalBatchDetail = new RequestForCalBatchDetail();
        BeanUtils.copyProperties(calculationBatchDetailDTO, requestForCalBatchDetail);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方名称数组
            requestForCalBatchDetail.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchDetailService.queryTaxBatchDetailByRes(requestForCalBatchDetail);
        jr.fill(responseForCalBatchDetail);

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

        if (calculationBatchDetailDTO.getIds() != null && calculationBatchDetailDTO.getIds().length > 0) {
            calculationBatchDetailService.queryCalculationBatchDetail(calculationBatchDetailDTO.getIds());
        }

        return jr;
    }

}
