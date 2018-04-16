package com.ciicsh.gto.fcbusinesscenter.site.service.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.site.service.business.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.site.service.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.site.service.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.site.service.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 薪资发放报盘任务单 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
public class SalaryGrantOfferDocumentTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantOfferDocumentTaskService {
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }
}
