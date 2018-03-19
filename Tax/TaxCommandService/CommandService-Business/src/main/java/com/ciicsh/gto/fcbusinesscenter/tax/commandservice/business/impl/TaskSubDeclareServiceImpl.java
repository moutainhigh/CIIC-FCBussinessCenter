package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@Service
public class TaskSubDeclareServiceImpl extends ServiceImpl<TaskSubDeclareMapper, TaskSubDeclarePO> implements TaskSubDeclareService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubDeclareServiceImpl.class);

    @Autowired
    private TaskSubProofService taskSubProofService;

    @Autowired
    private TaskSubDeclareDetailServiceImpl taskSubDeclareDetailService;

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
        if (StrKit.isNotEmpty(requestForTaskSubDeclare.getDeclareAccount())) {
            wrapper.like("declare_account", requestForTaskSubDeclare.getDeclareAccount());
        }
        //判断是否包含管理方名称条件
        if (StrKit.isNotEmpty(requestForTaskSubDeclare.getManagerName())) {
            wrapper.like("manager_name", requestForTaskSubDeclare.getManagerName());
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
        if (StrKit.notBlank(requestForTaskSubDeclare.getStatusType())) {
            wrapper.andNew("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE, requestForTaskSubDeclare.getStatusType().toUpperCase()));
        }
        //申报子任务ID为空
        wrapper.isNull("task_sub_declare_id");
        //是否可用
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("modified_time", false);
        Page<TaskSubDeclarePO> page = new Page<TaskSubDeclarePO>(requestForTaskSubDeclare.getCurrentNum(), requestForTaskSubDeclare.getPageSize());
        List<TaskSubDeclarePO> taskSubDeclarePOList = baseMapper.selectPage(page, wrapper);
        responseForTaskSubDeclare.setRowList(taskSubDeclarePOList);
        responseForTaskSubDeclare.setTotalNum(page.getTotal());
        responseForTaskSubDeclare.setCurrentNum(requestForTaskSubDeclare.getCurrentNum());
        responseForTaskSubDeclare.setPageSize(requestForTaskSubDeclare.getPageSize());
        return responseForTaskSubDeclare;
    }

    /**
     * 合并申报子任务
     *
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
        if (taskSubDeclarePOList.size() > 0) {
            //个税总金额
            BigDecimal taxAmount = new BigDecimal(0);
            //滞纳金
            BigDecimal overdue = new BigDecimal(0);
            //罚金
            BigDecimal fine = new BigDecimal(0);
            //总人数
            int headcount = 0;
            //中方人数
            int chineseNum = 0;
            //外方人数
            int foreignerNum = 0;
            //计算总人数，中方人数，外方人数
            for (TaskSubDeclarePO taskSubDeclarePO : taskSubDeclarePOList) {
                //如果总金额为null,默认为0
                if (taskSubDeclarePO.getTaxAmount() != null && !"".equals(taskSubDeclarePO.getTaxAmount())) {
                    taxAmount = taxAmount.add(taskSubDeclarePO.getTaxAmount());
                }
                //如果滞纳金为null,默认为0
                if (taskSubDeclarePO.getOverdue() != null && !"".equals(taskSubDeclarePO.getOverdue())) {
                    overdue = overdue.add(taskSubDeclarePO.getOverdue());
                }
                //如果罚金为null,默认为0
                if (taskSubDeclarePO.getFine() != null && !"".equals(taskSubDeclarePO.getFine())) {
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
                if (taskSubDeclarePO.getCombined()) {
                    sbCombinedParams.append(taskSubDeclarePO.getId() + ",");
                    //先将合并后的申报子任务拆分
                    RequestForTaskSubDeclare requestForTaskSubDeclare1 = new RequestForTaskSubDeclare();
                    //设置拆分任务ID
                    requestForTaskSubDeclare1.setId(taskSubDeclarePO.getId());
                    //设置修改人
                    requestForTaskSubDeclare1.setModifiedBy(requestForTaskSubDeclare.getModifiedBy());
                    List<Long> ids = this.splitSubDeclare(requestForTaskSubDeclare1, "merge");
                    unMergeIds.addAll(ids);
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
            //设置是否为合并任务
            taskSubDeclare.setCombined(true);
            //新增申报子任务
            baseMapper.insert(taskSubDeclare);

            //修改申报子任务
            TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
            //申报子任务ID
            taskSubDeclarePO.setTaskSubDeclareId(taskSubDeclare.getId());
            //修改时间
            taskSubDeclarePO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclarePO());
            wrapper.andNew("status = {0}", "02");
            wrapper.andNew("is_active = {0}", true);
            //将list转为数组
            Long[] ids = unMergeIds.toArray(new Long[unMergeIds.size()]);
            wrapper.in("id", ids);
            baseMapper.update(taskSubDeclarePO, wrapper);

            //申报子任务明细合并
            merge(taskSubDeclare.getId(), ids);
        }

    }

    /**
     * 申报子任务明细合并处理
     *
     * @param taskSubDeclareCombinedId 合并后的申报子任务id
     * @param taskSubDeclareIds        被合并的申报子任务ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void merge(Long taskSubDeclareCombinedId, Long[] taskSubDeclareIds) {

        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = new ArrayList<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("task_sub_declare_id", taskSubDeclareIds);
        taskSubDeclareDetailPOList = this.taskSubDeclareDetailService.selectList(wrapper);
//        taskSubDeclareDetailPOList = baseMapper.selectList(wrapper);

        //按照雇员、所得期间、所得项目分组
        Map<String, List<TaskSubDeclareDetailPO>> groupbys = taskSubDeclareDetailPOList.stream()
                .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::groupBys));

        for (Map.Entry<String, List<TaskSubDeclareDetailPO>> entry : groupbys.entrySet()) {

            if (entry.getValue().size() > 1) {

                TaskSubDeclareDetailPO taskSubDeclareDetailPO = new TaskSubDeclareDetailPO();
                taskSubDeclareDetailPO.setCombined(true);//为合并明细
                taskSubDeclareDetailPO.setTaskSubDeclareId(taskSubDeclareCombinedId);//申报子任务id
                //taskSubDeclareDetailPO.setCalculationBatchDetailId(entry.getValue().get(0).getCalculationBatchDetailId());//批次明细id
                taskSubDeclareDetailPO.setEmployeeNo(entry.getValue().get(0).getEmployeeNo());//雇员编号
                taskSubDeclareDetailPO.setEmployeeName(entry.getValue().get(0).getEmployeeName());//雇员姓名
                taskSubDeclareDetailPO.setIdType(entry.getValue().get(0).getIdType());//证件类型
                taskSubDeclareDetailPO.setIdNo(entry.getValue().get(0).getIdNo());//证件编号
                taskSubDeclareDetailPO.setDeclareAccount(entry.getValue().get(0).getDeclareAccount());//申报账号
                taskSubDeclareDetailPO.setPayAccount(entry.getValue().get(0).getPayAccount());//缴纳账号
                taskSubDeclareDetailPO.setPeriod(entry.getValue().get(0).getPeriod());//个税期间
                taskSubDeclareDetailPO.setIncomeSubject(entry.getValue().get(0).getIncomeSubject());//所得项目
                //新建合并后的明细
                this.taskSubDeclareDetailService.insert(taskSubDeclareDetailPO);

                List<Long> taskSubDeclareDetailIds = new ArrayList<>();

                BigDecimal incomeTotal = new BigDecimal(0);//收入额
                BigDecimal deductRetirementInsurance = new BigDecimal(0);//基本养老保险费（税前扣除项目）
                BigDecimal deductMedicalInsurance = new BigDecimal(0);//基本医疗保险费（税前扣除项目）
                BigDecimal deductDlenessInsurance = new BigDecimal(0);//失业保险费（税前扣除项目）
                BigDecimal deductHouseFund = new BigDecimal(0);//住房公积金（税前扣除项目）
                BigDecimal deduction = new BigDecimal(0);//减除费用(3500;4800)
                BigDecimal taxAmount = new BigDecimal(0);//应纳税额

                List<TaskSubDeclareDetailPO> tps = entry.getValue();
                //计算合并后的各项值
                for (TaskSubDeclareDetailPO tp : tps) {

                    taskSubDeclareDetailIds.add(tp.getId());

                    incomeTotal = incomeTotal.add(tp.getIncomeTotal());
                    deductRetirementInsurance = deductRetirementInsurance.add(tp.getDeductRetirementInsurance());
                    deductMedicalInsurance = deductMedicalInsurance.add(tp.getDeductMedicalInsurance());
                    deductDlenessInsurance = deductDlenessInsurance.add(tp.getDeductDlenessInsurance());
                    deductHouseFund = deductHouseFund.add(tp.getDeductHouseFund());
                    deduction = deduction.add(tp.getDeduction());
                    taxAmount = taxAmount.add(tp.getTaxAmount());
                }

                taskSubDeclareDetailPO.setIncomeTotal(incomeTotal);
                taskSubDeclareDetailPO.setDeductRetirementInsurance(deductRetirementInsurance);
                taskSubDeclareDetailPO.setDeductMedicalInsurance(deductMedicalInsurance);
                taskSubDeclareDetailPO.setDeductDlenessInsurance(deductDlenessInsurance);
                taskSubDeclareDetailPO.setDeductHouseFund(deductHouseFund);
                taskSubDeclareDetailPO.setDeduction(deduction);
                taskSubDeclareDetailPO.setTaxAmount(taxAmount);
                //更新合并后数据值
                this.taskSubDeclareDetailService.updateById(taskSubDeclareDetailPO);

                EntityWrapper wrapper2 = new EntityWrapper();
                wrapper2.in("id", taskSubDeclareDetailIds);
                TaskSubDeclareDetailPO tsddp = new TaskSubDeclareDetailPO();
                tsddp.setTaskSubDeclareDetailId(taskSubDeclareDetailPO.getId());
                tsddp.setActive(false);
                this.taskSubDeclareDetailService.update(tsddp, wrapper2);//更新合并的明细

                TaskSubDeclarePO tsp = new TaskSubDeclarePO();
                tsp.setId(taskSubDeclareCombinedId);
                tsp.setHasCombined(true);
                this.baseMapper.updateById(tsp);//更新主任务信息，标记任务存在合并的明细
            }
        }
    }

    /**
     * 申报子任务明细拆分处理
     *
     * @param taskSubDeclareCombinedId 合并后的申报子任务id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void unMerge(Long taskSubDeclareCombinedId) {

        //将合并后的明细设置为失效
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_sub_declare_id={0}", taskSubDeclareCombinedId);
        //获取合并后申报子任务ID明细集合
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = this.taskSubDeclareDetailService.selectList(wrapper);
        TaskSubDeclareDetailPO taskSubDeclareDetailPO = new TaskSubDeclareDetailPO();
        taskSubDeclareDetailPO.setActive(false);
        this.taskSubDeclareDetailService.update(taskSubDeclareDetailPO, wrapper);

        List<Long> ids = taskSubDeclareDetailPOList.stream().map(m -> m.getId()).collect(Collectors.toList());
        TaskSubDeclareDetailPO taskSubDeclareDetailPO1 = new TaskSubDeclareDetailPO();
        //将合并之前的明细置为有效状态
        taskSubDeclareDetailPO1.setActive(true);
        //把明细指向设为null
        taskSubDeclareDetailPO1.setTaskSubDeclareDetailId(null);
        EntityWrapper wrapperMerge = new EntityWrapper();
        wrapperMerge.setEntity(new TaskSubDeclareDetailPO());
        wrapperMerge.in("task_sub_declare_detail_id", ids);
        this.taskSubDeclareDetailService.update(taskSubDeclareDetailPO1, wrapperMerge);

    }

    /**
     * 拆分申报子任务
     *
     * @param requestForTaskSubDeclare
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Long> splitSubDeclare(RequestForTaskSubDeclare requestForTaskSubDeclare, String type) {
        List<Long> list = new ArrayList<>();
        if (requestForTaskSubDeclare.getId() != null && !"".equals(requestForTaskSubDeclare.getId())) {
            if (!"only".equals(type)) {
                List<Long> mergedSubIds = baseMapper.querySubDeclareIdsByMergeIds(requestForTaskSubDeclare.getId().toString());
                list.addAll(mergedSubIds);
            }
            String modifiedBy = requestForTaskSubDeclare.getModifiedBy();
            //修改申报合并任务ID为失效
            baseMapper.updateDeclareByCombinedId(requestForTaskSubDeclare.getId(), modifiedBy, LocalDateTime.now());
            //修改合并前申报子任务为有效状态
            baseMapper.updateDeclareToActiveById(requestForTaskSubDeclare.getId(), modifiedBy, LocalDateTime.now());
            //修改合并前申报明细为有效状态
            baseMapper.updateDeclareDetailToActiveById(requestForTaskSubDeclare.getId(), modifiedBy, LocalDateTime.now());
            //修改合并申报明细为失效状态
            baseMapper.updateDeclareDetailById(requestForTaskSubDeclare.getId(), modifiedBy, LocalDateTime.now());
        }
        return list;
    }

    /**
     * 根据申报ID查询申报信息
     *
     * @param subDeclareId
     * @return
     */
    @Override
    public TaskSubDeclarePO queryTaskSubDeclaresById(long subDeclareId) {
        TaskSubDeclarePO taskSubDeclarePO = baseMapper.selectById(subDeclareId);
        return taskSubDeclarePO;
    }

    /**
     * 批量完成申报任务
     *
     * @param requestForTaskSubDeclare
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare) {
        if (requestForTaskSubDeclare.getSubDeclareIds() != null && !"".equals(requestForTaskSubDeclare.getSubDeclareIds())) {
            TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
            //设置任务状态
            taskSubDeclarePO.setStatus(requestForTaskSubDeclare.getStatus());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclarePO());
            //任务为通过状态
            wrapper.andNew("status = {0} ", "02");
            //任务为可用状态
            wrapper.andNew("is_active = {0} ", true);
            //主任务ID IN条件
            wrapper.in("id", requestForTaskSubDeclare.getSubDeclareIds());
            //修改完税凭证子任务
            baseMapper.update(taskSubDeclarePO, wrapper);
            //将数组转成集合(long[])
            List<Long> declareIdList = Arrays.asList(requestForTaskSubDeclare.getSubDeclareIds()).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            //创建完税凭证自动任务
            taskSubProofService.createTaskSubProof(declareIdList);
        }
    }

    /**
     * 根据ID查询合并之前的申报子任务
     *
     * @param mergeId
     * @return
     */
    @Override
    public List<TaskSubDeclarePO> queryTaskSubDeclareByMergeId(long mergeId) {
        //构造查询条件
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubDeclarePO());
        wrapper.andNew("task_sub_declare_id = {0}", mergeId);
        //修改时间排序
        wrapper.orderBy("modified_time", false);
        List<TaskSubDeclarePO> taskSubDeclarePOList = baseMapper.selectList(wrapper);
        return taskSubDeclarePOList;
    }


}
