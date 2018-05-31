package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>Description: 薪资发放报盘文件 DTO</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/10 0010
 */
@Data
public class OfferDocumentDTO {
    /**
     * 银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
     */
    private Integer bankcardType;
    /**
     * 银行卡种类名称
     */
    private String bankcardName;
    /**
     * 付款账号
     */
    private String paymentAccountCode;
    /**
     * 付款账户名
     */
    private String paymentAccountName;
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
    /**
     * 薪资发放总金额（RMB）
     */
    private BigDecimal paymentTotalSum;
    /**
     * 公司名称
     */
    private String companyName;

}
