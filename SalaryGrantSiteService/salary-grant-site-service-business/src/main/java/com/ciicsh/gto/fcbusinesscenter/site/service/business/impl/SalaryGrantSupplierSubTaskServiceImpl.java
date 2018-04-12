package com.ciicsh.gto.fcbusinesscenter.site.service.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.site.service.business.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.site.service.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.site.service.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.site.service.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 薪资发放供应商任务单 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantSupplierSubTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantSupplierSubTaskService {

    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;


    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }
}
