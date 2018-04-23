package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.PrEmployeeEntity;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/31.
 */
public interface PrEmployeeService {

    /**
     * 获取员工列表
     * @param param
     * @return 查询结果列表
     */
    PageInfo<PrEmployeeEntity> getEmployeeList(PrEmployeeEntity param, Integer pageNum);

    List<PrEmployeePO> getEmployeesByGroupCode(String empGroupCode);

}
