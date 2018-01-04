package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class CalculationBatchServiceImpl extends ServiceImpl<CalculationBatchMapper, CalculationBatchPO> implements CalculationBatchService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchServiceImpl.class);

    @Override
    public ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch) {

        List<CalculationBatchPO> calculationBatchPOList = null;//new ArrayList<CalculationBatchPO>();
        ResponseForCalBatch responseForCalBatch = new ResponseForCalBatch();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new CalculationBatchPO());
        //管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.andNew("batch_no={0}",requestForCalBatch.getBatchNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForCalBatch.getCurrentNum(),requestForCalBatch.getPageSize());
        calculationBatchPOList = baseMapper.selectPage(page, wrapper);
        //获取总数目
        int total = baseMapper.selectCount(wrapper);
        for(CalculationBatchPO p: calculationBatchPOList){
            p.setStatusName(EnumUtil.getMessage(EnumUtil.BATCH_NO_STATUS,p.getStatus()));
        }
        responseForCalBatch.setRowList(calculationBatchPOList);
        responseForCalBatch.setCurrentNum(requestForCalBatch.getCurrentNum());
        responseForCalBatch.setTotalNum(total);

        return responseForCalBatch;
    }

}
