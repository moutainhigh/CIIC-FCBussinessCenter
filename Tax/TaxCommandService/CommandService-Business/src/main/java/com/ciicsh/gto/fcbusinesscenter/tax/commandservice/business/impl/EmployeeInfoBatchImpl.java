package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.EmployeeInfoBatchService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.EmployeeInfoBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing on 2018-05-22
 */
@Service
public class EmployeeInfoBatchImpl extends ServiceImpl<EmployeeInfoBatchMapper, EmployeeInfoBatchPO> implements EmployeeInfoBatchService, Serializable {
    /**
     * 根据划款明细计算批次明细ID查询雇员个税信息
     * @param batchIds
     * @return
     */
    @Override
    public List<EmployeeInfoBatchPO> queryEmployeeInfoBatchesByBatchIds(List batchIds) {
        List<EmployeeInfoBatchPO> employeeInfoBatchPOList = new ArrayList<>();
        try {
            //获取雇员个税信息
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new EmployeeInfoBatchPO());
            wrapper.in("cal_batch_detail_id", batchIds);
            employeeInfoBatchPOList = baseMapper.selectList(wrapper);
        } catch (Exception e) {
            throw e;
        }
        return employeeInfoBatchPOList;
    }
}
