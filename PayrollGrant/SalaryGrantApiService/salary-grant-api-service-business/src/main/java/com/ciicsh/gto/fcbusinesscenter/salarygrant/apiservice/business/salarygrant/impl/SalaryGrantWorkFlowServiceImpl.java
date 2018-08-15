package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantEmployeeService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.RejectTaskProcessBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.ResponseRejectBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskHistoryPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.EventName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.FCBizTransactionMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放工作流调用 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-10
 */
@Service
public class SalaryGrantWorkFlowServiceImpl implements SalaryGrantWorkFlowService {
    @Autowired
    LogClientService logClientService;
    @Autowired
    SalaryGrantTaskMapper salaryGrantTaskMapper;
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private SalaryGrantEmpHisOpt salaryGrantEmpHisOpt;
    @Autowired
    private FCBizTransactionMongoOpt fcBizTransactionMongoOpt;
    @Autowired
    SalaryGrantEmployeeService salaryGrantEmployeeService;

    /**
     *  任务单驳回处理
     * @author gaoyang
     * @date 2018-08-10
     * @param codeList
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean rejectTask(List<SalaryGrantTaskBO> codeList) {
        ResponseRejectBO rejectResult = new ResponseRejectBO();
        rejectResult.setProcessResult(true);
        Map<String, List<SalaryGrantTaskBO>> batchCodeMap = codeList.stream().collect(Collectors.groupingBy(SalaryGrantTaskBO::getBatchCode));
        batchCodeMap.forEach((batchCode,taskCodeList) -> {
            if(batchCode != null){
                if(!CollectionUtils.isEmpty(taskCodeList)){
                    List<SalaryGrantTaskBO> distinctList = taskCodeList.stream().collect(
                            Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getTaskCode()))),
                                    ArrayList::new));
                    EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                    subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", distinctList.get(0).getTaskCode());
                    List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                    if(!CollectionUtils.isEmpty(subTaskPOList)) {
                        EntityWrapper<SalaryGrantSubTaskPO> allSubTaskPOEntityWrapper = new EntityWrapper<>();
                        allSubTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_mode = 1 and is_active = 1", subTaskPOList.get(0).getSalaryGrantMainTaskCode());
                        List<SalaryGrantSubTaskPO> allSubTaskPOList = salaryGrantSubTaskMapper.selectList(allSubTaskPOEntityWrapper);
                        if(!CollectionUtils.isEmpty(allSubTaskPOList)){
                            if(distinctList.size() == allSubTaskPOList.size()){
                                RejectTaskProcessBO rejectTaskProcessBO = this.toProcessAllTask(subTaskPOList, allSubTaskPOList, distinctList, batchCode);
                                if(!rejectTaskProcessBO.isProcessResult()){
                                    rejectResult.setProcessResult(false);
                                    rejectResult.setTaskList(distinctList);
                                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("处理驳回信息：" + batchCode).setContent("处理失败！"));
                                }
                            }
                        }else{
                            rejectResult.setProcessResult(false);
                            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("查询驳回信息：" + subTaskPOList.get(0).getSalaryGrantMainTaskCode()).setContent("信息为空，查询失败！"));
                        }
                    }else{
                        rejectResult.setProcessResult(false);
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("查询驳回任务信息：" + taskCodeList.get(0).getTaskCode()).setContent("信息为空，查询失败！"));
                    }
                }
            }
        });
        return rejectResult.isProcessResult();
    }

    /**
     *  处理驳回信息
     * @author gaoyang
     * @date 2018-08-10
     * @param subTaskPOList
     * @param allSubTaskPOList
     * @param taskCodeList
     * @param batchCode
     * @return RejectTaskProcessBO
     */

    private RejectTaskProcessBO toProcessAllTask(List<SalaryGrantSubTaskPO> subTaskPOList, List<SalaryGrantSubTaskPO> allSubTaskPOList, List<SalaryGrantTaskBO> taskCodeList, String batchCode){
        RejectTaskProcessBO rejectTaskProcessBO = new RejectTaskProcessBO();
        List<SalaryGrantSubTaskPO> processTaskList = new ArrayList<>();
        EntityWrapper<SalaryGrantTaskPO> taskPOEntityWrapper = new EntityWrapper<>();
        taskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", subTaskPOList.get(0).getSalaryGrantMainTaskCode());
        List<SalaryGrantTaskPO> taskPOList = salaryGrantTaskMapper.selectList(taskPOEntityWrapper);
        if(!CollectionUtils.isEmpty(taskPOList)){
            SalaryGrantTaskPO mainTask = taskPOList.get(0);
            taskCodeList.parallelStream().forEach(taskCode ->{
                SalaryGrantSubTaskPO subTaskPO = allSubTaskPOList.stream().filter(salaryGrantSubTaskPO -> taskCode.getTaskCode().equals(salaryGrantSubTaskPO.getSalaryGrantSubTaskCode())).findFirst().orElse(null);
                if (!ObjectUtils.isEmpty(subTaskPO)) {
                    SalaryGrantTaskHistoryPO subTaskHistoryPO = subTaskPO2HistoryPO(subTaskPO);
                    subTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REJECT);
                    subTaskHistoryPO.setApprovedOpinion(SalaryGrantBizConsts.REJECT_MESSAGE);
                    subTaskHistoryPO.setOperatorUserId(SalaryGrantBizConsts.SYSTEM_EN);
                    subTaskHistoryPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
                    subTaskHistoryPO.setModifiedTime(new Date());
                    salaryGrantTaskHistoryMapper.insert(subTaskHistoryPO);

                    SalaryGrantEmployeeBO subEmployeeBO = new SalaryGrantEmployeeBO();
                    subEmployeeBO.setTaskCode(subTaskPO.getSalaryGrantSubTaskCode());
                    subEmployeeBO.setTaskType(subTaskPO.getTaskType());

                    if(!this.saveToHistory(subTaskHistoryPO.getSalaryGrantTaskHistoryId(), subEmployeeBO)){
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("插入子表雇员历史信息：" + subTaskHistoryPO.getSalaryGrantTaskHistoryId()).setContent("信息插入失败！"));
                        rejectTaskProcessBO.setProcessResult(false);
                        rejectTaskProcessBO.setProcessMessage("插入子表雇员历史信息失败");
                    }
                    salaryGrantSubTaskMapper.deleteById(subTaskPO.getSalaryGrantSubTaskId());
                    processTaskList.add(subTaskPO);
                }
            });

            if(taskCodeList.size() != processTaskList.size()){
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("处理驳回任务信息：" + batchCode).setContent("驳回任务未全部处理！"));
                rejectTaskProcessBO.setProcessResult(false);
                rejectTaskProcessBO.setProcessMessage("驳回任务未全部处理");
            }

            EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
            subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_mode in (2,3,4) and is_active = 1", mainTask.getSalaryGrantMainTaskCode());
            List<SalaryGrantSubTaskPO> subSTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);

            if(!CollectionUtils.isEmpty(subSTaskPOList)){
                subSTaskPOList.parallelStream().forEach(subSTaskPO ->{
                    SalaryGrantTaskHistoryPO subTaskHistoryPO = subTaskPO2HistoryPO(subSTaskPO);
                    subTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REJECT);
                    subTaskHistoryPO.setApprovedOpinion(SalaryGrantBizConsts.REJECT_MESSAGE);
                    subTaskHistoryPO.setOperatorUserId(SalaryGrantBizConsts.SYSTEM_EN);
                    subTaskHistoryPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
                    subTaskHistoryPO.setModifiedTime(new Date());
                    salaryGrantTaskHistoryMapper.insert(subTaskHistoryPO);

                    SalaryGrantEmployeeBO subEmployeeBO = new SalaryGrantEmployeeBO();
                    subEmployeeBO.setTaskCode(subSTaskPO.getSalaryGrantSubTaskCode());
                    subEmployeeBO.setTaskType(subSTaskPO.getTaskType());

                    if(!this.saveToHistory(subTaskHistoryPO.getSalaryGrantTaskHistoryId(), subEmployeeBO)){
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("插入子表雇员历史信息：" + subTaskHistoryPO.getSalaryGrantTaskHistoryId()).setContent("信息插入失败！"));
                        rejectTaskProcessBO.setProcessResult(false);
                        rejectTaskProcessBO.setProcessMessage("插入子表雇员历史信息失败");
                    }
                    salaryGrantSubTaskMapper.deleteById(subSTaskPO.getSalaryGrantSubTaskId());
                });
            }

            SalaryGrantTaskPO grantMainTaskPO = new SalaryGrantTaskPO();
            grantMainTaskPO.setSalaryGrantMainTaskId(mainTask.getSalaryGrantMainTaskId());
            grantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
            grantMainTaskPO.setOperatorUserId(SalaryGrantBizConsts.SYSTEM_EN);
            grantMainTaskPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
            grantMainTaskPO.setModifiedTime(new Date());
            salaryGrantTaskMapper.updateById(grantMainTaskPO);

            mainTask.setApprovedOpinion(SalaryGrantBizConsts.REJECT_MESSAGE);
            SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTask);
            mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REJECT);
            mainTaskHistoryPO.setOperatorUserId(SalaryGrantBizConsts.SYSTEM_EN);
            mainTaskHistoryPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
            mainTaskHistoryPO.setModifiedTime(new Date());
            salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

            SalaryGrantEmployeeBO mainEmployeeBO = new SalaryGrantEmployeeBO();
            mainEmployeeBO.setTaskCode(mainTask.getSalaryGrantMainTaskCode());
            mainEmployeeBO.setTaskType(SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK);

            if(!this.saveToHistory(mainTaskHistoryPO.getSalaryGrantTaskHistoryId(), mainEmployeeBO)){
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("插入主表雇员历史信息：" + mainTaskHistoryPO.getSalaryGrantTaskHistoryId()).setContent("信息插入失败！"));
                rejectTaskProcessBO.setProcessResult(false);
                rejectTaskProcessBO.setProcessMessage("插入主表雇员历史信息失败");
            }

            EntityWrapper<SalaryGrantEmployeePO> mainEmployeePOEntityWrapper = new EntityWrapper<>();
            mainEmployeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTask.getSalaryGrantMainTaskCode());
            List<SalaryGrantEmployeePO> mainTaskEmployeePOList = salaryGrantEmployeeMapper.selectList(mainEmployeePOEntityWrapper);

            mainTaskEmployeePOList.stream().forEach(salaryGrantEmployeePO -> {
                salaryGrantEmployeePO.setSalaryGrantSubTaskCode(null);
                salaryGrantEmployeePO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
                salaryGrantEmployeePO.setModifiedTime(new Date());
                salaryGrantEmployeeMapper.updateAllColumnById(salaryGrantEmployeePO);
            });

            if(fcBizTransactionMongoOpt.commitEvent(mainTask.getBatchCode(), mainTask.getGrantType(), EventName.FC_GRANT_EVENT, 0) > 0){
                rejectTaskProcessBO.setProcessResult(true);
            }else{
                rejectTaskProcessBO.setProcessResult(false);
                rejectTaskProcessBO.setProcessMessage("mongo修改主表信息失败");
            }
        }else{
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放驳回").setTitle("查询任务单主信息：" + subTaskPOList.get(0).getSalaryGrantMainTaskCode()).setContent("信息为空，查询失败！"));
            rejectTaskProcessBO.setProcessResult(false);
            rejectTaskProcessBO.setProcessMessage("查询任务单主信息失败");
        }

        return rejectTaskProcessBO;
    }

    /**
     * SalaryGrantMainTaskPO转换为SalaryGrantTaskHistoryPO
     *
     * @param salaryGrantMainTaskPO
     * @return
     */
    private SalaryGrantTaskHistoryPO mainTaskPO2HistoryPO(SalaryGrantTaskPO salaryGrantMainTaskPO) {
        SalaryGrantTaskHistoryPO salaryGrantTaskHistoryPO = new SalaryGrantTaskHistoryPO();
        salaryGrantTaskHistoryPO.setAdvanceType(salaryGrantMainTaskPO.getAdvanceType()); //垫付类型:1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付
        salaryGrantTaskHistoryPO.setAdversionType(salaryGrantMainTaskPO.getAdversionType()); //内转类型:1-AF转FC,2-BPO转FC,3-FC转AF,4-FC转BPO
        salaryGrantTaskHistoryPO.setApproveUserId(salaryGrantMainTaskPO.getApproveUserId()); //审核员
        salaryGrantTaskHistoryPO.setApprovedOpinion(salaryGrantMainTaskPO.getApprovedOpinion()); //审批意见
        salaryGrantTaskHistoryPO.setBalanceGrant(salaryGrantMainTaskPO.getBalanceGrant()); //结算发放标识:0-正常，1-垫付
        salaryGrantTaskHistoryPO.setBatchCode(salaryGrantMainTaskPO.getBatchCode()); //薪酬计算批次号
        salaryGrantTaskHistoryPO.setChineseCount(salaryGrantMainTaskPO.getChineseCount()); //中方发薪人数
        salaryGrantTaskHistoryPO.setCreatedBy(salaryGrantMainTaskPO.getCreatedBy()); //创建人
        salaryGrantTaskHistoryPO.setCreatedTime(salaryGrantMainTaskPO.getCreatedTime()); //创建时间
        salaryGrantTaskHistoryPO.setForeignerCount(salaryGrantMainTaskPO.getForeignerCount()); //外方发薪人数
        //发放账户编号 未设置
        //发放账户名称 未设置
        salaryGrantTaskHistoryPO.setGrantCycle(salaryGrantMainTaskPO.getGrantCycle()); //薪资周期
        salaryGrantTaskHistoryPO.setGrantDate(salaryGrantMainTaskPO.getGrantDate()); //薪资发放日期
        salaryGrantTaskHistoryPO.setGrantMode(salaryGrantMainTaskPO.getGrantMode()); //发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
        salaryGrantTaskHistoryPO.setGrantTime(salaryGrantMainTaskPO.getGrantTime()); //薪资发放时段:1-上午，2-下午
        salaryGrantTaskHistoryPO.setGrantType(salaryGrantMainTaskPO.getGrantType()); //发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
        salaryGrantTaskHistoryPO.setInvalidReason(salaryGrantMainTaskPO.getInvalidReason()); //失效原因
        salaryGrantTaskHistoryPO.setActive(true); //1-有效，0-无效
        salaryGrantTaskHistoryPO.setAdvance(salaryGrantMainTaskPO.getAdvance()); //是否垫付:0-否，1-是
        salaryGrantTaskHistoryPO.setAdversion(salaryGrantMainTaskPO.getAdversion()); //是否内转:0-非内转，1-内转
        salaryGrantTaskHistoryPO.setIncludeForeignCurrency(salaryGrantMainTaskPO.getIncludeForeignCurrency()); //外币发放标识:0-否，1-是

        salaryGrantTaskHistoryPO.setLocalGrantCount(salaryGrantMainTaskPO.getLocalGrantCount()); //中智上海发薪人数
        //主表编号 未设置
        salaryGrantTaskHistoryPO.setManagementId(salaryGrantMainTaskPO.getManagementId()); //管理方编号
        salaryGrantTaskHistoryPO.setManagementName(salaryGrantMainTaskPO.getManagementName()); //管理方名称
        salaryGrantTaskHistoryPO.setModifiedBy(salaryGrantMainTaskPO.getModifiedBy()); //最后修改人
        salaryGrantTaskHistoryPO.setModifiedTime(salaryGrantMainTaskPO.getModifiedTime()); //最后修改时间
        salaryGrantTaskHistoryPO.setOperatorUserId(salaryGrantMainTaskPO.getOperatorUserId()); //操作员
        salaryGrantTaskHistoryPO.setOriginBatchCode(salaryGrantMainTaskPO.getOriginBatchCode()); //参考批次号
        salaryGrantTaskHistoryPO.setPaymentTotalSum(salaryGrantMainTaskPO.getPaymentTotalSum()); //薪资发放总金额（RMB）
        salaryGrantTaskHistoryPO.setRemark(salaryGrantMainTaskPO.getRemark()); //备注

        //salaryGrantTaskHistoryPO.setSalaryGrantTaskHistoryId(); //任务单历史编号 未设置
        salaryGrantTaskHistoryPO.setSupplierGrantCount(salaryGrantMainTaskPO.getSupplierGrantCount()); //中智代发（委托机构）发薪人数
        salaryGrantTaskHistoryPO.setTaskCode(salaryGrantMainTaskPO.getSalaryGrantMainTaskCode()); //任务单编号
        salaryGrantTaskHistoryPO.setTaskId(salaryGrantMainTaskPO.getSalaryGrantMainTaskId()); //任务单ID
        salaryGrantTaskHistoryPO.setTaskStatus(salaryGrantMainTaskPO.getTaskStatus()); //状态:2-审批通过，3-审批拒绝，4-失效，8-驳回
        salaryGrantTaskHistoryPO.setTaskType(salaryGrantMainTaskPO.getTaskType()); //任务单类型:0-主表、1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
        salaryGrantTaskHistoryPO.setTotalPersonCount(salaryGrantMainTaskPO.getTotalPersonCount()); //发薪人数
        salaryGrantTaskHistoryPO.setWorkFlowUserInfo(salaryGrantMainTaskPO.getWorkFlowUserInfo()); //任务流转信息

        return salaryGrantTaskHistoryPO;
    }

    /**
     * SalaryGrantSubTaskPO转换为SalaryGrantTaskHistoryPO
     *
     * @param salaryGrantSubTaskPO
     * @return
     */
    private SalaryGrantTaskHistoryPO subTaskPO2HistoryPO(SalaryGrantSubTaskPO salaryGrantSubTaskPO) {
        SalaryGrantTaskHistoryPO salaryGrantTaskHistoryPO = new SalaryGrantTaskHistoryPO();
        salaryGrantTaskHistoryPO.setAdvanceType(salaryGrantSubTaskPO.getAdvanceType()); //垫付类型:1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付
        salaryGrantTaskHistoryPO.setAdversionType(salaryGrantSubTaskPO.getAdversionType()); //内转类型:1-AF转FC,2-BPO转FC,3-FC转AF,4-FC转BPO
        salaryGrantTaskHistoryPO.setApproveUserId(salaryGrantSubTaskPO.getApproveUserId()); //审核员
        salaryGrantTaskHistoryPO.setApprovedOpinion(salaryGrantSubTaskPO.getApprovedOpinion()); //审批意见
        salaryGrantTaskHistoryPO.setBalanceGrant(salaryGrantSubTaskPO.getBalanceGrant()); //结算发放标识:0-正常，1-垫付
        salaryGrantTaskHistoryPO.setBatchCode(salaryGrantSubTaskPO.getBatchCode()); //薪酬计算批次号
        salaryGrantTaskHistoryPO.setChineseCount(salaryGrantSubTaskPO.getChineseCount()); //中方发薪人数
        salaryGrantTaskHistoryPO.setCreatedBy(salaryGrantSubTaskPO.getCreatedBy()); //创建人
        salaryGrantTaskHistoryPO.setCreatedTime(salaryGrantSubTaskPO.getCreatedTime()); //创建时间
        salaryGrantTaskHistoryPO.setForeignerCount(salaryGrantSubTaskPO.getForeignerCount()); //外方发薪人数
        salaryGrantTaskHistoryPO.setGrantAccountCode(salaryGrantSubTaskPO.getGrantAccountCode()); //发放账户编号
        salaryGrantTaskHistoryPO.setGrantAccountName(salaryGrantSubTaskPO.getGrantAccountName()); //发放账户名称
        salaryGrantTaskHistoryPO.setGrantCycle(salaryGrantSubTaskPO.getGrantCycle()); //薪资周期
        salaryGrantTaskHistoryPO.setGrantDate(salaryGrantSubTaskPO.getGrantDate()); //薪资发放日期
        salaryGrantTaskHistoryPO.setGrantMode(ObjectUtils.isEmpty(salaryGrantSubTaskPO.getGrantMode()) ? null : salaryGrantSubTaskPO.getGrantMode().toString()); //发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
        salaryGrantTaskHistoryPO.setGrantTime(salaryGrantSubTaskPO.getGrantTime()); //薪资发放时段:1-上午，2-下午
        salaryGrantTaskHistoryPO.setGrantType(salaryGrantSubTaskPO.getGrantType()); //发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
        //失效原因 未设置
        salaryGrantTaskHistoryPO.setActive(true); //1-有效，0-无效
        salaryGrantTaskHistoryPO.setAdvance(salaryGrantSubTaskPO.getAdvance()); //是否垫付:0-否，1-是
        salaryGrantTaskHistoryPO.setAdversion(salaryGrantSubTaskPO.getAdversion()); //是否内转:0-非内转，1-内转

        //salaryGrantTaskHistoryPO.setIncludeForeignCurrency(); //外币发放标识:0-否，1-是 未设置
        //salaryGrantTaskHistoryPO.setLocalGrantCount(); //中智上海发薪人数 未设置
        //主表编号 未设置
        salaryGrantTaskHistoryPO.setManagementId(salaryGrantSubTaskPO.getManagementId()); //管理方编号
        salaryGrantTaskHistoryPO.setManagementName(salaryGrantSubTaskPO.getManagementName()); //管理方名称
        salaryGrantTaskHistoryPO.setModifiedBy(salaryGrantSubTaskPO.getModifiedBy()); //最后修改人
        salaryGrantTaskHistoryPO.setModifiedTime(salaryGrantSubTaskPO.getModifiedTime()); //最后修改时间
        salaryGrantTaskHistoryPO.setOperatorUserId(salaryGrantSubTaskPO.getOperatorUserId()); //操作员
        //salaryGrantTaskHistoryPO.setOriginBatchCode(); //参考批次号 未设置
        salaryGrantTaskHistoryPO.setPaymentTotalSum(salaryGrantSubTaskPO.getPaymentTotalSum()); //薪资发放总金额（RMB）
        salaryGrantTaskHistoryPO.setRemark(salaryGrantSubTaskPO.getRemark()); //备注
        //salaryGrantTaskHistoryPO.setSalaryGrantTaskHistoryId(); //任务单历史编号 未设置
        //salaryGrantTaskHistoryPO.setSupplierGrantCount(); //中智代发（委托机构）发薪人数 未设置
        salaryGrantTaskHistoryPO.setTaskCode(salaryGrantSubTaskPO.getSalaryGrantSubTaskCode()); //任务单编号
        salaryGrantTaskHistoryPO.setTaskId(salaryGrantSubTaskPO.getSalaryGrantSubTaskId()); //任务单ID

        salaryGrantTaskHistoryPO.setTaskStatus(salaryGrantSubTaskPO.getTaskStatus()); //状态:2-审批通过，3-审批拒绝，4-失效，8-驳回
        salaryGrantTaskHistoryPO.setTaskType(salaryGrantSubTaskPO.getTaskType()); //任务单类型:0-主表、1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
        salaryGrantTaskHistoryPO.setTotalPersonCount(salaryGrantSubTaskPO.getTotalPersonCount()); //发薪人数
        salaryGrantTaskHistoryPO.setWorkFlowUserInfo(salaryGrantSubTaskPO.getWorkFlowUserInfo()); //任务流转信息

        return salaryGrantTaskHistoryPO;
    }

    /**
     * 保存历史信息
     * @author gaoyang
     * @date 2018-08-10
     * @param task_his_id
     * @param salaryGrantEmployeeBO
     * @return boolean
     */
    public boolean saveToHistory(long task_his_id, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        boolean returnFlag = true;
        //查询任务单的雇员信息
        Page<SalaryGrantEmployeeBO> page = new Page<>();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Page<SalaryGrantEmployeeBO> salaryGrantEmployeeBOPage = salaryGrantEmployeeService.queryEmployeeTask(page, salaryGrantEmployeeBO);
        if (!ObjectUtils.isEmpty(salaryGrantEmployeeBOPage)) {
            List<SalaryGrantEmployeeBO> employeeBOList = salaryGrantEmployeeBOPage.getRecords();
            if (!CollectionUtils.isEmpty(employeeBOList)) {
                //保存雇员信息到MongoDB中
                DBObject dbObject = new BasicDBObject();
                dbObject.put("task_his_id", task_his_id);
                dbObject.put("employeeInfo", employeeBOList);
                salaryGrantEmpHisOpt.getMongoTemplate().insert(dbObject, SalaryGrantEmpHisOpt.SG_EMP_HIS);
            }else{
                returnFlag = false;
            }
        }
        return returnFlag;
    }
}
