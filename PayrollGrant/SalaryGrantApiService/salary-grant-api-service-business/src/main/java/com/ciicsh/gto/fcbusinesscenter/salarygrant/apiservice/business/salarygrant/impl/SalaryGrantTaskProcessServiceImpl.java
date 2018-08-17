package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmpFcBankProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.CmyFcBankCardRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.CmyFcBankCardResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantEmployeeService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantTaskProcessService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放任务单处理 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-01
 */
@Service
public class SalaryGrantTaskProcessServiceImpl extends ServiceImpl<SalaryGrantTaskMapper, SalaryGrantTaskPO> implements SalaryGrantTaskProcessService {
    @Autowired
    CommonService commonService;
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    SalaryGrantTaskMapper salaryGrantTaskMapper;
    @Autowired
    LogClientService logClientService;
    @Autowired
    SalaryGrantEmployeeService salaryGrantEmployeeService;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    EmpFcBankProxy empFcBankProxy;

    /**
     * 根据退票雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-08-01
     * @param employeeRefundList
     * @param batchCode
     * @return ResponseRefundBO
     */
    @Override
    public ResponseRefundBO createRefundTask(List<SalaryGrantEmployeeRefundBO> employeeRefundList, String batchCode) {
        ResponseRefundBO responseRefundBO = new ResponseRefundBO();
        if(!CollectionUtils.isEmpty(employeeRefundList)){
            Integer grantType = employeeRefundList.get(0).getGrantType();

            EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
            subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} and batch_code = {1} and is_active = 1", employeeRefundList.get(0).getTaskCode(), batchCode);
            List<SalaryGrantSubTaskPO> existSubTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
            if(existSubTaskPOList != null && existSubTaskPOList.size() > 0){
                String mainTaskCode = existSubTaskPOList.get(0).getSalaryGrantMainTaskCode();
                String newMainTaskCode = "";
                SalaryGrantTaskPO mainTaskPO = new SalaryGrantTaskPO();
                SalaryGrantTaskPO salaryGrantTaskPO = new SalaryGrantTaskPO();
                EntityWrapper<SalaryGrantTaskPO> taskPOEntityWrapper = new EntityWrapper<>();
                taskPOEntityWrapper.where("salary_grant_main_task_code = {0} and batch_code = {1} and is_active = 1", mainTaskCode, batchCode);
                List<SalaryGrantTaskPO> mainTaskPOList = salaryGrantTaskMapper.selectList(taskPOEntityWrapper);
                if(!CollectionUtils.isEmpty(mainTaskPOList)){
                    mainTaskPO = mainTaskPOList.get(0);
                }else{
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放退票发放").setTitle("获取任务单主表：" + mainTaskCode).setContent("信息为空，查询失败！"));
                    responseRefundBO.setProcessResult(false);
                    responseRefundBO.setProcessMessage("传入退票雇员信息所属原主任务信息不存在");
                }

                EntityWrapper<SalaryGrantTaskPO> existTaskPOEntityWrapper = new EntityWrapper<>();
                existTaskPOEntityWrapper.where("batch_code = {0} and  grant_type = {1} and task_status = 0 and is_active = 1", batchCode, grantType);
                List<SalaryGrantTaskPO> refundTaskPOList = salaryGrantTaskMapper.selectList(existTaskPOEntityWrapper);
                if(!CollectionUtils.isEmpty(refundTaskPOList)){
                    newMainTaskCode = refundTaskPOList.get(0).getSalaryGrantMainTaskCode();
                    salaryGrantTaskPO = refundTaskPOList.get(0);
                }else{
                    SalaryGrantTaskPO newMainTaskPO = this.createNewTask(mainTaskPO, grantType);
                    if(!ObjectUtils.isEmpty(newMainTaskPO)){
                        newMainTaskCode = newMainTaskPO.getSalaryGrantMainTaskCode();
                        salaryGrantTaskPO = newMainTaskPO;
                    }else{
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放退票发放").setTitle("生成雇员信息：" + mainTaskCode).setContent("信息生成失败！"));
                        responseRefundBO.setProcessResult(false);
                        responseRefundBO.setProcessMessage("创建发放任务失败");
                    }
                }

                final String newCode = newMainTaskCode;

                EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_status = 3 and is_active = 1", mainTaskCode);
                List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);

                EntityWrapper<SalaryGrantEmployeePO> existEmployeePOEntityWrapper = new EntityWrapper<>();
                existEmployeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", newMainTaskCode);
                List<SalaryGrantEmployeePO> existEmployeePOList = salaryGrantEmployeeMapper.selectList(existEmployeePOEntityWrapper);

                List<SalaryGrantEmployeePO> employeePONewList = new ArrayList<>();

                if(!CollectionUtils.isEmpty(employeePOList)){
                    employeePONewList = this.toProcessEmpData(employeePOList, employeeRefundList,existEmployeePOList, newCode);
                }else{
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放退票发放").setTitle("查询退票雇员信息：" + mainTaskCode).setContent("信息为空，查询失败！"));
                    responseRefundBO.setProcessResult(false);
                    responseRefundBO.setProcessMessage("传入退票雇员信息所属原任务信息未查询到雇员信息");
                }
                if(!CollectionUtils.isEmpty(employeePONewList)){
                    this.createMainTaskInfo(employeePONewList, salaryGrantTaskPO);
                    responseRefundBO.setProcessResult(true);
                }else{
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放退票发放").setTitle("查询雇员信息").setContent("信息为空，生成任务失败！"));
                    responseRefundBO.setProcessResult(false);
                    responseRefundBO.setProcessMessage("传入退票雇员信息未在原任务中查询到对应雇员信息，或者已在退票任务中不进行重复创建处理。");
                }
            }else{
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放退票发放").setTitle("查询任务单信息").setContent("信息为空，查询失败！"));
                responseRefundBO.setProcessResult(false);
                responseRefundBO.setProcessMessage("传入退票雇员信息所属原任务信息不存在");
            }
        }else{
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放退票发放").setTitle("获取传入退票雇员信息").setContent("传入退票信息为空！"));
            responseRefundBO.setProcessResult(false);
            responseRefundBO.setProcessMessage("传入退票雇员信息为空");
        }
        return responseRefundBO;
    }

    /**
     * 处理退票雇员信息
     * @author gaoyang
     * @date 2018-08-01
     * @param employeePOList
     * @param employeeRefundList
     * @param existEmployeePOList
     * @param newCode
     * @return List<SalaryGrantEmployeePO>
     */
    private List<SalaryGrantEmployeePO> toProcessEmpData(List<SalaryGrantEmployeePO> employeePOList, List<SalaryGrantEmployeeRefundBO> employeeRefundList, List<SalaryGrantEmployeePO> existEmployeePOList, String newCode){
        List<SalaryGrantEmployeePO> employeePONewList = new ArrayList<>();
        employeeRefundList.stream().forEach(salaryGrantEmployeeRefundBO ->{
            String companyId = salaryGrantEmployeeRefundBO.getCompanyId();
            String employeeId = salaryGrantEmployeeRefundBO.getEmployeeId();
            String employeeOldBankAccount = salaryGrantEmployeeRefundBO.getCardNum();
            String employeeNewBankAccount = salaryGrantEmployeeRefundBO.getCardNumNew();
            BigDecimal payAmount = salaryGrantEmployeeRefundBO.getPaymentAmount();
            Long bankcardId = salaryGrantEmployeeRefundBO.getBankcardId();

            SalaryGrantEmployeePO salaryGrantEmployeePO =  employeePOList.stream().filter(employeePO -> !StringUtils.isEmpty(employeePO.getCompanyId()) && employeePO.getCompanyId().equals(companyId) &&
                    !StringUtils.isEmpty(employeePO.getEmployeeId()) && employeePO.getEmployeeId().equals(employeeId) &&
                    !StringUtils.isEmpty(employeePO.getCardNum()) && employeePO.getCardNum().equals(employeeOldBankAccount) &&
                    !ObjectUtils.isEmpty(employeePO.getPaymentAmount()) && employeePO.getPaymentAmount().compareTo(payAmount) == 0 ).findFirst().orElse(null);
            if (!ObjectUtils.isEmpty(salaryGrantEmployeePO)) {
                if(!CollectionUtils.isEmpty(existEmployeePOList)){
                    SalaryGrantEmployeePO existSalaryGrantEmployeePO =  existEmployeePOList.stream().filter(existEmployeePO -> !StringUtils.isEmpty(existEmployeePO.getCompanyId()) && existEmployeePO.getCompanyId().equals(companyId) &&
                            !StringUtils.isEmpty(existEmployeePO.getEmployeeId()) && existEmployeePO.getEmployeeId().equals(employeeId) &&
                            !StringUtils.isEmpty(existEmployeePO.getCardNum()) && existEmployeePO.getCardNum().equals(employeeNewBankAccount) &&
                            !ObjectUtils.isEmpty(existEmployeePO.getPaymentAmount()) && existEmployeePO.getPaymentAmount().compareTo(payAmount) == 0 ).findFirst().orElse(null);
                    if(ObjectUtils.isEmpty(existSalaryGrantEmployeePO)){
                        SalaryGrantEmployeePO salaryGrantEmployeePONew = this.createNewEmployee(salaryGrantEmployeePO, employeeNewBankAccount, newCode, bankcardId);
                        employeePONewList.add(salaryGrantEmployeePONew);
                    }
                }else{
                    SalaryGrantEmployeePO salaryGrantEmployeePONew = this.createNewEmployee(salaryGrantEmployeePO, employeeNewBankAccount, newCode, bankcardId);
                    employeePONewList.add(salaryGrantEmployeePONew);
                }
            }
        });
        return employeePONewList;
    }

    /**
     * 新建雇员信息
     * @author gaoyang
     * @date 2018-08-01
     * @param salaryGrantEmployeePO
     * @param employeeNewBankAccount
     * @param bankcardId
     * @param newCode
     * @return SalaryGrantEmployeePO
     */
    private SalaryGrantEmployeePO createNewEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO, String employeeNewBankAccount, String newCode, Long bankcardId){
        if(!salaryGrantEmployeePO.getCardNum().equals(employeeNewBankAccount)){
            salaryGrantEmployeePO.setCardNum(employeeNewBankAccount);
            // query new card info
            EmployeeBankcardBO employeeBankcardBO = this.getEmployeeBankcardInfo(bankcardId);
            salaryGrantEmployeePO.setBankcardId(bankcardId);
            salaryGrantEmployeePO.setCardNum(employeeNewBankAccount);
            salaryGrantEmployeePO.setAccountName(employeeBankcardBO.getAccountName());
            salaryGrantEmployeePO.setBankCode(employeeBankcardBO.getBankCode());
            salaryGrantEmployeePO.setDepositBank(employeeBankcardBO.getDepositBank());
            salaryGrantEmployeePO.setSwiftCode(employeeBankcardBO.getSwiftCode());
            salaryGrantEmployeePO.setIban(employeeBankcardBO.getIban());
            salaryGrantEmployeePO.setBankcardType(employeeBankcardBO.getBankcardType());
            salaryGrantEmployeePO.setBankcardProvinceCode(employeeBankcardBO.getProvinceCode());
            salaryGrantEmployeePO.setBankcardCityCode(employeeBankcardBO.getCityCode());
            salaryGrantEmployeePO.setDefaultCard(employeeBankcardBO.getDefaultCard());
        }

        salaryGrantEmployeePO.setSalaryGrantMainTaskCode(newCode);
        salaryGrantEmployeePO.setSalaryGrantSubTaskCode("");
        salaryGrantEmployeePO.setGrantStatus(SalaryGrantBizConsts.GRANT_STATUS_NORMAL);
        salaryGrantEmployeePO.setActive(true);
        salaryGrantEmployeePO.setCreatedBy(SalaryGrantBizConsts.SYSTEM_EN);
        salaryGrantEmployeePO.setCreatedTime(new Date());
        salaryGrantEmployeePO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
        salaryGrantEmployeePO.setModifiedTime(new Date());
        return salaryGrantEmployeePO;
    }

    /**
     * 查询银行卡信息
     * @author gaoyang
     * @date 2018-08-01
     * @param bankcardId
     * @return EmployeeBankcardBO
     */
    private EmployeeBankcardBO getEmployeeBankcardInfo(Long bankcardId){
        EmployeeBankcardBO employeeBankcardBO = new EmployeeBankcardBO();
        CmyFcBankCardRequestDTO cmyFcBankCardRequestDTO = new CmyFcBankCardRequestDTO();
        cmyFcBankCardRequestDTO.setBankcardId(String.valueOf(bankcardId));

        JsonResult<List<CmyFcBankCardResponseDTO>> bankCardInfoResult = empFcBankProxy.getFcBankCardInfo(cmyFcBankCardRequestDTO);
        List<CmyFcBankCardResponseDTO>  cmyFcBankCardResponseDTOList = bankCardInfoResult.getData();
        if(!CollectionUtils.isEmpty(cmyFcBankCardResponseDTOList)){
            CmyFcBankCardResponseDTO cmyFcBankCardResponseDTO = cmyFcBankCardResponseDTOList.get(0);
            employeeBankcardBO = this.toConvertBankCardInfo(cmyFcBankCardResponseDTO);
        }
        return employeeBankcardBO;
    }

    /**
     * 处理银行卡信息
     * @author gaoyang
     * @date 2018-08-01
     * @param cmyFcBankCardResponseDTO
     * @return EmployeeBankcardBO
     */
    private EmployeeBankcardBO toConvertBankCardInfo(CmyFcBankCardResponseDTO cmyFcBankCardResponseDTO){
        EmployeeBankcardBO employeeBankcardBO = new EmployeeBankcardBO();
        employeeBankcardBO.setBankcardId(cmyFcBankCardResponseDTO.getBankcardId());
        employeeBankcardBO.setEmployeeId(cmyFcBankCardResponseDTO.getEmployeeId());
        employeeBankcardBO.setCardNum(cmyFcBankCardResponseDTO.getCardNum());
        employeeBankcardBO.setAccountName(cmyFcBankCardResponseDTO.getAccountName());
        employeeBankcardBO.setBankCode(cmyFcBankCardResponseDTO.getBankCode());
        employeeBankcardBO.setDepositBank(cmyFcBankCardResponseDTO.getDepositBank());
        employeeBankcardBO.setSwiftCode(cmyFcBankCardResponseDTO.getSwiftCode());
        employeeBankcardBO.setIban(cmyFcBankCardResponseDTO.getIban());
        employeeBankcardBO.setBankcardType(cmyFcBankCardResponseDTO.getBankcardType());
        employeeBankcardBO.setUsage(cmyFcBankCardResponseDTO.getUsage());
        if(!ObjectUtils.isEmpty(cmyFcBankCardResponseDTO.getDefult()) && cmyFcBankCardResponseDTO.getDefult() == 1){
            employeeBankcardBO.setDefaultCard(true);
        }else{
            employeeBankcardBO.setDefaultCard(false);
        }
        employeeBankcardBO.setDefaultCardCurrencyCode(cmyFcBankCardResponseDTO.getCurrencyCode());
        employeeBankcardBO.setDefaultCardExchange(cmyFcBankCardResponseDTO.getExchange());
        employeeBankcardBO.setProvinceCode(cmyFcBankCardResponseDTO.getProvinceCode());
        employeeBankcardBO.setCityCode(cmyFcBankCardResponseDTO.getCityCode());
        return employeeBankcardBO;
    }

    /**
     * 创建任务信息
     * @author gaoyang
     * @date 2018-08-01
     * @param originPO
     * @param grantType
     * @return SalaryGrantTaskPO
     */
    private SalaryGrantTaskPO createNewTask(SalaryGrantTaskPO originPO, Integer grantType){
        SalaryGrantTaskPO newPO = new SalaryGrantTaskPO();
        newPO.setManagementId(originPO.getManagementId());
        newPO.setManagementName(originPO.getManagementName());
        newPO.setBatchCode(originPO.getBatchCode());
        newPO.setOriginBatchCode(originPO.getOriginBatchCode());
        newPO.setGrantCycle(originPO.getGrantCycle());
        newPO.setGrantDate(originPO.getGrantDate());
        newPO.setGrantTime(originPO.getGrantTime());
        newPO.setGrantType(grantType);
        newPO.setGrantMode(originPO.getGrantMode());
        newPO.setAdversion(originPO.getAdversion());
        newPO.setAdvanceType(originPO.getAdvanceType());
        newPO.setAdvance(originPO.getAdvance());
        newPO.setAdvanceType(originPO.getAdvanceType());
        newPO.setProcess(originPO.getProcess());
        newPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
        newPO.setTaskType(SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK);
        newPO.setBalanceGrant(originPO.getBalanceGrant());
        newPO.setBatchVersion(originPO.getBatchVersion());
        newPO.setActive(true);
        newPO.setCreatedBy(SalaryGrantBizConsts.SYSTEM_EN);
        newPO.setCreatedTime(new Date());
        newPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
        newPO.setModifiedTime(new Date());

        Map entityParam = new HashMap(2);
        entityParam.put("idCode",SalaryGrantBizConsts.SALARY_GRANT_MAIN_TASK_ENTITY_PREFIX);
        String entityId = commonService.getEntityIdForSalaryGrantTask(entityParam);
        if(!StringUtils.isEmpty(entityId)){
            newPO.setSalaryGrantMainTaskCode(entityId);
            insertOrUpdate(newPO);
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单退票信息，计算批次号为："+newPO.getBatchCode()).setContent("调用公共服务生成entity_id失败！"));
            return null;
        }
        return newPO;
    }

    /**
     * 创建雇员及任务信息
     * @author gaoyang
     * @date 2018-08-01
     * @param employeePONewList
     * @param salaryGrantTaskPO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    protected void createMainTaskInfo(List<SalaryGrantEmployeePO> employeePONewList, SalaryGrantTaskPO salaryGrantTaskPO){
        salaryGrantEmployeeService.insertBatch(employeePONewList);
        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", salaryGrantTaskPO.getSalaryGrantMainTaskCode());
        List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);

        List<SalaryGrantEmployeePO> localList = employeePOList.parallelStream().filter(SalaryGrantEmployeePO -> SalaryGrantEmployeePO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL)).collect(Collectors.toList());
        Long ltwCount = localList.parallelStream().filter(SalaryGrantEmployeePO -> !SalaryGrantEmployeePO.getCurrencyCode().equals(SalaryGrantBizConsts.CURRENCY_CNY)).count();
        if(ltwCount > 0L){
            salaryGrantTaskPO.setIncludeForeignCurrency(true);
        }
        Double paymentTotalSum = Double.valueOf(String.valueOf(employeePOList.parallelStream().map(SalaryGrantEmployeePO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));
        salaryGrantTaskPO.setPaymentTotalSum(BigDecimal.valueOf(paymentTotalSum));
        salaryGrantTaskPO.setTotalPersonCount(Integer.valueOf(employeePOList.size()));
        Long chineseCount = employeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
        salaryGrantTaskPO.setChineseCount(Integer.valueOf(chineseCount.intValue()));
        Long foreignerCount = employeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> !SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
        salaryGrantTaskPO.setForeignerCount(Integer.valueOf(foreignerCount.intValue()));
        Long localGrantCount = employeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL)).count();
        salaryGrantTaskPO.setLocalGrantCount(Integer.valueOf(localGrantCount.intValue()));
        Long supplierGrantCount = employeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER)).count();
        salaryGrantTaskPO.setSupplierGrantCount(Integer.valueOf(supplierGrantCount.intValue()));
        StringBuffer grantModeForTask = new StringBuffer();
        for(SalaryGrantEmployeePO salaryGrantEmployeePO : employeePOList){
            Integer grantMode = salaryGrantEmployeePO.getGrantMode();
            if(grantMode != null && grantModeForTask.indexOf(String.valueOf(grantMode)) < 0){
                grantModeForTask = grantModeForTask.append(grantMode);
            }
        }
        salaryGrantTaskPO.setGrantMode(grantModeForTask.toString());
        salaryGrantTaskPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
        salaryGrantTaskPO.setModifiedTime(new Date());
        salaryGrantTaskMapper.updateById(salaryGrantTaskPO);
    }

    /**
     * 根据暂缓雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-08-07
     * @param employeeReprieveList
     * @param batchCode
     * @param taskCode
     * @return ResponseReprieveBO
     */
    @Override
    public ResponseReprieveBO createReprieveTask(List<SalaryGrantEmployeeReprieveBO> employeeReprieveList, String batchCode, String taskCode) {
        ResponseReprieveBO responseReprieveBO = new ResponseReprieveBO();
        if(!CollectionUtils.isEmpty(employeeReprieveList)){
            String newMainTaskCode = "";
            SalaryGrantTaskPO mainTaskPO = new SalaryGrantTaskPO();
            SalaryGrantTaskPO salaryGrantTaskPO = new SalaryGrantTaskPO();
            List<SalaryGrantEmployeePO> employeePONewList = new ArrayList<>();

            EntityWrapper<SalaryGrantTaskPO> mainTaskEntityWrapper = new EntityWrapper<>();
            mainTaskEntityWrapper.where("salary_grant_main_task_code = {0} and batch_code = {1} and grant_type in(1,2,3) and is_active = 1", taskCode, batchCode);
            List<SalaryGrantTaskPO> mainTaskList = salaryGrantTaskMapper.selectList(mainTaskEntityWrapper);

            EntityWrapper<SalaryGrantTaskPO> originTaskEntityWrapper = new EntityWrapper<>();
            originTaskEntityWrapper.where("salary_grant_main_task_code = {0} and batch_code = {1} and grant_type in(1,2,3) and task_status = 0 and is_active = 1", taskCode, batchCode);
            List<SalaryGrantTaskPO> originTaskList = salaryGrantTaskMapper.selectList(originTaskEntityWrapper);

            EntityWrapper<SalaryGrantTaskPO> existReprieveTaskEntityWrapper = new EntityWrapper<>();
            existReprieveTaskEntityWrapper.where("batch_code = {0} and grant_type = 4 and task_status = 0 and is_active = 1", batchCode);
            List<SalaryGrantTaskPO> existReprieveTaskList = salaryGrantTaskMapper.selectList(existReprieveTaskEntityWrapper);

            EntityWrapper<SalaryGrantEmployeePO> originEmployeeEntityWrapper = new EntityWrapper<>();
            originEmployeeEntityWrapper.where("salary_grant_main_task_code = {0} and grant_status in (1,2) and is_active = 1", taskCode);
            List<SalaryGrantEmployeePO> originEmployeePOList = salaryGrantEmployeeMapper.selectList(originEmployeeEntityWrapper);

            if(!CollectionUtils.isEmpty(mainTaskList)){
                mainTaskPO = mainTaskList.get(0);
                if(!CollectionUtils.isEmpty(originTaskList)){
                    if(!CollectionUtils.isEmpty(originEmployeePOList)){
                        employeePONewList = this.toProcessEmpData(employeeReprieveList,originEmployeePOList,taskCode, true);
                        if(!CollectionUtils.isEmpty(employeePONewList)){
                            responseReprieveBO.setProcessResult(true);
                        }else{
                            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询雇员信息").setContent("信息为空，生成任务失败！"));
                            responseReprieveBO.setProcessResult(false);
                            responseReprieveBO.setProcessMessage("传入暂缓雇员信息未在原任务中查询到对应雇员信息，修改暂缓发放雇员信息失败！");
                        }
                    }else{
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询原暂缓雇员信息").setContent("暂缓信息为空！"));
                        responseReprieveBO.setProcessResult(false);
                        responseReprieveBO.setProcessMessage("查询原暂缓雇员信息为空");
                    }
                }else if(!CollectionUtils.isEmpty(existReprieveTaskList)){
                    if(!CollectionUtils.isEmpty(originEmployeePOList)){
                        newMainTaskCode = existReprieveTaskList.get(0).getSalaryGrantMainTaskCode();
                        salaryGrantTaskPO = existReprieveTaskList.get(0);
                        final String newCode = newMainTaskCode;
                        employeePONewList = this.toProcessEmpData(employeeReprieveList,originEmployeePOList,newCode, false);
                        if(!CollectionUtils.isEmpty(employeePONewList)){
                            this.createMainTaskInfo(employeePONewList, salaryGrantTaskPO);
                            responseReprieveBO.setProcessResult(true);
                        }else{
                            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询雇员信息").setContent("信息为空，生成任务失败！"));
                            responseReprieveBO.setProcessResult(false);
                            responseReprieveBO.setProcessMessage("传入暂缓雇员信息未在原任务中查询到对应雇员信息，生成暂缓发放雇员信息失败！");
                        }
                    }else{
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询原暂缓雇员信息").setContent("暂缓信息为空！"));
                        responseReprieveBO.setProcessResult(false);
                        responseReprieveBO.setProcessMessage("查询原暂缓雇员信息为空");
                    }
                }else{
                    SalaryGrantTaskPO newMainTaskPO = this.createNewTask(mainTaskPO, SalaryGrantBizConsts.GRANT_TYPE_REPRIEVE);
                    if(!ObjectUtils.isEmpty(newMainTaskPO)){
                        if(!CollectionUtils.isEmpty(originEmployeePOList)){
                            newMainTaskCode = newMainTaskPO.getSalaryGrantMainTaskCode();
                            salaryGrantTaskPO = newMainTaskPO;
                            final String newCode = newMainTaskCode;
                            employeePONewList = this.toProcessEmpData(employeeReprieveList,originEmployeePOList,newCode, false);
                            if(!CollectionUtils.isEmpty(employeePONewList)){
                                this.createMainTaskInfo(employeePONewList, salaryGrantTaskPO);
                                responseReprieveBO.setProcessResult(true);
                            }else{
                                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询雇员信息").setContent("信息为空，生成任务失败！"));
                                responseReprieveBO.setProcessResult(false);
                                responseReprieveBO.setProcessMessage("传入暂缓雇员信息未在原任务中查询到对应雇员信息，生成暂缓发放任务失败！");
                            }
                        }else{
                            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询原暂缓雇员信息").setContent("暂缓信息为空！"));
                            responseReprieveBO.setProcessResult(false);
                            responseReprieveBO.setProcessMessage("查询原暂缓雇员信息为空");
                        }
                    }else{
                        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("生成雇员信息：" + taskCode).setContent("信息生成失败！"));
                        responseReprieveBO.setProcessResult(false);
                        responseReprieveBO.setProcessMessage("创建发放任务失败");
                    }
                }
            }else{
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("查询原任务信息").setContent("信息为空，查询原任务失败！"));
                responseReprieveBO.setProcessResult(false);
                responseReprieveBO.setProcessMessage("传入暂缓雇员信息对应原任务信息不存在，查询原任务信息失败！");
            }
        }else{
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放暂缓发放").setTitle("获取传入暂缓雇员信息").setContent("传入暂缓信息为空！"));
            responseReprieveBO.setProcessResult(false);
            responseReprieveBO.setProcessMessage("传入暂缓雇员信息为空");
        }
        return responseReprieveBO;
    }

    /**
     * 处理暂缓雇员信息
     * @author gaoyang
     * @date 2018-08-07
     * @param employeeReprieveList
     * @param originEmployeePOList
     * @param newCode
     * @param processFlag
     * @return List<SalaryGrantEmployeePO>
     */
    private List<SalaryGrantEmployeePO> toProcessEmpData(List<SalaryGrantEmployeeReprieveBO> employeeReprieveList, List<SalaryGrantEmployeePO> originEmployeePOList, String newCode, boolean processFlag) {
        List<SalaryGrantEmployeePO> employeePONewList = new ArrayList<>();
        employeeReprieveList.stream().forEach(salaryGrantEmployeeReprieveBO ->{
            String companyId = salaryGrantEmployeeReprieveBO.getCompanyId();
            String employeeId = salaryGrantEmployeeReprieveBO.getEmployeeId();
            String employeeOldBankAccount = salaryGrantEmployeeReprieveBO.getCardNum();
            String employeeNewBankAccount = salaryGrantEmployeeReprieveBO.getCardNumNew();
            BigDecimal payAmount = salaryGrantEmployeeReprieveBO.getPaymentAmount();
            Long bankcardId = salaryGrantEmployeeReprieveBO.getBankcardId();
            SalaryGrantEmployeePO salaryGrantEmployeePO =  originEmployeePOList.stream().filter(employeePO -> !StringUtils.isEmpty(employeePO.getCompanyId()) && employeePO.getCompanyId().equals(companyId) &&
                    !StringUtils.isEmpty(employeePO.getEmployeeId()) && employeePO.getEmployeeId().equals(employeeId) &&
                    !StringUtils.isEmpty(employeePO.getCardNum()) && employeePO.getCardNum().equals(employeeOldBankAccount) &&
                    !ObjectUtils.isEmpty(employeePO.getPaymentAmount()) && employeePO.getPaymentAmount().compareTo(payAmount) == 0 ).findFirst().orElse(null);
            if (!ObjectUtils.isEmpty(salaryGrantEmployeePO)) {
                salaryGrantEmployeePO.setGrantStatus(SalaryGrantBizConsts.GRANT_STATUS_NORMAL);
                salaryGrantEmployeePO.setSalaryGrantMainTaskCode(newCode);
                if(SalaryGrantBizConsts.GRANT_STATUS_AUTO_REPRIEVE.equals(salaryGrantEmployeePO.getGrantStatus())){
                    if(StringUtils.isEmpty(salaryGrantEmployeePO.getBankcardId())){
                        EmployeeBankcardBO employeeBankcardBO = this.getEmployeeBankcardInfo(bankcardId);
                        salaryGrantEmployeePO.setBankcardId(bankcardId);
                        salaryGrantEmployeePO.setCardNum(employeeNewBankAccount);
                        salaryGrantEmployeePO.setAccountName(employeeBankcardBO.getAccountName());
                        salaryGrantEmployeePO.setBankCode(employeeBankcardBO.getBankCode());
                        salaryGrantEmployeePO.setDepositBank(employeeBankcardBO.getDepositBank());
                        salaryGrantEmployeePO.setSwiftCode(employeeBankcardBO.getSwiftCode());
                        salaryGrantEmployeePO.setIban(employeeBankcardBO.getIban());
                        salaryGrantEmployeePO.setBankcardType(employeeBankcardBO.getBankcardType());
                        salaryGrantEmployeePO.setBankcardProvinceCode(employeeBankcardBO.getProvinceCode());
                        salaryGrantEmployeePO.setBankcardCityCode(employeeBankcardBO.getCityCode());
                        salaryGrantEmployeePO.setDefaultCard(employeeBankcardBO.getDefaultCard());
                    }
                }
                if(processFlag){
                    salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePO);
                }else{
                    salaryGrantEmployeePO.setSalaryGrantSubTaskCode("");
                    salaryGrantEmployeePO.setActive(true);
                    salaryGrantEmployeePO.setCreatedBy(SalaryGrantBizConsts.SYSTEM_EN);
                    salaryGrantEmployeePO.setCreatedTime(new Date());
                    salaryGrantEmployeePO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
                    salaryGrantEmployeePO.setModifiedTime(new Date());
                }
                employeePONewList.add(salaryGrantEmployeePO);
            }
        });
        return employeePONewList;
    }
}
