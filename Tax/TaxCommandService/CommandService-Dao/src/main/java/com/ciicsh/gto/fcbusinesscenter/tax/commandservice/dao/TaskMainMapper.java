package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
public interface TaskMainMapper extends BaseMapper<TaskMainPO> {

    /**
     * 查询主任务明细列表
     * @param taskMainId
     * @return
     */
    List<TaskMainDetailBO> queryTaskMainDetails(@Param("taskMainId")Long taskMainId);

    /**
     * 查询存在滞纳金、罚金的子任务
     * @param taskMainId
     * @return
     */
    int selectNumsForOverdueOrFine(@Param("taskMainId")Long taskMainId);

    /**
     * 查询待审批主任务
     * @return
     */
    List<TaskMainPO> queryTaskMainForCheck(Pagination page, Map<String,Object> params);

    /**
     * 查询主任务所在批次的服务类型
     * @return
     */
    List<String> queryServiceCategoryForTaskMain(@Param("taskMainId")Long taskMainId);

    /**
     * 根据主任务ID数组以及状态查询个子任务此状态的数目
     * @param taskMainIds
     * @param status
     * @return
     */
    int querySubTaskNumByMainIdsAndStatus(@Param("taskMainIds")List<Long> taskMainIds,@Param("status")String status);
}