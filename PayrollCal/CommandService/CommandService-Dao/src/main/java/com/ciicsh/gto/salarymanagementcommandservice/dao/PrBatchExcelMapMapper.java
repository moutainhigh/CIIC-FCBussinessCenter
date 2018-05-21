package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrBatchExcelMapPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 18/5/14.
 */
@Mapper
@Component
public interface PrBatchExcelMapMapper extends BaseMapper<PrBatchExcelMapPO> {

    int insertBatchExcelMap(PrBatchExcelMapPO prBatchExcelMapPO);

    int updateBatchExcelMap(PrBatchExcelMapPO prBatchExcelMapPO);


}
