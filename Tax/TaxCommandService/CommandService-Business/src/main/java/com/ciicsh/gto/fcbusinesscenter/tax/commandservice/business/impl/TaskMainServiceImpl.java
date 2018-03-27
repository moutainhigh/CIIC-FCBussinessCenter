package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMainDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@Service
public class TaskMainServiceImpl extends ServiceImpl<TaskMainMapper, TaskMainPO> implements TaskMainService, Serializable {

    @Autowired
    private CalculationBatchTaskMainServiceImpl calculationBatchTaskMainService;

    @Autowired
    private TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    private TaskSubMoneyServiceImpl taskSubMoneyService;

    @Autowired
    private TaskSubPaymentServiceImpl taskSubPaymentService;

    @Autowired
    private TaskSubSupplierServiceImpl taskSubSupplierService;

    /*@Autowired
    private CalculationBatchMapper calculationBatchMapper;*/

    @Autowired
    private TaskMainDetailMapper taskMainDetailMapper;


    /**
     * 任务管理查询
     * @param requestForTaskMain
     * @return
     */
    @Override
    public ResponseForTaskMain queryTaskMains(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        if(StrKit.isNotEmpty(requestForTaskMain.getManagerName())){
            wrapper.like("manager_name",requestForTaskMain.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForTaskMain.getTaskNo())){
            wrapper.like("task_no",requestForTaskMain.getTaskNo());
        }
        wrapper.orderBy("created_time",false);

        if(StrKit.notBlank(requestForTaskMain.getTabsName())){

            wrapper.in("status", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE,requestForTaskMain.getTabsName().toUpperCase()));
        }

        wrapper.orderBy("created_time",false);
        return this.query(requestForTaskMain,wrapper);
    }

    /**
     * 查询草稿任务
     * @param requestForTaskMain
     * @return
     */
    @Override
    public ResponseForTaskMain queryTaskMainsForDraft(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        if(StrKit.isNotEmpty(requestForTaskMain.getManagerName())){
            wrapper.like("manager_name",requestForTaskMain.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForTaskMain.getTaskNo())){
            wrapper.like("task_no",requestForTaskMain.getTaskNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.in("status","00,03");
        wrapper.orderBy("created_time",false);
        return this.query(requestForTaskMain,wrapper);
    }

    /**
     * 查询审批中任务
     * @param requestForTaskMain
     * @return
     */
    @Override
    public ResponseForTaskMain queryTaskMainsForCheck(RequestForTaskMain requestForTaskMain) {

        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        if(StrKit.isNotEmpty(requestForTaskMain.getManagerName())){
            wrapper.like("manager_name",requestForTaskMain.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForTaskMain.getTaskNo())){
            wrapper.like("task_no",requestForTaskMain.getTaskNo());
        }
        //wrapper.like("manager_name","恒大");里
        wrapper.in("status","01");
        wrapper.orderBy("created_time",false);

        return this.query(requestForTaskMain,wrapper);
    }

    private ResponseForTaskMain query(RequestForTaskMain requestForTaskMain , EntityWrapper wrapper){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForTaskMain.getCurrentNum(),requestForTaskMain.getPageSize());
        List<TaskMainPO> taskMainPOList = baseMapper.selectPage(page, wrapper);
        //查询主任务对应的批次号
        for(TaskMainPO p : taskMainPOList){
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id",p.getId());
            List<CalculationBatchTaskMainPO> l = calculationBatchTaskMainService.selectByMap(columnMap);
            //组合批次号
            String sb = l.stream().map(CalculationBatchTaskMainPO::getBatchNo).collect(Collectors.joining(", "));
            p.setBatchIds(sb);
        }
        responseForTaskMain.setRowList(taskMainPOList);
        responseForTaskMain.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMain.setTotalNum(page.getTotal());

        return responseForTaskMain;
    }

    /**
     * 提交主任务
     * @param
     * @return
     */
    public ResponseForTaskMain submitTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"01",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }

    /**
     * 审批通过主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain passTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"02",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }
    /**
     * 失效主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain invalidTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"05",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }
    /**
     * 退回主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain rejectTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"03",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }

    //更新主任务状态
    private void updateTaskMainsStatus(String[] taskMainIds,String status,String[] currentStatus){

        List<TaskMainPO> tps = new ArrayList<>();
        for(String taskMainId : taskMainIds){

            TaskMainPO taskMainPO =  new TaskMainPO();
            taskMainPO.setId(Long.valueOf(taskMainId));
            taskMainPO.setStatus(status);
            tps.add(taskMainPO);

            //更新子任务状态
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_main_id={0}",Long.valueOf(taskMainId));
            //只更新与主任务状态相同的子任务
            wrapper.andNew("status={0}",currentStatus);
            //申报
            TaskSubDeclarePO taskSubDeclarePO =  new TaskSubDeclarePO();
            taskSubDeclarePO.setStatus(status);
            this.taskSubDeclareService.update(taskSubDeclarePO,wrapper);
            //划款
            TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
            taskSubMoneyPO.setStatus(status);
            this.taskSubMoneyService.update(taskSubMoneyPO,wrapper);
            //缴纳
            TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
            taskSubPaymentPO.setStatus(status);
            this.taskSubPaymentService.update(taskSubPaymentPO,wrapper);
            //供应商处理
            TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
            taskSubSupplierPO.setStatus(status);
            this.taskSubSupplierService.update(taskSubSupplierPO,wrapper);

        }

        //更新主任务状态
        this.updateBatchById(tps);
    }

    /**
     * 查询主任务明细
     * @param requestForTaskMain
     * @return
     */
    public ResponseForTaskMainDetail queryTaskMainDetails(RequestForTaskMain requestForTaskMain){

        /*EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_main_id={0}",requestForTaskMain.getTaskMainId());
        wrapper.isNull("task_main_detail_id");
        wrapper.andNew("is_combined={0}",requestForTaskMain.getIsCombined());*/
        Page<TaskMainDetailPO> page = new Page<TaskMainDetailPO>(requestForTaskMain.getCurrentNum(), requestForTaskMain.getPageSize());

        Map<String,Object> params = new HashMap<>();
        params.put("taskMainId",requestForTaskMain.getTaskMainId());//主任务id
        params.put("isCombined",requestForTaskMain.getIsCombined());//是否为合并明细
        params.put("employeeNo",requestForTaskMain.getEmployeeNo());
        params.put("employeeName",requestForTaskMain.getEmployeeName());
        params.put("idType",requestForTaskMain.getIdType());
        params.put("idNo",requestForTaskMain.getIdNo());

        List<TaskMainDetailBO> taskMainDetailBOs = taskMainDetailMapper.queryTaskMainDetails(page,params);

        ResponseForTaskMainDetail responseForTaskMainDetail = new ResponseForTaskMainDetail();

        responseForTaskMainDetail.setRowList(taskMainDetailBOs);
        responseForTaskMainDetail.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMainDetail.setPageSize(requestForTaskMain.getPageSize());
        responseForTaskMainDetail.setTotalNum(page.getTotal());

        return responseForTaskMainDetail;
    }

    /**
     * 更新主任务状态(子任务退回)
     * @param taskMainIds
     * @return
     */
    public void updateTaskMainStatus(Long[] taskMainIds){

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskMainIds);
        TaskMainPO tmp = new TaskMainPO();
        tmp.setStatus("03");
        this.update(tmp,wrapper);//更新主任务
    }

}
