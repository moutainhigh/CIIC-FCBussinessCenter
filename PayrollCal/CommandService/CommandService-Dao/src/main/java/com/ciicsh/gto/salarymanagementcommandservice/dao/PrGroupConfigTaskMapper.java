package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollConfigTaskPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-07-11 17:04
 * @description 薪资组实例变更任务单mapper
 */
@Mapper
@Component
public interface PrGroupConfigTaskMapper extends BaseMapper<PrPayrollConfigTaskPO> {

    /**
     * 获取薪资组实例变更任务单列表
     * @param prPayrollConfigTaskPO 查询参数
     * @return 结果列表
     */
    List<PrPayrollConfigTaskPO> selectPrGroupConfigTaskList(PrPayrollConfigTaskPO prPayrollConfigTaskPO);

    void insertPrPayrollConfigTask(PrPayrollConfigTaskPO prPayrollConfigTaskPO);
}
