package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.basicdataservice.api.CountryServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareServiceImpl;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private CountryServiceProxy countryServiceProxy;

    /**
     * 查询申报子任务列表
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @GetMapping(value = "/queryTaskSubDeclares")
    public JsonResult<ResponseForTaskSubDeclare> queryTaskSubDeclares(TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<ResponseForTaskSubDeclare> jr = new JsonResult<>();

        RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
        BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方名称数组
            requestForTaskSubDeclare.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForTaskSubDeclare responseForTaskSubDeclare = taskSubDeclareService.queryTaskSubDeclares(requestForTaskSubDeclare);
        jr.fill(responseForTaskSubDeclare);
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

        RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
        BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForTaskSubDeclare.setModifiedBy(userInfoResponseDTO.getLoginName());
        taskSubDeclareService.mergeTaskSubDeclares(requestForTaskSubDeclare);

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

        RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
        BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForTaskSubDeclare.setModifiedBy(userInfoResponseDTO.getLoginName());
        taskSubDeclareService.splitSubDeclare(requestForTaskSubDeclare, "only");

        return jr;
    }

    /**
     * 根据任务ID,导出报税文件
     *
     * @param subDeclareId
     */
    @RequestMapping(value = "/exportSubDeclare/{subDeclareId}", method = RequestMethod.GET)
    public void exportSubDeclare(@PathVariable(value = "subDeclareId") Long subDeclareId, HttpServletResponse response) {
        Map<String,Object> map = this.exportFileService.exportForDeclareOffline(subDeclareId);
        //导出excel
        exportExcel(response, (HSSFWorkbook)map.get("wb"), map.get("fileName").toString());
    }

    /**
     * 根据申报任务ID查询申报任务信息
     *
     * @param subDeclareId
     * @return
     */
    @GetMapping(value = "/queryDeclareDetailsById/{subDeclareId}")
    public JsonResult<TaskSubDeclarePO> queryDeclareDetailsById(@PathVariable(value = "subDeclareId") Long subDeclareId) {
        JsonResult<TaskSubDeclarePO> jr = new JsonResult<>();

        //根据申报子任务ID查询申报信息
        TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
        jr.fill(taskSubDeclarePO);

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

        //导出excel
        exportExcel(response, this.exportFileService.exportForDeclareOnline(subDeclareId), fileName);
    }

    /**
     * 根据任务ID,导出线上文件
     *
     * @param subDeclareId
     */
    @RequestMapping(value = "/exportDeclareOnLine/{subDeclareId}", method = RequestMethod.GET)
    public void exportDeclareUnderLine(@PathVariable(value = "subDeclareId") Long subDeclareId, HttpServletResponse response) {
        String tempFileName = "tempFile";
        String dateStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
//        String compressedFileName = "上海地区个税模板对应关系" + dateStr + ".zip";
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        String dateStrFolder = dateStr + "_" + userInfoResponseDTO.getLoginName();
        //导出压缩文件
        try {
//            downloadFile(response, compressedFileName, this.exportFileService.getCompressedFileByte(subDeclareId, tempFileName, dateStrFolder));
            Map<String,Object> map = this.exportFileService.getCompressedFileByte(subDeclareId, tempFileName, dateStrFolder);
            downloadFile(response, map.get("zipName").toString(), (byte[])map.get("byte"));
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareController.exportDeclareUnderLine", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
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

        int count = 0;
        if (taskSubDeclareDTO.getHasCombinedDeclareIds().length > 0) {
            //根据有合并明细的申报ID查询未确认的数目
            count = taskSubDeclareDetailService.selectCount(taskSubDeclareDTO.getHasCombinedDeclareIds());
        }
        if (count > 0) {
            jr.fill(JsonResult.ReturnCode.DE_ER01);
        } else {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            requestForTaskSubDeclare.setModifiedBy(userInfoResponseDTO.getLoginName());
            //任务状态
            requestForTaskSubDeclare.setStatus("04");
            taskSubDeclareService.completeTaskSubDeclares(requestForTaskSubDeclare);
        }

        return jr;
    }

    /**
     * 批量退回申报任务
     *
     * @param taskSubDeclareDTO
     * @return
     */
    @PostMapping(value = "/rejectTaskSubDeclares")
    public JsonResult<Boolean> rejectTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
        BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
        //修改人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        requestForTaskSubDeclare.setModifiedBy(userInfoResponseDTO.getLoginName());
        //任务状态
        requestForTaskSubDeclare.setStatus("03");
        taskSubDeclareService.rejectTaskSubDeclares(requestForTaskSubDeclare);

        return jr;
    }

    /**
     * 根据ID查询合并之前的申报子任务
     *
     * @param mergeId
     * @return
     */
    @GetMapping(value = "/queryTaskSubDeclareByMergeId/{mergeId}")
    public JsonResult<List<TaskSubDeclareDTO>> queryTaskSubDeclareByMergeId(@PathVariable(value = "mergeId") Long mergeId) {
        JsonResult<List<TaskSubDeclareDTO>> jr = new JsonResult<>();

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

        TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.selectById(taskSubDeclareDTO.getId());
        taskSubDeclarePO.setOverdue(taskSubDeclareDTO.getOverdue());
        taskSubDeclarePO.setFine(taskSubDeclareDTO.getFine());
        taskSubDeclareService.updateById(taskSubDeclarePO);

        return jr;
    }

    /**
     * 导出离职人员
     *
     * @param subDeclareId
     */
    @RequestMapping(value = "/exportQuitPerson/{subDeclareId}", method = RequestMethod.GET)
    public void exportQuitPerson(@PathVariable(value = "subDeclareId") Long subDeclareId, HttpServletResponse response) {
        String fileName = "人员信息-离职.xls";
        //导出excel
        exportExcel(response, this.exportFileService.exportQuitPerson(subDeclareId), fileName);
    }
}
