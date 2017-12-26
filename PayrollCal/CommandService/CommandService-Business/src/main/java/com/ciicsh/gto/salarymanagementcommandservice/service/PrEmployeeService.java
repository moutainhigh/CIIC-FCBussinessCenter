package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.PrEmployeeEntity;
import com.github.pagehelper.PageInfo;

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

    /**
     * 获取导入社保数据的员工列表
     * @param managementId
     * @return
     */
    List<PrEmployeeEntity> getImportedEmployeeList(String managementId);
}
