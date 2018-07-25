package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.ConfigTaskMsgDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollConfigTaskPO;
import com.github.pagehelper.PageInfo;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-07-11 16:42
 * @description 薪资组实例变更任务单Service
 */
public interface PrGroupConfigTaskService {

    /**
     * 获取薪资组实例变更任务单列表
     *
     * @param prPayrollConfigTaskPO
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<PrPayrollConfigTaskPO> selectPrGroupConfigTaskList(PrPayrollConfigTaskPO prPayrollConfigTaskPO,
                                                             Integer pageNum, Integer pageSize);

    void filterToSave(ConfigTaskMsgDTO taskMsgDTO);
}
