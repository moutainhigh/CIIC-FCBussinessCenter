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

<<<<<<< HEAD
    /**
     * 根据任务单编号，任务单类型，雇员编号，发放状态暂缓雇员
     * @author chenpb
     * @since 2018-05-23
     * @param bo
     * @return
     */
    Integer deferEmployee(SalaryGrantEmployeeBO bo);

    /**
     * 更新雇员薪酬服务费到雇员表
     *
     * @param taskCode
     * @return
     */
    boolean updateForServiceFeeAmount(String taskCode);

=======
>>>>>>> c6b3599683f9a5b5625a6444c97e4f05a56dfae6
}
