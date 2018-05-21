package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放报盘信息表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@Component
public interface OfferDocumentMapper extends BaseMapper<OfferDocumentPO> {
    /**
     * 查询薪资发放报盘文件
     *
     * @return
     */
    List<OfferDocumentBO> queryOfferDocument(String taskCode);
}