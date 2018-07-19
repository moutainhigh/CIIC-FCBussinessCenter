package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.stereotype.Service;

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
}
