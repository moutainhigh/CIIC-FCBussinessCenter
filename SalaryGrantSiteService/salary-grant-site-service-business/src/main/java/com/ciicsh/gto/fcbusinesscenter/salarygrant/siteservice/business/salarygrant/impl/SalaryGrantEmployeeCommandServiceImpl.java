package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryEmployeeProxyDTO;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryProxyDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.EmployeeReturnTicketDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 薪资发放雇员信息维护 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-23
 */
@Service
public class SalaryGrantEmployeeCommandServiceImpl extends ServiceImpl<SalaryGrantEmployeeMapper, SalaryGrantEmployeePO> implements SalaryGrantEmployeeCommandService {

    @Autowired
    private SalaryGrantEmpHisOpt salaryGrantEmpHisOpt;
    @Autowired
    private SalaryGrantEmployeeQueryService employeeQueryService;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private SalaryGrantSupplierSubTaskService salaryGrantSupplierSubTaskService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    private SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;

    @Override
    public boolean saveToHistory(long task_his_id, String task_code, int task_type) {
        //查询任务单的雇员信息
        Page<SalaryGrantEmployeeBO> page = new Page<>();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        SalaryGrantEmployeeBO salaryGrantEmployeeBO = new SalaryGrantEmployeeBO();
        salaryGrantEmployeeBO.setTaskCode(task_code);
        salaryGrantEmployeeBO.setTaskType(task_type);
        Page<SalaryGrantEmployeeBO> salaryGrantEmployeeBOPage = employeeQueryService.queryEmployeeTask(page, salaryGrantEmployeeBO);
        if (!ObjectUtils.isEmpty(salaryGrantEmployeeBOPage)) {
            List<SalaryGrantEmployeeBO> employeeBOList = salaryGrantEmployeeBOPage.getRecords();
            if (!CollectionUtils.isEmpty(employeeBOList)) {
                //保存雇员信息到MongoDB中
                DBObject dbObject = new BasicDBObject();
                dbObject.put("task_his_id", task_his_id);
                dbObject.put("employeeInfo", employeeBOList);
                salaryGrantEmpHisOpt.getMongoTemplate().insert(dbObject, SalaryGrantEmpHisOpt.SG_EMP_HIS);
            }
        }

        return true;
    }

    /**
     * 根据任务单编号，任务单类型，雇员编号，发放状态暂缓雇员
     *
     * @param bo
     * @return
     * @author chenpb
     * @since 2018-05-23
     */
    @Override
    public Integer deferEmployee(SalaryGrantEmployeeBO bo) {
        return salaryGrantEmployeeMapper.deferEmployee(bo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateForServiceFeeAmount(String taskCode) {
        //查询任务单子表记录列表
        EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
        subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} task_status = 2 and is_active = 1", taskCode);
        List<SalaryGrantSubTaskPO> salaryGrantSubTaskPOList = salaryGrantSupplierSubTaskService.selectList(subTaskPOEntityWrapper);
        if (!CollectionUtils.isEmpty(salaryGrantSubTaskPOList)) {
            //循环处理任务单子表记录
            salaryGrantSubTaskPOList.stream().forEach(subTaskPO -> {
                //任务单编号
                String salaryGrantSubTaskCode = subTaskPO.getSalaryGrantSubTaskCode();
                //根据子任务单编号查询雇员信息表记录列表
                EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", salaryGrantSubTaskCode);
                List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
                if (!CollectionUtils.isEmpty(employeePOList)) {
                    //根据子任务单编号、子任务单雇员信息记录列表调用账单中心接口获取雇员薪酬服务费
                    SalaryProxyDTO salaryProxyDTO = commonService.selectEmployeeServiceFeeAmount(salaryGrantSubTaskCode, employeePOList);
                    if (!ObjectUtils.isEmpty(salaryProxyDTO)) {
                        String batchCode = salaryProxyDTO.getBatchCode();

                        List<SalaryEmployeeProxyDTO> proxyDTOEmployeeList = salaryProxyDTO.getEmployeeList();
                        if (!CollectionUtils.isEmpty(proxyDTOEmployeeList)) {
                            proxyDTOEmployeeList.stream().forEach(dto -> {
                                //更新字段
                                SalaryGrantEmployeePO employeePO = new SalaryGrantEmployeePO();
                                employeePO.setServiceFeeAmount(dto.getServiceFeeAmount());

                                //更新条件
                                EntityWrapper<SalaryGrantEmployeePO> grantEmployeePOEntityWrapper = new EntityWrapper<>();
                                grantEmployeePOEntityWrapper.where("salary_grant_sub_task_code = {0}", batchCode)
                                        .where("company_id = {0}", dto.getCompanyId())
                                        .where("employee_id = {0}", dto.getEmployeeId());
                                salaryGrantEmployeeMapper.update(employeePO, grantEmployeePOEntityWrapper);
                            });
                        }
                    }
                }
            });
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateForRefund(String taskCode, List<EmployeeReturnTicketDTO> employeeReturnTicketDTOList) {
        if (!CollectionUtils.isEmpty(employeeReturnTicketDTOList)) {
            //根据发放批次号查询雇员信息列表
            EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
            employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", taskCode);
            List<SalaryGrantEmployeePO> employeeList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
            if (!CollectionUtils.isEmpty(employeeList)) {
                employeeReturnTicketDTOList.stream().forEach(employeeReturnTicketDTO -> {
                    String companyId = employeeReturnTicketDTO.getCompanyId(); //公司ID
                    String employeeId = employeeReturnTicketDTO.getEmployeeId(); //雇员ID
                    String employeeBankAccount = employeeReturnTicketDTO.getEmployeeBankAccount(); //雇员银行账号
                    BigDecimal payAmount = employeeReturnTicketDTO.getPayAmount(); //支付金额

                    //筛选批次雇员信息
                    SalaryGrantEmployeePO salaryGrantEmployeePO = employeeList.stream().filter(employeePO ->
                            !StringUtils.isEmpty(employeePO.getCompanyId()) && employeePO.getCompanyId().equals(companyId) &&
                                    !StringUtils.isEmpty(employeePO.getEmployeeId()) && employeePO.getEmployeeId().equals(employeeId) &&
                                    !StringUtils.isEmpty(employeePO.getCardNum()) && employeePO.getCardNum().equals(employeeBankAccount) &&
                                    !ObjectUtils.isEmpty(employeePO.getPaymentAmount()) && employeePO.getPaymentAmount().compareTo(payAmount) == 0
                    ).findFirst().orElse(null);

                    //筛选雇员存在时更新雇员信息
                    if (!ObjectUtils.isEmpty(salaryGrantEmployeePO)) {
                        SalaryGrantEmployeePO updateEmployeePO = new SalaryGrantEmployeePO();
                        updateEmployeePO.setSalaryGrantEmployeeId(salaryGrantEmployeePO.getSalaryGrantEmployeeId());
                        updateEmployeePO.setGrantStatus(SalaryGrantBizConsts.GRANT_STATUS_REFUND); //发放状态:3-退票
                        salaryGrantEmployeeMapper.updateById(updateEmployeePO);
                    }
                });
            }
        }

        return true;
    }

    @Override
    public boolean processReprieveToPoll(SalaryGrantTaskBO salaryGrantTaskBO) {
        //任务单类型
        Integer taskType = salaryGrantTaskBO.getTaskType();
        if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK == taskType) {
            EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
            mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", salaryGrantTaskBO.getTaskCode());
            List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
            if (!CollectionUtils.isEmpty(mainTaskPOList)) {
                for (SalaryGrantMainTaskPO mainTaskPO : mainTaskPOList) {
                    //（3）查询暂缓雇员信息(sg_salary_grant_employee)，查询结果为employeeList
                    //           查询条件：(salary_grant_sub_task_code = taskCode or  salary_grant_main_task_code= taskCode)  and grant_status=1 and is_active=1
                    //           查询结果字段：如下面雇员信息表格所列字段
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_status = 1 and is_active = 1", mainTaskPO.getSalaryGrantMainTaskCode());
                    List<SalaryGrantEmployeePO> employeeList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);

                    //（4）调用客服中心暂缓池操作接口，把SalaryGrantTaskBO赋值给接口的主表信息，把employeeList赋值到雇员信息中。
                    //     SalaryGrantTaskBO. taskCode=SalaryGrantSubTaskPO. salaryGrantMainTaskCode，
                    //     把任务单子表对应的任务单主表编号传给暂缓池接口的主表信息中
                    //（5）根据客服中心暂缓池操作接口的返回值，判断processReprieveToPoll方法的返回值是true or false。--待客服中心接口确认后再定
                    salaryGrantTaskBO.setTaskCode(mainTaskPO.getSalaryGrantMainTaskCode());
                    return commonService.addDeferredPool(salaryGrantTaskBO, employeeList);
                }
            }
        } else {
            //（1）传入参数：SalaryGrantTaskBO（taskCode、managementId、managementName、batchCode、grantCycle、taskType）
            //（2）根据传入的参数taskCode，查询任务单子表信息SalaryGrantSubTaskPO，
            //     查询条件：子表.salary_grant_sub_task_code =SalaryGrantTaskBO. taskCode and is_active = 1
            EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
            subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", salaryGrantTaskBO.getTaskCode());
            List<SalaryGrantSubTaskPO> subTaskPOList = salaryGrantSubTaskMapper.selectList(subTaskPOEntityWrapper);
            if (!CollectionUtils.isEmpty(subTaskPOList)) {
                for (SalaryGrantSubTaskPO subTaskPO : subTaskPOList) {
                    //（3）查询暂缓雇员信息(sg_salary_grant_employee)，查询结果为employeeList
                    //           查询条件：(salary_grant_sub_task_code = taskCode or  salary_grant_main_task_code= taskCode)  and grant_status=1 and is_active=1
                    //           查询结果字段：如下面雇员信息表格所列字段
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and grant_status = 1 and is_active = 1", subTaskPO.getSalaryGrantSubTaskCode());
                    List<SalaryGrantEmployeePO> employeeList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);

                    //（4）调用客服中心暂缓池操作接口，把SalaryGrantTaskBO赋值给接口的主表信息，把employeeList赋值到雇员信息中。
                    //     SalaryGrantTaskBO. taskCode=SalaryGrantSubTaskPO. salaryGrantMainTaskCode，
                    //     把任务单子表对应的任务单主表编号传给暂缓池接口的主表信息中
                    //（5）根据客服中心暂缓池操作接口的返回值，判断processReprieveToPoll方法的返回值是true or false。--待客服中心接口确认后再定
                    salaryGrantTaskBO.setTaskCode(subTaskPO.getSalaryGrantMainTaskCode());
                    return commonService.addDeferredPool(salaryGrantTaskBO, employeeList);
                }
            }
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSalaryAdjustmentInfoForEmployee(SalaryGrantMainTaskPO mainTaskPO) {
        //1、当计算批次类型是调整批次和回溯批次时，
        // 在薪资发放任务单主表sg_salary_grant_main_task中会记录计算批次号batch_code和参考批次号origin_batch_code
        //薪酬计算批次号
        String batchCode = mainTaskPO.getBatchCode();
        //参考批次号
        String originBatchCode = mainTaskPO.getOriginBatchCode();

        //2、根据计算批次号到Mysql sg_salary_grant_employee中查询雇员信息（即调整差异数据）
        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", batchCode);
        List<SalaryGrantEmployeePO> adjustDiffEmployeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper); //调整差异雇员数据
        if (!CollectionUtils.isEmpty(adjustDiffEmployeePOList)) {
            //3、查询基于调整或回溯的批次数据，可以根据任务单主表查询对应的雇员信息：（即调整前数据）
            //3、（1）查询任务单条件batch_code=origin_batch_code and task_status=7 and grant_type in (1,2,3),查询任务单主表编号salary_grant_main_task_code
            EntityWrapper<SalaryGrantMainTaskPO> beforeMainTaskPOEntityWrapper = new EntityWrapper<>();
            beforeMainTaskPOEntityWrapper.where("batch_code = {0} and task_status = 7 and grant_type in (1, 2, 3) and is_active = 1", originBatchCode);
            List<SalaryGrantMainTaskPO> adjustBeforeMainTaskPOList = salaryGrantMainTaskMapper.selectList(beforeMainTaskPOEntityWrapper);
            if (!CollectionUtils.isEmpty(adjustBeforeMainTaskPOList)) {
                //只有1条记录，获取第1条
                SalaryGrantMainTaskPO adjustBeforeMainTaskPO = adjustBeforeMainTaskPOList.get(0);

                //3、（2）根据salary_grant_main_task_code查询雇员信息列表。
                EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper2 = new EntityWrapper<>();
                employeePOEntityWrapper2.where("salary_grant_main_task_code = {0} and is_active = 1", adjustBeforeMainTaskPO.getBatchCode());
                List<SalaryGrantEmployeePO> adjustBeforeEmployeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper2); //调整前雇员数据

                //3、（3）比对当前任务单雇员信息和基于计算批次任务单的雇员信息，对2条数据进行记录。
                if (!CollectionUtils.isEmpty(adjustBeforeEmployeePOList)) {
                    for (SalaryGrantEmployeePO adjustDiffEmployeePO : adjustDiffEmployeePOList) {
                        //获取调整前数据中对应的雇员记录
                        SalaryGrantEmployeePO adjustBeforeEmployeePO = adjustBeforeEmployeePOList.stream().filter(salaryGrantEmployeePO -> salaryGrantEmployeePO.equals(adjustDiffEmployeePO.getEmployeeId())).findFirst().orElse(null);

                        if (!ObjectUtils.isEmpty(adjustBeforeEmployeePO)) {
                            //生成调整后数据
                            SalaryGrantEmployeePO adjustAfterEmployeePO = new SalaryGrantEmployeePO();
                            BeanUtils.copyProperties(adjustDiffEmployeePO, adjustAfterEmployeePO);
                            //发放金额 = 调整差异记录发放金额 + 调整前记录发放金额
                            adjustAfterEmployeePO.setPaymentAmount(adjustDiffEmployeePO.getPaymentAmount().add(adjustBeforeEmployeePO.getPaymentAmount()));

                            //生成调整信息
                            List<SalaryGrantEmployeeBO> adjustCompareInfo = new ArrayList<>();
                            SalaryGrantEmployeeBO adjustBeforeEmployeeBO = new SalaryGrantEmployeeBO();
                            BeanUtils.copyProperties(adjustBeforeEmployeePO, adjustBeforeEmployeeBO);
                            //调整信息
                            adjustBeforeEmployeeBO.setAdjustInfo("调整前信息");
                            //发放币种名称
                            adjustBeforeEmployeeBO.setCurrencyName(commonService.getNameByValue("currency", String.valueOf(adjustBeforeEmployeeBO.getCurrencyCode())));
                            //国籍中文
                            adjustBeforeEmployeeBO.setCountryName(commonService.getCountryName(adjustBeforeEmployeeBO.getCountryCode()));
                            adjustCompareInfo.add(adjustBeforeEmployeeBO); //调整前记录

                            SalaryGrantEmployeeBO adjustDiffEmployeeBO = new SalaryGrantEmployeeBO();
                            BeanUtils.copyProperties(adjustDiffEmployeePO, adjustDiffEmployeeBO);
                            //调整信息
                            adjustDiffEmployeeBO.setAdjustInfo("调整差异信息");
                            //发放币种名称
                            adjustDiffEmployeeBO.setCurrencyName(commonService.getNameByValue("currency", String.valueOf(adjustDiffEmployeeBO.getCurrencyCode())));
                            //国籍中文
                            adjustDiffEmployeeBO.setCountryName(commonService.getCountryName(adjustDiffEmployeeBO.getCountryCode()));
                            adjustCompareInfo.add(adjustDiffEmployeeBO); //调整差异记录

                            SalaryGrantEmployeeBO adjustAfterEmployeeBO = new SalaryGrantEmployeeBO();
                            BeanUtils.copyProperties(adjustAfterEmployeePO, adjustAfterEmployeeBO);
                            //调整信息
                            adjustAfterEmployeeBO.setAdjustInfo("调整后信息");
                            //发放币种名称
                            adjustAfterEmployeeBO.setCurrencyName(commonService.getNameByValue("currency", String.valueOf(adjustAfterEmployeeBO.getCurrencyCode())));
                            //国籍中文
                            adjustAfterEmployeeBO.setCountryName(commonService.getCountryName(adjustAfterEmployeeBO.getCountryCode()));
                            adjustCompareInfo.add(adjustAfterEmployeeBO); //调整后记录

                            //更新调整信息到调整差异记录
                            SalaryGrantEmployeePO updateEmployeePO = new SalaryGrantEmployeePO();
                            updateEmployeePO.setSalaryGrantEmployeeId(adjustDiffEmployeePO.getSalaryGrantEmployeeId());
                            updateEmployeePO.setAdjustCompareInfo(JSONObject.toJSONString(adjustCompareInfo));
                            salaryGrantEmployeeMapper.updateById(updateEmployeePO);
                        }
                    }
                }
            }
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean compareAndUpdateEmployeeNewestInfo(SalaryGrantEmployeePO currentEmployeePO, SalaryGrantEmployeePO newestEmployeePO) {
        return salaryGrantSupplierSubTaskService.compareAndUpdateEmployeeNewestInfo(currentEmployeePO, newestEmployeePO);
    }
}
