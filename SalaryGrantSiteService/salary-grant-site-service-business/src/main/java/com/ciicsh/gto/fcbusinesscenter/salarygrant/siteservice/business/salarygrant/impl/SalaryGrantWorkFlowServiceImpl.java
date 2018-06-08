package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskMissionRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantTaskHistoryPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.Result;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放工作流调用 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantWorkFlowServiceImpl implements SalaryGrantWorkFlowService {
    @Autowired
    private SheetServiceProxy sheetServiceProxy;
    @Autowired
    private SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    private SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;
    @Autowired
    private CommonService commonService;

    @Override
    public Map startSalaryGrantTaskProcess(SalaryGrantTaskMissionRequestDTO salaryGrantTaskMissionRequestDTO) {
        Map<String, String> startProcessResponseMap = null;
        MissionRequestDTO missionRequestDTO = new MissionRequestDTO();
        missionRequestDTO.setMissionId(salaryGrantTaskMissionRequestDTO.getMissionId());
        missionRequestDTO.setProcessDefinitionKey(salaryGrantTaskMissionRequestDTO.getProcessDefinitionKey());
        missionRequestDTO.setVariables(salaryGrantTaskMissionRequestDTO.getVariables());
        try {
            Result restResult = sheetServiceProxy.startProcess(missionRequestDTO);
            startProcessResponseMap = (Map<String, String>) restResult.getObject();
            com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result returnResult = ResultGenerator.genSuccessResult(true);
        } catch (Exception e) {
            ResultGenerator.genServerFailResult();
        }
        return startProcessResponseMap;
    }

    @Override
    public String getProcessId(Map startProcessResponseMap) {
        String processId = null;
        //工作流返回的流程编号
        if (startProcessResponseMap != null) {
            processId = (String) startProcessResponseMap.get("processId");
        }
        return processId;
    }

    @Override
    public Map completeSalaryGrantTask(SalaryGrantTaskRequestDTO salaryGrantTaskRequestDTO) {
        Map<String, String> completeTaskResponseMap = null;
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTaskId(salaryGrantTaskRequestDTO.getTaskId());
        taskRequestDTO.setAssignee(salaryGrantTaskRequestDTO.getAssignee());
        taskRequestDTO.setVariables(salaryGrantTaskRequestDTO.getVariables());
        try {
            Result restResult = sheetServiceProxy.completeTask(taskRequestDTO);
            completeTaskResponseMap = (Map<String, String>) restResult.getObject();
            com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result returnResult = ResultGenerator.genSuccessResult(true);
        } catch (Exception e) {
            ResultGenerator.genServerFailResult();
        }
        return completeTaskResponseMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean doCancelTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        //任务单主表操作
        //查询薪资发放任务单主表记录
        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_type in (1, 2, 3) and task_status in (0, 1, 2) and is_active = 1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> salaryGrantMainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);

        //任务单子表操作
        //查询薪资发放任务单子表
        EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
        subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantSubTaskPO> salaryGrantSubTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);

        //更新薪资发放任务单主表
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
        salaryGrantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CANCEL); //任务单状态：4-失效
        salaryGrantMainTaskPO.setInvalidReason(salaryGrantTaskBO.getInvalidReason()); //失效原因
        salaryGrantMainTaskPO.setActive(false); //是否有效:1-有效，0-无效
        salaryGrantMainTaskMapper.update(salaryGrantMainTaskPO, mainTaskPOEntityWrapper);

        //更新薪资发放任务单子表
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = new SalaryGrantSubTaskPO();
        salaryGrantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CANCEL); //任务单状态：4-失效
        salaryGrantSubTaskPO.setActive(false); //是否有效:1-有效，0-无效
        salaryGrantSubTaskMapper.update(salaryGrantSubTaskPO, subTaskPOEntityWrapper);

        //新增薪资发放任务单主表历史记录
        if (!CollectionUtils.isEmpty(salaryGrantMainTaskPOList)) {
            salaryGrantMainTaskPOList.stream().forEach(grantMainTaskPO -> {
                //新增任务单主表历史记录
                SalaryGrantTaskHistoryPO salaryGrantTaskHistoryPO = mainTaskPO2HistoryPO(grantMainTaskPO);
                salaryGrantTaskHistoryMapper.insert(salaryGrantTaskHistoryPO);

                //保存雇员信息到mongodb 表sg_emp_his_table
                salaryGrantEmployeeCommandService.saveToHistory(salaryGrantTaskHistoryPO.getSalaryGrantTaskHistoryId(), grantMainTaskPO.getSalaryGrantMainTaskCode(), grantMainTaskPO.getTaskType());
            });
        }

        //新增薪资发放任务单子表历史记录
        if (!CollectionUtils.isEmpty(salaryGrantSubTaskPOList)) {
            salaryGrantSubTaskPOList.stream().forEach(grantSubTaskPO -> {
                //新增任务单子表历史记录
                SalaryGrantTaskHistoryPO salaryGrantTaskHistoryPO = subTaskPO2HistoryPO(grantSubTaskPO);
                salaryGrantTaskHistoryMapper.insert(salaryGrantTaskHistoryPO);

                //保存雇员信息到mongodb 表sg_emp_his_table
                salaryGrantEmployeeCommandService.saveToHistory(salaryGrantTaskHistoryPO.getSalaryGrantTaskHistoryId(), grantSubTaskPO.getSalaryGrantSubTaskCode(), grantSubTaskPO.getTaskType());
            });
        }

        //逻辑删除雇员信息
        //更新字段
        SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();
        salaryGrantEmployeePO.setActive(false); //是否有效:1-有效，0-无效

        //更新条件
        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        employeePOEntityWrapper.where("salary_grant_main_task_code = {0}", salaryGrantTaskBO.getTaskCode());
        salaryGrantEmployeeMapper.update(salaryGrantEmployeePO, employeePOEntityWrapper);

        return true;
    }

    /**
     * SalaryGrantMainTaskPO转换为SalaryGrantTaskHistoryPO
     *
     * @param salaryGrantMainTaskPO
     * @return
     */
    private SalaryGrantTaskHistoryPO mainTaskPO2HistoryPO(SalaryGrantMainTaskPO salaryGrantMainTaskPO) {
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
        //salaryGrantTaskHistoryPO.setActive(salaryGrantMainTaskPO.getActive()); //1-有效，0-无效
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
        //salaryGrantTaskHistoryPO.setActive(salaryGrantSubTaskPO.getActive()); //1-有效，0-无效
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateGrantDateAndTime(SalaryGrantTaskBO salaryGrantTaskBO) {
        //查询条件(更新条件)
        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            mainTaskPOList.stream().forEach(mainTaskPO -> {
                if (!salaryGrantTaskBO.getGrantDate().equals(mainTaskPO.getGrantDate()) || salaryGrantTaskBO.getGrantTime().compareTo(mainTaskPO.getGrantTime()) != 0) {
                    //更新字段
                    SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
                    salaryGrantMainTaskPO.setGrantDate(salaryGrantTaskBO.getGrantDate()); //薪资发放日期
                    salaryGrantMainTaskPO.setGrantTime(salaryGrantTaskBO.getGrantTime()); //薪资发放时段:1-上午，2-下午

                    salaryGrantMainTaskMapper.update(salaryGrantMainTaskPO, mainTaskPOEntityWrapper);

                    SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();
                    salaryGrantEmployeePO.setGrantDate(salaryGrantTaskBO.getGrantDate()); //薪资发放日期
                    salaryGrantEmployeePO.setGrantTime(salaryGrantTaskBO.getGrantTime()); //薪资发放时段:1-上午，2-下午

                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", salaryGrantTaskBO.getTaskCode());
                    salaryGrantEmployeeMapper.update(salaryGrantEmployeePO, employeePOEntityWrapper);
                }
            });
        }

        return true;
    }

    @Override
    public Boolean isOverdue(SalaryGrantTaskBO salaryGrantTaskBO) {
        //查询管理方发放批次信息
        List<PrNormalBatchDTO> batchDTOList =  commonService.getBatchListByManagementId(salaryGrantTaskBO.getManagementId());
        if (!CollectionUtils.isEmpty(batchDTOList)) {
            //过滤batchList的status in (8,9)的记录
            List<PrNormalBatchDTO> filterBatchDTOList = batchDTOList.stream().filter(normalBatchDTO -> 8 == normalBatchDTO.getStatus() || 9 == normalBatchDTO.getStatus()).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterBatchDTOList)) {
                //按照修改时间升序排序
                List<PrNormalBatchDTO> sortBatchDTOList = filterBatchDTOList.stream().sorted(Comparator.comparing(PrNormalBatchDTO::getModifiedTime)).collect(Collectors.toList());
                sortBatchDTOList.stream().forEach(prNormalBatchDTO -> {

                });
            }
        }

        return false;
    }
}
