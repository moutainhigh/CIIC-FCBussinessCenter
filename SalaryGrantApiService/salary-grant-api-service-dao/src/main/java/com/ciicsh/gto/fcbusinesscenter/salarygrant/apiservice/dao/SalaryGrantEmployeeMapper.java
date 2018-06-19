package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 薪资发放雇员信息表 Mapper 接口
 * </p>
 *
 * @author chenpb
 * @since 2018-06-19
 */
@Component
public interface SalaryGrantEmployeeMapper extends BaseMapper<SalaryGrantEmployeePO> {

    /**
     * 修改雇员发放状态
     * @author chenpb
     * @since 2018-06-19
     * @param po
     * @return
     */
    Integer updateForReprieveEmployee(SalaryGrantEmployeePO po);
}