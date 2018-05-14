package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.OfferDocumentDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
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
     *
     * @param taskCode
     * @return
     */
    List<OfferDocumentBO> generateOfferDocument(String taskCode);

    /**
     * 创建薪资发放报盘文件
     *
     * @param taskCode
     * @return
     */
    void createOfferDocument(String taskCode);

    /**
     * 查询薪资发放报盘文件
     *
     * @param taskCode
     * @return
     */
    List<OfferDocumentBO> queryOfferDocument(String taskCode);

    //todo
    // 46、报盘文件（调用结算中心接口，目前已有找周浩伟）
}
