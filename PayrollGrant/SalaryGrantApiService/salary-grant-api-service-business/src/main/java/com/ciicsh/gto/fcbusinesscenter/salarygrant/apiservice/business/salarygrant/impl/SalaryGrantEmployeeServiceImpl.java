package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantEmployeeService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 薪资发放任务单批量处理 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-29
 */
@Service
public class SalaryGrantEmployeeServiceImpl extends ServiceImpl<SalaryGrantEmployeeMapper, SalaryGrantEmployeePO> implements SalaryGrantEmployeeService {
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    CommonService commonService;

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        Page<SalaryGrantEmployeeBO> employeeBOList;
        if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantEmployeeBO.getTaskType())) {
            salaryGrantEmployeeBO.setSalaryGrantMainTaskCode(salaryGrantEmployeeBO.getTaskCode());
            employeeBOList = queryEmployeeForTask(page, salaryGrantEmployeeBO);
        } else {
            //查询子表的雇员信息
            salaryGrantEmployeeBO.setSalaryGrantSubTaskCode(salaryGrantEmployeeBO.getTaskCode());
            employeeBOList = queryEmployeeForTask(page, salaryGrantEmployeeBO);
        }
        return assignName(employeeBOList);
    }

    private Page<SalaryGrantEmployeeBO> queryEmployeeForTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        return page.setRecords(salaryGrantEmployeeMapper.selectEmpList(page, salaryGrantEmployeeBO));
    }

    /**
     * 国家名称，发放状态名称获取
     */
    private Page<SalaryGrantEmployeeBO> assignName(Page<SalaryGrantEmployeeBO> page) {
        if (!page.getRecords().isEmpty()) {
            page.getRecords().forEach(salaryGrantTaskBO -> {
                //国籍转码
                if (!StringUtils.isEmpty(salaryGrantTaskBO.getCountryCode())) {
                    salaryGrantTaskBO.setCountryName(commonService.getCountryName(salaryGrantTaskBO.getCountryCode()));
                }
                //发放状态
                if (!ObjectUtils.isEmpty(salaryGrantTaskBO.getGrantStatus())) {
                    salaryGrantTaskBO.setGrantStatusName(commonService.getNameByValue("sgGrantStatus", String.valueOf(salaryGrantTaskBO.getGrantStatus())));
                }
            });
        }
        return page;
    }
}
