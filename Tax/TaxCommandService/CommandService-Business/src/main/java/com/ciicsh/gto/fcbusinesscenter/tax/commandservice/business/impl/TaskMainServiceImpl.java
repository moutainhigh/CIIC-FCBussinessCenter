package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchTaskMainMapper;
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
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class TaskMainServiceImpl extends ServiceImpl<TaskMainMapper, TaskMainPO> implements TaskMainService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskMainServiceImpl.class);

    /*@Autowired(required = false)
    private TaskMainMapper taskMainMapper;*/

    @Autowired(required = false)
    private CalculationBatchTaskMainMapper calculationBatchTaskMainMapper;

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
        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForTaskMain.getCurrentNum(),requestForTaskMain.getPageSize());
        List<TaskMainPO> taskMainPOList = baseMapper.selectPage(page, wrapper);
        //查询主任务对应的批次号
        for(TaskMainPO p : taskMainPOList){
            EntityWrapper<CalculationBatchTaskMainPO> wrapper2 = new EntityWrapper<CalculationBatchTaskMainPO>();
            wrapper2.andNew("task_main_id={0}",p.getId());
            List<CalculationBatchTaskMainPO> l = calculationBatchTaskMainMapper.selectList(wrapper2);
            StringBuilder sb = new StringBuilder();
            for(CalculationBatchTaskMainPO cp : l){
                sb.append(cp.getBatchNo());sb.append(",");
            }
            p.setBatchIds(sb.toString());
        }
        responseForTaskMain.setRowList(taskMainPOList);
        responseForTaskMain.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMain.setTotalNum(page.getTotal());
        return responseForTaskMain;
    }
}
