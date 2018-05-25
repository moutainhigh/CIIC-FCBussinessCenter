package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
}
