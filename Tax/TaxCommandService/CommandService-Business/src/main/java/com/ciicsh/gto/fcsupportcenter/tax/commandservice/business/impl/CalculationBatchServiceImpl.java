package com.ciicsh.gto.fcsupportcenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.dao.CalculationBatchMapper;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.data.ResponseForCalBatch;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class CalculationBatchServiceImpl extends ServiceImpl<CalculationBatchMapper, CalculationBatchPO> implements CalculationBatchService, Serializable {


    @Override
    public ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch) {

        List calculationBatchPOList = null;//new ArrayList<CalculationBatchPO>();
        ResponseForCalBatch responseForCalBatch = new ResponseForCalBatch();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new CalculationBatchPO());
        //wrapper.like("manager_name","恒大");
        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(0,10);
        calculationBatchPOList = baseMapper.selectPage(page, wrapper);

        responseForCalBatch.setRowList(calculationBatchPOList);

        return responseForCalBatch;
    }
}
