package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupEmpRelationPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupEmpRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

;import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/31.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private PrEmployeeMapper employeeMapper;

    @Autowired
    private PrEmpGroupEmpRelationMapper empGroupEmpRelationMapper;


    @Autowired
    private KafkaSender sender;

    @Override
    public Integer addEmployee(PrEmployeePO employeePO) {
        return employeeMapper.insert(employeePO);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addEmployees(List<String> empIdAndCompanyIds, String empGroupCode) {
        try {
            List<String> ids = new ArrayList<>();
            empIdAndCompanyIds.forEach(i -> {
                String empId = i.split(",")[0];
                String companyId = i.split(",")[1];
                Integer revalue = empGroupEmpRelationMapper.isExistEmpGroupEmpRelation(empGroupCode, empId, companyId);
                if(revalue <= 0){
                    empGroupEmpRelationMapper.insert(this.toEmpGroupEmpRelationPO(empId,companyId, empGroupCode));
                    ids.add(i);
                }
            });
            if(ids.size() > 0){
                PayrollEmpGroup payrollEmpGroup = new PayrollEmpGroup();
                payrollEmpGroup.setEmpGroupIds(empGroupCode);
                payrollEmpGroup.setIds(StringUtils.join(empIdAndCompanyIds,"$"));
                payrollEmpGroup.setOperateType(OperateTypeEnum.ADD.getValue());
                sender.SendEmpGroup(payrollEmpGroup);
            }
            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PageInfo<PrEmployeePO> getEmployees(String empGroupCode, Integer pageNum, Integer pageSize) {
        List<PrEmployeePO> employeePOS = new ArrayList<>();
        PageHelper.startPage(pageNum,pageSize);
        try{
            employeePOS = employeeMapper.getEmployeesByGroupCode(empGroupCode);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        PageInfo<PrEmployeePO> pageInfo = new PageInfo<>(employeePOS);
        return pageInfo;
    }

    @Override
    public List<PrEmployeePO> getAllEmployees(String empGroupCode) {
        return employeeMapper.getEmployeesByGroupCode(empGroupCode);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer batchDelete(List<String> employeeIdAndCompanyIds,String empGroupCode) {

        Integer result = 0;
        if(employeeIdAndCompanyIds.size() > 0) {
            for (String empIdAndCompanyId: employeeIdAndCompanyIds) {
                String employeeId = empIdAndCompanyId.split("-")[0];
                String companyId = empIdAndCompanyId.split("-")[1];
                result += empGroupEmpRelationMapper.deleteEmpGroupRelation(employeeId,companyId,empGroupCode);
            }
        }
        if(result > 0){
            if(employeeIdAndCompanyIds != null && employeeIdAndCompanyIds.size() > 0){
                PayrollEmpGroup payrollEmpGroup = new PayrollEmpGroup();
                payrollEmpGroup.setEmpGroupIds(empGroupCode);
                payrollEmpGroup.setIds(StringUtils.join(employeeIdAndCompanyIds,"$"));
                payrollEmpGroup.setOperateType(OperateTypeEnum.DELETE.getValue());
                sender.SendEmpGroup(payrollEmpGroup);
            }
        }
        return result;
    }

    @Override
    public int hasEmployees(String empGroupCode) {
        return employeeMapper.hasEmployees(empGroupCode);
    }

    @Override
    public int upsertEmployee(PrEmployeePO employeePO) {
        employeeMapper.upsertEmployee(employeePO);
        return 0;
    }

    private PrEmpGroupEmpRelationPO toEmpGroupEmpRelationPO(String empId, String companyId, String empGroupCode){
        PrEmpGroupEmpRelationPO empGroupEmpRelationPO = new PrEmpGroupEmpRelationPO();
        empGroupEmpRelationPO.setEmpGroupCode(empGroupCode);
        empGroupEmpRelationPO.setEmployeeId(empId);
        empGroupEmpRelationPO.setCompanyId(companyId);
        empGroupEmpRelationPO.setActive(true);
        empGroupEmpRelationPO.setCreatedTime(new Date());
        empGroupEmpRelationPO.setCreatedBy(UserContext.getUser().getUserId());
        empGroupEmpRelationPO.setModifiedTime(new Date());
        empGroupEmpRelationPO.setModifiedBy(UserContext.getUser().getUserId());
        return empGroupEmpRelationPO;
    }

}