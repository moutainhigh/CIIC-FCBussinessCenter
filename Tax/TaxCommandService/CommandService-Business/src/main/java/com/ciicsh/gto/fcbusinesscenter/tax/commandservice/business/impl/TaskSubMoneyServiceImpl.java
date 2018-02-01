package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubMoneyMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@Service
public class TaskSubMoneyServiceImpl extends ServiceImpl<TaskSubMoneyMapper, TaskSubMoneyPO> implements TaskSubMoneyService, Serializable {

    /**
     * 条件查询划款子任务
     *
     * @param requestForSubMoney
     * @return
     */
    @Override
    public ResponseForSubMoney querySubMoney(RequestForSubMoney requestForSubMoney) {
        //当期
        String currentPan = "currentPan";
        //当期前
        String currentBeforePan = "currentBeforePan";
        //当期后
        String currentAfterPan = "currentAfterPan";
        ResponseForSubMoney responseForSubMoney = new ResponseForSubMoney();
        List<TaskSubMoneyPO> taskSubMoneyPOList;
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubMoneyPO());
        //判断是否包含主键ID条件
        if (null != requestForSubMoney.getId()) {
            wrapper.andNew("id = {0}", requestForSubMoney.getId());
        }
        //缴纳账户模糊查询条件
        if (StrKit.notBlank(requestForSubMoney.getPaymentAccount())) {
            wrapper.like("payment_account", requestForSubMoney.getPaymentAccount());
        }
        //管理方名称模糊查询条件
        if (StrKit.notBlank(requestForSubMoney.getManagerName())) {
            wrapper.like("manager_name", requestForSubMoney.getManagerName());
        }
        //期间类型currentPan,currentBeforePan,currentAfterPan
        if (StrKit.notBlank(requestForSubMoney.getPeriodType())) {
            String currentDateStr = DateTimeKit.format(new Date(), "YYYY-MM") + "-01";
            if (currentPan.equals(requestForSubMoney.getPeriodType())) {
                wrapper.andNew("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (currentBeforePan.equals(requestForSubMoney.getPeriodType())) {
                wrapper.andNew("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (currentAfterPan.equals(requestForSubMoney.getPeriodType())) {
                wrapper.andNew("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        //任务状态
        if(StrKit.notBlank(requestForSubMoney.getStatusType())){
            wrapper.andNew("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE,requestForSubMoney.getStatusType().toUpperCase()));
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);

        //判断是否分页
        if (null != requestForSubMoney.getPageSize() && null != requestForSubMoney.getCurrentNum()) {
            Page<TaskSubMoneyPO> pageInfo = new Page<>(requestForSubMoney.getCurrentNum(), requestForSubMoney.getPageSize());
            taskSubMoneyPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubMoneyPOList);
            //获取完税凭证任务状态中文名
            for (TaskSubMoneyPO p : taskSubMoneyPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
            }
            responseForSubMoney.setRowList(taskSubMoneyPOList);
            responseForSubMoney.setTotalNum(pageInfo.getTotal());
            responseForSubMoney.setCurrentNum(requestForSubMoney.getCurrentNum());
            responseForSubMoney.setPageSize(requestForSubMoney.getPageSize());
        } else {
            taskSubMoneyPOList = baseMapper.selectList(wrapper);
            //获取完税凭证任务状态中文名
            for (TaskSubMoneyPO p : taskSubMoneyPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
            }
            responseForSubMoney.setRowList(taskSubMoneyPOList);
        }
        return responseForSubMoney;
    }


    /**
     * 批量完成划款子任务
     *
     * @param requestForSubMoney
     */
    @Override
    public void completeTaskSubMoney(RequestForSubMoney requestForSubMoney) {
        updateTaskSubMoneyStatus(requestForSubMoney);
    }
    /**
     * 批量退回划款子任务
     *
     * @param requestForSubMoney
     */
    @Override
    public void rejectTaskSubMoney(RequestForSubMoney requestForSubMoney) {
        updateTaskSubMoneyStatus(requestForSubMoney);
    }

    /**
     * 修改划款任务状态
     * @param requestForSubMoney
     */
    private void updateTaskSubMoneyStatus(RequestForSubMoney requestForSubMoney) {
        if (requestForSubMoney.getSubMoneyIds() != null && !"".equals(requestForSubMoney.getSubMoneyIds())) {
            TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
            //任务状态
            taskSubMoneyPO.setStatus(requestForSubMoney.getStatus());
            //修改人
            taskSubMoneyPO.setModifiedBy(requestForSubMoney.getModifiedBy());
            //修改时间
            taskSubMoneyPO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubMoneyPO());
            wrapper.andNew("is_active = {0}",true);
            wrapper.in("id",requestForSubMoney.getSubMoneyIds());
            //修改划款子任务状态
            baseMapper.update(taskSubMoneyPO,wrapper);
        }
    }
    /**
     * 根据划款子任务ID查询划款子任务信息
     *
     * @param subMoneyId
     * @return
     */
    @Override
    public TaskSubMoneyPO querySubMoneyById(long subMoneyId) {
        TaskSubMoneyPO taskSubMoneyPO = baseMapper.selectById(subMoneyId);
        taskSubMoneyPO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubMoneyPO.getStatus()));
        return taskSubMoneyPO;
    }
}
