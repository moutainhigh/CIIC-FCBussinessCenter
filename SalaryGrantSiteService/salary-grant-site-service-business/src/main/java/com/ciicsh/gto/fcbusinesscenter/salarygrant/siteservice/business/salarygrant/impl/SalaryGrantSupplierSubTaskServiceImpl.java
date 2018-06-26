package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 薪资发放供应商任务单 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Service
public class SalaryGrantSupplierSubTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantSupplierSubTaskService {

    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPageUpdateEmployee(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        Page<SalaryGrantTaskBO> retTaskBOPage = querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO);
        if (!ObjectUtils.isEmpty(retTaskBOPage)) {
            List<SalaryGrantTaskBO> salaryGrantTaskBOList = retTaskBOPage.getRecords();
            if (!CollectionUtils.isEmpty(salaryGrantTaskBOList)) {
                for (SalaryGrantTaskBO subTaskBO : salaryGrantTaskBOList) {
                    //根据子任务表task_code查询雇员列表
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", subTaskBO.getTaskCode());
                    List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
                    if (!CollectionUtils.isEmpty(employeePOList)) {
                        for (SalaryGrantEmployeePO employeePO : employeePOList) {
                            //调用接口获取最新雇员信息

                            //比较雇员信息

                        }
                    }
                }
            }
        }

        return retTaskBOPage;
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForApprovePage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForHaveApprovedPage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForPassPage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantTaskHistoryMapper.querySupplierSubTaskForRejectPage(page, salaryGrantTaskBO));
    }

    public Page<SalaryGrantTaskBO> compareEmployeeInfo(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }
}
