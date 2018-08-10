package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

import java.util.List;

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
     *
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 生成薪资发放报盘文件
     * @param taskCode
     * @param userId
     * @return List<OfferDocumentBO>
     */
    List<OfferDocumentBO> generateOfferDocument(String taskCode, String userId);

    //todo
    // 46、报盘文件（调用结算中心接口，目前已有找周浩伟）
}
