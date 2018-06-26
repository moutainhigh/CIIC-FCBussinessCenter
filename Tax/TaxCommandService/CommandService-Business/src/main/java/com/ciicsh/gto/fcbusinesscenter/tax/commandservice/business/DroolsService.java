package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import java.math.BigDecimal;
import java.util.Map;

public interface DroolsService {

    //根据税金倒推应纳税所得额
    Object preTaxRevenue(BigDecimal tax);

    //计算收入额
    BigDecimal incomeTotal(Map<String,BigDecimal> item);
}
