package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuantongqing  on 2018/02/09
 */
@RestController
public class TaskSubSupplierController extends BaseController {

    @Autowired
    private TaskSubSupplierService taskSubSupplierService;

    @Autowired
    private TaskSubSupplierDetailService taskSubSupplierDetailService;

    /**
     * 查询供应商子任务列表
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @GetMapping(value = "/queryTaskSubSupplier")
    public JsonResult<ResponseForTaskSubSupplier> queryTaskSubSupplier(TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<ResponseForTaskSubSupplier> jr = new JsonResult<>();

        RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
        BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方数组
            requestForTaskSubSupplier.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForTaskSubSupplier responseForTaskSubSupplier = taskSubSupplierService.queryTaskSubSupplier(requestForTaskSubSupplier);
        jr.fill(responseForTaskSubSupplier);

        return jr;
    }


    /**
     * 根据供应商子任务ID查询供应商信息
     *
     * @param subSupplierId
     * @return
     */
    @GetMapping(value = "/querySupplierDetailsById/{subSupplierId}")
    public JsonResult<TaskSubSupplierPO> querySupplierDetailsById(@PathVariable(value = "subSupplierId") Long subSupplierId) {
        JsonResult<TaskSubSupplierPO> jr = new JsonResult();

        //根据供应商子任务ID查询供应商信息
        TaskSubSupplierPO taskSubSupplierPO = taskSubSupplierService.querySupplierDetailsById(subSupplierId);
        jr.fill(taskSubSupplierPO);

        return jr;
    }

    /**
     * 合并全国委托供应商任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/mergeTaskSubSuppliers")
    public JsonResult<Boolean> mergeTaskSubSuppliers(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
        BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForTaskSubSupplier.setModifiedBy(userInfoResponseDTO.getLoginName());
        taskSubSupplierService.mergeTaskSubSuppliers(requestForTaskSubSupplier);

        return jr;
    }

    /**
     * 拆分供应商子任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "splitSubSupplier")
    public JsonResult<Boolean> splitSubSupplier(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
        BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForTaskSubSupplier.setModifiedBy(userInfoResponseDTO.getLoginName());
        taskSubSupplierService.splitSubSupplier(requestForTaskSubSupplier, "only");

        return jr;
    }

    /**
     * 根据ID查询合并之前的供应商子任务
     *
     * @param mergeId
     * @return
     */
    @GetMapping(value = "/queryTaskSubSupplierByMergeId/{mergeId}")
    public JsonResult<List<TaskSubSupplierDTO>> queryTaskSubSupplierByMergeId(@PathVariable(value = "mergeId") Long mergeId) {
        JsonResult<List<TaskSubSupplierDTO>> jr = new JsonResult<>();

        List<TaskSubSupplierDTO> taskSubSupplierDTOList = new ArrayList<>();
        List<TaskSubSupplierPO> taskSubSupplierPOList = taskSubSupplierService.queryTaskSubSupplierByMergeId(mergeId);
        for (TaskSubSupplierPO taskSubSupplierPO : taskSubSupplierPOList) {
            TaskSubSupplierDTO taskSubSupplierDTO = new TaskSubSupplierDTO();
            BeanUtils.copyProperties(taskSubSupplierPO, taskSubSupplierDTO);
            if (taskSubSupplierPO.getPeriod() != null) {
                taskSubSupplierDTO.setPeriod(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubSupplierPO.getPeriod()));
            }
            taskSubSupplierDTOList.add(taskSubSupplierDTO);
        }
        jr.fill(taskSubSupplierDTOList);

        return jr;
    }

    /**
     * 批量完成供应商任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/completeTaskSuppliers")
    public JsonResult<Boolean> completeTaskSuppliers(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        int count = 0;
        if(taskSubSupplierDTO.getSubHasCombinedSupplierIds().length > 0){
            //根据有合并明细的供应商ID查询未确认的数目
            count = taskSubSupplierDetailService.selectCount(taskSubSupplierDTO.getSubHasCombinedSupplierIds());
        }
        if (count > 0) {
            jr.fill(JsonResult.ReturnCode.SU_ER01);
        }else{
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            requestForTaskSubSupplier.setModifiedBy(userInfoResponseDTO.getLoginName());
            //任务状态
            requestForTaskSubSupplier.setStatus("04");
            taskSubSupplierService.completeTaskSubSupplier(requestForTaskSubSupplier);
        }

        return jr;
    }


    /**
     * 批量退回供应商任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/rejectTaskSuppliers")
    public JsonResult<Boolean> rejectTaskSuppliers(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
        BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
        //任务状态
        requestForTaskSubSupplier.setStatus("03");
        Boolean flag = taskSubSupplierService.rejectTaskSuppliers(requestForTaskSubSupplier);
        if(!flag){
            jr.fill(JsonResult.ReturnCode.BILLCENTER_2);
        }
        return jr;
    }

    /**
     * 更新滞纳金、罚金
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/updateTaskSubSupplier")
    public JsonResult<Boolean> updateTaskSubSupplier(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        taskSubSupplierService.updateTaskSubSupplierOverdueAndFine(taskSubSupplierDTO.getId(),taskSubSupplierDTO.getOverdue(),taskSubSupplierDTO.getFine());
        return jr;
    }
}
