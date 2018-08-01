package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubMoneyMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.DisposableChargeProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.DisposableChargeBusinessTypeEmum;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.DisposableChargeDetailProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.DisposableChargeProxyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@Service
public class TaskSubMoneyServiceImpl extends ServiceImpl<TaskSubMoneyMapper, TaskSubMoneyPO> implements TaskSubMoneyService, Serializable {

    @Autowired
    private TaskMainService taskMainService;

    @Autowired
    private DisposableChargeProxy disposableChargeProxy;

    @Autowired
    private CalculationBatchServiceImpl calculationBatchServiceImpl;

    /**
     * 条件查询划款子任务
     *
     * @param requestForSubMoney
     * @return
     */
    @Override
    public ResponseForSubMoney querySubMoney(RequestForSubMoney requestForSubMoney) {
        //当期
        String currentPan = "currentPan";
        //当期前
        String currentBeforePan = "currentBeforePan";
        //当期后
        String currentAfterPan = "currentAfterPan";
        ResponseForSubMoney responseForSubMoney = new ResponseForSubMoney();
        List<TaskSubMoneyPO> taskSubMoneyPOList;
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubMoneyPO());
        //判断是否包含主键ID条件
        if (null != requestForSubMoney.getId()) {
            wrapper.and("id = {0}", requestForSubMoney.getId());
        }
        //缴纳账户模糊查询条件
        if (StrKit.notBlank(requestForSubMoney.getPayAccountName())) {
            wrapper.like("pay_account_name", requestForSubMoney.getPayAccountName());
        }
        //管理方名称
        Optional.ofNullable(requestForSubMoney.getManagerNos()).ifPresent(managerNos -> {
            wrapper.in("manager_no",managerNos);
        });
        //期间类型currentPan,currentBeforePan,currentAfterPan
        if (StrKit.notBlank(requestForSubMoney.getPeriodType())) {
            String currentDateStr = DateTimeKit.format(new Date(), "YYYY-MM") + "-01";
            if (currentPan.equals(requestForSubMoney.getPeriodType())) {
                wrapper.and("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (currentBeforePan.equals(requestForSubMoney.getPeriodType())) {
                wrapper.and("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (currentAfterPan.equals(requestForSubMoney.getPeriodType())) {
                wrapper.and("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        //任务状态
        if (StrKit.notBlank(requestForSubMoney.getStatusType())) {
            wrapper.and("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE, requestForSubMoney.getStatusType().toUpperCase()));
        }
        //区域类型(00:本地,01:异地)
        if (StrKit.notBlank(requestForSubMoney.getAreaType())) {
            wrapper.and("area_type = {0}", requestForSubMoney.getAreaType());
        }
        wrapper.and("is_active = {0} ", true);
        wrapper.orderBy("modified_time", false);

        //判断是否分页
        if (null != requestForSubMoney.getPageSize() && null != requestForSubMoney.getCurrentNum()) {
            Page<TaskSubMoneyPO> pageInfo = new Page<>(requestForSubMoney.getCurrentNum(), requestForSubMoney.getPageSize());
            taskSubMoneyPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubMoneyPOList);
            //获取完税凭证任务状态中文名
            for (TaskSubMoneyPO p : taskSubMoneyPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
            }
            responseForSubMoney.setRowList(taskSubMoneyPOList);
            responseForSubMoney.setTotalNum(pageInfo.getTotal());
            responseForSubMoney.setCurrentNum(requestForSubMoney.getCurrentNum());
            responseForSubMoney.setPageSize(requestForSubMoney.getPageSize());
        } else {
            taskSubMoneyPOList = baseMapper.selectList(wrapper);
            //获取完税凭证任务状态中文名
            for (TaskSubMoneyPO p : taskSubMoneyPOList) {
                p.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, p.getStatus()));
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
            updateTaskSubMoneyStatus(requestForSubMoney);
            //根据划款子任务ID数组获取相关主任务ID
            List<Long> taskMainIds = this.getTaskMainIdsBySubMoneyIds(requestForSubMoney.getSubMoneyIds());
            calculationBatchServiceImpl.commitBatchNoToSalaryCal(taskMainIds);
        }
    }

    /**
     * 批量退回划款子任务
     *
     * @param requestForSubMoney
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean rejectTaskSubMoney(RequestForSubMoney requestForSubMoney) {
        Boolean flag = taskMainService.updateTaskMainStatus(requestForSubMoney.getMainIds());
        if(flag){
            if (requestForSubMoney.getSubMoneyIds() != null && !"".equals(requestForSubMoney.getSubMoneyIds())) {
                updateTaskSubMoneyStatus(requestForSubMoney);
            }
        }
        return flag;

    }

    /**
     * 修改划款任务状态
     *
     * @param requestForSubMoney
     */
    private void updateTaskSubMoneyStatus(RequestForSubMoney requestForSubMoney) {
        TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
        //任务状态
        taskSubMoneyPO.setStatus(requestForSubMoney.getStatus());
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubMoneyPO());
        wrapper.and("is_active = {0}", true);
        wrapper.in("id", requestForSubMoney.getSubMoneyIds());
        //修改划款子任务状态
        baseMapper.update(taskSubMoneyPO, wrapper);
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
        taskSubMoneyPO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubMoneyPO.getStatus()));
        return taskSubMoneyPO;
    }

    /**
     * 根据划款任务BO对象修改划款任务信息
     *
     * @param taskSubMoneyBO
     */
    @Override
    public void updateTaskSubMoneyById(TaskSubMoneyBO taskSubMoneyBO) {
        TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
        BeanUtils.copyProperties(taskSubMoneyBO, taskSubMoneyPO);
        baseMapper.updateById(taskSubMoneyPO);
    }

    /**
     * 更新划款滞纳金和罚金
     * @param subMoneyId
     * @param overdue
     * @param fine
     */
    @Override
    public void updateTaskSubMoneyOverdueAndFine(Long subMoneyId, BigDecimal overdue, BigDecimal fine) {
        TaskSubMoneyPO taskSubMoneyPO = baseMapper.selectById(subMoneyId);
        taskSubMoneyPO.setOverdue(overdue);
        taskSubMoneyPO.setFine(fine);
        baseMapper.updateById(taskSubMoneyPO);
    }

    /**
     * 获取划款子任务()来款情况列表
     * @param moneyIds
     * @return
     */
    @Override
    public DisposableChargeProxyDTO whetherHasMoneyBySubMoneyIds(Long[] moneyIds) {
        DisposableChargeProxyDTO disposableChargeProxyDTOReturn = new DisposableChargeProxyDTO();
        DisposableChargeProxyDTO disposableChargeProxyDTO = new DisposableChargeProxyDTO();
        List<DisposableChargeDetailProxyDTO> disposableChargeDetailProxyDTOS = new ArrayList<>();
        for(int i = 0;i < moneyIds.length;i++){
            Long moneyId = moneyIds[i];
            TaskSubMoneyPO taskSubMoneyPO = baseMapper.selectById(moneyId);
            //判断滞纳金是否来款
            if(taskSubMoneyPO.getOverdue() != null && taskSubMoneyPO.getOverdue().compareTo(BigDecimal.ZERO) == 1){
                DisposableChargeDetailProxyDTO disposableChargeDetailProxyDTO = new DisposableChargeDetailProxyDTO();
                disposableChargeDetailProxyDTO.setBusinessId(taskSubMoneyPO.getTaskNo());
                disposableChargeDetailProxyDTO.setBusinessTypeEnum(DisposableChargeBusinessTypeEmum.FC_TAX_LATE_FEE);
                disposableChargeDetailProxyDTOS.add(disposableChargeDetailProxyDTO);
            }
            //判断罚金是否来款
            if(taskSubMoneyPO.getFine() != null && taskSubMoneyPO.getFine().compareTo(BigDecimal.ZERO) == 1){
                DisposableChargeDetailProxyDTO disposableChargeDetailProxyDTO = new DisposableChargeDetailProxyDTO();
                disposableChargeDetailProxyDTO.setBusinessId(taskSubMoneyPO.getTaskNo());
                disposableChargeDetailProxyDTO.setBusinessTypeEnum(DisposableChargeBusinessTypeEmum.FC_TAX_FINE);
                disposableChargeDetailProxyDTOS.add(disposableChargeDetailProxyDTO);
            }
        }
        disposableChargeProxyDTO.setList(disposableChargeDetailProxyDTOS);
        JsonResult<DisposableChargeProxyDTO> jsonResult = disposableChargeProxy.disposableChargeIsReceived(disposableChargeProxyDTO);
        if ("0".equals(jsonResult.getCode())) {
            disposableChargeProxyDTOReturn = jsonResult.getData();
        }
        return disposableChargeProxyDTOReturn;
    }

    /**
     * 根据划款子任务ID和状态查询相关子任务状态的数目
     * @param subMoneyIds
     * @param status
     * @return
     */
    @Override
    public int selectRejectCount(String[] subMoneyIds, String status) {
        //根据划款子任务ID数组获取相关主任务ID
        List<Long> taskMainIds = this.getTaskMainIdsBySubMoneyIds(subMoneyIds);
        return taskMainService.querySubTaskNumByMainIdsAndStatus(taskMainIds,status);
    }

    /**
     * 根据划款子任务ID查询相关主任务ID
     * @param subMoneyIds
     * @return
     */
    public List<Long> getTaskMainIdsBySubMoneyIds(String[] subMoneyIds){
        //查询出相关划款子任务集合
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.andNew().in("id", subMoneyIds).or().in("task_sub_money_id", subMoneyIds);
        List<TaskSubMoneyPO> taskSubMoneyPOS = baseMapper.selectList(entityWrapper);
//            //筛选出没有合并任务的相关申报子任务
//            List<TaskSubMoneyPO> unMergeTaskSubMoneyList = taskSubMoneyPOS.stream().filter(item -> !item.getCombined()).collect(Collectors.toList());
        //筛选出对应主任务ID集合
        List<Long> taskMainIds = taskSubMoneyPOS.stream().map(TaskSubMoneyPO::getTaskMainId).collect(Collectors.toList());
        return taskMainIds;
    }
}
