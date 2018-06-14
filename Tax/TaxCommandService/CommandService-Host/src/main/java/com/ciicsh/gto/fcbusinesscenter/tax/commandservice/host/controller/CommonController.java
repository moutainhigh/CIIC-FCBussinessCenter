package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.CommonDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.DroolsService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.CommonService;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhua
 */
@RestController
public class CommonController extends BaseController{


    @Autowired
    private CommonService commonService;

    @Autowired
    private DroolsService droolsService;

    /**
     * 倒推应纳税所得额
     *
     * @param commonDTO
     * @return
     */
    @PostMapping(value = "/common/cal")
    public JsonResult<CommonDTO> cal(@RequestBody CommonDTO commonDTO) {
        JsonResult<CommonDTO> jr = new JsonResult<>();

        Object o = droolsService.preTaxRevenue(commonDTO.getTax());//税金
        commonDTO.setIncomeTotal(BigDecimal.valueOf((Double)o));
        jr.fill(commonDTO);

        return jr;
    }

    @GetMapping(value = "/common/dic")
    public JsonResult<Map<String,Map<String,String>>> cal(@RequestParam(value = "dicValues")String dicValues) {

        JsonResult<Map<String,Map<String,String>>> jr = new JsonResult<>();

        Map<String,Map<String,String>> map = new HashMap<>();
        if(StrKit.isNotEmpty(dicValues)){
            String[] dicValueArray = dicValues.split(",");
            for(String dicV : dicValueArray){
                map.put(dicV,commonService.getDicItemByDicValue(dicV));
            }
        }
        jr.fill(map);
        return jr;
    }
}
