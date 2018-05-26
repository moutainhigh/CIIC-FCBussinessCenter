package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantFinanceBO;

import java.util.List;

/**
 * <p>
 * 薪资发放工资清单 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
public interface SalaryGrantPayrollService {

    /**
     * 生成工资清单--财务明细
     * 调用结算中心接口，明确插入的数据结构，被定时任务调用。2018-02-05后提供接口
     * @param salaryGrantMainTaskCode
     * @return Boolean
     */
    SalaryGrantFinanceBO createFinanceDetail(String salaryGrantMainTaskCode);

    /**
     * 生成工资清单--业务明细
     * 调用结算中心接口，明确插入的数据结构。2018-02-05后提供接口
     * @param salaryGrantMainTaskCode
     * @return Boolean
     */
    Boolean toCreatePayrollForBusiness(String salaryGrantMainTaskCode);

    /**
     * 工资清单--预览
     * @param reviewInfo
     * @return
     */
    void toPreviewForPayroll(List reviewInfo);

    /**
     * 工资清单--打印
     * @param printInfo
     * @return
     */
    void toPrintForPayroll(List printInfo);
}
