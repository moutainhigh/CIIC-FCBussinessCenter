package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.SalaryGrantReprieveEmployeeImportService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantReprieveEmployeeImportMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 薪资发放暂缓名单导入 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantReprieveEmployeeImportServiceImpl extends ServiceImpl<SalaryGrantReprieveEmployeeImportMapper, SalaryGrantReprieveEmployeeImportPO> implements SalaryGrantReprieveEmployeeImportService {
    @Autowired
    SalaryGrantReprieveEmployeeImportMapper salaryGrantReprieveEmployeeImportMapper;

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
}
