package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubSupplierDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing  on 2018/02/11
 */
@RestController
public class TaskSubSupplierDetailController extends BaseController {

    @Autowired
    private TaskSubSupplierDetailServiceImpl taskSubSupplierDetailService;

    /**
     * 查询供应商明细
     *
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @PostMapping(value = "querySubSupplierDetailsByParams")
    public JsonResult<ResponseForSubSupplierDetail> querySubSupplierDetailsByParams(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {
        JsonResult<ResponseForSubSupplierDetail> jr = new JsonResult<>();

        RequestForSubSupplierDetail requestForSubSupplierDetail = new RequestForSubSupplierDetail();
        BeanUtils.copyProperties(taskSubSupplierDetailDTO, requestForSubSupplierDetail);
        ResponseForSubSupplierDetail responseForSubSupplierDetail = taskSubSupplierDetailService.querySubSupplierDetailsByParams(requestForSubSupplierDetail);
        jr.fill(responseForSubSupplierDetail);

        return jr;
    }

    /**
     * 查询合并后的供应商明细
     *
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @PostMapping(value = "querySubSupplierDetailsByCombined")
    public JsonResult<ResponseForSubSupplierDetail> querySubSupplierDetailsByCombined(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {
        JsonResult<ResponseForSubSupplierDetail> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_sub_supplier_id={0}",taskSubSupplierDetailDTO.getSubSupplierId());
        wrapper.andNew("is_combined={0}",true);
        if(StrKit.isNotEmpty(taskSubSupplierDetailDTO.getEmployeeNo())){
            wrapper.like("employee_no",taskSubSupplierDetailDTO.getEmployeeNo());
        }
        if(StrKit.isNotEmpty(taskSubSupplierDetailDTO.getEmployeeName())){
            wrapper.like("employee_name",taskSubSupplierDetailDTO.getEmployeeName());
        }
        Page<TaskSubSupplierDetailPO> pageInfo = new Page<>(taskSubSupplierDetailDTO.getCurrentNum(), taskSubSupplierDetailDTO.getPageSize());
        List<TaskSubSupplierDetailPO> taskSubSupplierDetailPOList = new ArrayList<>();
        taskSubSupplierDetailPOList = this.taskSubSupplierDetailService.selectPage(pageInfo, wrapper).getRecords();
        ResponseForSubSupplierDetail responseForSubSupplierDetail = new ResponseForSubSupplierDetail();
        responseForSubSupplierDetail.setRowList(taskSubSupplierDetailPOList);
        responseForSubSupplierDetail.setTotalNum(pageInfo.getTotal());
        responseForSubSupplierDetail.setCurrentNum(taskSubSupplierDetailDTO.getCurrentNum());
        responseForSubSupplierDetail.setPageSize(taskSubSupplierDetailDTO.getPageSize());
        jr.fill(responseForSubSupplierDetail);

        return jr;
    }


    /**
     * 查询被合并供应商明细
     *
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @PostMapping(value = "querySubSupplierDetailsForCombined")
    public JsonResult<ResponseForSubSupplierDetail> querySubSupplierDetailsForCombined(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {
        JsonResult<ResponseForSubSupplierDetail> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_sub_supplier_detail_id={0}",taskSubSupplierDetailDTO.getTaskSubSupplierDetailId());
        List<TaskSubSupplierDetailPO> taskSubSupplierDetailPOList = new ArrayList<>();
        taskSubSupplierDetailPOList = this.taskSubSupplierDetailService.selectList(wrapper);
        ResponseForSubSupplierDetail responseForSubSupplierDetail = new ResponseForSubSupplierDetail();
        responseForSubSupplierDetail.setRowList(taskSubSupplierDetailPOList);
        jr.fill(responseForSubSupplierDetail);

        return jr;
    }

    /**
     * 合并明细确认
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @RequestMapping(value = "/confirmTaskSubSupplierDetailforCombined")
    public JsonResult<Boolean> confirmTaskSubSupplierDetailforCombined(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskSubSupplierDetailDTO.getTaskSubSupplierDetailIds());
        TaskSubSupplierDetailPO tpo = new TaskSubSupplierDetailPO();
        tpo.setCombineConfirmed(true);
        this.taskSubSupplierDetailService.update(tpo,wrapper);//更新合并的明细

        return jr;
    }

    /**
     * 合并明细调整
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @RequestMapping(value = "/unconfirmTaskSubSupplierDetailforCombined")
    public JsonResult<Boolean> unconfirmTaskSubSupplierDetailforCombined(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskSubSupplierDetailDTO.getTaskSubSupplierDetailIds());
        TaskSubSupplierDetailPO tsddp = new TaskSubSupplierDetailPO();
        tsddp.setCombineConfirmed(false);
        this.taskSubSupplierDetailService.update(tsddp,wrapper);//更新合并的明细

        return jr;
    }

    /**
     * 更新供应商子任务明细
     * @param taskSubSupplierDetailDTO
     * @return
     */
    @RequestMapping(value = "/updateSubSupplierDetail")
    public JsonResult<Boolean> updateSubSupplierDetail(@RequestBody TaskSubSupplierDetailDTO taskSubSupplierDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("id={0}",taskSubSupplierDetailDTO.getTaskSubSupplierDetailId());
        TaskSubSupplierDetailPO tssdp = new TaskSubSupplierDetailPO();
        tssdp.setDeductRetirementInsurance(taskSubSupplierDetailDTO.getDeductRetirementInsurance());
        tssdp.setDeductMedicalInsurance(taskSubSupplierDetailDTO.getDeductMedicalInsurance());
        tssdp.setDeductDlenessInsurance(taskSubSupplierDetailDTO.getDeductDlenessInsurance());
        tssdp.setDeductHouseFund(taskSubSupplierDetailDTO.getDeductHouseFund());
        tssdp.setIncomeTotal(taskSubSupplierDetailDTO.getIncomeTotal());
        this.taskSubSupplierDetailService.update(tssdp,wrapper);//更新明细

        return jr;
    }

}
