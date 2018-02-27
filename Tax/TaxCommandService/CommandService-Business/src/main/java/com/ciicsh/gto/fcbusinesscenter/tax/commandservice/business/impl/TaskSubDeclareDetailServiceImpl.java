package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;
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
public class TaskSubDeclareDetailServiceImpl extends ServiceImpl<TaskSubDeclareDetailMapper, TaskSubDeclareDetailPO> implements TaskSubDeclareDetailService, Serializable {


    @Autowired(required = false)
    private TaskSubDeclareMapper taskSubDeclareMapper;


    /**
     * 根据申报ID查询申报明细集合
     *
     * @param subDeclareId
     * @return
     */
    @Override
    public List<TaskSubDeclareDetailPO> querySubDeclareDetailList(Long subDeclareId) {
        List<TaskSubDeclareDetailPO> taskSubProofDetailPOList = new ArrayList<>();
        StringBuffer sbCombinedParams = new StringBuffer();
        sbCombinedParams.append(subDeclareId);
        //根据子任务ID判断该子任务是不是合并任务
        List<Long> longList = taskSubDeclareMapper.querySubDeclareIdsByMergeIds(subDeclareId.toString());
        if (longList.size() < 1) {
            longList.add(subDeclareId);
        }
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubDeclareDetailPO());
        Long[] ids = longList.toArray(new Long[longList.size()]);
        wrapper.in("id", ids);
        wrapper.andNew("is_active = {0}", true);
        taskSubProofDetailPOList = baseMapper.selectList(wrapper);
        return taskSubProofDetailPOList;
    }

    /**
     * 查询申报明细
     *
     * @param requestForSubDeclareDetail
     * @return
     */
    @Override
    public ResponseForSubDeclareDetail querySubDeclareDetailsByParams(RequestForSubDeclareDetail requestForSubDeclareDetail) {
        ResponseForSubDeclareDetail responseForSubDeclareDetail = new ResponseForSubDeclareDetail();
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubDeclareDetailPO());
        //判断是否包含主键ID条件
        if (null != requestForSubDeclareDetail.getId()) {
            wrapper.andNew("id = {0}", requestForSubDeclareDetail.getId());
        }
        //判断是否包含申报子任务ID条件
        if (null != requestForSubDeclareDetail.getSubDeclareId()) {
            wrapper.andNew("task_sub_declare_id = {0}", requestForSubDeclareDetail.getSubDeclareId());
        }
        //雇员编号模糊查询条件
        if (StrKit.notBlank(requestForSubDeclareDetail.getEmployeeNo())) {
            wrapper.like("employee_no", requestForSubDeclareDetail.getEmployeeNo());
        }
        //雇员名称模糊查询条件
        if (StrKit.notBlank(requestForSubDeclareDetail.getEmployeeName())) {
            wrapper.like("employee_name", requestForSubDeclareDetail.getEmployeeName());
        }
        //证件类型
        if (StrKit.notBlank(requestForSubDeclareDetail.getIdType())) {
            wrapper.andNew("id_type = {0}", requestForSubDeclareDetail.getIdType());
        }
        //证件号
        if (StrKit.notBlank(requestForSubDeclareDetail.getIdNo())) {
            wrapper.like("id_no", requestForSubDeclareDetail.getIdNo());
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        //判断是否分页
        if (null != requestForSubDeclareDetail.getPageSize() && null != requestForSubDeclareDetail.getCurrentNum()) {
            Page<TaskSubDeclareDetailPO> pageInfo = new Page<>(requestForSubDeclareDetail.getCurrentNum(), requestForSubDeclareDetail.getPageSize());
            taskSubDeclareDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubDeclareDetailPOList);
            //获取证件类型中文名和所得项目中文名
            for(TaskSubDeclareDetailPO p: taskSubDeclareDetailPOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
            }
            responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
            responseForSubDeclareDetail.setTotalNum(pageInfo.getTotal());
            responseForSubDeclareDetail.setCurrentNum(requestForSubDeclareDetail.getCurrentNum());
            responseForSubDeclareDetail.setPageSize(requestForSubDeclareDetail.getPageSize());
        } else {
            taskSubDeclareDetailPOList = baseMapper.selectList(wrapper);
            //获取证件类型中文名和所得项目中文名
            for(TaskSubDeclareDetailPO p: taskSubDeclareDetailPOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
            }
            responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
        }
        return responseForSubDeclareDetail;
    }
}
