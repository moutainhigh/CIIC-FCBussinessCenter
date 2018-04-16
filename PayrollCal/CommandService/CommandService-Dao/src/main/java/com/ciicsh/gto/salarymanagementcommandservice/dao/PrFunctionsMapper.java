package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrFunctionsPO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
  * 配置表，函数配置表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
public interface PrFunctionsMapper extends BaseMapper<PrFunctionsPO> {

    /**
     * 获取函数名称 列表
     * @param name
     * @return
     */
    List<HashMap<String, String>> selectFunctionNameList();

}