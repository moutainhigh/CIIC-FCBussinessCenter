package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class TaskSubDeclareServiceImpl extends ServiceImpl<TaskSubDeclareMapper, TaskSubDeclarePO> implements TaskSubDeclareService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubDeclareServiceImpl.class);

    @Override
    public ResponseForTaskSubDeclare queryTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare) {

        ResponseForTaskSubDeclare responseForTaskSubDeclare = new ResponseForTaskSubDeclare();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubDeclarePO());
        /*//管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.andNew("batch_no={0}",requestForCalBatch.getBatchNo());
        }*/
        //wrapper.like("manager_name","恒大");
        wrapper.isNull("task_sub_declare_id");
        wrapper.orderBy("created_time",false);
        Page<TaskSubDeclarePO> page = new Page<TaskSubDeclarePO>(requestForTaskSubDeclare.getCurrentNum(),requestForTaskSubDeclare.getPageSize());
        List<TaskSubDeclarePO> taskSubDeclarePOList = baseMapper.selectPage(page, wrapper);
        /*for(TaskSubDeclarePO p: taskSubDeclarePOList){
            p.setStatusName(EnumUtil.getMessage(EnumUtil.BATCH_NO_STATUS,p.getStatus()));
        }*/
        responseForTaskSubDeclare.setRowList(taskSubDeclarePOList);
        responseForTaskSubDeclare.setCurrentNum(requestForTaskSubDeclare.getCurrentNum());
        responseForTaskSubDeclare.setTotalNum(page.getTotal());

        return responseForTaskSubDeclare;
    }

    @Override
    public ResponseForTaskSubDeclare mergeTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare) {

        Long[] taskSubDeclareId = requestForTaskSubDeclare.getTaskSubDeclareId();

        ResponseForTaskSubDeclare responseForTaskSubDeclare = new ResponseForTaskSubDeclare();

        return responseForTaskSubDeclare;
    }

}
