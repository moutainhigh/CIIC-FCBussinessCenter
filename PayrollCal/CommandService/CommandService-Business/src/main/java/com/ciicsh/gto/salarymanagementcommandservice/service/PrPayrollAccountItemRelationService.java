package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;

import java.util.List;
import java.util.Map;

/**
 * Created by houwanhua on 2017/12/20.
 */
public interface PrPayrollAccountItemRelationService {
    /**
     * 根据薪资账套编码，薪资组模板编码，薪资项编码，管理方ID，获取薪资账套扩展薪资项关系数据
     * @param paramMap 查询参数
     * @return 返回薪资账套扩展薪资项关系数据
     */
    List<PayrollAccountItemRelationExtPO> getAccountItemRelationExts(Map<String, Object> paramMap);


    /**
     * 修改薪资账套薪资项关系
     * @param relationPO 薪资账套薪资项关系实体
     * @return 是否修改成功
     */
    Integer editAccountItemRelation(PrPayrollAccountItemRelationPO relationPO);
}
