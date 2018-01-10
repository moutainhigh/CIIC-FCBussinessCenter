package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubMoneyDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@Service
public class TaskSubMoneyDetailServiceImpl extends ServiceImpl<TaskSubMoneyDetailMapper, TaskSubMoneyDetailPO> implements TaskSubMoneyDetailService, Serializable {

    @Override
    public ResponseForSubMoneyDetail querySubMoneyDetailsByParams(RequestForSubMoneyDetail requestForSubMoneyDetail) {
        ResponseForSubMoneyDetail responseForSubMoneyDetail = new ResponseForSubMoneyDetail();
        List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubMoneyDetailPO());
        //判断是否包含主键ID条件
        if (null != requestForSubMoneyDetail.getId()) {
            wrapper.andNew("id = {0}", requestForSubMoneyDetail.getId());
        }
        //判断是否包含划款子任务ID条件
        if (null != requestForSubMoneyDetail.getTaskSubMoneyId()) {
            wrapper.andNew("task_sub_money_id = {0}", requestForSubMoneyDetail.getTaskSubMoneyId());
        }
        //雇员编号模糊查询条件
        if (StrKit.notBlank(requestForSubMoneyDetail.getEmployeeNo())) {
            wrapper.like("employee_no", requestForSubMoneyDetail.getEmployeeNo());
        }
        //雇员名称模糊查询条件
        if (StrKit.notBlank(requestForSubMoneyDetail.getEmployeeName())) {
            wrapper.like("employee_name", requestForSubMoneyDetail.getEmployeeName());
        }
        //证件类型
        if (StrKit.notBlank(requestForSubMoneyDetail.getIdType())) {
            wrapper.andNew("id_type = {0}", requestForSubMoneyDetail.getIdType());
        }
        //证件号
        if (StrKit.notBlank(requestForSubMoneyDetail.getIdNo())) {
            wrapper.like("id_no", requestForSubMoneyDetail.getIdNo());
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        //判断是否分页
        if (null != requestForSubMoneyDetail.getPageSize() && null != requestForSubMoneyDetail.getCurrentNum()) {
            Page<TaskSubPaymentDetailPO> pageInfo = new Page<>(requestForSubMoneyDetail.getCurrentNum(), requestForSubMoneyDetail.getPageSize());
            taskSubMoneyDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
            //获取查询总数目
            int total = baseMapper.selectCount(wrapper);
            responseForSubMoneyDetail.setRowList(taskSubMoneyDetailPOList);
            responseForSubMoneyDetail.setTotalNum(total);
            responseForSubMoneyDetail.setCurrentNum(requestForSubMoneyDetail.getCurrentNum());
            responseForSubMoneyDetail.setPageSize(requestForSubMoneyDetail.getPageSize());
        } else {
            taskSubMoneyDetailPOList = baseMapper.selectList(wrapper);
            responseForSubMoneyDetail.setRowList(taskSubMoneyDetailPOList);
        }
        return responseForSubMoneyDetail;
    }
}
