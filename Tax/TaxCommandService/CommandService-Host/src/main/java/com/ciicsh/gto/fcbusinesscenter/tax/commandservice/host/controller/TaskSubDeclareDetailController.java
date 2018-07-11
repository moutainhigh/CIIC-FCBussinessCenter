package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    @GetMapping(value = "querySubDeclareDetailsByParams")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsByParams(TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();

        RequestForSubDeclareDetail requestForSubDeclareDetail = new RequestForSubDeclareDetail();
        BeanUtils.copyProperties(taskSubDeclareDetailDTO, requestForSubDeclareDetail);
        ResponseForSubDeclareDetail responseForSubDeclareDetail = taskSubDeclareDetailService.querySubDeclareDetailsByParams(requestForSubDeclareDetail);
        jr.fill(responseForSubDeclareDetail);

        return jr;
    }
    /**
     * 查询合并后的申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @GetMapping(value = "querySubDeclareDetailsByCombined")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsByCombined(TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_sub_declare_id={0}",taskSubDeclareDetailDTO.getSubDeclareId());
        wrapper.andNew("is_combined={0}",true);
        if(StrKit.isNotEmpty(taskSubDeclareDetailDTO.getEmployeeNo())){
            wrapper.like("employee_no",taskSubDeclareDetailDTO.getEmployeeNo());
        }
        if(StrKit.isNotEmpty(taskSubDeclareDetailDTO.getEmployeeName())){
            wrapper.like("employee_name",taskSubDeclareDetailDTO.getEmployeeName());
        }
        Page<TaskSubDeclareDetailPO> pageInfo = new Page<>(taskSubDeclareDetailDTO.getCurrentNum(), taskSubDeclareDetailDTO.getPageSize());
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = new ArrayList<>();
        taskSubDeclareDetailPOList = this.taskSubDeclareDetailService.selectPage(pageInfo, wrapper).getRecords();
        ResponseForSubDeclareDetail responseForSubDeclareDetail = new ResponseForSubDeclareDetail();
        responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
        responseForSubDeclareDetail.setTotalNum(pageInfo.getTotal());
        responseForSubDeclareDetail.setCurrentNum(taskSubDeclareDetailDTO.getCurrentNum());
        responseForSubDeclareDetail.setPageSize(taskSubDeclareDetailDTO.getPageSize());
        jr.fill(responseForSubDeclareDetail);

        return jr;
    }
    /**
     * 查询被合并申报明细
     *
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @GetMapping(value = "querySubDeclareDetailsForCombined")
    public JsonResult<ResponseForSubDeclareDetail> querySubDeclareDetailsForCombined(TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {
        JsonResult<ResponseForSubDeclareDetail> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_sub_declare_detail_id={0}",taskSubDeclareDetailDTO.getTaskSubDeclareDetailId());
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = new ArrayList<>();
        taskSubDeclareDetailPOList = this.taskSubDeclareDetailService.selectList(wrapper);
        ResponseForSubDeclareDetail responseForSubDeclareDetail = new ResponseForSubDeclareDetail();
        responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
        jr.fill(responseForSubDeclareDetail);

        return jr;
    }

    /**
     * 合并明细确认
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "/confirmTaskSubDeclareDetailforCombined")
    public JsonResult<Boolean> confirmTaskSubDeclareDetailforCombined(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskSubDeclareDetailDTO.getTaskSubDeclareDetailIds());
        TaskSubDeclareDetailPO tpo = new TaskSubDeclareDetailPO();
        tpo.setCombineConfirmed(true);
        this.taskSubDeclareDetailService.update(tpo,wrapper);//更新合并的明细

        return jr;
    }

    /**
     * 合并明细调整
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @PostMapping(value = "/unconfirmTaskSubDeclareDetailforCombined")
    public JsonResult<Boolean> unconfirmTaskSubDeclareDetailforCombined(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskSubDeclareDetailDTO.getTaskSubDeclareDetailIds());
        TaskSubDeclareDetailPO tsddp = new TaskSubDeclareDetailPO();
        tsddp.setCombineConfirmed(false);
        this.taskSubDeclareDetailService.update(tsddp,wrapper);//更新合并的明细

        return jr;
    }

    /**
     * 更新申报子任务明细
     * @param taskSubDeclareDetailDTO
     * @return
     */
    @RequestMapping(value = "/updateSubDeclareDetail")
    public JsonResult<Boolean> updateSubDeclareDetail(@RequestBody TaskSubDeclareDetailDTO taskSubDeclareDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("id={0}",taskSubDeclareDetailDTO.getTaskSubDeclareDetailId());
        TaskSubDeclareDetailPO tsddp = new TaskSubDeclareDetailPO();
        tsddp.setDeductRetirementInsurance(taskSubDeclareDetailDTO.getDeductRetirementInsurance());
        tsddp.setDeductMedicalInsurance(taskSubDeclareDetailDTO.getDeductMedicalInsurance());
        tsddp.setDeductDlenessInsurance(taskSubDeclareDetailDTO.getDeductDlenessInsurance());
        tsddp.setDeductHouseFund(taskSubDeclareDetailDTO.getDeductHouseFund());
        tsddp.setIncomeTotal(taskSubDeclareDetailDTO.getIncomeTotal());
        tsddp.setTaxRate(taskSubDeclareDetailDTO.getTaxRate());
        tsddp.setQuickCalDeduct(taskSubDeclareDetailDTO.getQuickCalDeduct());
        tsddp.setDeductTotal(taskSubDeclareDetailDTO.getDeductTotal());
        this.taskSubDeclareDetailService.update(tsddp,wrapper);//更新明细

        return jr;
    }

}
