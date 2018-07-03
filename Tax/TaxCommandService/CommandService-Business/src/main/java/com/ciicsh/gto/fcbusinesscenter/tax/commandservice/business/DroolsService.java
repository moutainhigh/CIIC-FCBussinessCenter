package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import java.math.BigDecimal;
import java.util.Map;

public interface DroolsService {

    //根据税金倒推应纳税所得额、税金、速算扣除数
    Map<String,BigDecimal> preTaxRevenue(BigDecimal tax);

    //计算收入额
    Map<String,BigDecimal> incomeTotal(Map<String,BigDecimal> item);
}
