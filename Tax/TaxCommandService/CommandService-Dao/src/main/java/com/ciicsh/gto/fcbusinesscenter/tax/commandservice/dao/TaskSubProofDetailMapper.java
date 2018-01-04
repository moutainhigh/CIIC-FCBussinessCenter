package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 完税申请明细 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
public interface TaskSubProofDetailMapper extends BaseMapper<TaskSubProofDetailPO> {

    /**
     * 查询完税申请明细
     * @param taskSubProofDetailBO
     * @return
     */
    List<TaskSubProofDetailPO> queryTaskSubProofDetailByMainId(TaskSubProofDetailBO taskSubProofDetailBO);

    /**
     * 查询完税申请明细
     * @param taskSubProofDetailBO
     * @return
     */
    List<TaskSubProofDetailPO> queryTaskSubProofDetailBySubId(TaskSubProofDetailBO taskSubProofDetailBO);


    /**
     * 根据主键ID，将完税申请明细置为失效状态
     * @param subProofDetailIds
     * @return
     */
    Boolean invalidSubProofDetailByIds(@Param("subProofDetailIds") Integer[] subProofDetailIds);

    /**
     * 根据子任务ID查询申报明细
     * @param subProofId
     * @return
     */
    List<TaskSubProofDetailPO> querySubProofDetailBySubId(@Param("subProofId") Long subProofId);

    /**
     * 根据子任务ID分页查询完税凭证申报明细
     * @param page
     * @param longList
     * @param employeeNo
     * @param employeeName
     * @return
     */
    List<TaskSubProofDetailPO> queryApplyDetailsBySubIdsAndEmp(Pagination page,@Param("longList") List<Long> longList,@Param("employeeNo") String employeeNo,@Param("employeeName") String employeeName);

    /**
     * 根据子任务ID查询完税凭证申报明细总数
     * @param longList
     * @param employeeNo
     * @param employeeName
     * @return
     */
    int queryApplyDetailsTotalNumBySubIdsAndEmp(@Param("longList") List<Long> longList,@Param("employeeNo") String employeeNo,@Param("employeeName") String employeeName);

}