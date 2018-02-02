package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrApprovalHistoryPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrApprovalHistoryMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrApprovalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by NeoJiang on 2018/2/1
 *
 * @author NeoJiang
 */
public class PrApprovalHistoryServiceImpl implements PrApprovalHistoryService {

    @Autowired
    private PrApprovalHistoryMapper prApprovalHistoryMapper;

    @Override
    public List<PrApprovalHistoryPO> getApprovalHistory(BizTypeEnum bizType, String bizCode) {
        PrApprovalHistoryPO param = new PrApprovalHistoryPO();
        param.setBizType(bizType.getValue());
        param.setBizCode(bizCode);
        List<PrApprovalHistoryPO> result = prApprovalHistoryMapper.selectList(new EntityWrapper<>(param));
        return result;
    }

    @Override
    public Boolean addApprovalHistory(PrApprovalHistoryPO prApprovalHistoryPO) {
        prApprovalHistoryPO.setCreatedTime(new Date());
        prApprovalHistoryMapper.insert(prApprovalHistoryPO);
        return null;
    }

}
