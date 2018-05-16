package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    @Autowired
//    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO));
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
}
