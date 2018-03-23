package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept.LoginInfoHolder;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
@RestController
public class TaskSubDeclareController extends BaseController {

    @Autowired
    public TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    public TaskSubDeclareDetailService taskSubDeclareDetailService;

    @Autowired
    private ExportFileService exportFileService;

    /**
     * 查询申报子任务列表
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubDeclares")
    public JsonResult<ResponseForTaskSubDeclare> queryTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<ResponseForTaskSubDeclare> jr = new JsonResult<>();
        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            ResponseForTaskSubDeclare responseForTaskSubDeclare = taskSubDeclareService.queryTaskSubDeclares(requestForTaskSubDeclare);
            jr.fill(responseForTaskSubDeclare);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("declareAccount", taskSubDeclareDTO.getDeclareAccount());
            tags.put("managerName", taskSubDeclareDTO.getManagerName());
            tags.put("statusType", taskSubDeclareDTO.getStatusType());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.queryTaskSubDeclares", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 合并申报任务
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @PostMapping(value = "/mergeTaskSubDeclares")
    public JsonResult<Boolean> mergeTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            requestForTaskSubDeclare.setModifiedBy(userInfoResponseDTO.getLoginName());
            taskSubDeclareService.mergeTaskSubDeclares(requestForTaskSubDeclare);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareIds", taskSubDeclareDTO.getSubDeclareIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.mergeTaskSubDeclares", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 拆分申报子任务
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @PostMapping(value = "splitSubDeclare")
    public JsonResult<Boolean> splitSubDeclare(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            requestForTaskSubDeclare.setModifiedBy(userInfoResponseDTO.getLoginName());
            taskSubDeclareService.splitSubDeclare(requestForTaskSubDeclare, "only");
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskSubDeclareDTO.getId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.splitSubDeclare", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据任务ID,导出报税文件
     *
     * @param subDeclareId
     */
    @RequestMapping(value = "/exportSubDeclare/{subDeclareId}", method = RequestMethod.GET)
    public void exportSubDeclare(@PathVariable(value = "subDeclareId") Long subDeclareId, HttpServletResponse response) {

        String fileName = "扣缴个人所得税报告表.xls";

        HSSFWorkbook wb = this.exportFileService.exportForDeclareOffline(subDeclareId);

        try {
            //导出excel
            exportExcel(response, wb, fileName);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", subDeclareId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.exportSubDeclare", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
        }
    }

    /**
     * 根据申报任务ID查询申报任务信息
     *
     * @param subDeclareId
     * @return
     */
    @PostMapping(value = "/queryDeclareDetailsById/{subDeclareId}")
    public JsonResult<TaskSubDeclarePO> queryDeclareDetailsById(@PathVariable(value = "subDeclareId") Long subDeclareId) {
        JsonResult<TaskSubDeclarePO> jr = new JsonResult<>();
        try {
            //根据申报子任务ID查询申报信息
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
            jr.fill(taskSubDeclarePO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", subDeclareId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.queryDeclareDetailsById", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据任务ID,导出报税文件(所得项目)
     *
     * @param subDeclareId
     */
    @RequestMapping(value = "/exportDeclareBySubject/{subDeclareId}", method = RequestMethod.GET)
    public void exportDeclareBySubject(@PathVariable(value = "subDeclareId") Long subDeclareId, HttpServletResponse response) {
        String fileName = "上海地区个税.xls";
        try {
            //导出excel
            exportExcel(response, this.exportFileService.exportForDeclareOnline(subDeclareId), fileName);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", subDeclareId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.exportDeclareBySubject", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
        }
    }

    /**
     * 批量完成申报任务
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @PostMapping(value = "/completeTaskSubDeclares")
    public JsonResult<Boolean> completeTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            int count = 0;
            if(taskSubDeclareDTO.getHasCombinedDeclareIds().length > 0){
                //根据有合并明细的申报ID查询未确认的数目
                count = taskSubDeclareDetailService.selectCount(taskSubDeclareDTO.getHasCombinedDeclareIds());
            }
            if (count > 0) {
                jr.fill(JsonResult.ReturnCode.DE_ER01);
            }else{
                RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
                BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
                //任务状态
                requestForTaskSubDeclare.setStatus("04");
                taskSubDeclareService.completeTaskSubDeclares(requestForTaskSubDeclare);
            }
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareIds", taskSubDeclareDTO.getSubDeclareIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.completeTaskSubDeclares", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据ID查询合并之前的申报子任务
     *
     * @param mergeId
     * @return
     */
    @PostMapping(value = "/queryTaskSubDeclareByMergeId/{mergeId}")
    public JsonResult<List<TaskSubDeclareDTO>> queryTaskSubDeclareByMergeId(@PathVariable(value = "mergeId") Long mergeId) {
        JsonResult<List<TaskSubDeclareDTO>> jr = new JsonResult<>();
        try {
            List<TaskSubDeclareDTO> taskSubProofDTOLists = new ArrayList<>();
            List<TaskSubDeclarePO> taskSubDeclarePOLists = taskSubDeclareService.queryTaskSubDeclareByMergeId(mergeId);
            for (TaskSubDeclarePO taskSubDeclarePO : taskSubDeclarePOLists) {
                TaskSubDeclareDTO taskSubDeclareDTO = new TaskSubDeclareDTO();
                BeanUtils.copyProperties(taskSubDeclarePO, taskSubDeclareDTO);
                if (taskSubDeclarePO.getPeriod() != null) {
                    taskSubDeclareDTO.setPeriod(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubDeclarePO.getPeriod()));
                }
                taskSubProofDTOLists.add(taskSubDeclareDTO);
            }
            jr.fill(taskSubProofDTOLists);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("mergeId", mergeId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.queryTaskSubDeclareByMergeId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 更新滞纳金、罚金
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @PostMapping(value = "/updateTaskSubDeclare")
    public JsonResult<Boolean> updateTaskSubDeclare(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.selectById(taskSubDeclareDTO.getId());
            taskSubDeclarePO.setOverdue(taskSubDeclareDTO.getOverdue());
            taskSubDeclarePO.setFine(taskSubDeclareDTO.getFine());
            taskSubDeclareService.updateById(taskSubDeclarePO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", taskSubDeclareDTO.getTaskSubDeclareId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.updateTaskSubDeclare", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
}
