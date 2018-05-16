package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
public class OfferDocumentBO extends OfferDocumentPO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 银行卡种类名称
     */
    private String bankcardName;
    /**
     * 报盘文件ID
     */
    private Long offerDocumentFileId;
    /**
     * 报盘文件名称
     */
    private String fileName;
    /**
     * 报盘文件地址
     */
    private String filePath;
}
