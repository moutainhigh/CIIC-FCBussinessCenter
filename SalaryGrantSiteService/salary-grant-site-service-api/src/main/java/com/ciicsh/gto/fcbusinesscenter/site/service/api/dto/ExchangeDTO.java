package com.ciicsh.gto.fcbusinesscenter.site.service.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 汇率表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-02
 */
public class ExchangeDTO extends CommonListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 货币编号
     */
    private String abbreviation;
    /**
     * 币种名称
     */
    private String currency;
    /**
     * 汇率
     */
    private BigDecimal exchange;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }
}
