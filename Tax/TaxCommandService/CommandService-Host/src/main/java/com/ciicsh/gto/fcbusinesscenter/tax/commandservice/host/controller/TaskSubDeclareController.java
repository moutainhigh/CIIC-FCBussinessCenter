package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
@RestController
public class TaskSubDeclareController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubDeclareController.class);

    @Autowired
    public TaskSubDeclareService taskSubDeclareService;

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
    public JsonResult queryTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            ResponseForTaskSubDeclare responseForTaskSubDeclare = taskSubDeclareService.queryTaskSubDeclares(requestForTaskSubDeclare);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForTaskSubDeclare);
        } catch (Exception e) {
            logger.error("queryTaskSubDeclares error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult mergeTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            // TODO 临时设置修改人
            //修改人
            requestForTaskSubDeclare.setModifiedBy("adminMain");
            taskSubDeclareService.mergeTaskSubDeclares(requestForTaskSubDeclare);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("combineSubDeclare error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
    public JsonResult splitSubDeclare(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            // TODO 临时设置修改人
            //修改人
            requestForTaskSubDeclare.setModifiedBy("adminMain");
            taskSubDeclareService.splitSubDeclare(requestForTaskSubDeclare);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("splitSubDeclare error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //根据申报子任务ID查询申报信息
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
            //根据申报子任务ID查询申报明细
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = taskSubDeclareDetailService.querySubDeclareDetailList(subDeclareId);
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            //文件名称
            String fileName = "";
            if ("中智上海财务咨询公司大库".equals(taskSubDeclarePO.getDeclareAccount())) {
                fileName = "扣缴个人所得税报告表.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName);
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //税款所属期
                map.put("taxPeriod", DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubDeclarePO.getPeriod()));
                //扣缴义务人名称
                map.put("withholdingAgent", "张三");
                //扣缴义务人编码
                map.put("withholdingAgentCode", "147258369");
                //根据不同的业务需要处理wb
                exportFileService.exportAboutTax(wb, map, taskSubDeclareDetailPOList);
            }
            //导出excel
            exportExcel(response, wb, fileName);
        } catch (Exception e) {
            logger.error("exportSubDeclare error " + e.toString());
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("exportSubDeclare wb close error" + e.toString());
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    logger.error("exportSubDeclare fs close error" + e.toString());
                }
            }
        }
    }

    /**
     * 根据申报任务ID查询申报任务信息
     * @param subDeclareId
     * @return
     */
    @PostMapping(value = "/queryDeclareDetailsById/{subDeclareId}")
    public JsonResult queryDeclareDetailsById(@PathVariable(value = "subDeclareId") Long subDeclareId){
        JsonResult jr = new JsonResult();
        try {
            //根据申报子任务ID查询申报信息
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubDeclarePO);
        } catch (Exception e) {
            logger.error("queryDeclareDetailsById error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }
}
