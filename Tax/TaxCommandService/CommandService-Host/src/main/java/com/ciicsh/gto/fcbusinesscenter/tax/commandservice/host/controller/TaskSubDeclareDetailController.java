package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yuantongqing  on 2018/02/08
 */
@RestController
public class TaskSubDeclareDetailController extends BaseController {

    @Autowired
    public TaskSubDeclareDetailServiceImpl taskSubDeclareDetailService;

    /**
     * 查询申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "querySubDeclareDetailsByParams")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsByParams(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();
        try {
            RequestForSubDeclareDetail requestForSubDeclareDetail = new RequestForSubDeclareDetail();
            BeanUtils.copyProperties(taskSubDeclareDetailDTO, requestForSubDeclareDetail);
            ResponseForSubDeclareDetail responseForSubDeclareDetail = taskSubDeclareDetailService.querySubDeclareDetailsByParams(requestForSubDeclareDetail);
            jr.fill(responseForSubDeclareDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", taskSubDeclareDetailDTO.getSubDeclareId().toString());
            tags.put("employeeNo", taskSubDeclareDetailDTO.getEmployeeNo());
            tags.put("employeeName", taskSubDeclareDetailDTO.getEmployeeName());
            tags.put("idType", taskSubDeclareDetailDTO.getIdType());
            tags.put("idNo", taskSubDeclareDetailDTO.getIdNo());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareDetailController.querySubDeclareDetailsByParams", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
    /**
     * 查询合并后的申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "querySubDeclareDetailsByCombined")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsByCombined(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();
        try {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_sub_declare_id={0}",taskSubDeclareDetailDTO.getSubDeclareId());
            wrapper.andNew("is_combined={0}",true);
            Page<TaskSubDeclareDetailPO> pageInfo = new Page<>(taskSubDeclareDetailDTO.getCurrentNum(), taskSubDeclareDetailDTO.getPageSize());
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = new ArrayList<>();
            taskSubDeclareDetailPOList = this.taskSubDeclareDetailService.selectPage(pageInfo, wrapper).getRecords();
            ResponseForSubDeclareDetail responseForSubDeclareDetail = new ResponseForSubDeclareDetail();
            responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
            responseForSubDeclareDetail.setTotalNum(pageInfo.getTotal());
            responseForSubDeclareDetail.setCurrentNum(taskSubDeclareDetailDTO.getCurrentNum());
            responseForSubDeclareDetail.setPageSize(taskSubDeclareDetailDTO.getPageSize());
            jr.fill(responseForSubDeclareDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", taskSubDeclareDetailDTO.getSubDeclareId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareDetailController.querySubDeclareDetailsByCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
    /**
     * 查询被合并申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "querySubDeclareDetailsForCombined")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsForCombined(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();
        try {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_sub_declare_detail_id={0}",taskSubDeclareDetailDTO.getTaskSubDeclareDetailId());
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = new ArrayList<>();
            taskSubDeclareDetailPOList = this.taskSubDeclareDetailService.selectList(wrapper);
            ResponseForSubDeclareDetail responseForSubDeclareDetail = new ResponseForSubDeclareDetail();
            responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
            jr.fill(responseForSubDeclareDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subDeclareId", taskSubDeclareDetailDTO.getSubDeclareId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareDetailController.querySubDeclareDetailsForCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "02"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 合并明细确认
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @RequestMapping(value = "/confirmTaskSubDeclareDetailforCombined")
    public JsonResult<Boolean> confirmTaskSubDeclareDetailforCombined(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.in("id",taskSubDeclareDetailDTO.getTaskSubDeclareDetailIds());
            TaskSubDeclareDetailPO tpo = new TaskSubDeclareDetailPO();
            tpo.setCombineConfirmed(true);
            this.taskSubDeclareDetailService.update(tpo,wrapper);//更新合并的明细
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskSubDeclareDetailIds", taskSubDeclareDetailDTO.getTaskSubDeclareDetailIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareDetailController.confirmTaskSubDeclareDetailforCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 合并明细调整
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @RequestMapping(value = "/unconfirmTaskSubDeclareDetailforCombined")
    public JsonResult<Boolean> unconfirmTaskSubDeclareDetailforCombined(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.in("id",taskSubDeclareDetailDTO.getTaskSubDeclareDetailIds());
            TaskSubDeclareDetailPO tsddp = new TaskSubDeclareDetailPO();
            tsddp.setCombineConfirmed(false);
            this.taskSubDeclareDetailService.update(tsddp,wrapper);//更新合并的明细
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskSubDeclareDetailIds", taskSubDeclareDetailDTO.getTaskSubDeclareDetailIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubDeclareDetailController.unconfirmTaskSubDeclareDetailforCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

}
