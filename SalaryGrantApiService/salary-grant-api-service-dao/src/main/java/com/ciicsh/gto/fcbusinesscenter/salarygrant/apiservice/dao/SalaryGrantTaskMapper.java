package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * <p>
  * 薪资发放任务单 Mapper 接口
 * </p>
 *
 * @author chenpb
 * @since 2018-04-18
 */
@Component
public interface SalaryGrantTaskMapper extends BaseMapper<SalaryGrantTaskPO> {

    List<SalaryGrantTaskBO> listTask(SalaryGrantTaskBO salaryGrantTaskBO);
}