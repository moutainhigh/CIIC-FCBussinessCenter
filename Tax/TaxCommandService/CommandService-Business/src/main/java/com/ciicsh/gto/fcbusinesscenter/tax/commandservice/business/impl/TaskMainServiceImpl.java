package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchTaskMainPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
