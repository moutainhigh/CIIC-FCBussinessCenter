package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubPaymentMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuantongqing on 2018/01/02
 */
@Service
public class TaskSubPaymentServiceImpl extends ServiceImpl<TaskSubPaymentMapper, TaskSubPaymentPO> implements TaskSubPaymentService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubPaymentServiceImpl.class);

    /**
     * 条件查询缴纳子任务
     * @param requestForSubPayment
     * @return
     */
    @Override
    public ResponseForSubPayment querySubPayment(RequestForSubPayment requestForSubPayment) {
        ResponseForSubPayment responseForSubPayment = new ResponseForSubPayment();
        List<TaskSubPaymentPO> taskSubPaymentPOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubPaymentPO());
        //判断是否包含主键ID条件
        if (null != requestForSubPayment.getId()) {
            wrapper.andNew("id = {0}", requestForSubPayment.getId());
        }
        //缴纳账户模糊查询条件
        if (StrKit.notBlank(requestForSubPayment.getPaymentAccount())) {
            wrapper.like("payment_account", requestForSubPayment.getPaymentAccount());
        }
        //管理方名称模糊查询条件
        if (StrKit.notBlank(requestForSubPayment.getManagerName())) {
            wrapper.like("manager_name", requestForSubPayment.getManagerName());
        }
        //期间类型currentPan,currentBeforePan,currentAfterPan  DateTimeKit.format(taskSubPaymentPO.getPeriod(),"YYYY-MM")
        if(StrKit.notBlank(requestForSubPayment.getPeriodType())){
            String currentDateStr =  DateTimeKit.format(new Date(),"YYYY-MM")+"-01";
            if("currentPan".equals(requestForSubPayment.getPeriodType())){
                wrapper.andNew("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }else if("currentBeforePan".equals(requestForSubPayment.getPeriodType())){
                wrapper.andNew("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }else{
                wrapper.andNew("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);

        //判断是否分页
        if (null != requestForSubPayment.getPageSize() && null != requestForSubPayment.getCurrentNum()) {
            Page<TaskSubPaymentPO> pageInfo = new Page<>(requestForSubPayment.getCurrentNum(), requestForSubPayment.getPageSize());
            taskSubPaymentPOList = baseMapper.selectPage(pageInfo, wrapper);
            //获取查询总数目
            int total = baseMapper.selectCount(wrapper);
            responseForSubPayment.setRowList(taskSubPaymentPOList);
            responseForSubPayment.setTotalNum(total);
            responseForSubPayment.setCurrentNum(requestForSubPayment.getCurrentNum());
            responseForSubPayment.setPageSize(requestForSubPayment.getPageSize());
        } else {
            taskSubPaymentPOList = baseMapper.selectList(wrapper);
            responseForSubPayment.setRowList(taskSubPaymentPOList);
        }
        return responseForSubPayment;
    }

    /**
     * 批量修改缴纳子任务为完成
     * @param requestForSubPayment
     * @return
     */
    @Override
    public Boolean completeTaskSubPayment(RequestForSubPayment requestForSubPayment) {
        Boolean flag = true;
        try {
            if (requestForSubPayment.getSubPaymentIds() != null && !"".equals(requestForSubPayment.getSubPaymentIds())) {
                //修改缴纳子任务状态为完成
                flag = baseMapper.updateTaskSubPaymentStatus(requestForSubPayment.getSubPaymentIds(), requestForSubPayment.getStatus(), requestForSubPayment.getModifiedBy());
            }
        } catch (Exception e) {
            logger.error("completeTaskSubPayment error "+e.toString());
            flag = false;
        }
        return flag;
    }

    /**
     * 批量修改缴纳子任务为退回
     * @param requestForSubPayment
     * @return
     */
    @Override
    public Boolean rejectTaskSubPayment(RequestForSubPayment requestForSubPayment) {
        Boolean flag = true;
        try {
            if (requestForSubPayment.getSubPaymentIds() != null && !"".equals(requestForSubPayment.getSubPaymentIds())) {
                //修改缴纳子任务状态为退回
                flag = baseMapper.updateTaskSubPaymentStatus(requestForSubPayment.getSubPaymentIds(), requestForSubPayment.getStatus(), requestForSubPayment.getModifiedBy());
            }
        } catch (Exception e) {
            logger.error("rejectTaskSubPayment error "+e.toString());
            flag = false;
        }
        return flag;
    }

    /**
     * 根据缴纳子任务ID查询缴纳任务信息
     * @param subPaymentId
     * @return
     */
    @Override
    public TaskSubPaymentPO querySubPaymentById(Long subPaymentId) {
        return baseMapper.selectById(subPaymentId);
    }
}
