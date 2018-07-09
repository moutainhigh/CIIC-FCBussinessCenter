package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchAccountPO;

import java.util.List;

/**
 * @author yuantongqing
 */
public interface CalculationBatchAccountService {

    /**
     * 根据扣缴义务人编码查询批次账户信息
     * @param accountNumber
     * @return
     */
    CalculationBatchAccountPO getCalculationBatchAccountInfoByAccountNo(String accountNumber);

    /**
     * 根据识别号查询批次信息集合
     * @param accountNumbers
     * @return
     */
    List<CalculationBatchAccountPO> queryCalculationBatchAccountInfoByAccountNos(List<String> accountNumbers);
}
