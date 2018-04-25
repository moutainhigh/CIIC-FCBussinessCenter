package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

/**
 * <p>
 * 薪资发放报盘任务单 服务类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
public interface SalaryGrantOfferDocumentTaskService extends IService<SalaryGrantSubTaskPO> {

    /**
     * 查询薪资发放报盘任务单列表
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    //todo
    // 46、报盘文件（调用结算中心接口，目前已有找周浩伟）
}
