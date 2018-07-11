package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchAccountService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchAccountMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchAccountPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuantongqing
 */
@Service
public class CalculationBatchAccountServiceImpl extends ServiceImpl<CalculationBatchAccountMapper, CalculationBatchAccountPO> implements CalculationBatchAccountService, Serializable {
    /**
     * 根据扣缴义务人编码查询批次账户信息
     * @param accountNumber
     * @return
     */
    @Override
    public CalculationBatchAccountPO getCalculationBatchAccountInfoByAccountNo(String accountNumber) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new CalculationBatchAccountPO());
        wrapper.and("account_number = {0}",accountNumber);
        wrapper.and("is_active = {0}",true);
        wrapper.orderBy("modified_time", false);
        List<CalculationBatchAccountPO> calculationBatchAccountPOList = baseMapper.selectList(wrapper);
        if(calculationBatchAccountPOList.size() > 0 ){
            return calculationBatchAccountPOList.get(0);
        }else{
            return new CalculationBatchAccountPO();
        }
    }

    /**
     * 根据识别号查询批次信息集合
     * @param accountNumbers
     * @return
     */
    @Override
    public List<CalculationBatchAccountPO> queryCalculationBatchAccountInfoByAccountNos(List<String> accountNumbers) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new CalculationBatchAccountPO());
        wrapper.in("account_number",accountNumbers);
        wrapper.and("is_active = {0}",true);
        wrapper.orderBy("modified_time", true);
        List<CalculationBatchAccountPO> calculationBatchAccountPOList = baseMapper.selectList(wrapper);
       return calculationBatchAccountPOList;
    }
}
