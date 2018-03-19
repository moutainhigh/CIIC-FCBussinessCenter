package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author wuhua
 */
public interface TaskSubDeclareMapper extends BaseMapper<TaskSubDeclarePO> {

    /**
     * 根据合并的ID拼接的in条件查询合并前的子任务ID
     * @param sbCombinedParams
     * @return
     */
    List<Long> querySubDeclareIdsByMergeIds(@Param("sbCombinedParams") String sbCombinedParams);
}