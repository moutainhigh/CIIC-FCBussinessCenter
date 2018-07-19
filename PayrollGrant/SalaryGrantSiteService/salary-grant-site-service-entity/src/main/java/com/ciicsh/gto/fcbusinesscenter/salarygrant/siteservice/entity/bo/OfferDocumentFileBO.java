package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 薪资发放报盘文件表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OfferDocumentFileBO extends OfferDocumentFilePO implements Serializable {
    private static final long serialVersionUID = 1L;
}
