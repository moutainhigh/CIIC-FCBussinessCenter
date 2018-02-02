package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrApprovalHistoryPO;

import java.util.List;

/**
 * Created by NeoJiang on 2018/2/1.
 *
 * @author NeoJiang
 */
public interface PrApprovalHistoryService {

    /**
     * 根据提供的业务类型,业务code返回审核历史列表
     * @param bizType
     * @return
     */
    List<PrApprovalHistoryPO> getApprovalHistory(BizTypeEnum bizType, String bizCode);

    /**
     * 新增一条审批历史
     * @param prApprovalHistoryPO
     * @return
     */
    Boolean addApprovalHistory(PrApprovalHistoryPO prApprovalHistoryPO);
}
