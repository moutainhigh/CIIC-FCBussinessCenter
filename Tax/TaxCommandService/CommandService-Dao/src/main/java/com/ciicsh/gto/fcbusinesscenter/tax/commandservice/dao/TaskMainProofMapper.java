package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 完税凭证主任务 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
public interface TaskMainProofMapper extends BaseMapper<TaskMainProofPO> {

    /**
     * 根据主键ID重新计算主任务总人数id
     * @param id
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateMainHeadcountById(@Param("id") Long id,@Param("modifiedBy") String modifiedBy,@Param("modifiedTime") LocalDateTime modifiedTime);

}