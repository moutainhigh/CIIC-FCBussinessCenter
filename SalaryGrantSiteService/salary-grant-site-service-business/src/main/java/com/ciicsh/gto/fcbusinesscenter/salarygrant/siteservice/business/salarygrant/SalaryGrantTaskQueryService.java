package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;

/**
 * <p>
 * 薪资发放任务单查询 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
public interface SalaryGrantTaskQueryService {

    /**
     * 查询薪资发放任务单列表
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> sgList(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 根据任务单编号查询任务单
     * @param salaryGrantTaskBO
     * @return
     */
    SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO salaryGrantTaskBO);
}
