package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.EmployeeReturnTicketDTO;

import java.util.List;

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

    /**
     * 更新退票的雇员信息
     *
     * @param taskCode 批次号
     * @param employeeReturnTicketDTOList 退票雇员信息列表
     * @return
     */
    boolean updateForRefund(String taskCode, List<EmployeeReturnTicketDTO> employeeReturnTicketDTOList);

    /**
     * 暂缓人员信息进入暂缓池
     *
     * @param salaryGrantTaskBO
     * @return
     */
    boolean processReprieveToPoll(SalaryGrantTaskBO salaryGrantTaskBO);
}
