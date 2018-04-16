package com.ciicsh.gto.fcbusinesscenter.site.service.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.site.service.business.SalaryGrantReprieveEmployeeImportService;
import com.ciicsh.gto.fcbusinesscenter.site.service.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.site.service.dao.SalaryGrantReprieveEmployeeImportMapper;
import com.ciicsh.gto.fcbusinesscenter.site.service.entity.po.SalaryGrantReprieveEmployeeImportPO;
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
