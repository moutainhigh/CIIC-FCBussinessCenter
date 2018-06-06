package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
  * 配置表，薪资组表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Component
public interface PrPayrollGroupMapper extends BaseMapper<PrPayrollGroupPO> {

    /**
     * 使用左匹配的方式获取薪资组模板列表 by entity
     * @param param
     * @return
     */
    List<PrPayrollGroupPO> selectListByEntityUseLike(PrPayrollGroupPO param);

    /**
     * 更新薪资组，不增加version
     * @param param
     * @return
     */
    Integer updateItemByCode(PrPayrollGroupPO param);


    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<KeyValuePO> getPayrollGroupNames(@Param("managementId") String managementId);

    /**
     * 删除一批薪资组by code
     * @param codes
     * @return
     */
    Integer deleteByCodes(@Param("codes") List<String> codes);

    /**
     * 获取薪资组名称 BY name 模糊查询
     * @param name
     * @return
     */
    List<HashMap<String, String>> selectGroupNameListByName(@Param("name") String name, @Param("managementId") String managementId);

    /**
     * 根据模板名称查找模板是否存在
     * @param name
     * @return
     */
    Integer selectCountGroupByNameAndManagement(@Param("name") String name, @Param("managementId") String managementId);
}