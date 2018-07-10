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
            requestForCalBatchDetail.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForCalBatchDetail responseForCalBatchDetail = calculationBatchDetailService.queryTaxBatchDetailByRes(requestForCalBatchDetail);
        jr.fill(responseForCalBatchDetail);

        return jr;
    }


    /**
     *
     * @param calculationBatchDetailDTO
     * @return
     */
    @PostMapping(value = "batchPostponeCalBatchDetail")
    public JsonResult<Boolean> batchPostponeCalBatchDetail(@RequestBody CalculationBatchDetailDTO calculationBatchDetailDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        if (calculationBatchDetailDTO.getIds() != null && calculationBatchDetailDTO.getIds().length > 0) {
            calculationBatchDetailService.batchPostponeCalBatchDetail(calculationBatchDetailDTO.getIds());
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
        //验证批次任务0:允许恢复;1:是"取消关账-01"状态;2:已经创建任务
        int i = calculationBatchDetailService.checkTaskByDetailIds(calculationBatchDetailDTO.getIds());
        if(i == 1){
            jr.fill(JsonResult.ReturnCode.RECOVERY_1);
            return jr;
        }
        if(i == 2){
            jr.fill(JsonResult.ReturnCode.RECOVERY_2);
            return jr;
        }
        if (calculationBatchDetailDTO.getIds() != null && calculationBatchDetailDTO.getIds().length > 0) {
            calculationBatchDetailService.recoveryCalculationBatchDetail(calculationBatchDetailDTO.getIds());
        }
        return jr;
    }

}
