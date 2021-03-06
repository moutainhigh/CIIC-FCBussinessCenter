package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 审批历史（薪资模版，薪资组，正常批次，调整批次，回溯批次） Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2018-02-01
 */
@Component
public interface ApprovalHistoryMapper extends BaseMapper<ApprovalHistoryPO> {

}