package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.ApprovalHistoryMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by NeoJiang on 2018/2/1
 *
 * @author NeoJiang
 */
@Service
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService {

    @Autowired
    private ApprovalHistoryMapper approvalHistoryMapper;

    @Override
    public List<ApprovalHistoryPO>  getApprovalHistory(Integer bizType, String bizCode) {
        ApprovalHistoryPO param = new ApprovalHistoryPO();
        param.setBizType(bizType);
        param.setBizCode(bizCode);
        List<ApprovalHistoryPO> result = approvalHistoryMapper.selectList(new EntityWrapper<>(param));
        return result;
    }

    @Override
    public Boolean addApprovalHistory(ApprovalHistoryPO approvalHistoryPO) {
        approvalHistoryPO.setCreatedTime(new Date());
        approvalHistoryMapper.insert(approvalHistoryPO);
        return null;
    }

}
