package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubSupplierMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@Service
public class TaskSubSupplierServiceImpl extends ServiceImpl<TaskSubSupplierMapper, TaskSubSupplierPO> implements TaskSubSupplierService, Serializable {

    @Autowired
    public TaskNoService taskNoService;

    @Autowired
    public TaskSubSupplierDetailServiceImpl taskSubSupplierDetailService;

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

    /**
     * 查询供应商子任务
     *
     * @param requestForTaskSubSupplier
     * @return
     */
    @Override
    public ResponseForTaskSubSupplier queryTaskSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier) {
        ResponseForTaskSubSupplier responseForTaskSubSupplier = new ResponseForTaskSubSupplier();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubSupplierPO());
        //判断是否包含申报账户条件
        if (StrKit.isNotEmpty(requestForTaskSubSupplier.getDeclareAccount())) {
            wrapper.like("declare_account", requestForTaskSubSupplier.getDeclareAccount());
        }
        //判断是否包含管理方名称条件
        if (StrKit.isNotEmpty(requestForTaskSubSupplier.getManagerName())) {
            wrapper.like("manager_name", requestForTaskSubSupplier.getManagerName());
        }
        //判断是否包含个税期间条件
        if (StrKit.notBlank(requestForTaskSubSupplier.getPeriod())) {
            wrapper.andNew("period = {0} ", LocalDate.parse(requestForTaskSubSupplier.getPeriod()));
        }
        //判断是否包含供应商名称条件
        if (StrKit.isNotEmpty(requestForTaskSubSupplier.getSupportName())) {
            wrapper.like("support_name", requestForTaskSubSupplier.getSupportName());
        }
        //期间类型currentPan,currentBeforePan,currentAfterPan
        if (StrKit.notBlank(requestForTaskSubSupplier.getPeriodType())) {
            String currentDateStr = DateTimeKit.format(new Date(), "YYYY-MM") + "-01";
            if (CURRENT_PAN.equals(requestForTaskSubSupplier.getPeriodType())) {
                wrapper.andNew("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (CURRENT_BEFORE_PAN.equals(requestForTaskSubSupplier.getPeriodType())) {
                wrapper.andNew("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (CURRENT_AFTER_PAN.equals(requestForTaskSubSupplier.getPeriodType())) {
                wrapper.andNew("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        //任务状态
        if (StrKit.notBlank(requestForTaskSubSupplier.getStatusType())) {
            wrapper.andNew("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE, requestForTaskSubSupplier.getStatusType().toUpperCase()));
        }
        //账户类型(00:独立户,01:大库)
        if (StrKit.notBlank(requestForTaskSubSupplier.getAccountType())) {
            wrapper.andNew("account_type = {0}", requestForTaskSubSupplier.getAccountType());
        }
        //供应商子任务ID为空
        wrapper.isNull("task_sub_supplier_id");
        //是否可用
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        Page<TaskSubSupplierPO> page = new Page<TaskSubSupplierPO>(requestForTaskSubSupplier.getCurrentNum(), requestForTaskSubSupplier.getPageSize());
        List<TaskSubSupplierPO> taskSubSupplierPOList = baseMapper.selectPage(page, wrapper);
        responseForTaskSubSupplier.setRowList(taskSubSupplierPOList);
        responseForTaskSubSupplier.setTotalNum(page.getTotal());
        responseForTaskSubSupplier.setCurrentNum(requestForTaskSubSupplier.getCurrentNum());
        responseForTaskSubSupplier.setPageSize(requestForTaskSubSupplier.getPageSize());
        return responseForTaskSubSupplier;
    }

    /**
     * 根据供应商子任务ID查询供应商信息
     *
     * @param subSupplierId
     * @return
     */
    @Override
    public TaskSubSupplierPO querySupplierDetailsById(long subSupplierId) {
        TaskSubSupplierPO taskSubSupplierPO = baseMapper.selectById(subSupplierId);
        return taskSubSupplierPO;
    }

    /**
     * 合并全国委托供应商任务
     *
     * @param requestForTaskSubSupplier
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergeTaskSubSuppliers(RequestForTaskSubSupplier requestForTaskSubSupplier) {
        List<TaskSubSupplierPO> taskSubSupplierPOList = new ArrayList<>();
        StringBuffer sbCombinedParams = new StringBuffer();
        //未合并的id集合
        List<Long> unMergeIds = new ArrayList<>();
        //如果供应商子任务数组不为空则查询数组内ID的任务信息
        if (requestForTaskSubSupplier.getSubSupplierIds() != null && !"".equals(requestForTaskSubSupplier.getSubSupplierIds())) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubSupplierPO());
            wrapper.isNull("task_sub_supplier_id");
            wrapper.andNew("is_active = {0} ", true);
            wrapper.in("id", requestForTaskSubSupplier.getSubSupplierIds());
            wrapper.orderBy("modified_time", false);
            wrapper.orderBy("created_time", false);
            taskSubSupplierPOList = baseMapper.selectList(wrapper);
        }
        if (taskSubSupplierPOList.size() > 0) {
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
            for (TaskSubSupplierPO taskSubSupplierPO : taskSubSupplierPOList) {
                //如果总金额为null,默认为0
                if (taskSubSupplierPO.getTaxAmount() != null && !"".equals(taskSubSupplierPO.getTaxAmount())) {
                    taxAmount = taxAmount.add(taskSubSupplierPO.getTaxAmount());
                }
                //如果滞纳金为null,默认为0
                if (taskSubSupplierPO.getOverdue() != null && !"".equals(taskSubSupplierPO.getOverdue())) {
                    overdue = overdue.add(taskSubSupplierPO.getOverdue());
                }
                //如果罚金为null,默认为0
                if (taskSubSupplierPO.getFine() != null && !"".equals(taskSubSupplierPO.getFine())) {
                    fine = fine.add(taskSubSupplierPO.getFine());
                }
                //如果总人数为null,默认为0 )
                if (taskSubSupplierPO.getHeadcount() != null && !"".equals(taskSubSupplierPO.getHeadcount())) {
                    headcount += taskSubSupplierPO.getHeadcount();
                } else {
                    headcount += 0;
                }
                //如果中方人数为null，默认为0
                if (taskSubSupplierPO.getChineseNum() != null && !"".equals(taskSubSupplierPO.getChineseNum())) {
                    chineseNum += taskSubSupplierPO.getChineseNum();
                } else {
                    chineseNum += 0;
                }
                //如果外方人数为null,默认为0
                if (taskSubSupplierPO.getForeignerNum() != null && !"".equals(taskSubSupplierPO.getForeignerNum())) {
                    foreignerNum += taskSubSupplierPO.getForeignerNum();
                } else {
                    foreignerNum += 0;
                }
                //判断是不是合并后的任务
                if (taskSubSupplierPO.getCombined()) {
                    sbCombinedParams.append(taskSubSupplierPO.getId() + ",");
//                    //先将合并后的供应商子任务拆分
//                    RequestForTaskSubSupplier requestForTaskSubSupplier1 = new RequestForTaskSubSupplier();
//                    //设置拆分任务ID
//                    requestForTaskSubSupplier1.setId(taskSubDeclarePO.getId());
//                    //设置修改人
//                    requestForTaskSubSupplier1.setModifiedBy(requestForTaskSubSupplier.getModifiedBy());
//                    List<Long> ids = this.splitSubSupplier(requestForTaskSubSupplier1, "merge");
//                    unMergeIds.addAll(ids);
                } else {
                    unMergeIds.add(taskSubSupplierPO.getId());
                }
            }
            TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
            //设置任务编号
            taskSubSupplierPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_SUPPLIER));
            //设置申报账户
            taskSubSupplierPO.setDeclareAccount(taskSubSupplierPOList.get(0).getDeclareAccount());
            //设置个税期间
            taskSubSupplierPO.setPeriod(taskSubSupplierPOList.get(0).getPeriod());
            //设置总金额
            taskSubSupplierPO.setTaxAmount(taxAmount);
            //设置滞纳金
            taskSubSupplierPO.setOverdue(overdue);
            //设置罚金
            taskSubSupplierPO.setFine(fine);
            //设置总人数
            taskSubSupplierPO.setHeadcount(headcount);
            //设置中方人数
            taskSubSupplierPO.setChineseNum(chineseNum);
            //设置外方人数
            taskSubSupplierPO.setForeignerNum(foreignerNum);
            //供应商编号supportNo
            taskSubSupplierPO.setSupportNo(taskSubSupplierPOList.get(0).getSupportNo());
            //供应商名称supportName
            taskSubSupplierPO.setSupportName(taskSubSupplierPOList.get(0).getSupportName());
            //设置任务状态
            taskSubSupplierPO.setStatus(taskSubSupplierPOList.get(0).getStatus());
            //设置是否为合并任务
            taskSubSupplierPO.setCombined(true);
            //设置账户类型(00:独立户,01:大库)
            taskSubSupplierPO.setAccountType(taskSubSupplierPOList.get(0).getAccountType() == null ? "01" : taskSubSupplierPOList.get(0).getAccountType());
            //新增供应商子任务
            baseMapper.insert(taskSubSupplierPO);

            //修改供应商子任务
            TaskSubSupplierPO taskSubSupplierPO1 = new TaskSubSupplierPO();
            //申报子任务ID
            taskSubSupplierPO1.setTaskSubSupplierId(taskSubSupplierPO.getId());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubSupplierPO());
            wrapper.andNew("status = {0}", "02");
            wrapper.andNew("is_active = {0}", true);
            //将list转为数组
            Long[] ids = unMergeIds.toArray(new Long[unMergeIds.size()]);
            wrapper.in("id", ids);
            baseMapper.update(taskSubSupplierPO1, wrapper);

        }
    }

    /**
     * 供应商子任务明细合并处理
     *
     * @param taskSubSupplierCombinedId 合并后的供应商子任务id
     * @param taskSubSupplierIds        被合并的供应商子任务ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void merge(Long taskSubSupplierCombinedId, Long[] taskSubSupplierIds) {

        List<TaskSubSupplierDetailPO> taskSubSupplierDetailPOList = new ArrayList<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("task_sub_supplier_id", taskSubSupplierIds);
        wrapper.andNew("is_active",true);
        taskSubSupplierDetailPOList = this.taskSubSupplierDetailService.selectList(wrapper);
//        taskSubDeclareDetailPOList = baseMapper.selectList(wrapper);

        //按照雇员、所得期间、所得项目分组
        Map<String, List<TaskSubSupplierDetailPO>> groupbys = taskSubSupplierDetailPOList.stream()
                .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::groupBys));

        for (Map.Entry<String, List<TaskSubSupplierDetailPO>> entry : groupbys.entrySet()) {

            if (entry.getValue().size() > 1) {

                TaskSubSupplierDetailPO taskSubSupplierDetailPO = new TaskSubSupplierDetailPO();
                taskSubSupplierDetailPO.setCombined(true);//为合并明细
                taskSubSupplierDetailPO.setTaskSubSupplierId(taskSubSupplierCombinedId);//供应商子任务id
                //taskSubDeclareDetailPO.setCalculationBatchDetailId(entry.getValue().get(0).getCalculationBatchDetailId());//批次明细id
                taskSubSupplierDetailPO.setEmployeeNo(entry.getValue().get(0).getEmployeeNo());//雇员编号
                taskSubSupplierDetailPO.setEmployeeName(entry.getValue().get(0).getEmployeeName());//雇员姓名
                taskSubSupplierDetailPO.setIdType(entry.getValue().get(0).getIdType());//证件类型
                taskSubSupplierDetailPO.setIdNo(entry.getValue().get(0).getIdNo());//证件编号
                taskSubSupplierDetailPO.setDeclareAccount(entry.getValue().get(0).getDeclareAccount());//申报账号
                taskSubSupplierDetailPO.setPayAccount(entry.getValue().get(0).getPayAccount());//缴纳账号
                taskSubSupplierDetailPO.setPeriod(entry.getValue().get(0).getPeriod());//个税期间
                taskSubSupplierDetailPO.setIncomeSubject(entry.getValue().get(0).getIncomeSubject());//所得项目
                //新建合并后的明细
                this.taskSubSupplierDetailService.insert(taskSubSupplierDetailPO);

                List<Long> taskSubSupplierDetailIds = new ArrayList<>();

                BigDecimal incomeTotal = new BigDecimal(0);//收入额
                BigDecimal deductRetirementInsurance = new BigDecimal(0);//基本养老保险费（税前扣除项目）
                BigDecimal deductMedicalInsurance = new BigDecimal(0);//基本医疗保险费（税前扣除项目）
                BigDecimal deductDlenessInsurance = new BigDecimal(0);//失业保险费（税前扣除项目）
                BigDecimal deductHouseFund = new BigDecimal(0);//住房公积金（税前扣除项目）
                BigDecimal deduction = new BigDecimal(0);//减除费用(3500;4800)
                BigDecimal taxAmount = new BigDecimal(0);//应纳税额

                List<TaskSubSupplierDetailPO> tps = entry.getValue();
                //计算合并后的各项值
                for (TaskSubSupplierDetailPO tp : tps) {

                    taskSubSupplierDetailIds.add(tp.getId());

                    incomeTotal = incomeTotal.add(tp.getIncomeTotal());
                    deductRetirementInsurance = deductRetirementInsurance.add(tp.getDeductRetirementInsurance());
                    deductMedicalInsurance = deductMedicalInsurance.add(tp.getDeductMedicalInsurance());
                    deductDlenessInsurance = deductDlenessInsurance.add(tp.getDeductDlenessInsurance());
                    deductHouseFund = deductHouseFund.add(tp.getDeductHouseFund());
                    deduction = deduction.add(tp.getDeduction());
                    taxAmount = taxAmount.add(tp.getTaxAmount());
                }

                taskSubSupplierDetailPO.setIncomeTotal(incomeTotal);
                taskSubSupplierDetailPO.setDeductRetirementInsurance(deductRetirementInsurance);
                taskSubSupplierDetailPO.setDeductMedicalInsurance(deductMedicalInsurance);
                taskSubSupplierDetailPO.setDeductDlenessInsurance(deductDlenessInsurance);
                taskSubSupplierDetailPO.setDeductHouseFund(deductHouseFund);
                taskSubSupplierDetailPO.setDeduction(deduction);
                taskSubSupplierDetailPO.setTaxAmount(taxAmount);
                //更新合并后数据值
                this.taskSubSupplierDetailService.updateById(taskSubSupplierDetailPO);

                EntityWrapper wrapper2 = new EntityWrapper();
                wrapper2.in("id", taskSubSupplierDetailIds);
                TaskSubSupplierDetailPO tsddp = new TaskSubSupplierDetailPO();
                tsddp.setTaskSubSupplierDetailId(taskSubSupplierDetailPO.getId());
                tsddp.setActive(false);
                this.taskSubSupplierDetailService.update(tsddp, wrapper2);//更新合并的明细

                TaskSubSupplierPO tsp = new TaskSubSupplierPO();
                tsp.setId(taskSubSupplierCombinedId);
                tsp.setHasCombined(true);
                this.baseMapper.updateById(tsp);//更新主任务信息，标记任务存在合并的明细
            }
        }
    }

    /**
     * 拆分供应商子任务
     *
     * @param requestForTaskSubSupplier
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Long> splitSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier, String type) {
        List<Long> list = new ArrayList<>();
        if (requestForTaskSubSupplier.getId() != null && !"".equals(requestForTaskSubSupplier.getId())) {
            if (!"only".equals(type)) {
                List<Long> mergedSubIds = baseMapper.querySubSupplierIdsByMergeIds(requestForTaskSubSupplier.getId().toString());
                list.addAll(mergedSubIds);
            }
            String modifiedBy = requestForTaskSubSupplier.getModifiedBy();
            //修改申报合并任务ID为失效
            baseMapper.updateSupplierByCombinedId(requestForTaskSubSupplier.getId(), modifiedBy, LocalDateTime.now());
            //修改合并前申报子任务为有效状态
            baseMapper.updateSupplierToActiveById(requestForTaskSubSupplier.getId(), modifiedBy, LocalDateTime.now());
//            //修改合并前申报明细为有效状态
//            baseMapper.updateDeclareDetailToActiveById(requestForTaskSubSupplier.getId(), modifiedBy, LocalDateTime.now());
//            //修改合并申报明细为失效状态
//            baseMapper.updateDeclareDetailById(requestForTaskSubSupplier.getId(), modifiedBy, LocalDateTime.now());
        }
        return list;
    }

    /**
     * 根据ID查询合并之前的申报子任务
     *
     * @param mergeId
     * @return
     */
    @Override
    public List<TaskSubSupplierPO> queryTaskSubSupplierByMergeId(long mergeId) {
        //构造查询条件
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubSupplierPO());
        wrapper.andNew("task_sub_supplier_id = {0}", mergeId);
        //修改时间排序
        wrapper.orderBy("modified_time", false);
        List<TaskSubSupplierPO> taskSubSupplierPOList = baseMapper.selectList(wrapper);
        return taskSubSupplierPOList;
    }

    /**
     * 批量完成供应商任务
     *
     * @param requestForTaskSubSupplier
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeTaskSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier) {
        if (requestForTaskSubSupplier.getSubSupplierIds() != null && !"".equals(requestForTaskSubSupplier.getSubSupplierIds())) {
            TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
            //设置任务状态
            taskSubSupplierPO.setStatus(requestForTaskSubSupplier.getStatus());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubSupplierPO());
            //任务为通过状态
            wrapper.andNew("status = {0} ", "02");
            //任务为可用状态
            wrapper.andNew("is_active = {0} ", true);
            //主任务ID IN条件
            wrapper.in("id", requestForTaskSubSupplier.getSubSupplierIds());
            //修改完税凭证子任务
            baseMapper.update(taskSubSupplierPO, wrapper);
//            //将数组转成集合(long[])
//            List<Long> declareIdList = Arrays.asList(requestForTaskSubSupplier.getSubSupplierIds()).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
//            //创建完税凭证自动任务
//            taskSubProofService.createTaskSubProof(declareIdList);
        }
    }
}
