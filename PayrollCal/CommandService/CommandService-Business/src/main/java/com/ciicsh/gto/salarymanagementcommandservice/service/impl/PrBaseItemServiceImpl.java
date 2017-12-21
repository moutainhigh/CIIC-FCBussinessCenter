package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollBaseItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollBaseItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiangtianning on 2017/12/20.
 * @author jiangtianning
 */
@Service
public class PrBaseItemServiceImpl implements PrBaseItemService{

    @Autowired
    private PrPayrollBaseItemMapper prPayrollBaseItemMapper;

    @Override
    public PrPayrollBaseItemPO getItem(PrPayrollBaseItemPO param) {
        // TODO 根据需求确定是否需要该方法
        return null;
    }

    @Override
    public List<PrPayrollBaseItemPO> getList(PrPayrollBaseItemPO param) {
        EntityWrapper<PrPayrollBaseItemPO> ew = new EntityWrapper<>(param);
        List<PrPayrollBaseItemPO> resultList = prPayrollBaseItemMapper.selectList(ew);
        return resultList;
    }
}
