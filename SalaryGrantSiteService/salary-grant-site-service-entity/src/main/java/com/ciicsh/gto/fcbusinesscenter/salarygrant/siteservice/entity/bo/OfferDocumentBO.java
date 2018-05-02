package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 薪资发放报盘信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OfferDocumentBO extends OfferDocumentPO implements Serializable {
    private static final long serialVersionUID = 1L;
}
