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
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
            subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} task_status = 2", taskCode);
            List<SalaryGrantSubTaskPO> salaryGrantSubTaskPOList = salaryGrantSupplierSubTaskService.selectList(subTaskPOEntityWrapper);
            if (!CollectionUtils.isEmpty(salaryGrantSubTaskPOList)) {
                //循环处理任务单子表记录
                salaryGrantSubTaskPOList.stream().forEach(subTaskPO -> {
                    //任务单编号
                    String salaryGrantSubTaskCode = subTaskPO.getSalaryGrantSubTaskCode();
                    //根据子任务单编号查询雇员信息表记录列表
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_sub_task_code = {0}", salaryGrantSubTaskCode);
                    List<SalaryGrantEmployeePO> employeePOList = selectList(employeePOEntityWrapper);
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
                                    update(employeePO, grantEmployeePOEntityWrapper);
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
}
