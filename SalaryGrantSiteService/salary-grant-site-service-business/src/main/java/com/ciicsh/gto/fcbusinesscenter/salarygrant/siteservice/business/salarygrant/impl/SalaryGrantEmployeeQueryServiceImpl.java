package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 薪资发放雇员信息查询 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-23
 */
@Service
public class SalaryGrantEmployeeQueryServiceImpl extends ServiceImpl<SalaryGrantEmployeeMapper, SalaryGrantEmployeePO> implements SalaryGrantEmployeeQueryService {

    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        //查询主表的雇员信息
        if (salaryGrantEmployeeBO.getTaskType() == Integer.valueOf(SalaryGrantBizConsts.TASK_STATUS_DRAFT)) {
            salaryGrantEmployeeBO.setSalaryGrantMainTaskCode(salaryGrantEmployeeBO.getTaskCode());
            return queryEmployeeForMainTask(page, salaryGrantEmployeeBO);
        } else {
            //查询子表的雇员信息
            salaryGrantEmployeeBO.setSalaryGrantSubTaskCode(salaryGrantEmployeeBO.getTaskCode());
            return queryEmployeeForSubTask(page, salaryGrantEmployeeBO);
        }
    }

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeForMainTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        return page.setRecords(salaryGrantEmployeeMapper.selectBOList(page, salaryGrantEmployeeBO));
    }

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeForSubTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        return page.setRecords(salaryGrantEmployeeMapper.selectBOList(page, salaryGrantEmployeeBO));
    }

    @Override
    public List listAdjustCalcInfo(SalaryGrantEmployeePO salaryGrantEmployeePO) {
        // todo
        return null;
    }
}
