package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;

import java.util.List;

/**
 * Created by NeoJiang on 2018/2/1.
 *
 * @author NeoJiang
 */
public interface ApprovalHistoryService {

    /**
     * 根据提供的业务类型,业务code返回审核历史列表
     * @param bizType
     * @return
     */
    List<ApprovalHistoryPO> getApprovalHistory(Integer bizType, String bizCode);

    /**
     * 新增一条审批历史
     * @param approvalHistoryPO
     * @return
     */
    Boolean addApprovalHistory(ApprovalHistoryPO approvalHistoryPO);
}
