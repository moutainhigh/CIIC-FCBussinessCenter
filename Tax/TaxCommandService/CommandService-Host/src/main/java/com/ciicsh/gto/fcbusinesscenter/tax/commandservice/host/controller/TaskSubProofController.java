package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.basicdataservice.api.CityServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CityDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TaskSubProofProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchAccountService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchAccountPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuantongqing on 2017/12/12
 */
@RestController
public class TaskSubProofController extends BaseController implements TaskSubProofProxy {

    @Autowired
    private TaskSubProofService taskSubProofService;

    @Autowired
    private ExportFileService exportFileService;

    @Autowired
    private CalculationBatchAccountService calculationBatchAccountService;

    @Autowired
    private CityServiceProxy cityServiceProxy;

    /**
     * 根据主任务ID查询子任务
     *
     * @param taskMainProofId
     * @return
     */
    @Override
    public JsonResult<List<TaskSubProofDTO>> queryTaskSubProofByMainId(@PathVariable(value = "taskMainProofId") Long taskMainProofId) {
        JsonResult<List<TaskSubProofDTO>> jr = new JsonResult<>();

        List<TaskSubProofDTO> taskSubProofDTOLists = new ArrayList<>();
        List<TaskSubProofPO> taskSubProofPOLists = taskSubProofService.queryTaskSubProofByMainId(taskMainProofId);
        for (TaskSubProofPO taskSubProofPO : taskSubProofPOLists) {
            TaskSubProofDTO taskSubProofDTO = new TaskSubProofDTO();
            BeanUtils.copyProperties(taskSubProofPO, taskSubProofDTO);
            taskSubProofDTO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofDTO.getStatus()));
            CalculationBatchAccountPO calculationBatchAccountPO = calculationBatchAccountService.getCalculationBatchAccountInfoByAccountNo(taskSubProofPO.getDeclareAccount());
            if(calculationBatchAccountPO.getId() != null){
                //设置城市
                CityDTO cityDTO = cityServiceProxy.selectByCityCode(calculationBatchAccountPO.getCityCode());
                taskSubProofDTO.setCity(cityDTO.getCityName());
                //设置税务局
                taskSubProofDTO.setTaxOrganization(calculationBatchAccountPO.getStation());
            }
            taskSubProofDTOLists.add(taskSubProofDTO);
        }
        jr.fill(taskSubProofDTOLists);

        return jr;
    }

    /**
     * 查询子任务信息
     *
     * @param taskSubProofDTO
     * @return
     */
    @Override
    public JsonResult<ResponseForSubProof> queryTaskSubProofByRes(@RequestBody TaskSubProofDTO taskSubProofDTO) {
        JsonResult<ResponseForSubProof> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskSubProofDTO, requestForProof);
        //其中管理方名称
        ResponseForSubProof responseForSubProof = taskSubProofService.queryTaskSubProofByRes(requestForProof);
        jr.fill(responseForSubProof);

        return jr;
    }

    /**
     * 根据子任务ID复制其关联数据
     *
     * @param taskSubProofId
     * @return
     */
    @Override
    public JsonResult<Boolean> copyProofInfoBySubId(@PathVariable(value = "taskSubProofId") Long taskSubProofId) {
        JsonResult<Boolean> jr = new JsonResult<>();

        //登录信息
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        taskSubProofService.copyProofInfoBySubId(taskSubProofId, userInfoResponseDTO.getLoginName());

        return jr;
    }

    /**
     * 多表查询完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @GetMapping(value = "/querySubProofInfoByTaskType")
    public JsonResult<ResponseForSubProof> querySubProofInfoByTaskType(TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubProof> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方名称数组
            requestForProof.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForSubProof responseForSubProof = taskSubProofService.querySubProofInfoByTaskType(requestForProof);
        jr.fill(responseForSubProof);

        return jr;
    }

    /**
     * 合并完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/combineSubTaskProof")
    public JsonResult<Boolean> combineTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        taskSubProofService.combineTaskProofByRes(requestForProof);

        return jr;
    }

    /**
     * 拆分合并的任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/splitSubTaskProof")
    public JsonResult<Boolean> splitTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        taskSubProofService.splitTaskProofByRes(requestForProof);

        return jr;
    }

    /**
     * 批量完成完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/completeSubTaskProof")
    public JsonResult<Boolean> completeTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForProof.setModifiedBy(userInfoResponseDTO.getLoginName());
        //任务状态：00:草稿，01:已提交/处理中，02:通过，03:退回，04:已完成，05:已失效
        requestForProof.setStatus("04");
        taskSubProofService.completeTaskProofByRes(requestForProof);

        return jr;
    }

    /**
     * 批量退回完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/rejectSubTaskProof")
    public JsonResult<Boolean> rejectTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForProof.setModifiedBy(userInfoResponseDTO.getLoginName());
        //任务状态：00:草稿，01:已提交/处理中，02:通过,03:被退回，04:已完成，05:已失效
        requestForProof.setStatus("03");
        taskSubProofService.rejectTaskProofByRes(requestForProof);

        return jr;
    }

    /**
     * 批量失效完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/invalidSubTaskProof")
    public JsonResult<Boolean> invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForProof.setModifiedBy(userInfoResponseDTO.getLoginName());
        //任务状态：00:草稿，01:已提交/处理中，02:通过,03:被退回，04:已完成，05:已失效
        requestForProof.setStatus("05");
        taskSubProofService.invalidTaskProofByRes(requestForProof);

        return jr;
    }


    /**
     * 根据子任务ID查询子任务详细信息
     *
     * @param subProofId
     * @return
     */
    @GetMapping(value = "/queryApplyDetailsBySubId/{subProofId}")
    public JsonResult<TaskSubProofBO> queryApplyDetailsBySubId(@PathVariable(value = "subProofId") Long subProofId) {
        JsonResult<TaskSubProofBO> jr = new JsonResult<>();

        TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
        jr.fill(taskSubProofBO);

        return jr;
    }

    /**
     * 根据子任务ID分页查询完税凭证子任务申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @GetMapping(value = "/queryTaskSubProofDetailBySubId")
    public JsonResult<ResponseForSubProofDetail> queryTaskSubProofDetailBySubId(TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubProofDetail> jr = new JsonResult<>();

        RequestForProof requestForProof = new RequestForProof();
        BeanUtils.copyProperties(taskProofDTO, requestForProof);
        ResponseForSubProofDetail responseForSubProofDetail = taskSubProofService.queryTaskSubProofDetail(requestForProof);
        jr.fill(responseForSubProofDetail);

        return jr;
    }

    /**
     * 根据任务ID,导出完税凭证申请单
     *
     * @param subProofId
     */
    @RequestMapping(value = "/exportSubTaskProof/{subProofId}", method = RequestMethod.GET)
    public void exportSubTaskProof(@PathVariable(value = "subProofId") Long subProofId, HttpServletResponse response) {

        String fileName = "完税凭证申请.xls";

        //导出excel
        exportExcel(response, this.exportFileService.exportForProof(subProofId), fileName);
    }


}
