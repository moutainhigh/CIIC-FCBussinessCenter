package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
@Service
public class TaskMainServiceImpl extends ServiceImpl<TaskMainMapper, TaskMainPO> implements TaskMainService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskMainServiceImpl.class);

    /*@Autowired(required = false)
    private TaskMainMapper taskMainMapper;*/

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

    @Override
    public ResponseForTaskMain queryTaskMains(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
       /* wrapper.setEntity(new CalculationBatchPO());
        //管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.andNew("batch_no={0}",requestForCalBatch.getBatchNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.orderBy("created_time",false);*/
//        wrapper.andNew("status={0}","00");

        if(StrKit.notBlank(requestForTaskMain.getTabsName())){

            wrapper.in("status", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE,requestForTaskMain.getTabsName().toUpperCase()));
        }

        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForTaskMain.getCurrentNum(),requestForTaskMain.getPageSize());
        List<TaskMainPO> taskMainPOList = baseMapper.selectPage(page, wrapper);
        //查询主任务对应的批次号
        for(TaskMainPO p : taskMainPOList){
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id",p.getId());
            List<CalculationBatchTaskMainPO> l = calculationBatchTaskMainService.selectByMap(columnMap);
            StringBuilder sb = new StringBuilder();
            int k =0;
            for(CalculationBatchTaskMainPO cp : l){
                if(k>0){
                    sb.append(",");
                }
                sb.append(cp.getBatchNo());
                k++;
            }
            p.setBatchIds(sb.toString());
        }
        responseForTaskMain.setRowList(taskMainPOList);
        responseForTaskMain.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMain.setTotalNum(page.getTotal());
        return responseForTaskMain;
    }

    @Override
    public ResponseForTaskMain queryTaskMainsForDraft(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
       /* wrapper.setEntity(new CalculationBatchPO());
        //管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.andNew("batch_no={0}",requestForCalBatch.getBatchNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.orderBy("created_time",false);*/
        wrapper.andNew("status={0}","00");
        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForTaskMain.getCurrentNum(),requestForTaskMain.getPageSize());
        List<TaskMainPO> taskMainPOList = baseMapper.selectPage(page, wrapper);
        //查询主任务对应的批次号
        for(TaskMainPO p : taskMainPOList){
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id",p.getId());
            List<CalculationBatchTaskMainPO> l = calculationBatchTaskMainService.selectByMap(columnMap);
            StringBuilder sb = new StringBuilder();
            int k =0;
            for(CalculationBatchTaskMainPO cp : l){
                if(k>0){
                    sb.append(",");
                }
                sb.append(cp.getBatchNo());
                k++;
            }
            p.setBatchIds(sb.toString());
        }
        responseForTaskMain.setRowList(taskMainPOList);
        responseForTaskMain.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMain.setTotalNum(page.getTotal());
        return responseForTaskMain;
    }

    @Override
    public ResponseForTaskMain queryTaskMainsForCheck(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
       /* wrapper.setEntity(new CalculationBatchPO());
        //管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.andNew("batch_no={0}",requestForCalBatch.getBatchNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.orderBy("created_time",false);*/
        wrapper.andNew("status={0}","01");
        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForTaskMain.getCurrentNum(),requestForTaskMain.getPageSize());
        List<TaskMainPO> taskMainPOList = baseMapper.selectPage(page, wrapper);
        //查询主任务对应的批次号
        for(TaskMainPO p : taskMainPOList){
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id",p.getId());
            List<CalculationBatchTaskMainPO> l = calculationBatchTaskMainService.selectByMap(columnMap);
            StringBuilder sb = new StringBuilder();
            int k =0;
            for(CalculationBatchTaskMainPO cp : l){
                if(k>0){
                    sb.append(",");
                }
                sb.append(cp.getBatchNo());
                k++;
            }
            p.setBatchIds(sb.toString());
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

        List<TaskMainPO> tps = new ArrayList<>();
        for(String taskMainId : taskMainIds){

            TaskMainPO taskMainPO =  new TaskMainPO();
            taskMainPO.setId(Long.valueOf(taskMainId));
            taskMainPO.setStatus("01");
            tps.add(taskMainPO);
        }

        this.updateBatchById(tps);

        return responseForTaskMain;
    }

    /**
     * 提交主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain passTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        List<TaskMainPO> tps = new ArrayList<>();
        for(String taskMainId : taskMainIds){

            TaskMainPO taskMainPO =  new TaskMainPO();
            taskMainPO.setId(Long.valueOf(taskMainId));
            taskMainPO.setStatus("02");
            tps.add(taskMainPO);

            //更新子任务状态
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_main_id={0}",Long.valueOf(taskMainId));
            //申报
            TaskSubDeclarePO taskSubDeclarePO =  new TaskSubDeclarePO();
            taskSubDeclarePO.setStatus("02");
            this.taskSubDeclareService.update(taskSubDeclarePO,wrapper);
            //划款
            TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
            taskSubMoneyPO.setStatus("02");
            this.taskSubMoneyService.update(taskSubMoneyPO,wrapper);
            //缴纳
            TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
            taskSubPaymentPO.setStatus("02");
            this.taskSubPaymentService.update(taskSubPaymentPO,wrapper);
            //供应商处理
            TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
            taskSubSupplierPO.setStatus("02");
            this.taskSubSupplierService.update(taskSubSupplierPO,wrapper);

        }

        //更新主任务状态
        this.updateBatchById(tps);

        return responseForTaskMain;
    }


}
