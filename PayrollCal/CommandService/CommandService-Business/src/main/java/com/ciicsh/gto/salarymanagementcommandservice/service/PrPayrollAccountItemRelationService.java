package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/20.
 */
public interface PrPayrollAccountItemRelationService {
    /**
     * 根据薪资账套Code 获取薪资账套扩展薪资项关系数据
     * @param accountSetCode 薪资账套Code
     * @return 返回薪资账套扩展薪资项关系数据
     */
    List<PayrollAccountItemRelationExtPO> getAccountItemRelationExts(String accountSetCode);


    /**
     * 修改薪资账套薪资项关系
     * @param relationPO 薪资账套薪资项关系实体
     * @return 是否修改成功
     */
    Integer editAccountItemRelation(PrPayrollAccountItemRelationPO relationPO);
}
