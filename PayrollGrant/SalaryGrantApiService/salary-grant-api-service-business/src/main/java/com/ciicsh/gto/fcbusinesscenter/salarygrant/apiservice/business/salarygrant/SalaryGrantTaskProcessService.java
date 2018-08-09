package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.ResponseRefundBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.ResponseReprieveBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeRefundBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeReprieveBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;

import java.util.List;

/**
 * <p>
 * 薪资发放任务单处理 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-01
 */
public interface SalaryGrantTaskProcessService extends IService<SalaryGrantTaskPO> {
    /**
     * 根据退票雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-08-01
     * @param employeeRefundList
     * @param batchCode
     * @return ResponseRefundBO
     */
    ResponseRefundBO createRefundTask(List<SalaryGrantEmployeeRefundBO> employeeRefundList, String batchCode);

    /**
     * 根据暂缓雇员信息创建薪资发放任务单
     * @author gaoyang
     * @date 2018-08-07
     * @param employeeReprieveList
     * @param batchCode
     * @param taskCode
     * @return ResponseReprieveBO
     */
    ResponseReprieveBO createReprieveTask(List<SalaryGrantEmployeeReprieveBO> employeeReprieveList, String batchCode, String taskCode);
}
