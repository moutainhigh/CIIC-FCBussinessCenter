package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;


import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;

import java.util.List;

/**
 * <p>
 * 薪资发放 服务类
 * </p>
 *
 * @author chenpb
 * @since 2018-02-27
 */
public interface SalaryGrantService extends IService<SalaryGrantTaskPO> {
    /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param po
     * @return
     */
    List<SalaryGrantTaskBO> getTask(SalaryGrantTaskPO po);
}
