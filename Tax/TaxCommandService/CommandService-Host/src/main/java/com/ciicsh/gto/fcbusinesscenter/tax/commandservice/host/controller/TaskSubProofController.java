package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TaskSubProofProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept.LoginInfoHolder;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuantongqing on 2017/12/12
 */
@RestController
public class TaskSubProofController extends BaseController implements TaskSubProofProxy {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubProofController.class);

    @Autowired
    private TaskSubProofService taskSubProofService;

    @Autowired
    private ExportFileService exportFileService;

    /**
     * 根据主任务ID查询子任务
     *
     * @param taskMainProofId
     * @return
     */
    @Override
    public JsonResult<List<TaskSubProofDTO>> queryTaskSubProofByMainId(@PathVariable(value = "taskMainProofId") Long taskMainProofId) {
        JsonResult<List<TaskSubProofDTO>> jr = new JsonResult<>();
        try {
            List<TaskSubProofDTO> taskSubProofDTOLists = new ArrayList<>();
            List<TaskSubProofPO> taskSubProofPOLists = taskSubProofService.queryTaskSubProofByMainId(taskMainProofId);
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOLists) {
                TaskSubProofDTO taskSubProofDTO = new TaskSubProofDTO();
                BeanUtils.copyProperties(taskSubProofPO, taskSubProofDTO);
                taskSubProofDTO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofDTO.getStatus()));
                taskSubProofDTOLists.add(taskSubProofDTO);
            }
            jr.fill(taskSubProofDTOLists);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("mainProofIds", taskMainProofId.toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.queryTaskSubProofByMainId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskSubProofDTO, requestForProof);
            //其中管理方名称
            ResponseForSubProof responseForSubProof = taskSubProofService.queryTaskSubProofByRes(requestForProof);
            jr.fill(responseForSubProof);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskSubProofDTO.getId().toString());
            tags.put("declareAccount", taskSubProofDTO.getDeclareAccount());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.queryTaskSubProofByRes", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            //登录信息
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            taskSubProofService.copyProofInfoBySubId(taskSubProofId, userInfoResponseDTO.getLoginName());
            jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskSubProofId", taskSubProofId.toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.copyProofInfoBySubId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 多表查询完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/querySubProofInfoByTaskType")
    public JsonResult<ResponseForSubProof> querySubProofInfoByTaskType(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubProof> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProof responseForSubProof = taskSubProofService.querySubProofInfoByTaskType(requestForProof);
            jr.fill(responseForSubProof);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskProofDTO.getManagerName());
            tags.put("declareAccount", taskProofDTO.getDeclareAccount());
            tags.put("status", taskProofDTO.getStatus());
            tags.put("period", taskProofDTO.getPeriod());
            tags.put("taskType", taskProofDTO.getTaskType());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.querySubProofInfoByTaskType", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskSubProofService.combineTaskProofByRes(requestForProof);
            jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getManagerName());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.combineTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskSubProofService.splitTaskProofByRes(requestForProof);
            jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskProofDTO.getId().toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.splitTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //任务状态：00:草稿，01:已提交/处理中，02:通过，03:退回，04:已完成，05:已失效
            requestForProof.setStatus("04");
            taskSubProofService.completeTaskProofByRes(requestForProof);
            jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getSubProofIds().toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.completeTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //任务状态：00:草稿，01:已提交/处理中，02:通过,03:被退回，04:已完成，05:已失效
            requestForProof.setStatus("03");
            taskSubProofService.rejectTaskProofByRes(requestForProof);
            jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getSubProofIds().toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.rejectTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
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
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //任务状态：00:草稿，01:已提交/处理中，02:通过,03:被退回，04:已完成，05:已失效
            requestForProof.setStatus("05");
            taskSubProofService.invalidTaskProofByRes(requestForProof);
            jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getSubProofIds().toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.invalidTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 根据子任务ID查询子任务详细信息
     *
     * @param subProofId
     * @return
     */
    @PostMapping(value = "/queryApplyDetailsBySubId/{subProofId}")
    public JsonResult<TaskSubProofBO> queryApplyDetailsBySubId(@PathVariable(value = "subProofId") Long subProofId) {
        JsonResult<TaskSubProofBO> jr = new JsonResult<>();
        try {
            TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
            jr.fill(taskSubProofBO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofId", subProofId.toString());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.queryApplyDetailsBySubId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据子任务ID分页查询完税凭证子任务申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubProofDetailBySubId")
    public JsonResult<ResponseForSubProofDetail> queryTaskSubProofDetailBySubId(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubProofDetail> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProofDetail responseForSubProofDetail = taskSubProofService.queryTaskSubProofDetail(requestForProof);
            jr.fill(responseForSubProofDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskProofDTO.getId().toString());
            tags.put("employeeNo", taskProofDTO.getEmployeeNo());
            tags.put("employeeName", taskProofDTO.getEmployeeName());
            //日志工具类返回
            logService.error(e, "TaskSubProofController.queryTaskSubProofDetailBySubId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据任务ID,导出完税凭证申请单
     *
     * @param subProofId
     */
    @RequestMapping(value = "/exportSubTaskProof/{subProofId}", method = RequestMethod.GET)
    public void exportSubTaskProof(@PathVariable(value = "subProofId") Long subProofId, HttpServletResponse response) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //根据完税凭证子任务查询任务信息
            TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
            //根据完税凭证子任务ID查询完税凭证详情
            List<TaskSubProofDetailPO> taskSubProofDetailPOList = taskSubProofService.querySubProofDetailList(subProofId);
            //文件名称
            String fileName = "";
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            // TODO 测试代码："蓝天科技上海独立户"=>"完税凭证_三分局","中智上海财务咨询公司大库"=>"完税凭证_徐汇","蓝天科技无锡独立户"=>"完税凭证_浦东"
            //根据申报账户选择模板
            if ("联想独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_三分局.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //扣缴义务人名称
                map.put("withholdingAgent", "上海中智");
                //扣缴义务人代码(税务电脑编码)
                map.put("withholdingAgentCode", "BM123456789");
                //扣缴义务人电话
                map.put("withholdingAgentPhone", "18201880000");
                //换开人姓名
                map.put("changePersonName", "admin");
                //换开人身份证号码
                map.put("changePersonIdNo", "321281199001011234");
                //根据不同的业务需要处理wb
                exportFileService.exportAboutSFJ(wb, map, taskSubProofDetailPOList);
            } else if ("西门子独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_徐汇.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //单位税号（必填）
                map.put("unitNumber", "TEST123456");
                //单位名称（必填）
                map.put("unitName", "上海中智");
                //根据不同的业务需要处理wb
                exportFileService.exportAboutXH(wb, map, taskSubProofDetailPOList);
            } else if ("蓝天科技独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_浦东.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //扣缴单位
                map.put("withholdingUnit", "上海中智");
                //电脑编码
                map.put("withholdingCode", "123456789");
                //通用缴款书流水号
                map.put("generalPaymentBook", "147258369");
                //办税人员
                map.put("taxationPersonnel", "admin");
                //联系电话
                map.put("phone", "18201886666");
                //换开份数
                map.put("changeNum", "2");
                //换开原因
                map.put("changeReason", "重新申报");
                //根据不同的业务需要处理wb
                exportFileService.exportAboutPD(wb, map, taskSubProofDetailPOList);
            }
            //导出excel
            exportExcel(response, wb, fileName);
        } catch (Exception e) {
            //日志工具类返回
            logService.error(e, "TaskSubProofController.exportSubTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, null);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("exportSubTaskProof wb close error" + e.toString());
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    logger.error("exportSubTaskProof fs close error" + e.toString());
                }
            }
        }
    }


}
