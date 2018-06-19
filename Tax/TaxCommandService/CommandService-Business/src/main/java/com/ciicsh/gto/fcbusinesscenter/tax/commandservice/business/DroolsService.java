package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import java.math.BigDecimal;

public interface DroolsService {

    //根据税金倒推应纳税所得额
    Object preTaxRevenue(BigDecimal tax);
}
