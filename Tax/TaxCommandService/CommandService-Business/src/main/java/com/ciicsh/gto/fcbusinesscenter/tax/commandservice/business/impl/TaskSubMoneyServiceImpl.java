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
import java.util.ArrayList;
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
        ResponseForSubMoney responseForSubMoney = new ResponseForSubMoney();
        List<TaskSubMoneyPO> taskSubMoneyPOList = new ArrayList<>();
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
            if ("currentPan".equals(requestForSubMoney.getPeriodType())) {
                wrapper.andNew("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if ("currentBeforePan".equals(requestForSubMoney.getPeriodType())) {
                wrapper.andNew("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else {
                wrapper.andNew("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);

        //判断是否分页
        if (null != requestForSubMoney.getPageSize() && null != requestForSubMoney.getCurrentNum()) {
            Page<TaskSubMoneyPO> pageInfo = new Page<>(requestForSubMoney.getCurrentNum(), requestForSubMoney.getPageSize());
            taskSubMoneyPOList = baseMapper.selectPage(pageInfo, wrapper);
            //获取查询总数目
            int total = baseMapper.selectCount(wrapper);
            //获取完税凭证任务状态中文名
            for(TaskSubMoneyPO p: taskSubMoneyPOList){
                p.setStatusName(EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS,p.getStatus()));
            }
            responseForSubMoney.setRowList(taskSubMoneyPOList);
            responseForSubMoney.setTotalNum(total);
            responseForSubMoney.setCurrentNum(requestForSubMoney.getCurrentNum());
            responseForSubMoney.setPageSize(requestForSubMoney.getPageSize());
        } else {
            taskSubMoneyPOList = baseMapper.selectList(wrapper);
            //获取完税凭证任务状态中文名
            for(TaskSubMoneyPO p: taskSubMoneyPOList){
                p.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,p.getStatus()));
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
        if (requestForSubMoney.getSubMoneyIds() != null && !"".equals(requestForSubMoney.getSubMoneyIds())) {
            //修改划款子任务状态为完成
            baseMapper.updateTaskSubMoneyStatus(requestForSubMoney.getSubMoneyIds(), requestForSubMoney.getStatus(), requestForSubMoney.getModifiedBy());
        }
    }

    /**
     * 批量退回划款子任务
     *
     * @param requestForSubMoney
     */
    @Override
    public void rejectTaskSubMoney(RequestForSubMoney requestForSubMoney) {
        if (requestForSubMoney.getSubMoneyIds() != null && !"".equals(requestForSubMoney.getSubMoneyIds())) {
            //修改划款子任务状态为退回
            baseMapper.updateTaskSubMoneyStatus(requestForSubMoney.getSubMoneyIds(), requestForSubMoney.getStatus(), requestForSubMoney.getModifiedBy());
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
        taskSubMoneyPO.setStatusName(EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS,taskSubMoneyPO.getStatus()));
        return taskSubMoneyPO;
    }
}
