package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;


import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantReprieveEmployeeImportBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;

import java.util.List;

/**
 * <p>
 * 薪资发放暂缓名单导入 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
public interface SalaryGrantReprieveEmployeeImportService extends IService<SalaryGrantReprieveEmployeeImportPO> {

    /**
     * 根据任务单编号，任务单类型，雇员编号，发放状态暂缓雇员
     * @author chenpb
     * @since 2018-05-23
     * @param bos
     * @param taskCode
     * @param taskType
     * @param userId
     * @return
     */
    List<SalaryGrantEmployeeBO> deferEmployee(List<SalaryGrantEmployeeBO> bos, String taskCode, Integer taskType, String userId);

    /**
     * 根据任务单编号，任务单类型 查询暂缓失败雇员
     * @author chenpb
     * @since 2018-05-25
     * @param bo
     * @return
     */
    List<SalaryGrantReprieveEmployeeImportBO> selectDeferEmployee(SalaryGrantReprieveEmployeeImportBO bo);
}
