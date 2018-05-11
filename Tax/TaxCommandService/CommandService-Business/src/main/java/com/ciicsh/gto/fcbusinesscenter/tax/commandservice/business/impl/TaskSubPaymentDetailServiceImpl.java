package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubPaymentDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPaymentDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPaymentDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/3
 */
@Service
public class TaskSubPaymentDetailServiceImpl extends ServiceImpl<TaskSubPaymentDetailMapper, TaskSubPaymentDetailPO> implements TaskSubPaymentDetailService, Serializable {

    /**
     * 查询缴纳明细
     *
     * @param requestForSubPaymentDetail
     * @return
     */
    @Override
    public ResponseForSubPaymentDetail querySubPaymentDetailsByParams(RequestForSubPaymentDetail requestForSubPaymentDetail) {
        ResponseForSubPaymentDetail responseForSubPaymentDetail = new ResponseForSubPaymentDetail();
        List<TaskSubPaymentDetailPO> taskSubPaymentDetailPOList;
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubPaymentDetailPO());
        //判断是否包含主键ID条件
        if (null != requestForSubPaymentDetail.getId()) {
            wrapper.and("id = {0}", requestForSubPaymentDetail.getId());
        }
        //判断是否包含缴纳子任务ID条件
        if (null != requestForSubPaymentDetail.getTaskSubPaymentId()) {
            wrapper.and("task_sub_payment_id = {0}", requestForSubPaymentDetail.getTaskSubPaymentId());
        }
        //雇员编号模糊查询条件
        if (StrKit.notBlank(requestForSubPaymentDetail.getEmployeeNo())) {
            wrapper.like("employee_no", requestForSubPaymentDetail.getEmployeeNo());
        }
        //雇员名称模糊查询条件
        if (StrKit.notBlank(requestForSubPaymentDetail.getEmployeeName())) {
            wrapper.like("employee_name", requestForSubPaymentDetail.getEmployeeName());
        }
        //证件类型
        if (StrKit.notBlank(requestForSubPaymentDetail.getIdType())) {
            wrapper.and("id_type = {0}", requestForSubPaymentDetail.getIdType());
        }
        //证件号
        if (StrKit.notBlank(requestForSubPaymentDetail.getIdNo())) {
            wrapper.like("id_no", requestForSubPaymentDetail.getIdNo());
        }
        wrapper.and("is_active = {0} ", true);
        wrapper.orderBy("modified_time", false);
        //判断是否分页
        if (null != requestForSubPaymentDetail.getPageSize() && null != requestForSubPaymentDetail.getCurrentNum()) {
            Page<TaskSubPaymentDetailPO> pageInfo = new Page<>(requestForSubPaymentDetail.getCurrentNum(), requestForSubPaymentDetail.getPageSize());
            taskSubPaymentDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubPaymentDetailPOList);
            //获取证件类型中文名和所得项目中文名
            for(TaskSubPaymentDetailPO p: taskSubPaymentDetailPOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
            }
            responseForSubPaymentDetail.setRowList(taskSubPaymentDetailPOList);
            responseForSubPaymentDetail.setTotalNum(pageInfo.getTotal());
            responseForSubPaymentDetail.setCurrentNum(requestForSubPaymentDetail.getCurrentNum());
            responseForSubPaymentDetail.setPageSize(requestForSubPaymentDetail.getPageSize());
        } else {
            taskSubPaymentDetailPOList = baseMapper.selectList(wrapper);
            //获取证件类型中文名和所得项目中文名
            for(TaskSubPaymentDetailPO p: taskSubPaymentDetailPOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
            }
            responseForSubPaymentDetail.setRowList(taskSubPaymentDetailPOList);
        }
        return responseForSubPaymentDetail;
    }
}
