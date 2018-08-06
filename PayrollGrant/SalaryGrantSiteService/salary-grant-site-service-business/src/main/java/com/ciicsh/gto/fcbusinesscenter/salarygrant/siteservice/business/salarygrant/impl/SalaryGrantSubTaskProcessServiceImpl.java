package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSubTaskProcessService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSubTaskWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放任务单子表处理 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-30
 */
@Service
public class SalaryGrantSubTaskProcessServiceImpl implements SalaryGrantSubTaskProcessService {
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    LogClientService logClientService;
    @Autowired
    SalaryGrantSubTaskWorkFlowService salaryGrantSubTaskWorkFlowService;
    @Autowired
    SalaryGrantEmployeeService salaryGrantEmployeeService;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

    @Override
    public Boolean toSubmitMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = this.getCommonInfoFromMainTask(salaryGrantMainTaskPO);
        Map paraMap = new HashMap(4);

        paraMap.put("salaryGrantMainTaskCode", salaryGrantMainTaskPO.getSalaryGrantMainTaskCode());
        // 是否拆分，通过标志开关进行走不同分支。
        paraMap.put("isSplit", true);
        // 拆分条件
        paraMap.put("splitCondition", SalaryGrantBizConsts.SPLIT_CONDITION);
        // 发放方式 3-客户自行
        paraMap.put("grantMode", SalaryGrantBizConsts.GRANT_MODE_CUSTOMER);
        this.toDealWithSubTask(paraMap, salaryGrantSubTaskPO);

        // 发放方式 4-中智代发（客户账户）
        paraMap.put("grantMode", SalaryGrantBizConsts.GRANT_MODE_INDEPENDENCE);
        this.toDealWithSubTask(paraMap, salaryGrantSubTaskPO);
        return true;
    }

    @Override
    public Boolean toApproveMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = this.getCommonInfoFromMainTask(salaryGrantMainTaskPO);
        Map paraMap = new HashMap(4);

        paraMap.put("salaryGrantMainTaskCode", salaryGrantMainTaskPO.getSalaryGrantMainTaskCode());
        // 是否拆分，通过标志开关进行走不同分支。
        paraMap.put("isSplit", true);
        // 拆分条件
        paraMap.put("splitCondition", SalaryGrantBizConsts.SPLIT_CONDITION);
        // 发放方式 1-中智上海账户
        paraMap.put("grantMode", SalaryGrantBizConsts.GRANT_MODE_LOCAL);
        // 是否有外币发放
        paraMap.put("isIncludeForeignCurrency", salaryGrantMainTaskPO.getIncludeForeignCurrency());
        this.toDealWithSubTask(paraMap, salaryGrantSubTaskPO);

        // 发放方式 2-中智代发（委托机构）
        paraMap.put("grantMode", SalaryGrantBizConsts.GRANT_MODE_SUPPLIER);
        this.toDealWithSubTask(paraMap, salaryGrantSubTaskPO);
        return true;
    }

    /**
     * 把任务单主表的共用信息，复制到子表中。
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantSubTaskPO
     */
    private SalaryGrantSubTaskPO getCommonInfoFromMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = new SalaryGrantSubTaskPO();
        salaryGrantSubTaskPO.setSalaryGrantMainTaskCode(salaryGrantMainTaskPO.getSalaryGrantMainTaskCode());
        salaryGrantSubTaskPO.setManagementId(salaryGrantMainTaskPO.getManagementId());
        salaryGrantSubTaskPO.setManagementName(salaryGrantMainTaskPO.getManagementName());
        salaryGrantSubTaskPO.setBatchCode(salaryGrantMainTaskPO.getBatchCode());
        salaryGrantSubTaskPO.setGrantCycle(salaryGrantMainTaskPO.getGrantCycle());
        salaryGrantSubTaskPO.setGrantDate(salaryGrantMainTaskPO.getGrantDate());
        salaryGrantSubTaskPO.setGrantTime(salaryGrantMainTaskPO.getGrantTime());
        salaryGrantSubTaskPO.setGrantType(salaryGrantMainTaskPO.getGrantType());
        salaryGrantSubTaskPO.setAdversion(salaryGrantMainTaskPO.getAdversion());
        salaryGrantSubTaskPO.setAdversionType(salaryGrantMainTaskPO.getAdversionType());
        salaryGrantSubTaskPO.setAdvance(salaryGrantMainTaskPO.getAdvance());
        salaryGrantSubTaskPO.setAdvanceType(salaryGrantMainTaskPO.getAdvanceType());
        salaryGrantSubTaskPO.setProcess(false);
        salaryGrantSubTaskPO.setBalanceGrant(salaryGrantMainTaskPO.getBalanceGrant());
        salaryGrantSubTaskPO.setActive(true);
        salaryGrantSubTaskPO.setCreatedBy(SalaryGrantBizConsts.SYSTEM_EN);
        salaryGrantSubTaskPO.setCreatedTime(new Date());
        salaryGrantSubTaskPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
        salaryGrantSubTaskPO.setModifiedTime(new Date());
        /** 添加操作员字段处理，代码维度处理。业务待确认  2017-07-17*/
        if (StringUtils.isNotBlank(salaryGrantMainTaskPO.getOperatorUserId())) {
            salaryGrantSubTaskPO.setOperatorUserId(salaryGrantMainTaskPO.getOperatorUserId());
        }
        if (StringUtils.isNotBlank(salaryGrantMainTaskPO.getApproveUserId())) {
            salaryGrantSubTaskPO.setApproveUserId(salaryGrantMainTaskPO.getApproveUserId());
        }
        return salaryGrantSubTaskPO;
    }

    /**
     * 处理任务单子表信息，根据雇员信息及子表拆分规则，生成任务单子表，回写子表任务单编号至雇员信息。
     * @param salaryGrantSubTaskPO
     * @return
     */
    private void toDealWithSubTask(Map paraMap, SalaryGrantSubTaskPO salaryGrantSubTaskPO){
        if(!isExistSubTask(paraMap, salaryGrantSubTaskPO)){
            List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList = this.listSalaryGrantEmployeeBOForSubTask(paraMap);
            if(salaryGrantEmployeeBOList != null && salaryGrantEmployeeBOList.size() > 0){
                Map subTaskForCustomerMap = this.getGatherInfoForSubTask(salaryGrantSubTaskPO, salaryGrantEmployeeBOList, paraMap);
                Map<String,SalaryGrantSubTaskPO> splitSubTaskMap = (HashMap)subTaskForCustomerMap.get("splitSubTaskMap");
                Map<String, List<SalaryGrantEmployeeBO>> batchUpdateMap = (HashMap)subTaskForCustomerMap.get("batchUpdateMap");
                this.toCreateSubTask(paraMap, splitSubTaskMap, batchUpdateMap);
            }
        }
    }

    /**
     * 查询任务主表对应的发放类型任务子表是否已创建，如已存在子表信息避免重复创建。
     * @param salaryGrantSubTaskPO
     * @param paraMap
     * @return Boolean
     */
    private Boolean isExistSubTask(Map paraMap, SalaryGrantSubTaskPO salaryGrantSubTaskPO){
        EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
        subTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_type = {1} and grant_mode = {2} and is_active = 1", salaryGrantSubTaskPO.getSalaryGrantMainTaskCode(), salaryGrantSubTaskPO.getGrantType(), paraMap.get("grantMode"));
        List<SalaryGrantSubTaskPO> existSubTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
        if(existSubTaskPOList != null && existSubTaskPOList.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * 根据发放方式查询统计薪资发放雇员信息
     * @param paraMap
     * @return List<SalaryGrantEmployeeBO>
     */
    private List<SalaryGrantEmployeeBO> listSalaryGrantEmployeeBOForSubTask(Map paraMap){
        SalaryGrantEmployeeBO conditions = new SalaryGrantEmployeeBO();
        conditions.setSalaryGrantMainTaskCode((String)paraMap.get("salaryGrantMainTaskCode"));
        conditions.setGrantMode((Integer)paraMap.get("grantMode"));
        conditions.setActive(true);
        List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList = salaryGrantEmployeeMapper.selectEmpInfoByGrantMode(conditions);
        return salaryGrantEmployeeBOList;
    }

    /**
     * 通过查询统计薪资发放雇员信息中的发放方式和发放账户对雇员信息进行归类到各子表中。
     * @param salaryGrantSubTaskPO
     * @param salaryGrantEmployeeBOList
     * @param paraMap
     * @return Map
     */
    private Map getGatherInfoForSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO, List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList, Map paraMap){
        Map returnMap = new HashMap(2);
        // 发放方式
        Integer grantMode = (Integer)paraMap.get("grantMode");
        salaryGrantSubTaskPO.setGrantMode(grantMode);
        // 任务单类型
        salaryGrantSubTaskPO.setTaskType(grantMode);
        // 是否按条件对子表进行拆分
        boolean isSplit = (Boolean) paraMap.get("isSplit");

        if(isSplit){
            // 拆分条件
            String splitCondition = (String)paraMap.get("splitCondition");
            if(SalaryGrantBizConsts.SPLIT_CONDITION.equals(splitCondition)){
                returnMap = this.toSplitByGrantAccountCode(salaryGrantSubTaskPO, salaryGrantEmployeeBOList, paraMap);
            }else{
                // 预留未来需求变更
            }
        }else{
            // 预留未来需求变更
        }
        return returnMap;
    }

    /**
     * 建立子表信息
     * @param paraMap
     * @param splitSubTaskMap
     * @param batchUpdateMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    protected void toCreateSubTask(Map paraMap, Map<String,SalaryGrantSubTaskPO> splitSubTaskMap, Map<String, List<SalaryGrantEmployeeBO>> batchUpdateMap){
        List<SalaryGrantSubTaskPO> salaryGrantSubTaskPOList = new ArrayList<>();
        Map<String, List<SalaryGrantEmployeeBO>> batchUpdateLastMap = new HashMap<>();
        // 发放方式
        Integer grantMode = (Integer)paraMap.get("grantMode");
        String salaryGrantMainTaskCode = (String)paraMap.get("salaryGrantMainTaskCode");
        // 生成薪资发放任务单子表的entity_id
        Map entityParam = new HashMap(2);
        splitSubTaskMap.forEach((grantAccountCode,salaryGrantSubTaskPO) -> {
            String idCode = "";
            if(SalaryGrantBizConsts.GRANT_MODE_LOCAL.equals(grantMode)){
                if((Boolean) paraMap.get("isIncludeForeignCurrency")){
                    if(SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_RMB_ENTITY_PREFIX.equals(grantAccountCode)){
                        idCode = SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_RMB_ENTITY_PREFIX;
                    } else{
                        idCode = SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_FOREIGN_CURRENCY_ENTITY_PREFIX;
                    }
                }else{
                    idCode = SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_RMB_ENTITY_PREFIX;
                }
                salaryGrantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_PASS);
            }else if(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(grantMode)){
                idCode = SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_NONLOCAL_ENTITY_PREFIX;
                salaryGrantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_COMBINE_WAIT);
            }else if(SalaryGrantBizConsts.GRANT_MODE_INDEPENDENCE.equals(grantMode)){
                idCode = SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_INDEPENDENCE_ENTITY_PREFIX;
                salaryGrantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CONFIRM);
            }else{
                idCode = SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_CUSTOMER_ENTITY_PREFIX;
                salaryGrantSubTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_CONFIRM);
            }
            entityParam.put("idCode",idCode);
            String entityId = commonService.getEntityIdForSalaryGrantTask(entityParam);
            if(!StringUtils.isEmpty(entityId)){
                salaryGrantSubTaskPO.setSalaryGrantSubTaskCode(entityId);
            }else{
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单子表信息，计算批次号为：" + salaryGrantSubTaskPO.getBatchCode()).setContent("调用公共服务生成entity_id失败！"));
            }
            salaryGrantSubTaskPOList.add(salaryGrantSubTaskPO);
            batchUpdateLastMap.put(entityId, batchUpdateMap.get(grantAccountCode));
        });

        if (!CollectionUtils.isEmpty(salaryGrantSubTaskPOList)) {
            Boolean isCreated = salaryGrantSubTaskWorkFlowService.insertBatch(salaryGrantSubTaskPOList, salaryGrantSubTaskPOList.size());
            if(isCreated){
                this.toBatchUpdateSalaryGrantEmployee(batchUpdateLastMap, salaryGrantMainTaskCode);
            }else{
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单子表信息，任务单主表编号为：" + salaryGrantMainTaskCode).setContent("新增薪资发放任务单子表信息失败！"));
            }
        }
    }

    /**
     * 回填薪资发放雇员信息表中的子表编号，批量修改。
     * @param batchUpdateMap
     * @param salaryGrantMainTaskCode
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    protected void toBatchUpdateSalaryGrantEmployee(Map<String, List<SalaryGrantEmployeeBO>> batchUpdateMap, String salaryGrantMainTaskCode){
        if (!CollectionUtils.isEmpty(batchUpdateMap)) {
            batchUpdateMap.forEach((salaryGrantSubTaskCode,salaryGrantEmployeeBOList)-> {
                List<SalaryGrantEmployeePO> updateList = new ArrayList();

                salaryGrantEmployeeBOList.forEach(SalaryGrantEmployeeBO -> {
                    SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();
                    salaryGrantEmployeePO.setSalaryGrantEmployeeId(SalaryGrantEmployeeBO.getSalaryGrantEmployeeId());
                    salaryGrantEmployeePO.setSalaryGrantSubTaskCode(salaryGrantSubTaskCode);
                    updateList.add(salaryGrantEmployeePO);
                });

                if (!CollectionUtils.isEmpty(updateList)) {
                    // 调用薪资发放雇员信息表的批量修改方法
                    Boolean isModified = salaryGrantEmployeeService.updateBatchById(updateList);

                    if(!isModified){
                        logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单子表信息，任务单主表编号为：" + salaryGrantMainTaskCode).setContent("修改雇员信息的薪资发放任务单子表编号失败！"));
                    }
                }
            }) ;
        }
    }

    /**
     * 薪资发放任务单子表拆分
     * @param salaryGrantSubTaskPO
     * @param salaryGrantEmployeeBOList
     * @param paraMap
     * @return Map
     */
    private Map toSplitByGrantAccountCode(SalaryGrantSubTaskPO salaryGrantSubTaskPO, List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList, Map paraMap){
        Map returnMap = new HashMap(2);
        Map<String,SalaryGrantSubTaskPO> splitSubTaskMap = new HashMap();
        Map<String,List<SalaryGrantEmployeeBO>> batchUpdateMap = new HashMap<>();

        // 1、对不同发放账户信息进行分别统计，生成list
        if(!CollectionUtils.isEmpty(salaryGrantEmployeeBOList)){
            // 发放方式
            Integer grantMode = (Integer)paraMap.get("grantMode");
            // 上海本地发放的任务子表，按照人民币、外币进行拆分2个子表。
            if(SalaryGrantBizConsts.GRANT_MODE_LOCAL.equals(grantMode)){
                // 如果有外币发放，则对本地发放子表拆分2张，一张人民币LTB，一张外币LTW。
                if((Boolean) paraMap.get("isIncludeForeignCurrency")){
                    // 按照发放币种currencyCode分组
                    List<SalaryGrantEmployeeBO> ltbList = salaryGrantEmployeeBOList.stream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.CURRENCY_CNY)).collect(Collectors.toList());
                    Map returnMapLTB = this.toCollectForSubTask(salaryGrantSubTaskPO, ltbList);
                    Map<String,SalaryGrantSubTaskPO> splitSubTaskMapLTB = (HashMap)returnMapLTB.get("splitSubTaskMap");
                    Map<String,List<SalaryGrantEmployeeBO>> batchUpdateMapLTB = (HashMap)returnMapLTB.get("batchUpdateMap");
                    // 上海本地发放
                    splitSubTaskMap.put(SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_RMB_ENTITY_PREFIX, splitSubTaskMapLTB.get(grantMode));
                    batchUpdateMap.put(SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_RMB_ENTITY_PREFIX, batchUpdateMapLTB.get(grantMode));

                    List<SalaryGrantEmployeeBO> ltwList = salaryGrantEmployeeBOList.stream().filter(SalaryGrantEmployeeBO -> !SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.CURRENCY_CNY)).collect(Collectors.toList());
                    Map returnMapLTW = this.toCollectForSubTask(salaryGrantSubTaskPO, ltwList);
                    Map<String,SalaryGrantSubTaskPO> splitSubTaskMapLTW = (HashMap)returnMapLTW.get("splitSubTaskMap");
                    Map<String,List<SalaryGrantEmployeeBO>> batchUpdateMapLTW = (HashMap)returnMapLTW.get("batchUpdateMap");
                    splitSubTaskMap.put(SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_FOREIGN_CURRENCY_ENTITY_PREFIX, splitSubTaskMapLTW.get(grantMode));
                    batchUpdateMap.put(SalaryGrantBizConsts.SALARY_GRANT_SUB_TASK_LOCAL_FOREIGN_CURRENCY_ENTITY_PREFIX, batchUpdateMapLTW.get(grantMode));
                }else{
                    // 没有外币发放，只生成一张人民币LTB子表
                    returnMap = this.toCollectForSubTask(salaryGrantSubTaskPO, salaryGrantEmployeeBOList);
                }
            }else{
                // 其他发放方式按照发放账户进行拆分
                returnMap = this.toCollectForSubTask(salaryGrantSubTaskPO, salaryGrantEmployeeBOList);
            }

            splitSubTaskMap = (Map<String, SalaryGrantSubTaskPO>) returnMap.get("splitSubTaskMap");
            batchUpdateMap = (Map<String, List<SalaryGrantEmployeeBO>>) returnMap.get("batchUpdateMap");
        }

        returnMap.put("splitSubTaskMap", splitSubTaskMap);
        returnMap.put("batchUpdateMap", batchUpdateMap);

        return returnMap;
    }

    /**
     * 根据传入雇员信息list，对结果集进行汇总统计操作。
     * @param salaryGrantSubTaskPO
     * @param salaryGrantEmployeeBOList
     * @return Map
     */
    private Map toCollectForSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO, List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList){
        Map returnMap = new HashMap(2);
        Map<String,SalaryGrantSubTaskPO> splitSubTaskMap = new HashMap();
        Map<String, List<SalaryGrantEmployeeBO>> batchUpdateMap = new HashMap<>();

        // 按照发放账户grantAccountCode分组
        Map<String, List<SalaryGrantEmployeeBO>> accountMap = salaryGrantEmployeeBOList.stream().collect(Collectors.groupingBy(SalaryGrantEmployeeBO::getGrantAccountCode));
        // 遍历每个发放账户的分组信息
        accountMap.forEach((account,accountList) -> {
            if(account != null){
                try {
                    SalaryGrantEmployeeBO salaryGrantEmployeeBO = accountList.get(0);
                    SalaryGrantSubTaskPO salaryGrantSubTaskPOTemp = (SalaryGrantSubTaskPO) salaryGrantSubTaskPO.clone();
                    salaryGrantSubTaskPOTemp.setGrantAccountCode(account);
                    salaryGrantSubTaskPOTemp.setGrantAccountName(salaryGrantEmployeeBO.getGrantAccountName());
                    BigDecimal totalMoney = accountList.stream().map(SalaryGrantEmployeeBO::getPaymentAmountForRMB).reduce(BigDecimal.ZERO, BigDecimal::add);
                    salaryGrantSubTaskPOTemp.setPaymentTotalSum(totalMoney);

                    // 对相同发放账户的雇员信息遍历list，统计总人数
                    salaryGrantSubTaskPOTemp.setTotalPersonCount(Integer.valueOf(accountList.size()));
                    // 针对发放账户统计中、外方人数，根据country_code。按照拆分后的雇员个数进行统计
                    Long chineseCount = accountList.stream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
                    salaryGrantSubTaskPOTemp.setChineseCount(ObjectUtils.isEmpty(chineseCount) ? 0 : Integer.valueOf(chineseCount.toString()));
                    Long foreignerCount = accountList.stream().filter(SalaryGrantEmployeeBO -> !SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
                    salaryGrantSubTaskPOTemp.setForeignerCount(ObjectUtils.isEmpty(foreignerCount) ? 0 : Integer.valueOf(foreignerCount.toString()));

                    Long remarkCount = accountList.stream().filter(SalaryGrantEmployeeBO -> !"".equals(SalaryGrantEmployeeBO.getRemark()) && SalaryGrantEmployeeBO.getRemark() != null).map(SalaryGrantEmployeeBO::getEmployeeId).count();
                    if(remarkCount > 0L){
                        salaryGrantSubTaskPOTemp.setRemark(SalaryGrantBizConsts.TASK_REMARK);
                    }
                    splitSubTaskMap.put(account, salaryGrantSubTaskPOTemp);
                    batchUpdateMap.put(account,accountList);
                } catch (CloneNotSupportedException e) {
                    logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("克隆任务单子表异常 --> exception").setContent(e.getMessage()));
                }
            }
            }
            );
        returnMap.put("splitSubTaskMap", splitSubTaskMap);
        returnMap.put("batchUpdateMap", batchUpdateMap);

        return returnMap;
    }
}
