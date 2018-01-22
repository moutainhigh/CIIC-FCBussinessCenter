package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForMainProof;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuantongqing
 * created 2017/12/08
 */
@Service
public class TaskMainProofServiceImpl extends ServiceImpl<TaskMainProofMapper, TaskMainProofPO> implements TaskMainProofService, Serializable {

    @Autowired(required = false)
    private TaskSubProofMapper taskSubProofMapper;

    /**
     * 条件查询完税凭证总任务
     *
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForMainProof queryTaskMainProofByRes(RequestForProof requestForProof) {
        ResponseForMainProof responseForMainProof = new ResponseForMainProof();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskMainProofPO());
        //判断是否包含主键ID条件
        if (requestForProof.getId() != null && !"".equals(requestForProof.getId())) {
            wrapper.andNew("id = {0}", requestForProof.getId());
        }
        //管理方名称模糊查询条件
        wrapper.like("manager_name", requestForProof.getManagerName());
        //判断是否包含起始时间条件
        if (requestForProof.getSubmitTimeStart() != null && !"".equals(requestForProof.getSubmitTimeStart())) {
            wrapper.andNew("created_time >= {0}", requestForProof.getSubmitTimeStart());
        }
        //判断是否包含结束时间条件
        if (requestForProof.getSubmitTimeEnd() != null && !"".equals(requestForProof.getSubmitTimeEnd())) {
            wrapper.andNew("created_time < {0} ", requestForProof.getSubmitTimeEnd());
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        //判断是否是分页查询
        if (requestForProof.getCurrentNum() != null && requestForProof.getPageSize() != 0) {
            Page<TaskMainProofPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
            List<TaskMainProofPO> taskMainProofPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskMainProofPOList);
            //获取完税凭证任务状态中文名
            for(TaskMainProofPO p: taskMainProofPOList){
                p.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,p.getStatus()));
            }
            responseForMainProof.setCurrentNum(requestForProof.getCurrentNum());
            responseForMainProof.setPageSize(requestForProof.getPageSize());
            responseForMainProof.setTotalNum(pageInfo.getTotal());
            responseForMainProof.setRowList(taskMainProofPOList);
        } else {
            List<TaskMainProofPO> taskMainProofPOList = baseMapper.selectList(wrapper);
            //获取完税凭证任务状态中文名
            for(TaskMainProofPO p: taskMainProofPOList){
                p.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,p.getStatus()));
            }
            responseForMainProof.setRowList(taskMainProofPOList);
        }
        return responseForMainProof;
    }

    /**
     * 新增完税凭证主任务和完税凭证子任务
     *
     * @param taskMainProofPO
     * @param taskSubProofPO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addTaskProof(TaskMainProofPO taskMainProofPO, TaskSubProofPO taskSubProofPO) {
        if (taskMainProofPO != null) {
            baseMapper.addTaskMainProof(taskMainProofPO);
        }

        if (taskSubProofPO != null) {
            taskSubProofPO.setTaskMainProofId(taskMainProofPO.getId());
            taskSubProofMapper.addTaskSubProof(taskSubProofPO);
        }
    }

    /**
     * 修改（即：提交/失效）完税凭证状态
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTaskProofByRes(RequestForProof requestForProof) {
        //当主任务ID数组不为空时，修改主任务以及
        if (requestForProof.getMainProofIds() != null && requestForProof.getMainProofIds().length > 0) {
            baseMapper.updateMainTaskProof(requestForProof.getMainProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
            taskSubProofMapper.updateSubTaskProofByMainIds(requestForProof.getMainProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
        } else {
            if (requestForProof.getSubProofIds() != null && requestForProof.getSubProofIds().length > 0) {
                taskSubProofMapper.updateSubTaskProof(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
            }
        }
    }

    /**
     * 将完税凭证任务置为失效
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void invalidTaskProof(RequestForProof requestForProof) {
        //当主任务ID数组不为空时，修改主任务以及
        if (requestForProof.getMainProofIds() != null && requestForProof.getMainProofIds().length > 0) {
            baseMapper.invalidMainTaskProofByIds(requestForProof.getMainProofIds(), requestForProof.getModifiedBy());
            taskSubProofMapper.invalidSubTaskProofByMainIds(requestForProof.getMainProofIds(), requestForProof.getModifiedBy());
        } else {
            if (requestForProof.getSubProofIds() != null && requestForProof.getSubProofIds().length > 0) {
                taskSubProofMapper.invalidSubTaskProofByIds(requestForProof.getSubProofIds(), requestForProof.getModifiedBy());
            }
        }
    }
}
