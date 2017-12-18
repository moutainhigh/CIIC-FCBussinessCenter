package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrCodeSequencePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 业务编码自增序列 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Component
public interface PrCodeSequenceMapper extends BaseMapper<PrCodeSequencePO> {

    /**
     * 获取序列号 BY 序列名
     * @param seqName
     * @return
     */
    long selectSeq(@Param("seqName") String seqName);

}