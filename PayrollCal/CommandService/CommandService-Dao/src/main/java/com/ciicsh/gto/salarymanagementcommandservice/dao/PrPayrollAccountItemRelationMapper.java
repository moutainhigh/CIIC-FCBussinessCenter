package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 薪资账套薪资项名表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-19
 */
@Mapper
@Component
public interface PrPayrollAccountItemRelationMapper extends BaseMapper<PrPayrollAccountItemRelationPO> {

    /**
     * 根据薪资账套Code 删除薪资账套薪资项关系数据
     * @param accountSetCode 薪资账套Code
     * @return 返回影响的行数
     */
    Integer delAccountItemRelationByAccountCode(@Param("accountSetCode") String accountSetCode);

    /**
     * 根据薪资账套编码，薪资组模板编码，薪资项编码，管理方ID，获取薪资账套扩展薪资项关系数据
     * @param paramMap 查询条件Map
     * @return 返回薪资账套扩展薪资项关系数据
     */
    List<PayrollAccountItemRelationExtPO> getAccountItemRelationExts(Map<String, Object> paramMap);

    Integer insertAccountItemRelationList(List<PrPayrollAccountItemRelationPO> list);
}