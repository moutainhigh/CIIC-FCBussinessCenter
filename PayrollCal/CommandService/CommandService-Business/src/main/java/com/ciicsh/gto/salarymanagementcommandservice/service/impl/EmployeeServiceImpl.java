package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupEmpRelationPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupEmpRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeService;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;import java.util.ArrayList;
import java.util.Arrays;
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
    public Boolean addEmployees(List<PrEmployeePO> employeeTestPOS, String empGroupCode) {
        try {
            List<String> ids = new ArrayList<>();
            employeeTestPOS.forEach(employeeTestPO -> {
                Integer revalue = empGroupEmpRelationMapper.isExistEmpGroupEmpRelation(empGroupCode,employeeTestPO.getEmployeeId());
                if(revalue <= 0){
                    Integer isExist = employeeMapper.isExistEmployee(employeeTestPO.getEmployeeId());
                    if(isExist <= 0)
                    {
//                        employeeMapper.insert(this.toEmployeePO(employeeTestPO));
                    }
                    empGroupEmpRelationMapper.insert(this.toEmpGroupEmpRelationPO(employeeTestPO,empGroupCode));
                    ids.add(employeeTestPO.getEmployeeId());
                }
            });
            if(ids.size() > 0){
                PayrollEmpGroup payrollEmpGroup = new PayrollEmpGroup();
                payrollEmpGroup.setEmpGroupIds(Arrays.asList(empGroupCode));
                payrollEmpGroup.setIds(ids);
                payrollEmpGroup.setOperateType(OperateTypeEnum.ADD.getValue());
                sender.SendEmpGroup(payrollEmpGroup);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public PageInfo<EmployeeExtensionPO> getEmployees(String empGroupCode, String empCode, String empName,Integer pageNum, Integer pageSize) {
        List<EmployeeExtensionPO> employeeExtensionPOS = new ArrayList<>();
        PageHelper.startPage(pageNum,pageSize);
        try{
            employeeExtensionPOS = employeeMapper.getEmployees(empGroupCode,empCode,empName);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        PageInfo<EmployeeExtensionPO> pageInfo = new PageInfo<>(employeeExtensionPOS);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer batchDelete(List<String> ids,List<String> employeeIds,String empGroupCode) {
        Integer result = empGroupEmpRelationMapper.deleteBatchIds(ids);
        if(result > 0){
            if(employeeIds != null && employeeIds.size() > 0){
                PayrollEmpGroup payrollEmpGroup = new PayrollEmpGroup();
                payrollEmpGroup.setEmpGroupIds(Arrays.asList(empGroupCode));
                payrollEmpGroup.setIds(employeeIds);
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

//    private PrEmployeePO toEmployeePO(PrEmployeePO employeeTestPO){
//        PrEmployeePO employeePO = new PrEmployeePO();
//        employeePO.setEmpId(employeeTestPO.getEmployeeId());
//        employeePO.setEmpName(employeeTestPO.getEmployeeName());
//        employeePO.setEmpNameEn(employeeTestPO.getEmployeeName());
//        employeePO.setFormerName(employeeTestPO.getFormerName());
//        employeePO.setIdCardType(employeeTestPO.getIdCardType());
//        employeePO.setIdNum(employeeTestPO.getIdNum());
//        employeePO.setGender(employeeTestPO.getGender());
//        employeePO.setBirthday(employeeTestPO.getBirthday());
//        employeePO.setJoinDate(employeeTestPO.getJoinDate());
//        employeePO.setCountryCode(employeeTestPO.getCountryCode());
//        employeePO.setProvinceCode(employeeTestPO.getProvinceCode());
//        employeePO.setCityCode(employeeTestPO.getCityCode());
//        employeePO.setDepartment(employeeTestPO.getDepartment());
//        employeePO.setPosition(employeeTestPO.getPosition());
//        employeePO.setActive(true);
//        employeePO.setCreatedTime(new Date());
//        employeePO.setCreatedBy("macor");
//        employeePO.setModifiedTime(new Date());
//        employeePO.setModifiedBy("macor");
//        return employeePO;
//    }

    private PrEmpGroupEmpRelationPO toEmpGroupEmpRelationPO(PrEmployeePO employeeTestPO, String empGroupCode){
        PrEmpGroupEmpRelationPO empGroupEmpRelationPO = new PrEmpGroupEmpRelationPO();
        empGroupEmpRelationPO.setEmpGroupCode(empGroupCode);
        empGroupEmpRelationPO.setEmpId(employeeTestPO.getEmployeeId());
        empGroupEmpRelationPO.setActive(true);
        empGroupEmpRelationPO.setCreatedTime(new Date());
        empGroupEmpRelationPO.setCreatedBy("macor");
        empGroupEmpRelationPO.setModifiedTime(new Date());
        empGroupEmpRelationPO.setModifiedBy("macor");
        return empGroupEmpRelationPO;
    }

}