package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;

/**
 * <p>
 * 薪资发放雇员信息维护 服务类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-23
 */
public interface SalaryGrantEmployeeCommandService extends IService<SalaryGrantEmployeePO> {
    /**
     * 雇员数据进历史表
     *
     * @param task_his_id
     * @param task_code
     * @param task_type
     * @return
     */
    boolean saveToHistory(long task_his_id, String task_code, int task_type);

}
