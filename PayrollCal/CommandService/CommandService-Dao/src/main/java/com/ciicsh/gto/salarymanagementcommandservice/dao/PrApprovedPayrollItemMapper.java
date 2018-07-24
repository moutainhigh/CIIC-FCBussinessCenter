package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrApprovedPayrollItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-7-18 13:23:22
 * @description 审批通过的薪资项mapper
 */
@Mapper
@Component
public interface PrApprovedPayrollItemMapper extends BaseMapper<PrApprovedPayrollItemPO> {

}
