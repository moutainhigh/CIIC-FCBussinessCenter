package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TaskSubProofProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
    public JsonResult queryTaskSubProofByMainId(@PathVariable(value = "taskMainProofId") Long taskMainProofId) {
        JsonResult jr = new JsonResult();
        try {
            List<TaskSubProofDTO> taskSubProofDTOLists = new ArrayList<>();
            List<TaskSubProofPO> taskSubProofPOLists = taskSubProofService.queryTaskSubProofByMainId(taskMainProofId);
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOLists) {
                TaskSubProofDTO taskSubProofDTO = new TaskSubProofDTO();
                BeanUtils.copyProperties(taskSubProofPO, taskSubProofDTO);
                taskSubProofDTO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofDTO.getStatus()));
                taskSubProofDTOLists.add(taskSubProofDTO);
            }
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubProofDTOLists);
        } catch (Exception e) {
            logger.error("queryTaskSubProofByMainId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult queryTaskSubProofByRes(@RequestBody TaskSubProofDTO taskSubProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskSubProofDTO, requestForProof);
            //其中管理方名称
            ResponseForSubProof responseForSubProof = taskSubProofService.queryTaskSubProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProof);
        } catch (Exception e) {
            logger.error("queryTaskSubProofByRes error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult copyProofInfoBySubId(@PathVariable(value = "taskSubProofId") Long taskSubProofId) {
        JsonResult jr = new JsonResult();
        try {
            taskSubProofService.copyProofInfoBySubId(taskSubProofId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("copyProofInfoBySubId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult querySubProofInfoByTaskType(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProof responseForSubProof = taskSubProofService.querySubProofInfoByTaskType(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProof);
        } catch (Exception e) {
            logger.error("querySubProofInfoByTaskType error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult combineTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            // TODO 临时设置修改人
            //修改人
            requestForProof.setModifiedBy("adminMain");
            taskSubProofService.combineTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("combineTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult splitTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            // TODO 临时设置修改人
            //修改人
            requestForProof.setModifiedBy("adminMain");
            taskSubProofService.splitTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("splitTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult completeTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            // TODO 临时设置修改人
            //修改人
            requestForProof.setModifiedBy("adminMain");
            //任务状态：00:草稿，01:已提交/处理中，02:被退回，03:已完成，04:已失效
            requestForProof.setStatus("03");
            taskSubProofService.completeTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("completeTaskProof error:" + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult rejectTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            // TODO 临时设置修改人
            //修改人
            requestForProof.setModifiedBy("adminMain");
            //任务状态：00:草稿，01:已提交/处理中，02:被退回，03:已完成，04:已失效
            requestForProof.setStatus("02");
            taskSubProofService.rejectTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("rejectTaskProof error:" + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            // TODO 临时设置修改人
            //修改人
            requestForProof.setModifiedBy("adminMain");
            //任务状态：00:草稿，01:已提交/处理中，02:被退回，03:已完成，04:已失效
            requestForProof.setStatus("04");
            taskSubProofService.invalidTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("invalidTaskProof error:" + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult queryApplyDetailsBySubId(@PathVariable(value = "subProofId") Long subProofId) {
        JsonResult jr = new JsonResult();
        try {
            TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubProofBO);
        } catch (Exception e) {
            logger.error("queryApplyDetailsBySubId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult queryTaskSubProofDetailBySubId(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProofDetail responseForSubProofDetail = taskSubProofService.queryTaskSubProofDetail(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProofDetail);
        } catch (Exception e) {
            logger.error("queryTaskSubProofDetailBySubId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
            // TODO 测试代码："蓝天科技上海独立户"=>"完税凭证_三分局","中智上海财务咨询公司大库"=>"完税凭证_徐汇","蓝天科技无锡独立户"=>"完税凭证_浦东"
            //根据申报账户选择模板
            if ("蓝天科技上海独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_三分局.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                wb = exportFileService.exportAboutSFJ(wb, taskSubProofDetailPOList);
            } else if ("中智上海财务咨询公司大库".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_徐汇.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                wb = exportFileService.exportAboutXH(wb, taskSubProofDetailPOList);
            } else if ("蓝天科技无锡独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_浦东.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                wb = exportFileService.exportAboutPD(wb, taskSubProofDetailPOList);
            }
            //导出新的excel
            exportNewExcel(response, wb, fileName);
        } catch (Exception e) {
            logger.error("exportSubTaskProof error " + e.toString());
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
