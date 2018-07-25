package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
  * 配置表，薪资组模板表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Component
public interface PrPayrollGroupTemplateMapper extends BaseMapper<PrPayrollGroupTemplatePO> {

    /**
     * 获取薪资组模板列表
     * @param param
     * @return
     */
    List<PrPayrollGroupTemplatePO> selectListByEntityUseLike(PrPayrollGroupTemplatePO param);

    /**
     * 获取薪资组模板名称列表
     * @return 薪资组模板名称列表
     */
    List<HashMap<String, String>> selectNameList();

    /**
     * 更新薪资组，不增加version
     * @param param
     * @return
     */
    Integer updateItemByCode(PrPayrollGroupTemplatePO param);

    /**
     * 删除薪资组模板by code
     * @param codes
     * @return
     */
    Integer deleteByCodes(@Param("codes") List<String> codes);

    /**
     * 获取薪资组模板名称,code by name
     * @param name
     * @return
     */
    List<HashMap<String, String>> selectGroupTemplateNameListByName(@Param("name") String name);

    /**
     * 根据模板名称查找模板是否存在
     * @param name
     * @return
     */
    Integer selectCountGroupTemplateByName(@Param("name") String name);
}