package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskMissionRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.WorkFlowTaskInfoService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.Result;
import com.ciicsh.gto.sheetservice.api.dto.TaskCreateMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
    @Autowired
    private WorkFlowTaskInfoService workFlowTaskInfoService;

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
    public Boolean doCancelTask(SalaryGrantTaskBO salaryGrantTaskBO) throws Exception {
        //（1）根据传入参数taskCode查询任务单主表信息SalaryGrantMainTaskPO，
        //     查询条件：salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and is_active=1 and sg_salary_grant_main_task.grant_type in (1、2、3) and sg_salary_grant_main_task.task_status in (0、1、2)。
        //     判断状态SalaryGrantMainTaskPO.task_status，如果task_status=2（审批通过），则进行（2）操作，否则进行（3）操作。
        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1 and grant_type in (1, 2, 3) and task_status in (0, 1, 2)", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回
                String taskStatus = mainTaskPO.getTaskStatus();

                Long task_his_id = null;
                if (SalaryGrantBizConsts.TASK_STATUS_PASS.equals(taskStatus)) {
                    //（2）查询雇员信息sg_salary_grant_employee，
                    //     查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and雇员表.grant_status in (1,2),
                    //     如果查询结果为空，
                    //         则跳转到（3）；
                    //     否则查询结果有记录，
                    //         需要调用暂缓池的删除接口（接口参数为SalaryGrantMainTaskPO.batchCode、SalaryGrantMainTaskPO.salaryGrantMainTaskCode），对查询的雇员信息在发放暂缓池进行删除操作。--待顾伟发布好调用
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_status in (1, 2)", mainTaskPO.getSalaryGrantMainTaskCode());
                    List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
                    if (CollectionUtils.isEmpty(employeePOList)) {
                        //（3）对任务单主表SalaryGrantMainTaskPO进行处理
                        //     任务单状态修改为task_status=5（待生成），batchVersion=salaryGrantTaskBO.batchVersion；
                        //     将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history.task_status=4（失效），sg_salary_grant_task_history.invalid_reason = salaryGrantTaskBO. invalidReason；
                        SalaryGrantMainTaskPO updateMainTaskPO = new SalaryGrantMainTaskPO();
                        updateMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                        updateMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_WAIT); //状态:5-待生成
                        updateMainTaskPO.setBatchVersion(salaryGrantTaskBO.getBatchVersion()); //计算批次版本号
                        salaryGrantMainTaskMapper.updateById(updateMainTaskPO);

                        //将任务单主表记录新增入历史表
                        SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                        mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CANCEL); //状态:4-失效
                        mainTaskHistoryPO.setInvalidReason(salaryGrantTaskBO.getInvalidReason()); //失效原因
                        salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                        task_his_id = mainTaskHistoryPO.getSalaryGrantTaskHistoryId();
                    } else {
                        //调用暂缓池的删除接口
                        boolean deleteResult = commonService.delDeferredEmp(mainTaskPO);
                        if (false == deleteResult) {
                            throw new Exception("调用暂缓池删除接口失败！");
                        }
                    }
                } else {
                    //（3）对任务单主表SalaryGrantMainTaskPO进行处理
                    //     任务单状态修改为task_status=5（待生成），batchVersion=salaryGrantTaskBO.batchVersion；
                    //     将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history.task_status=4（失效），sg_salary_grant_task_history.invalid_reason = salaryGrantTaskBO. invalidReason；
                    SalaryGrantMainTaskPO updateMainTaskPO = new SalaryGrantMainTaskPO();
                    updateMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                    updateMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_WAIT); //状态:5-待生成
                    updateMainTaskPO.setBatchVersion(salaryGrantTaskBO.getBatchVersion()); //计算批次版本号
                    salaryGrantMainTaskMapper.updateById(updateMainTaskPO);

                    //将任务单主表记录新增入历史表
                    SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                    mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CANCEL); //状态:4-失效
                    mainTaskHistoryPO.setInvalidReason(salaryGrantTaskBO.getInvalidReason()); //失效原因
                    salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                    task_his_id = mainTaskHistoryPO.getSalaryGrantTaskHistoryId();
                }

                //（4）根据任务单主表查询雇员信息，将任务单主表雇员信息存入历史表，
                //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(主表记录历史记录主键), String task_code(主表task_code), int task_type(主表task_type))
                salaryGrantEmployeeCommandService.saveToHistory(task_his_id, mainTaskPO.getSalaryGrantMainTaskCode(), mainTaskPO.getTaskType());

                //（5）根据任务单主表查询子表信息，如果存在子表信息，对任务单子表进行处理
                //     将任务单子表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history.task_status=4（失效）；
                EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                if (!CollectionUtils.isEmpty(subTaskPOList)) {
                    subTaskPOList.stream().forEach(subTaskPO -> {
                        //将任务单子表记录新增入历史表
                        SalaryGrantTaskHistoryPO subTaskHistoryPO = subTaskPO2HistoryPO(subTaskPO);
                        subTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CANCEL); //状态:4-失效
                        salaryGrantTaskHistoryMapper.insert(subTaskHistoryPO);

                        //（6）根据任务单子表查询雇员信息，将任务单子表雇员信息存入历史表,
                        //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(子表记录历史记录主键), String task_code(子表task_code), int task_type(子表task_type))
                        salaryGrantEmployeeCommandService.saveToHistory(subTaskHistoryPO.getSalaryGrantTaskHistoryId(), subTaskPO.getSalaryGrantSubTaskCode(), subTaskPO.getTaskType());

                        //（7）任务单子表进行物理删除，调用delete方法。
                        salaryGrantSubTaskMapper.deleteById(subTaskPO.getSalaryGrantSubTaskId());
                    });
                }

                //（8）根据任务单主表查询雇员信息，进行物理删除，调用delete方法。
                salaryGrantMainTaskMapper.deleteById(mainTaskPO.getSalaryGrantMainTaskId());

            }
        }

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
        //薪资发放日期未传入时返回false
        if (StringUtils.isEmpty(salaryGrantTaskBO.getGrantDate())) {
            return false;
        }

        //查询管理方发放批次信息
        List<PrNormalBatchDTO> batchDTOList =  commonService.getBatchListByManagementId(salaryGrantTaskBO.getManagementId());
        if (!CollectionUtils.isEmpty(batchDTOList)) {
            //过滤batchList的status in (8,9) 且 修改时间不为null的记录
            List<PrNormalBatchDTO> filterBatchDTOList = batchDTOList.stream().filter(normalBatchDTO -> (8 == normalBatchDTO.getStatus() || 9 == normalBatchDTO.getStatus()) && !ObjectUtils.isEmpty(normalBatchDTO.getModifiedTime())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterBatchDTOList)) {
                //按照修改时间升序排序
                List<PrNormalBatchDTO> sortBatchDTOList = filterBatchDTOList.stream().sorted(Comparator.comparing(PrNormalBatchDTO::getModifiedTime)).collect(Collectors.toList());
                //薪资发放日期
                String grantDate = salaryGrantTaskBO.getGrantDate().replace("-", "");

                for (PrNormalBatchDTO batchDTO : sortBatchDTOList) {
                    //垫付逾期日期
                    String advancePeriod = StringUtils.isEmpty(batchDTO.getAdvancePeriod()) ? null : batchDTO.getAdvancePeriod().replace("-", "");
                    if (grantDate.compareToIgnoreCase(advancePeriod) > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean doSubmitTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        //1、根据taskCode、taskType，如果taskType=0 则查询任务单主表信息SalaryGrantMainTaskPO，
        //   查询条件：salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and is_active=1；
        //   如果taskType!=0，直接返回结果false,不执行下面处理逻辑。
        if (0 != salaryGrantTaskBO.getTaskType()) {
            return false;
        }

        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active=1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);

        //2、先判断薪资发放日和时段信息前端页面是否有改动，调用接口内部方法 updateGrantDateAndTime(SalaryGrantTaskBO salaryGrantTaskBO)
        Boolean grantDateAndTimeResult = updateGrantDateAndTime(salaryGrantTaskBO);

        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
                Integer grantType = mainTaskPO.getGrantType();

                //3、如果SalaryGrantMainTaskPO. grantType in (1,2,3)，则继续执行第4步；否则直接跳转到第5步。
                if (SalaryGrantBizConsts.GRANT_TYPE_NORMAL == grantType || SalaryGrantBizConsts.GRANT_TYPE_ADJUST == grantType || SalaryGrantBizConsts.GRANT_TYPE_BACK_TRACE == grantType) {
                    //4、查询计算批次信息，调用接口 BatchProxy.getBatchInfo，查询条件batchCode=batchCode，batchType= grantType，查询返回结果PrBatchDTO。
                    //   根据PrBatchDTO. hasMoney和PrBatchDTO. hasAdvance进行条件分支判断：
                    PrBatchDTO prBatchDTO = commonService.getBatchInfo(salaryGrantTaskBO.getBatchCode(), salaryGrantTaskBO.getGrantType());
                    //4、（1）PrBatchDTO. hasMoney=0 && PrBatchDTO. hasAdvance=0 直接返回结果false,不执行下面处理逻辑。
                    if (false == prBatchDTO.isHasMoney() && 0 == prBatchDTO.getHasAdvance()) {
                        return false;
                    }

                    //4、（2）PrBatchDTO. hasMoney=0 && PrBatchDTO. hasAdvance in (1,2,3,4)，调用接口内部方法 isOverdue(SalaryGrantTaskBO salaryGrantTaskBO)，
                    //     如果isOverdue方法返回true，直接返回结果false,不执行下面处理逻辑；
                    //     如果isOverdue方法返回false，修改SalaryGrantMainTaskPO. balanceGrant=1。
                    if (false == prBatchDTO.isHasMoney() && (1 == prBatchDTO.getHasAdvance() || 2 == prBatchDTO.getHasAdvance() || 3 == prBatchDTO.getHasAdvance() || 4 == prBatchDTO.getHasAdvance())){
                        Boolean isOverdueResult = isOverdue(salaryGrantTaskBO);

                        if (true == isOverdueResult) {
                            return false;
                        } else {
                            SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                            grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                            grantMainTaskPO.setBalanceGrant(1); //结算发放标识:0-正常，1-垫付
                            salaryGrantMainTaskMapper.updateById(grantMainTaskPO);
                        }
                    }

                    //4、（3）PrBatchDTO. hasMoney=1，修改SalaryGrantMainTaskPO. balanceGrant=0
                    if (true == prBatchDTO.isHasMoney()) {
                        //（3）PrBatchDTO. hasMoney=1，修改SalaryGrantMainTaskPO. balanceGrant=0
                        SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                        grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                        grantMainTaskPO.setBalanceGrant(0); //结算发放标识:0-正常，1-垫付
                        salaryGrantMainTaskMapper.updateById(grantMainTaskPO);
                    }
                } else {
                    //5、修改任务单主表状态为SalaryGrantMainTaskPO. taskStatus=1
                    SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                    grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                    grantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_APPROVAL); //状态:1-审批中
                    salaryGrantMainTaskMapper.updateById(grantMainTaskPO);
                }

                //6、检查提交的雇员信息是否有变更，可以从自动暂缓改为正常，调用雇员信息变更接口。--后面补充，步骤预留
                //7、调用接口方法进行拆分子表处理 --后面补充，步骤预留
                //8、如果有拆分的子表，把子表任务单状态为taskStatus=12，批量修改。--后面补充，步骤预留
                //9、调用工作流程，提交动作处理。--后面补充，步骤预留
            }
        }

        //10、返回结果true
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean doReturnTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        //1、根据taskCode、taskType查询，如果taskType!=0，直接返回结果false,不执行下面处理逻辑。
        //   如果taskType=0 则查询任务单主表信息SalaryGrantMainTaskPO，
        //   查询条件：salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and is_active=1；
        if (0 != salaryGrantTaskBO.getTaskType()) {
            return false;
        }

        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active=1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //1、（1）更新主表字段: task_status=0 ，approved_opinion = salaryGrantTaskBO. approvedOpinion， is_active = 0
                SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                grantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT); //状态:0-草稿
                grantMainTaskPO.setApprovedOpinion(salaryGrantTaskBO.getApprovedOpinion()); //审批意见
                grantMainTaskPO.setActive(false); //是否有效:1-有效，0-无效
                salaryGrantMainTaskMapper.updateById(grantMainTaskPO);

                //1、（2）将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history. task_status=3
                SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REFUSE); //状态:3-审批拒绝
                salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                //1、（3）将任务单主表雇员信息存入历史表，
                //       调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(主表记录历史记录主键), String task_code(主表task_code), int task_type(主表task_type))
                salaryGrantEmployeeCommandService.saveToHistory(mainTaskHistoryPO.getSalaryGrantTaskHistoryId(), mainTaskPO.getSalaryGrantMainTaskCode(), mainTaskPO.getTaskType());

                //2、查询任务单子表信息SalaryGrantSubTaskPO，
                //   查询条件：子表.salary_grant_main_task_code = 主表.salary_grant_main_task_code and is_active = 1，如果查询结果不为空则：
                EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                if (!CollectionUtils.isEmpty(subTaskPOList)) {
                    subTaskPOList.stream().forEach(subTaskPO -> {
                        //2、（1）将任务单子表记录新增入历史表sg_salary_grant_task_history  添加sg_salary_grant_task_history. task_status=3
                        SalaryGrantTaskHistoryPO subGrantTaskHistoryPO = subTaskPO2HistoryPO(subTaskPO);
                        subGrantTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REFUSE); //状态:3-审批拒绝
                        salaryGrantTaskHistoryMapper.insert(subGrantTaskHistoryPO);

                        //2、（2）根据任务单子表salary_grant_sub_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                        //        查询条件：雇员表.salary_grant_sub_task_code = 任务单子表.salary_grant_sub_task_code and is_active=1
                        EntityWrapper<SalaryGrantEmployeePO> grantEmployeePOEntityWrapper = new EntityWrapper<>();
                        grantEmployeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active=1", subTaskPO.getSalaryGrantSubTaskCode());
                        List<SalaryGrantEmployeePO> grantEmployeePOList = salaryGrantEmployeeMapper.selectList(grantEmployeePOEntityWrapper);

                        //2、（3）将任务单子表雇员信息存入历史表，
                        //        调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(子表记录历史记录主键), String task_code(子表task_code), int task_type(子表task_type))
                        salaryGrantEmployeeCommandService.saveToHistory(subGrantTaskHistoryPO.getSalaryGrantTaskHistoryId(), subTaskPO.getSalaryGrantSubTaskCode(), subTaskPO.getTaskType());

                        //2、（4）调用delete方法，物理删除该任务单子表信息。
                        salaryGrantSubTaskMapper.deleteById(subTaskPO.getSalaryGrantSubTaskId());
                    });
                }

                //3、根据任务单主表salary_grant_main_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                //   查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and is_active=1
                //   更新雇员信息字段：雇员表.salary_grant_sub_task_code =“” 子表任务单编号置为空。
                EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active=1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
                if (!CollectionUtils.isEmpty(employeePOList)) {
                    employeePOList.stream().forEach(salaryGrantEmployeePO -> {
                        SalaryGrantEmployeePO updateEmployeePO = new SalaryGrantEmployeePO();
                        updateEmployeePO.setSalaryGrantEmployeeId(salaryGrantEmployeePO.getSalaryGrantEmployeeId());
                        updateEmployeePO.setSalaryGrantSubTaskCode("");
                        salaryGrantEmployeeMapper.updateById(updateEmployeePO);
                    });
                }

                //4、检查雇员信息是否有变更，调用雇员信息变更接口。--后面补充，步骤预留
                //5、如果雇员信息有变动，则重新统计任务主表汇总信息。--后面补充，步骤预留
                //6、调用工作流程，退回动作处理。--后面补充，步骤预留
            }
        }

        //7、返回结果true
        return true;
    }

    @Override
    public Boolean doApproveTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        //1、根据taskCode、taskType查询，如果taskType!=0，直接返回结果false,不执行下面处理逻辑。
        //   如果taskType=0 则查询任务单主表信息SalaryGrantMainTaskPO，
        //   查询条件：salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and is_active=1；
        if (0 != salaryGrantTaskBO.getTaskType()) {
            return false;
        }

        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active=1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //  （1）更新主表字段: task_status=2 ，approved_opinion = salaryGrantTaskBO. approvedOpinion
                SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                grantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_PASS); //状态:2-审批通过
                grantMainTaskPO.setApprovedOpinion(salaryGrantTaskBO.getApprovedOpinion()); //审批意见
                salaryGrantMainTaskMapper.updateById(grantMainTaskPO);

                //2、查询任务单子表信息SalaryGrantSubTaskPO，
                //   查询条件：子表.salary_grant_main_task_code = 主表.salary_grant_main_task_code and is_active = 1，如果查询结果不为空则：
                //  （1）更新子表字段:
                //         子表任务单类型task_type=1，则更新字段task_status = 2 ；
                //         子表任务单类型task_type=2，则更新字段task_status = 10 ；
                EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                if (!CollectionUtils.isEmpty(subTaskPOList)) {
                    for (SalaryGrantSubTaskPO subTaskPO : subTaskPOList) {
                        SalaryGrantSubTaskPO grantSubTaskPO = new SalaryGrantSubTaskPO();
                        grantSubTaskPO.setSalaryGrantSubTaskId(subTaskPO.getSalaryGrantSubTaskId());

                        if (1 == subTaskPO.getTaskType()) {
                            grantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_PASS); //状态:2-审批通过
                        }
                        if (2 == subTaskPO.getTaskType()) {
                            grantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_COMBINE_WAIT); //状态:10-待合并
                        }

                        salaryGrantSubTaskMapper.updateById(grantSubTaskPO);
                    }
                }

                //3、暂缓人员信息进入暂缓池，调用接口SalaryGrantEmployeeCommandService. processReprieveToPoll(SalaryGrantTaskBO salaryGrantTaskBO)
                salaryGrantEmployeeCommandService.processReprieveToPoll(salaryGrantTaskBO);

                //4、查询批次信息，调用接口BatchProxy.getBatchListByManagementId，获取第一条数据的PrNormalBatchDTO. hasAdvance。
                //   如果PrBatchDTO. has Advance=1，则调用接口BatchProxy.updateBatchStatus，
                //   设置BatchAuditDTO. advancePeriod=SalaryGrantMainTaskPO. grantDate + PrNormalBatchDTO. advanceDay（调用日期对象，日期+天数 获取最终日期）
                List<PrNormalBatchDTO> prNormalBatchDTOList = commonService.getBatchListByManagementId(salaryGrantTaskBO.getManagementId());
                if (!CollectionUtils.isEmpty(prNormalBatchDTOList)) {
                    PrNormalBatchDTO firstPrNormalBatchDTO = prNormalBatchDTOList.get(0);
                    if (1 == firstPrNormalBatchDTO.getHasAdvance()) {
                        commonService.updateBatchStatus(mainTaskPO, firstPrNormalBatchDTO);
                    }
                }

                //5、调用工作流程，提交动作处理。--后面补充，步骤预留
            }
        }

        //6、返回结果true
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean doRetreatTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        //1、根据taskCode、taskType查询，如果taskType!=0，直接返回结果false,不执行下面处理逻辑
        //   如果taskType=0 则查询任务单主表信息SalaryGrantMainTaskPO，
        //   查询条件：salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and is_active = 1
        if (0 != salaryGrantTaskBO.getTaskType()) {
            return false;
        }

        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active=1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //2、对任务单主表记录处理如下：
                //（1）更新主表字段: task_status=0 ，approved_opinion = salaryGrantTaskBO. approvedOpinion
                SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                grantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT); //状态:0-草稿
                grantMainTaskPO.setApprovedOpinion(salaryGrantTaskBO.getApprovedOpinion()); //审批意见
                salaryGrantMainTaskMapper.updateById(grantMainTaskPO);

                //（2）将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history. task_status=8
                SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_RETREAT); //状态:8-驳回
                salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                //（3）根据任务单主表salary_grant_main_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                //     查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and is_active=1
                EntityWrapper<SalaryGrantEmployeePO> mainEmployeePOEntityWrapper = new EntityWrapper<>();
                mainEmployeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantEmployeePO> mainEmployeePOList = salaryGrantEmployeeMapper.selectList(mainEmployeePOEntityWrapper);

                //（4）将任务单主表雇员信息存入历史表，
                //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(主表记录历史记录主键), String task_code(主表task_code), int task_type(主表task_type))
                salaryGrantEmployeeCommandService.saveToHistory(mainTaskHistoryPO.getSalaryGrantTaskHistoryId(), mainTaskPO.getSalaryGrantMainTaskCode(), mainTaskPO.getTaskType());

                //3、查询任务单子表信息SalaryGrantSubTaskPO，
                //   查询条件：子表.salary_grant_main_task_code = 主表.salary_grant_main_task_code and is_active = 1，如果查询结果不为空则：
                EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                if (!CollectionUtils.isEmpty(subTaskPOList)) {
                    for (SalaryGrantSubTaskPO subTaskPO : subTaskPOList) {
                        //（1）将任务单子表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history. task_status=8
                        SalaryGrantTaskHistoryPO subTaskHistoryPO = subTaskPO2HistoryPO(subTaskPO);
                        subTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_RETREAT); //状态:8-驳回
                        salaryGrantTaskHistoryMapper.insert(subTaskHistoryPO);

                        //（2）根据任务单子表salary_grant_sub_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                        //     查询条件：雇员表.salary_grant_sub_task_code = 任务单子表.salary_grant_sub_task_code and is_active = 1
                        EntityWrapper<SalaryGrantEmployeePO> subEmployeePOEntityWrapper = new EntityWrapper<>();
                        subEmployeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", subTaskPO.getSalaryGrantSubTaskCode());
                        List<SalaryGrantEmployeePO> subEmployeePOList = salaryGrantEmployeeMapper.selectList(subEmployeePOEntityWrapper);

                        //（3）将任务单子表雇员信息存入历史表，
                        //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(子表记录历史记录主键), String task_code(子表task_code), int task_type(子表task_type))
                        salaryGrantEmployeeCommandService.saveToHistory(subTaskHistoryPO.getSalaryGrantTaskHistoryId(), subTaskPO.getSalaryGrantSubTaskCode(), subTaskPO.getTaskType());

                        //（4）调用delete方法，物理删除任务单子表信息。
                        salaryGrantSubTaskMapper.deleteById(subTaskPO.getSalaryGrantSubTaskId());
                    }
                }

                //4、根据任务单主表salary_grant_main_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                //   查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and is_active = 1，
                //   更新雇员信息字段：雇员表.salary_grant_sub_task_code =“” 子表任务单编号置为空。
                //     查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and is_active=1
                EntityWrapper<SalaryGrantEmployeePO> mainEmployeePOEntityWrapper2 = new EntityWrapper<>();
                mainEmployeePOEntityWrapper2.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantEmployeePO> mainEmployeePOList2 = salaryGrantEmployeeMapper.selectList(mainEmployeePOEntityWrapper2);
                if (!CollectionUtils.isEmpty(mainEmployeePOList2)) {
                    mainEmployeePOList2.stream().forEach(salaryGrantEmployeePO -> {
                        SalaryGrantEmployeePO grantEmployeePO = new SalaryGrantEmployeePO();
                        grantEmployeePO.setSalaryGrantEmployeeId(salaryGrantEmployeePO.getSalaryGrantEmployeeId());
                        grantEmployeePO.setSalaryGrantSubTaskCode("");
                        salaryGrantEmployeeMapper.updateById(grantEmployeePO);
                    });
                }

                //5、检查雇员信息是否有变更，调用雇员信息变更接口。--后面补充，步骤预留
                //6、如果雇员信息有变动，则重新统计任务主表汇总信息。--后面补充，步骤预留
            }
        }

        //7、返回结果true
        return true;
    }

    @Override
    public Boolean doRejectTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        //1、根据taskCode、taskType查询，如果taskType!=0，直接返回结果false,不执行下面处理逻辑。
        //   如果taskType=0 则查询任务单主表信息SalaryGrantMainTaskPO，
        //   查询条件：salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and is_active=1；
        if (0 != salaryGrantTaskBO.getTaskType()) {
            return false;
        }

        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active=1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //2、对任务单主表记录处理如下：
                //（1）更新主表字段: task_status=0 ，approved_opinion = salaryGrantTaskBO. approvedOpinion
                SalaryGrantMainTaskPO grantMainTaskPO = new SalaryGrantMainTaskPO();
                grantMainTaskPO.setSalaryGrantMainTaskId(mainTaskPO.getSalaryGrantMainTaskId());
                grantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT); //状态:0-草稿
                grantMainTaskPO.setApprovedOpinion(salaryGrantTaskBO.getApprovedOpinion()); //审批意见
                salaryGrantMainTaskMapper.updateById(grantMainTaskPO);

                //（2）将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history. task_status = 9
                SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REJECT); //任务单状态：9-驳回
                salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                //（3）根据任务单主表salary_grant_main_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                //     查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and is_active=1
                EntityWrapper<SalaryGrantEmployeePO> mainEmployeePOEntityWrapper = new EntityWrapper<>();
                mainEmployeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantEmployeePO> mainEmployeePOList = salaryGrantEmployeeMapper.selectList(mainEmployeePOEntityWrapper);

                //（4）将任务单主表雇员信息存入历史表，
                //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(主表记录历史记录主键), String task_code(主表task_code), int task_type(主表task_type))
                salaryGrantEmployeeCommandService.saveToHistory(mainTaskHistoryPO.getSalaryGrantTaskHistoryId(), mainTaskPO.getSalaryGrantMainTaskCode(), mainTaskPO.getTaskType());

                //3、查询任务单子表信息SalaryGrantSubTaskPO，
                //   查询条件：子表.salary_grant_main_task_code = 主表.salary_grant_main_task_code and is_active = 1，如果查询结果不为空则：
                EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                if (!CollectionUtils.isEmpty(subTaskPOList)) {
                    for (SalaryGrantSubTaskPO subTaskPO : subTaskPOList) {
                        //（1）将任务单子表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history. task_status=9
                        SalaryGrantTaskHistoryPO subTaskHistoryPO = subTaskPO2HistoryPO(subTaskPO);
                        subTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_REJECT); //任务单状态：9-驳回
                        salaryGrantTaskHistoryMapper.insert(subTaskHistoryPO);

                        //（2）根据任务单子表salary_grant_sub_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                        //     查询条件：雇员表.salary_grant_sub_task_code = 任务单子表.salary_grant_sub_task_code and is_active=1
                        EntityWrapper<SalaryGrantEmployeePO> subEmployeePOEntityWrapper = new EntityWrapper<>();
                        subEmployeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", subTaskPO.getSalaryGrantSubTaskCode());
                        List<SalaryGrantEmployeePO> subEmployeePOList = salaryGrantEmployeeMapper.selectList(subEmployeePOEntityWrapper);

                        //（3）将任务单子表雇员信息存入历史表，
                        //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(子表记录历史记录主键), String task_code(子表task_code), int task_type(子表task_type))
                        salaryGrantEmployeeCommandService.saveToHistory(subTaskHistoryPO.getSalaryGrantTaskHistoryId(), subTaskPO.getSalaryGrantSubTaskCode(), subTaskPO.getTaskType());

                        //（4）调用delete方法，物理删除任务单子表信息。
                        salaryGrantSubTaskMapper.deleteById(subTaskPO.getSalaryGrantSubTaskId());
                    }
                }
                //4、根据任务单主表salary_grant_main_task_code在雇员表sg_salary_grant_employee中查询雇员信息，
                //   查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and is_active=1，
                //   更新雇员信息字段：雇员表.salary_grant_sub_task_code =“” 子表任务单编号置为空。
                EntityWrapper<SalaryGrantEmployeePO> mainEmployeePOEntityWrapper2 = new EntityWrapper<>();
                mainEmployeePOEntityWrapper2.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantEmployeePO> mainEmployeePOList2 = salaryGrantEmployeeMapper.selectList(mainEmployeePOEntityWrapper2);
                if (!CollectionUtils.isEmpty(mainEmployeePOList2)) {
                    mainEmployeePOList2.stream().forEach(salaryGrantEmployeePO -> {
                        SalaryGrantEmployeePO grantEmployeePO = new SalaryGrantEmployeePO();
                        grantEmployeePO.setSalaryGrantEmployeeId(salaryGrantEmployeePO.getSalaryGrantEmployeeId());
                        grantEmployeePO.setSalaryGrantSubTaskCode("");
                        salaryGrantEmployeeMapper.updateById(grantEmployeePO);
                    });
                }

                //5、检查雇员信息是否有变更，调用雇员信息变更接口。--后面补充，步骤预留
                //6、如果雇员信息有变动，则重新统计任务主表汇总信息。--后面补充，步骤预留

            }
        }

        //7、返回结果true
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean doInvalidTask(SalaryGrantTaskBO salaryGrantTaskBO) throws Exception {
        //（1）根据传入参数taskCode查询任务单主表信息SalaryGrantMainTaskPO，
        //     查询条件 salary_grant_main_task_code = SalaryGrantTaskBO.taskCode and sg_salary_grant_main_task.grant_type in (1、2、3) and sg_salary_grant_main_task.task_status in (0、1、2) and is_active=1。
        //     判断状态SalaryGrantMainTaskPO.task_status，如果task_status=2（审批通过），则进行（2）操作，否则进行（3）操作。
        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_type in (1, 2, 3) and task_status in (0, 1, 2) and is_active=1", salaryGrantTaskBO.getTaskCode());
        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
            for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                //状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待生成，6-未支付，7-已支付，8-撤回，9-驳回，13-作废
                String taskStatus = mainTaskPO.getTaskStatus();

                Long task_his_id = null;

                if (SalaryGrantBizConsts.TASK_STATUS_PASS.equals(taskStatus)) {
                    //（2）查询雇员信息sg_salary_grant_employee，
                    //     查询条件：雇员表.salary_grant_main_task_code = 任务单主表.salary_grant_main_task_code and雇员表.grant_status in (1,2),
                    //     如果查询结果为空，则跳转到（3）；
                    //     否则查询结果有记录，需要调用暂缓池的删除接口（接口参数为SalaryGrantMainTaskPO.batchCode、SalaryGrantMainTaskPO.salaryGrantMainTaskCode），对查询的雇员信息在发放暂缓池进行删除操作。--待顾伟发布好调用
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_status in (1, 2)", mainTaskPO.getSalaryGrantMainTaskCode());
                    List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
                    if (CollectionUtils.isEmpty(employeePOList)) {
                        //（3）对任务单主表SalaryGrantMainTaskPO进行处理
                        //     将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history.task_status= 13（作废），sg_salary_grant_task_history.invalid_reason = salaryGrantTaskBO.invalidReason；
                        SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                        mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_NULLIFY); //状态:13-作废
                        mainTaskHistoryPO.setInvalidReason(salaryGrantTaskBO.getInvalidReason()); //失效原因
                        salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                        task_his_id = mainTaskHistoryPO.getSalaryGrantTaskHistoryId();
                    } else {
                        //调用暂缓池的删除接口
                        boolean deleteResult = commonService.delDeferredEmp(mainTaskPO);
                        if (false == deleteResult) {
                            throw new Exception("调用暂缓池删除接口失败！");
                        }
                    }
                } else {
                    //（3）对任务单主表SalaryGrantMainTaskPO进行处理
                    //     将任务单主表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history.task_status= 13（作废），sg_salary_grant_task_history.invalid_reason = salaryGrantTaskBO.invalidReason；
                    SalaryGrantTaskHistoryPO mainTaskHistoryPO = mainTaskPO2HistoryPO(mainTaskPO);
                    mainTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_NULLIFY); //状态:13-作废
                    mainTaskHistoryPO.setInvalidReason(salaryGrantTaskBO.getInvalidReason()); //失效原因
                    salaryGrantTaskHistoryMapper.insert(mainTaskHistoryPO);

                    task_his_id = mainTaskHistoryPO.getSalaryGrantTaskHistoryId();
                }

                //（4）根据任务单主表查询雇员信息，将任务单主表雇员信息存入历史表，
                //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(主表记录历史记录主键), String task_code(主表task_code), int task_type(主表task_type))
                salaryGrantEmployeeCommandService.saveToHistory(task_his_id, mainTaskPO.getSalaryGrantMainTaskCode(), mainTaskPO.getTaskType());

                //（5）根据任务单主表查询子表信息，
                //     查询条件：子表.salary_grant_main_task_code = 主表.salary_grant_main_task_code and is_active = 1
                //     如果存在子表信息，对任务单子表进行处理
                //     将任务单子表记录新增入历史表sg_salary_grant_task_history，sg_salary_grant_task_history.task_status= 13（作废）；
                EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
                if (!CollectionUtils.isEmpty(subTaskPOList)) {
                    subTaskPOList.stream().forEach(subTaskPO -> {
                        //将任务单子表记录新增入历史表
                        SalaryGrantTaskHistoryPO subTaskHistoryPO = subTaskPO2HistoryPO(subTaskPO);
                        subTaskHistoryPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_NULLIFY); //状态:13-作废
                        salaryGrantTaskHistoryMapper.insert(subTaskHistoryPO);

                        //（6）根据任务单子表查询雇员信息，将任务单子表雇员信息存入历史表,
                        //     调用方法：SalaryGrantEmployeeCommandService.saveToHistory(long task_his_id(子表记录历史记录主键), String task_code(子表task_code), int task_type(子表task_type))
                        salaryGrantEmployeeCommandService.saveToHistory(subTaskHistoryPO.getSalaryGrantTaskHistoryId(), subTaskPO.getSalaryGrantSubTaskCode(), subTaskPO.getTaskType());

                        //（7）任务单子表进行物理删除，调用delete方法。
                        salaryGrantSubTaskMapper.deleteById(subTaskPO.getSalaryGrantSubTaskId());
                    });
                }

                //（8）根据任务单主表查询雇员信息，进行物理删除，调用delete方法。
                EntityWrapper<SalaryGrantEmployeePO> deleteEmployeePOEntityWrapper = new EntityWrapper<>();
                deleteEmployeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                salaryGrantEmployeeMapper.delete(deleteEmployeePOEntityWrapper);

                //（9）任务单主表进行物理删除，调用delete方法。
                salaryGrantMainTaskMapper.deleteById(mainTaskPO.getSalaryGrantMainTaskId());
            }
        }

        //（10）返回结果true
        return true;
    }


    /**
     * 记录操作信息
     * @author chenpb
     * @since 2018-06-21
     * @param taskCreateMsgDTO
     */
    @Override
    public void createTask (TaskCreateMsgDTO taskCreateMsgDTO) {
        WorkFlowTaskInfoPO po = BeanUtils.instantiate(WorkFlowTaskInfoPO.class);
        po.setWorkFlowProcessId(taskCreateMsgDTO.getProcessId());
        workFlowTaskInfoService.insert(po);
    }

    /**
     * 修改操作信息
     * @author chenpb
     * @since 2018-06-22
     * @param dto
     * @throws Exception
     */
    @Override
    public void completeTask(TaskRequestDTO dto) throws Exception {
        sheetServiceProxy.completeTask(dto);
    }

    /**
     * 启动流程
     * @author chenpb
     * @since 2018-06-22
     * @param dto
     */
    @Override
    public void startProcess (MissionRequestDTO dto) throws Exception {
        sheetServiceProxy.startProcess(dto);
    }

    /**
     * 结束工作流
     * @author chenpb
     * @since 2018-06-21
     * @param missionId
     * @param action
     */
    @Override
    public void  completeProcess(String missionId, String action) {

    }

}
