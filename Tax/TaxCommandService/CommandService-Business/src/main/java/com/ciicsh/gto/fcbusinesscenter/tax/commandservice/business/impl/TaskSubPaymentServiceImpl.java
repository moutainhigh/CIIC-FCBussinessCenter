package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubPaymentMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author yuantongqing on 2018/01/02
 */
@Service
public class TaskSubPaymentServiceImpl extends ServiceImpl<TaskSubPaymentMapper, TaskSubPaymentPO> implements TaskSubPaymentService, Serializable {

    @Autowired
    private TaskMainService taskMainService;

    /**
     * 条件查询缴纳子任务
     *
     * @param requestForSubPayment
     * @return
     */
    @Override
    public ResponseForSubPayment querySubPayment(RequestForSubPayment requestForSubPayment) {
        //当期
        String currentPan = "currentPan";
        //当期前
        String currentBeforePan = "currentBeforePan";
        //当期后
        String currentAfterPan = "currentAfterPan";
        ResponseForSubPayment responseForSubPayment = new ResponseForSubPayment();
        List<TaskSubPaymentPO> taskSubPaymentPOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubPaymentPO());
        //判断是否包含主键ID条件
        if (null != requestForSubPayment.getId()) {
            wrapper.and("id = {0}", requestForSubPayment.getId());
        }
        //缴纳账户模糊查询条件
        if (StrKit.notBlank(requestForSubPayment.getPaymentAccount())) {
            wrapper.like("payment_account", requestForSubPayment.getPaymentAccount());
        }
        //管理方名称
        Optional.ofNullable(requestForSubPayment.getManagerNames()).ifPresent(managerNames -> {
            wrapper.in("manager_name",managerNames);
        });
        //期间类型currentPan：当期,currentBeforePan：当期前,currentAfterPan：当期后
        if (StrKit.notBlank(requestForSubPayment.getPeriodType())) {
            //将年月的个税期间，转成1号，
            String currentDateStr = DateTimeKit.format(new Date(), "YYYY-MM") + "-01";
            if (currentPan.equals(requestForSubPayment.getPeriodType())) {
                wrapper.and("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (currentBeforePan.equals(requestForSubPayment.getPeriodType())) {
                wrapper.and("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (currentAfterPan.equals(requestForSubPayment.getPeriodType())) {
                wrapper.and("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        //任务状态
        if (StrKit.notBlank(requestForSubPayment.getStatusType())) {
            wrapper.and("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE, requestForSubPayment.getStatusType().toUpperCase()));
        }
        //区域类型(00:本地,01:异地)
        if (StrKit.notBlank(requestForSubPayment.getAreaType())) {
            wrapper.and("area_type = {0}", requestForSubPayment.getAreaType());
        }
        wrapper.and("is_active = {0} ", true);
        wrapper.orderBy("modified_time", false);

        //判断是否分页
        if (null != requestForSubPayment.getPageSize() && null != requestForSubPayment.getCurrentNum()) {
            Page<TaskSubPaymentPO> pageInfo = new Page<>(requestForSubPayment.getCurrentNum(), requestForSubPayment.getPageSize());
            taskSubPaymentPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubPaymentPOList);
            //获取缴纳任务状态中文名
            for (TaskSubPaymentPO p : taskSubPaymentPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
            }
            responseForSubPayment.setRowList(taskSubPaymentPOList);
            responseForSubPayment.setTotalNum(pageInfo.getTotal());
            responseForSubPayment.setCurrentNum(requestForSubPayment.getCurrentNum());
            responseForSubPayment.setPageSize(requestForSubPayment.getPageSize());
        } else {
            taskSubPaymentPOList = baseMapper.selectList(wrapper);
            //获取缴纳任务状态中文名
            for (TaskSubPaymentPO p : taskSubPaymentPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
            }
            responseForSubPayment.setRowList(taskSubPaymentPOList);
        }
        return responseForSubPayment;
    }

    /**
     * 批量修改缴纳子任务为完成
     *
     * @param requestForSubPayment
     * @return
     */
    @Override
    public void completeTaskSubPayment(RequestForSubPayment requestForSubPayment) {
        updateTaskSubPaymentStatus(requestForSubPayment);
    }

    /**
     * 批量修改缴纳子任务为退回
     *
     * @param requestForSubPayment
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectTaskSubPayment(RequestForSubPayment requestForSubPayment) {
        updateTaskSubPaymentStatus(requestForSubPayment);
        taskMainService.updateTaskMainStatus(requestForSubPayment.getMainIds());
    }

    /**
     * 修改缴纳任务状态
     *
     * @param requestForSubPayment
     */
    private void updateTaskSubPaymentStatus(RequestForSubPayment requestForSubPayment) {
        if (requestForSubPayment.getSubPaymentIds() != null && !"".equals(requestForSubPayment.getSubPaymentIds())) {
            TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
            //更新缴纳任务状态
            taskSubPaymentPO.setStatus(requestForSubPayment.getStatus());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubPaymentPO());
            wrapper.and("is_active = {0}", true);
            wrapper.in("id", requestForSubPayment.getSubPaymentIds());
            //修改缴纳子任务状态
            baseMapper.update(taskSubPaymentPO, wrapper);
        }
    }

    /**
     * 根据缴纳子任务ID查询缴纳任务信息
     *
     * @param subPaymentId
     * @return
     */
    @Override
    public TaskSubPaymentPO querySubPaymentById(long subPaymentId) {
        TaskSubPaymentPO taskSubPaymentPO = baseMapper.selectById(subPaymentId);
        taskSubPaymentPO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubPaymentPO.getStatus()));
        return taskSubPaymentPO;
    }

    /**
     * 更新划款滞纳金和罚金
     * @param subPayId
     * @param overdue
     * @param fine
     */
    @Override
    public void updateTaskSubPayOverdueAndFine(Long subPayId, BigDecimal overdue, BigDecimal fine) {
        TaskSubPaymentPO taskSubPaymentPO = baseMapper.selectById(subPayId);
        taskSubPaymentPO.setOverdue(overdue);
        taskSubPaymentPO.setFine(fine);
        baseMapper.updateById(taskSubPaymentPO);
    }
}
