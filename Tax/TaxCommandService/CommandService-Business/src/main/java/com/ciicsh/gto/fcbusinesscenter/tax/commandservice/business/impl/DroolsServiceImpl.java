package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.DroolsService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DroolsServiceImpl implements DroolsService {

    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.ciicsh.gt1.fcbusinesscenter.tax", artifactId = "fcbusinesscenter_tax_commandservice_drools", version = "1.0.0-SNAPSHOT")
    private KieSession kSession;

    @Override
    public Object preTaxRevenue(BigDecimal tax) {

        FactType factType = kSession.getKieBase().getFactType("com.ciicsh.gt1.fcbusinesscenter.tax.fcbusinesscenter_tax_commandservice_drools", "CalculationContext");
        Object cc = null;
        try {
            cc = factType.newInstance();
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "DroolsServiceImpl.preTaxRevenue", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "08"), LogType.APP, tags);
        }
        factType.set(cc, "taxAmount",tax);//税金
        factType.set(cc, "other", BigDecimal.ZERO);
        kSession.insert(cc);
        kSession.fireAllRules();
        Object o = factType.get(cc,"incomeForTax");
        return o;
    }
}
