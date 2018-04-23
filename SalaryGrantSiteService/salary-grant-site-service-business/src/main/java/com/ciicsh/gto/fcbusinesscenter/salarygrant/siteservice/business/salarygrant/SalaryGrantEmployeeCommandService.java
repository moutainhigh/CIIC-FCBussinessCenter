package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;

import java.util.List;

/**
 * <p>
 * 薪资发放雇员信息维护 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
public interface SalaryGrantEmployeeCommandService {

    /**
     * 暂缓雇员操作
     * 暂缓单个雇员信息，修改雇员的发放状态为手工暂缓。
     * @param salaryGrantEmployeePO
     * @return Boolean
     */
    Boolean toReprieveEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     * 恢复雇员操作
     * 恢复单个雇员信息，修改雇员的发放状态为正常。
     * @param salaryGrantEmployeePO
     * @return Boolean
     */
    Boolean toRecoverEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     * 批量暂缓雇员操作
     * 循环遍历雇员信息，调用单个暂缓雇员操作
     * @param salaryGrantEmployeePOList
     * @return Boolean
     */
    Boolean toBatchReprieveEmployee(List<SalaryGrantEmployeePO> salaryGrantEmployeePOList);

    /**
     * 批量恢复雇员操作
     * 循环遍历雇员信息，调用单个恢复雇员操作
     * @param salaryGrantEmployeePOList
     * @return Boolean
     */
    Boolean toBatchRecoverEmployee(List<SalaryGrantEmployeePO> salaryGrantEmployeePOList);

    /**
     * 导出雇员信息
     * @param salaryGrantMainTaskCode
     * @return
     */
    void toExportEmployee(String salaryGrantMainTaskCode);
}
