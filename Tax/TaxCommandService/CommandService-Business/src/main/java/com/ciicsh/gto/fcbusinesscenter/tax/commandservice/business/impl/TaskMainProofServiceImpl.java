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
import java.time.LocalDateTime;
import java.util.Date;
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
            wrapper.andNew("created_time >= {0}", requestForProof.getSubmitTimeStart() + "00:00:00");
        }
        //判断是否包含结束时间条件
        if (requestForProof.getSubmitTimeEnd() != null && !"".equals(requestForProof.getSubmitTimeEnd())) {
            wrapper.andNew("created_time <= {0} ", requestForProof.getSubmitTimeEnd() + " 23:59:59");
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        //判断是否是分页查询
        if (requestForProof.getCurrentNum() != null && requestForProof.getPageSize() != 0) {
            Page<TaskMainProofPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
            List<TaskMainProofPO> taskMainProofPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskMainProofPOList);
            //获取完税凭证任务状态中文名
            for (TaskMainProofPO p : taskMainProofPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
            }
            responseForMainProof.setCurrentNum(requestForProof.getCurrentNum());
            responseForMainProof.setPageSize(requestForProof.getPageSize());
            responseForMainProof.setTotalNum(pageInfo.getTotal());
            responseForMainProof.setRowList(taskMainProofPOList);
        } else {
            List<TaskMainProofPO> taskMainProofPOList = baseMapper.selectList(wrapper);
            //获取完税凭证任务状态中文名
            for (TaskMainProofPO p : taskMainProofPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
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
            //新增完税凭证主任务
            baseMapper.insert(taskMainProofPO);
        }
        if (taskSubProofPO != null) {
            //设置主任务ID
            taskSubProofPO.setTaskMainProofId(taskMainProofPO.getId());
            //新增完税凭证子任务
            taskSubProofMapper.insert(taskSubProofPO);
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
        //当主任务ID数组不为空时，修改主任务以及子任务状态为已提交
        if (requestForProof.getMainProofIds() != null && requestForProof.getMainProofIds().length > 0) {
            //修改完税凭证主任务状态
            TaskMainProofPO taskMainProofPO = new TaskMainProofPO();
            //设置任务状态
            taskMainProofPO.setStatus(requestForProof.getStatus());
            //设置修改人
            taskMainProofPO.setModifiedBy(requestForProof.getModifiedBy());
            //设置修改时间
            taskMainProofPO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapperMain = new EntityWrapper();
            wrapperMain.setEntity(new TaskMainProofPO());
            //任务为草稿状态
            wrapperMain.andNew("status = {0} ", "00");
            //任务为可用状态
            wrapperMain.andNew("is_active = {0} ", true);
            //主任务ID IN条件
            wrapperMain.in("id", requestForProof.getMainProofIds());
            //修改完税凭证主任务
            baseMapper.update(taskMainProofPO, wrapperMain);

            //修改完税凭证子任务状态
            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            taskSubProofPO.setStatus(requestForProof.getStatus());
            //设置修改人
            taskSubProofPO.setModifiedBy(requestForProof.getModifiedBy());
            //设置修改时间
            taskSubProofPO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapperSub = new EntityWrapper();
            wrapperSub.setEntity(new TaskSubProofPO());
            //任务为草稿状态
            wrapperSub.andNew("status = {0} ", "00");
            //任务为可用状态
            wrapperSub.andNew("is_active = {0} ", true);
            //主任务ID IN条件
            wrapperSub.in("task_main_proof_id", requestForProof.getMainProofIds());
            //修改完税凭证子任务
            taskSubProofMapper.update(taskSubProofPO, wrapperSub);
        } else {
            if (requestForProof.getSubProofIds() != null && requestForProof.getSubProofIds().length > 0) {
                //修改完税凭证子任务状态
                TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
                taskSubProofPO.setStatus(requestForProof.getStatus());
                //设置修改人
                taskSubProofPO.setModifiedBy(requestForProof.getModifiedBy());
                //设置修改时间
                taskSubProofPO.setModifiedTime(LocalDateTime.now());
                EntityWrapper wrapperSub = new EntityWrapper();
                wrapperSub.setEntity(new TaskSubProofPO());
                //任务为草稿状态
                wrapperSub.andNew("status = {0} ", "00");
                //任务为可用状态
                wrapperSub.andNew("is_active = {0} ", true);
                //主任务ID IN条件
                wrapperSub.in("id", requestForProof.getSubProofIds());
                //修改完税凭证子任务
                taskSubProofMapper.update(taskSubProofPO, wrapperSub);
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
        //当主任务ID数组不为空时，修改主任务以及子任务状态为失效
        if (requestForProof.getMainProofIds() != null && requestForProof.getMainProofIds().length > 0) {
            //修改完税凭证主任务状态
            TaskMainProofPO taskMainProofPO = new TaskMainProofPO();
            //设置任务状态, 05：为失效状态
            taskMainProofPO.setStatus("05");
            //设置修改人
            taskMainProofPO.setModifiedBy(requestForProof.getModifiedBy());
            //设置修改时间
            taskMainProofPO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapperMain = new EntityWrapper();
            wrapperMain.setEntity(new TaskMainProofPO());
            //任务为草稿状态
            wrapperMain.andNew("status = {0} ", "00");
            //任务为可用状态
            wrapperMain.andNew("is_active = {0} ", true);
            //主任务ID IN条件
            wrapperMain.in("id", requestForProof.getMainProofIds());
            //修改完税凭证主任务
            baseMapper.update(taskMainProofPO, wrapperMain);

            //修改完税凭证子任务状态
            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            //设置任务状态, 05：为失效状态
            taskSubProofPO.setStatus("05");
            //设置修改人
            taskSubProofPO.setModifiedBy(requestForProof.getModifiedBy());
            //设置修改时间
            taskSubProofPO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapperSub = new EntityWrapper();
            wrapperSub.setEntity(new TaskSubProofPO());
            //任务为草稿状态
            wrapperSub.andNew("status = {0} ", "00");
            //任务为可用状态
            wrapperSub.andNew("is_active = {0} ", true);
            //主任务ID IN条件
            wrapperSub.in("task_main_proof_id", requestForProof.getMainProofIds());
            //修改完税凭证子任务
            taskSubProofMapper.update(taskSubProofPO, wrapperSub);
        } else {
            if (requestForProof.getSubProofIds() != null && requestForProof.getSubProofIds().length > 0) {
                //修改完税凭证子任务状态
                TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
                //设置任务状态, 05：为失效状态
                taskSubProofPO.setStatus("05");
                //设置修改人
                taskSubProofPO.setModifiedBy(requestForProof.getModifiedBy());
                //设置修改时间
                taskSubProofPO.setModifiedTime(LocalDateTime.now());
                EntityWrapper wrapperSub = new EntityWrapper();
                wrapperSub.setEntity(new TaskSubProofPO());
                //任务为草稿状态
                wrapperSub.andNew("status = {0} ", "00");
                //任务为可用状态
                wrapperSub.andNew("is_active = {0} ", true);
                //主任务ID IN条件
                wrapperSub.in("id", requestForProof.getSubProofIds());
                //修改完税凭证子任务
                taskSubProofMapper.update(taskSubProofPO, wrapperSub);
            }
        }
    }
}
