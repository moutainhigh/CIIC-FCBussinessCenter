package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantSubTaskBO;
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
    /**
     *  根据计算批次编号查询薪资发放任务单主表
     * @param batchCodeList
     * @return List<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> listTask(List<String> batchCodeList);
    /**
     *  根据主表任务单编号查询薪资发放任务单子表
     * @param taskCode
     * @return List<SalaryGrantSubTaskBO>
     */
    List<SalaryGrantSubTaskBO> listSubTask(String taskCode);
}