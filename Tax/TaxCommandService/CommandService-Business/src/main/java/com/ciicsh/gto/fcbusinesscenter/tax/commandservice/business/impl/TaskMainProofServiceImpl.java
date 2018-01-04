package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForMainProof;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.util.ArrayList;
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
        List<TaskMainProofBO> taskMainProofBOList = new ArrayList<>();
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
            //获取总数目
            int total = baseMapper.selectCount(wrapper);
            for (TaskMainProofPO taskMainProofPO : taskMainProofPOList) {
                TaskMainProofBO taskMainProofBO = new TaskMainProofBO();
                BeanUtils.copyProperties(taskMainProofPO, taskMainProofBO);
                taskMainProofBOList.add(taskMainProofBO);
            }
            responseForMainProof.setCurrentNum(requestForProof.getCurrentNum());
            responseForMainProof.setPageSize(requestForProof.getPageSize());
            responseForMainProof.setTotalNum(total);
            responseForMainProof.setRowList(taskMainProofBOList);
        } else {
            List<TaskMainProofPO> taskMainProofPOList = baseMapper.selectList(wrapper);
            for (TaskMainProofPO taskMainProofPO : taskMainProofPOList) {
                TaskMainProofBO taskMainProofBO = new TaskMainProofBO();
                BeanUtils.copyProperties(taskMainProofPO, taskMainProofBO);
                taskMainProofBOList.add(taskMainProofBO);
            }
            responseForMainProof.setRowList(taskMainProofBOList);
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
    @Transactional
    @Override
    public Boolean addTaskProof(TaskMainProofPO taskMainProofPO, TaskSubProofPO taskSubProofPO) {
        Boolean flag = true;
        try {
            if (taskMainProofPO != null) {
                baseMapper.addTaskMainProof(taskMainProofPO);
            }

            if (taskSubProofPO != null) {
                taskSubProofPO.setTaskMainProofId(taskMainProofPO.getId());
                taskSubProofMapper.addTaskSubProof(taskSubProofPO);
            }
        } catch (Exception e) {
            flag = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            return flag;
        }
    }

    /**
     * 修改（即：提交/失效）完税凭证状态
     *
     * @param requestForProof
     * @return
     */
    @Transactional
    @Override
    public Boolean updateTaskProofByRes(RequestForProof requestForProof) {
        Boolean flag = true;
        try {
            //当主任务ID数组不为空时，修改主任务以及
            if (requestForProof.getMainProofIds() != null && requestForProof.getMainProofIds().length > 0) {
                baseMapper.updateMainTaskProof(requestForProof.getMainProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
                taskSubProofMapper.updateSubTaskProofByMainIds(requestForProof.getMainProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
            }else{
                if (requestForProof.getSubProofIds() != null && requestForProof.getSubProofIds().length > 0) {
                    taskSubProofMapper.updateSubTaskProof(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            return flag;
        }
    }

    /**
     * 将完税凭证任务置为失效
     * @param requestForProof
     * @return
     */
    @Transactional
    @Override
    public Boolean invalidTaskProof(RequestForProof requestForProof) {
        Boolean flag = true;
        try {
            //当主任务ID数组不为空时，修改主任务以及
            if (requestForProof.getMainProofIds() != null && requestForProof.getMainProofIds().length > 0) {
                baseMapper.invalidMainTaskProofByIds(requestForProof.getMainProofIds(),requestForProof.getModifiedBy());
                taskSubProofMapper.invalidSubTaskProofByMainIds(requestForProof.getMainProofIds(), requestForProof.getModifiedBy());
            }else{
                if (requestForProof.getSubProofIds() != null && requestForProof.getSubProofIds().length > 0) {
                    taskSubProofMapper.invalidSubTaskProofByIds(requestForProof.getSubProofIds(),requestForProof.getModifiedBy());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            return flag;
        }
    }
}
