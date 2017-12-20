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

}