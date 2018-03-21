package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@Service
public class TaskSubDeclareDetailServiceImpl extends ServiceImpl<TaskSubDeclareDetailMapper, TaskSubDeclareDetailPO> implements TaskSubDeclareDetailService, Serializable {


    @Autowired
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
        wrapper.in("task_sub_declare_id", ids);
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
            //如果是合并任务，需要查询出起子任务下的明细
            String str = requestForSubDeclareDetail.getSubDeclareId() + "";
            //根据合并id获取子任务ID集合
            List<Long> mergedSubIds = taskSubDeclareMapper.querySubDeclareIdsByMergeIds(str);
            mergedSubIds.add(requestForSubDeclareDetail.getSubDeclareId());
            wrapper.in("task_sub_declare_id", mergedSubIds);
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
        //页签
        if (StrKit.notBlank(requestForSubDeclareDetail.getTabType())) {
            wrapper.andNew("is_combined = {0} ", requestForSubDeclareDetail.getTabType());
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("modified_time", false);
        //判断是否分页
        if (null != requestForSubDeclareDetail.getPageSize() && null != requestForSubDeclareDetail.getCurrentNum()) {
            Page<TaskSubDeclareDetailPO> pageInfo = new Page<>(requestForSubDeclareDetail.getCurrentNum(), requestForSubDeclareDetail.getPageSize());
            taskSubDeclareDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubDeclareDetailPOList);
            //获取证件类型中文名和所得项目中文名
            for (TaskSubDeclareDetailPO p : taskSubDeclareDetailPOList) {
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, p.getIncomeSubject()));
            }
            responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
            responseForSubDeclareDetail.setTotalNum(pageInfo.getTotal());
            responseForSubDeclareDetail.setCurrentNum(requestForSubDeclareDetail.getCurrentNum());
            responseForSubDeclareDetail.setPageSize(requestForSubDeclareDetail.getPageSize());
        } else {
            taskSubDeclareDetailPOList = baseMapper.selectList(wrapper);
            //获取证件类型中文名和所得项目中文名
            for (TaskSubDeclareDetailPO p : taskSubDeclareDetailPOList) {
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, p.getIdType()));
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, p.getIncomeSubject()));
            }
            responseForSubDeclareDetail.setRowList(taskSubDeclareDetailPOList);
        }
        return responseForSubDeclareDetail;
    }

    /**
     * 根据批量完成申报ID查询未确认的申报明细数目
     *
     * @param subDeclareIds
     * @return
     */
    @Override
    public int selectCount(String[] subDeclareIds) {
        List<Long> ids = new ArrayList<>();
        //根据批量ID数组查询申报任务信息
        if (subDeclareIds != null && !"".equals(subDeclareIds)) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclarePO());
            wrapper.isNull("task_sub_declare_id");
            wrapper.andNew("is_active = {0} ", true);
            wrapper.in("id", subDeclareIds);
            wrapper.orderBy("modified_time", false);
            wrapper.orderBy("created_time", false);
            List<TaskSubDeclarePO> taskSubDeclarePOList = taskSubDeclareMapper.selectList(wrapper);
            //通过stream过滤出合并任务id
            List<Long> mergeIds = taskSubDeclarePOList.stream().filter(x -> x.getCombined()).map(m -> m.getId()).collect(Collectors.toList());
            //根据合并任务ID查询出合并前的任务ID
            EntityWrapper wrapper1 = new EntityWrapper();
            wrapper1.setEntity(new TaskSubDeclarePO());
            wrapper1.in("task_sub_declare_id", mergeIds);
            List<TaskSubDeclarePO> taskSubDeclarePOList1 = taskSubDeclareMapper.selectList(wrapper1);
            List<Long> subIds = taskSubDeclarePOList1.stream().map(m -> m.getId()).collect(Collectors.toList());
            ids.addAll(subIds);
            //未合并任务id
            List<Long> unMergeIds = taskSubDeclarePOList.stream().filter(x -> !x.getCombined()).map(m -> m.getId()).collect(Collectors.toList());
            ids.addAll(unMergeIds);
        }
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("is_combine_confirmed = {0}", false);
        wrapper.andNew("is_combined = {0}", true);
        wrapper.in("task_sub_declare_id", ids);
        int count = baseMapper.selectCount(wrapper);
        return count;
    }
}
