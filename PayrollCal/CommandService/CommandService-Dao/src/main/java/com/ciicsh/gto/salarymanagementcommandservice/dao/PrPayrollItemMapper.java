package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 配置表，薪资项明细表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Component
public interface PrPayrollItemMapper extends BaseMapper<PrPayrollItemPO> {

    /**
     * 删除薪资项by code
     * @param codes
     * @return
     */
    Integer deleteItemByCodes(@Param("codes") List<String> codes);

    /**
     * 插入一组薪资项
     * @param items
     * @return
     */
    Integer insertBatchItems(@Param("items") List<PrPayrollItemPO> items);

    /**
     * 删除薪资项by group code
     * @param groupCode
     * @return
     */
    Integer deleteItemByGroupCode(@Param("groupCode") String groupCode);

    /**
     * 删除薪资项by groupTemplate code
     * @param groupTemplateCode
     * @return
     */
    Integer deleteItemByGroupTemplateCode(@Param("groupTemplateCode") String groupTemplateCode);

    /**
     * 更新薪资项 by code
     * @param prPayrollItemPO
     * @return
     */
    Integer updateItemByCode(PrPayrollItemPO prPayrollItemPO);

}