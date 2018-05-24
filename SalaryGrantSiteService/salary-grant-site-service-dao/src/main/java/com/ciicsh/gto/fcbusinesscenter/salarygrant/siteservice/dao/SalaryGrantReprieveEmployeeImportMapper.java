package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
  * 薪资发放暂缓名单导入表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Component
public interface SalaryGrantReprieveEmployeeImportMapper extends BaseMapper<SalaryGrantReprieveEmployeeImportPO> {
    /**
     * 删除暂缓失败雇员
     * @param taskCode
     * @param taskType
     * @return
     */
    Integer deleteReprieveEmp(@Param("taskCode") String taskCode, @Param("taskType") Integer taskType);

    /**
     * 批量新增暂缓失败雇员
     * @param taskCode
     * @param taskType
     * @param userId
     * @param failList
     * @return
     */
    Integer insertBatch(@Param("taskCode") String taskCode, @Param("taskType") Integer taskType, @Param("userId") String userId, @Param("failList") List<SalaryGrantEmployeeBO> failList);

}