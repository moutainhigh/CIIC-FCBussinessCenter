package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 薪资发放报盘任务单 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Service
public class SalaryGrantOfferDocumentTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantOfferDocumentTaskService {
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

//    @Autowired
//    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.queryOfferDocumentTaskPage(page, salaryGrantTaskBO));
    }
}