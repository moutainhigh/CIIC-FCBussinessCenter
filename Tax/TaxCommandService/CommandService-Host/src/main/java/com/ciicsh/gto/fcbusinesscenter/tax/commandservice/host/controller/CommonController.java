package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CommonDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhua
 */
@RestController
public class CommonController extends BaseController{

    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.ciicsh.gt1.fcbusinesscenter.tax", artifactId = "fcbusinesscenter_tax_commandservice_drools", version = "1.0.0-SNAPSHOT")
    private KieSession kSession;

    /**
     * 倒推税前收入
     *
     * @param commonDTO
     * @return
     */
    @RequestMapping(value = "/common/cal")
    public JsonResult<CommonDTO> cal(@RequestBody CommonDTO commonDTO) {
        JsonResult<CommonDTO> jr = new JsonResult<>();

        try {
            FactType factType = kSession.getKieBase().getFactType("com.ciicsh.gt1.fcbusinesscenter.tax.fcbusinesscenter_tax_commandservice_drools", "CalculationContext");
            Object cc = factType.newInstance();
            factType.set(cc, "taxAmount",commonDTO.getTax());//税金
            factType.set(cc, "other", commonDTO.getSocialTotal());//社保公积金总额
            kSession.insert(cc);
            kSession.fireAllRules();
            Object o = factType.get(cc,"incomeForTax");
            commonDTO.setIncomeTotal(BigDecimal.valueOf((Double)o));
            jr.fill(commonDTO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "CommonController.cal", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "08"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }
}
