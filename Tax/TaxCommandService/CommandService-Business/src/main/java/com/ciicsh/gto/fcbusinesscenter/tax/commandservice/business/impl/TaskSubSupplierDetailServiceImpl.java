package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubSupplierDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubSupplierMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class TaskSubSupplierDetailServiceImpl extends ServiceImpl<TaskSubSupplierDetailMapper, TaskSubSupplierDetailPO> implements TaskSubSupplierDetailService, Serializable {


    @Autowired
    private TaskSubSupplierMapper taskSubSupplierMapper;

    /**
     * 查询供应商申报明细
     *
     * @param requestForSubSupplierDetail
     * @return
     */
    @Override
    public ResponseForSubSupplierDetail querySubSupplierDetailsByParams(RequestForSubSupplierDetail requestForSubSupplierDetail) {
        ResponseForSubSupplierDetail responseForSubSupplierDetail = new ResponseForSubSupplierDetail();
        List<TaskSubSupplierDetailPO> taskSubSupplierDetailPOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubSupplierDetailPO());
        //判断是否包含主键ID条件
        if (null != requestForSubSupplierDetail.getId()) {
            wrapper.andNew("id = {0}", requestForSubSupplierDetail.getId());
        }
        //判断是否包含供应商子任务ID条件
        if (null != requestForSubSupplierDetail.getSubSupplierId()) {
            //如果是合并任务，需要查询出起子任务下的明细
            String str = requestForSubSupplierDetail.getSubSupplierId() + "";
            //根据合并id获取子任务ID集合
            List<Long> mergedSubIds = taskSubSupplierMapper.querySubSupplierIdsByMergeIds(str);
            mergedSubIds.add(requestForSubSupplierDetail.getSubSupplierId());
            wrapper.in("task_sub_supplier_id", mergedSubIds);
        }
        //雇员编号模糊查询条件
        if (StrKit.notBlank(requestForSubSupplierDetail.getEmployeeNo())) {
            wrapper.like("employee_no", requestForSubSupplierDetail.getEmployeeNo());
        }
        //雇员名称模糊查询条件
        if (StrKit.notBlank(requestForSubSupplierDetail.getEmployeeName())) {
            wrapper.like("employee_name", requestForSubSupplierDetail.getEmployeeName());
        }
        //证件类型
        if (StrKit.notBlank(requestForSubSupplierDetail.getIdType())) {
            wrapper.andNew("id_type = {0}", requestForSubSupplierDetail.getIdType());
        }
        //证件号
        if (StrKit.notBlank(requestForSubSupplierDetail.getIdNo())) {
            wrapper.like("id_no", requestForSubSupplierDetail.getIdNo());
        }
        //页签
        if (StrKit.notBlank(requestForSubSupplierDetail.getTabType())) {
            wrapper.andNew("is_combined = {0} ", requestForSubSupplierDetail.getTabType());
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        //判断是否分页
        if (null != requestForSubSupplierDetail.getPageSize() && null != requestForSubSupplierDetail.getCurrentNum()) {
            Page<TaskSubSupplierDetailPO> pageInfo = new Page<>(requestForSubSupplierDetail.getCurrentNum(), requestForSubSupplierDetail.getPageSize());
            taskSubSupplierDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubSupplierDetailPOList);
            //获取证件类型中文名和所得项目中文名
            for (TaskSubSupplierDetailPO p : taskSubSupplierDetailPOList) {
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, p.getIncomeSubject()));
            }
            responseForSubSupplierDetail.setRowList(taskSubSupplierDetailPOList);
            responseForSubSupplierDetail.setTotalNum(pageInfo.getTotal());
            responseForSubSupplierDetail.setCurrentNum(requestForSubSupplierDetail.getCurrentNum());
            responseForSubSupplierDetail.setPageSize(requestForSubSupplierDetail.getPageSize());
        } else {
            taskSubSupplierDetailPOList = baseMapper.selectList(wrapper);
            //获取证件类型中文名和所得项目中文名
            for (TaskSubSupplierDetailPO p : taskSubSupplierDetailPOList) {
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, p.getIncomeSubject()));
            }
            responseForSubSupplierDetail.setRowList(taskSubSupplierDetailPOList);
        }
        return responseForSubSupplierDetail;
    }
}
