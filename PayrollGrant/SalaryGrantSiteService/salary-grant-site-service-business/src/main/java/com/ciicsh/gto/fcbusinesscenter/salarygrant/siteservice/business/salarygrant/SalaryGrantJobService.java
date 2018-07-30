package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;


import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;

/**
 * <p>
 * 薪资发放定时任务 服务类
 * </p>
 *
 * @author chenpb
 * @since 2018-07-26
 */
public interface SalaryGrantJobService extends IService<SalaryGrantMainTaskPO> {

    /**
     * 薪资发放付款任务
     *
     * @param
     * @return
     * @author chenpb
     * @since 2018-07-26
     */
    void handle();
}
