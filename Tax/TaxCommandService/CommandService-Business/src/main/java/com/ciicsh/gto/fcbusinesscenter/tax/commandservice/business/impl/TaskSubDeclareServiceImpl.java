package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class TaskSubDeclareServiceImpl extends ServiceImpl<TaskSubDeclareMapper, TaskSubDeclarePO> implements TaskSubDeclareService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubDeclareServiceImpl.class);

    /**
     * 当期
     */
    private static final String CURRENT_PAN = "currentPan";
    /**
     * 当期前
     */
    private static final String CURRENT_BEFORE_PAN = "currentBeforePan";
    /**
     * 当期后
     */
    private static final String CURRENT_AFTER_PAN = "currentAfterPan";

    @Override
    public ResponseForTaskSubDeclare queryTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare) {

        ResponseForTaskSubDeclare responseForTaskSubDeclare = new ResponseForTaskSubDeclare();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubDeclarePO());
        //判断是否包含申报账户条件
        if(StrKit.isNotEmpty(requestForTaskSubDeclare.getDeclareAccount())){
            wrapper.like("declare_account",requestForTaskSubDeclare.getDeclareAccount());
        }
        //判断是否包含管理方名称条件
        if(StrKit.isNotEmpty(requestForTaskSubDeclare.getManagerName())){
            wrapper.like("manager_name",requestForTaskSubDeclare.getManagerName());
        }
        //判断是否包含个税期间条件
        if (StrKit.notBlank(requestForTaskSubDeclare.getPeriod())) {
            wrapper.andNew("period = {0} ", LocalDate.parse(requestForTaskSubDeclare.getPeriod()));
        }
        //期间类型currentPan,currentBeforePan,currentAfterPan
        if (StrKit.notBlank(requestForTaskSubDeclare.getPeriodType())) {
            String currentDateStr = DateTimeKit.format(new Date(), "YYYY-MM") + "-01";
            if (CURRENT_PAN.equals(requestForTaskSubDeclare.getPeriodType())) {
                wrapper.andNew("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (CURRENT_BEFORE_PAN.equals(requestForTaskSubDeclare.getPeriodType())) {
                wrapper.andNew("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (CURRENT_AFTER_PAN.equals(requestForTaskSubDeclare.getPeriodType())) {
                wrapper.andNew("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        //任务状态
        if(StrKit.notBlank(requestForTaskSubDeclare.getStatusType())){
            wrapper.andNew("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE,requestForTaskSubDeclare.getStatusType().toUpperCase()));
        }
        //申报子任务ID为空
        wrapper.isNull("task_sub_declare_id");
        //是否可用
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time",false);
        Page<TaskSubDeclarePO> page = new Page<TaskSubDeclarePO>(requestForTaskSubDeclare.getCurrentNum(),requestForTaskSubDeclare.getPageSize());
        List<TaskSubDeclarePO> taskSubDeclarePOList = baseMapper.selectPage(page, wrapper);
        responseForTaskSubDeclare.setRowList(taskSubDeclarePOList);
        responseForTaskSubDeclare.setTotalNum(page.getTotal());
        responseForTaskSubDeclare.setCurrentNum(requestForTaskSubDeclare.getCurrentNum());
        responseForTaskSubDeclare.setPageSize(requestForTaskSubDeclare.getPageSize());
        return responseForTaskSubDeclare;
    }

    /**
     * 合并申报子任务
     * @param requestForTaskSubDeclare
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergeTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare) {
        List<TaskSubDeclarePO> taskSubDeclarePOList = new ArrayList<>();
        StringBuffer sbCombinedParams = new StringBuffer();
        //未合并的id集合
        List<Long> unMergeIds = new ArrayList<>();
        //如果完税凭证子任务数组不为空则查询数组内ID的任务信息
        if (requestForTaskSubDeclare.getSubDeclareIds() != null && !"".equals(requestForTaskSubDeclare.getSubDeclareIds())) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclarePO());
            wrapper.isNull("task_sub_declare_id");
            wrapper.andNew("is_active = {0} ", true);
            wrapper.in("id", requestForTaskSubDeclare.getSubDeclareIds());
            wrapper.orderBy("modified_time", false);
            wrapper.orderBy("created_time", false);
            taskSubDeclarePOList = baseMapper.selectList(wrapper);
        }
        if(taskSubDeclarePOList.size() > 0 ){
            //个税总金额
            BigDecimal taxAmount = new  BigDecimal(0);
            //滞纳金
            BigDecimal overdue = new  BigDecimal(0);
            //罚金
            BigDecimal fine = new  BigDecimal(0);
            //总人数
            int headcount = 0;
            //中方人数
            int chineseNum = 0;
            //外方人数
            int foreignerNum = 0;
            //计算总人数，中方人数，外方人数
            for (TaskSubDeclarePO taskSubDeclarePO : taskSubDeclarePOList) {
                //如果总金额为null,默认为0
                if(taskSubDeclarePO.getTaxAmount() != null && !"".equals(taskSubDeclarePO.getTaxAmount())){
                    taxAmount = taxAmount.add(taskSubDeclarePO.getTaxAmount());
                }
                //如果滞纳金为null,默认为0
                if(taskSubDeclarePO.getOverdue()!= null && !"".equals(taskSubDeclarePO.getOverdue())){
                    overdue = overdue.add(taskSubDeclarePO.getOverdue());
                }
                //如果罚金为null,默认为0
                if(taskSubDeclarePO.getFine()!= null && !"".equals(taskSubDeclarePO.getFine())){
                    fine = fine.add(taskSubDeclarePO.getFine());
                }
                //如果总人数为null,默认为0 )
                if (taskSubDeclarePO.getHeadcount() != null && !"".equals(taskSubDeclarePO.getHeadcount())) {
                    headcount += taskSubDeclarePO.getHeadcount();
                } else {
                    headcount += 0;
                }
                //如果中方人数为null，默认为0
                if (taskSubDeclarePO.getChineseNum() != null && !"".equals(taskSubDeclarePO.getChineseNum())) {
                    chineseNum += taskSubDeclarePO.getChineseNum();
                } else {
                    chineseNum += 0;
                }
                //如果外方人数为null,默认为0
                if (taskSubDeclarePO.getForeignerNum() != null && !"".equals(taskSubDeclarePO.getForeignerNum())) {
                    foreignerNum += taskSubDeclarePO.getForeignerNum();
                } else {
                    foreignerNum += 0;
                }
                //判断是不是合并后的任务
                if (taskSubDeclarePO.getTaskMainId() == null || "".equals(taskSubDeclarePO.getTaskMainId())) {
                    sbCombinedParams.append(taskSubDeclarePO.getId() + ",");
                } else {
                    unMergeIds.add(taskSubDeclarePO.getId());
                }
            }
            //合并后的申报信息
            TaskSubDeclarePO taskSubDeclare = new TaskSubDeclarePO();
            //设置任务编号
            taskSubDeclare.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_SUB_DECLARE));
            //设置申报账户
            taskSubDeclare.setDeclareAccount(taskSubDeclarePOList.get(0).getDeclareAccount());
            //设置个税期间
            taskSubDeclare.setPeriod(taskSubDeclarePOList.get(0).getPeriod());
            //设置总金额
            taskSubDeclare.setTaxAmount(taxAmount);
            //设置滞纳金
            taskSubDeclare.setOverdue(overdue);
            //设置罚金
            taskSubDeclare.setFine(fine);
            //设置总人数
            taskSubDeclare.setHeadcount(headcount);
            //设置中方人数
            taskSubDeclare.setChineseNum(chineseNum);
            //设置外方人数
            taskSubDeclare.setForeignerNum(foreignerNum);
            //设置任务状态
            taskSubDeclare.setStatus(taskSubDeclarePOList.get(0).getStatus());
            //设置创建人
            taskSubDeclare.setCreatedBy(requestForTaskSubDeclare.getModifiedBy());
            //设置是否为合并任务
            taskSubDeclare.setCombined(true);
            //新增申报子任务
            baseMapper.insert(taskSubDeclare);

            if (sbCombinedParams.length() > 0) {
                String str = sbCombinedParams.substring(0, sbCombinedParams.length() - 1);
                //根据合并id获取子任务ID集合
                List<Long> mergedSubIds = baseMapper.querySubDeclareIdsByMergeIds(str);
                if (mergedSubIds.size() > 0) {
                    unMergeIds.addAll(mergedSubIds);
                }
                //将合并的id置为不可用
                TaskSubDeclarePO taskSubDeclarePOMerge = new TaskSubDeclarePO();
                taskSubDeclarePOMerge.setActive(false);
                EntityWrapper wrapperPO = new EntityWrapper();
                wrapperPO.setEntity(new TaskSubDeclarePO());
                wrapperPO.in(" id ",str);
                baseMapper.update(taskSubDeclarePOMerge,wrapperPO);

            }
            //修改申报子任务
            TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
            //申报子任务ID
            taskSubDeclarePO.setTaskSubDeclareId(taskSubDeclare.getId());
            //修改人
            taskSubDeclarePO.setModifiedBy(requestForTaskSubDeclare.getModifiedBy());
            //修改时间
            taskSubDeclarePO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclarePO());
            wrapper.andNew("status = {0}", "01");
            wrapper.andNew("is_active = {0}", true);
            //将list转为数组
            Long[] ids = unMergeIds.toArray(new Long[unMergeIds.size()]);
            wrapper.in("id", ids);
            baseMapper.update(taskSubDeclarePO, wrapper);
        }

    }

    /**
     * 拆分申报子任务
     * @param requestForTaskSubDeclare
     */
    @Override
    public void splitSubDeclare(RequestForTaskSubDeclare requestForTaskSubDeclare) {
        if (requestForTaskSubDeclare.getId() != null && !"".equals(requestForTaskSubDeclare.getId())) {
            TaskSubDeclarePO taskSubDeclarePOMerge = new TaskSubDeclarePO();
            //主键ID
            taskSubDeclarePOMerge.setId(requestForTaskSubDeclare.getId());
            //修改人
            taskSubDeclarePOMerge.setModifiedBy(requestForTaskSubDeclare.getModifiedBy());
            //修改时间
            taskSubDeclarePOMerge.setModifiedTime(LocalDateTime.now());
            //是否可用
            taskSubDeclarePOMerge.setActive(false);
            //将合并的任务置为失效状态
            baseMapper.updateById(taskSubDeclarePOMerge);

            TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
            //设置合并后的任务ID为空
            taskSubDeclarePO.setTaskSubDeclareId(null);
            //修改人
            taskSubDeclarePO.setModifiedBy(requestForTaskSubDeclare.getModifiedBy());
            //修改时间
            taskSubDeclarePO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclarePO());
            wrapper.andNew("is_active = {0}", true);
            wrapper.andNew("task_sub_declare_id = {0}",requestForTaskSubDeclare.getId());
            //根据合并的ID将原子任务的合并ID置为空
            baseMapper.update(taskSubDeclarePO,wrapper);

        }
    }

    /**
     * 根据申报ID查询申报信息
     * @param subDeclareId
     * @return
     */
    @Override
    public TaskSubDeclarePO queryTaskSubDeclaresById(long subDeclareId) {
        TaskSubDeclarePO taskSubDeclarePO = baseMapper.selectById(subDeclareId);
        return taskSubDeclarePO;
    }

}
