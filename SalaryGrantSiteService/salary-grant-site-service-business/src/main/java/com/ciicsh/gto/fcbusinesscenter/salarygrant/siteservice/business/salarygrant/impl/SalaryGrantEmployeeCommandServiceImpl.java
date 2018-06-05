package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.billcenter.fcmodule.api.ISalaryEmployeeProxy;
import com.ciicsh.gto.billcenter.fcmodule.api.common.JsonResult;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryEmployeeProxyDTO;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryProxyDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.EmployeeReturnTicketDTO;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
                DBObject dbObject = new BasicDBList();
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

    @Override
    public boolean updateForServiceFeeAmount(String taskCode) {
        try {
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
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateForRefund(String taskCode, List<EmployeeReturnTicketDTO> employeeReturnTicketDTOList) {
        try {
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
                            updateEmployeePO.setGrantStatus(3); //发放状态:3-退票
                            salaryGrantEmployeeMapper.updateById(updateEmployeePO);
                        }
                    });
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean processReprieveToPoll(SalaryGrantTaskBO salaryGrantTaskBO) {
        //任务单类型
        Integer taskType = salaryGrantTaskBO.getTaskType();
        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        if (0 == taskType) {
            //查询任务单主表雇员
            employeePOEntityWrapper.where("salary_grant_main_task_code = {0}", salaryGrantTaskBO.getTaskCode());
        } else {
            //查询任务单子表雇员
            employeePOEntityWrapper.where("salary_grant_sub_task_code = {0}", salaryGrantTaskBO.getTaskCode());
        }
        employeePOEntityWrapper.where("grant_status = 1 and is_active = 1");
        List<SalaryGrantEmployeePO> employeeList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);


        return true;
    }
}
