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
     * 计算收入额
     *
     * @param commonDTO
     * @return
     */
    @PostMapping(value = "/common/cal")
    public JsonResult<CommonDTO> cal(@RequestBody CommonDTO commonDTO) {
        JsonResult<CommonDTO> jr = new JsonResult<>();

        HashMap<String,BigDecimal> m = new HashMap<>();

        m.put("taxReal",commonDTO.getTaxReal());//税金
        m.put("preTaxAggregate",commonDTO.getPreTaxAggregate());//税前合计
        m.put("deduction",commonDTO.getDeduction());//免抵税额
        m.put("donation",commonDTO.getDonation());//准予扣除的捐赠额
        m.put("deductTakeoff",commonDTO.getDeductTakeoff());//允许扣除的税费
        m.put("others",commonDTO.getOthers());//其它扣除
        m.put("businessHealthInsurance",commonDTO.getBusinessHealthInsurance());//商业保险
        m.put("deductHouseFund",commonDTO.getDeductHouseFund());//住房公积金
        m.put("deductDlenessInsurance",commonDTO.getDeductDlenessInsurance());//失业保险费
        m.put("deductMedicalInsurance",commonDTO.getDeductMedicalInsurance());//基本医疗保险费
        m.put("deductRetirementInsurance",commonDTO.getDeductRetirementInsurance());//基本养老保险费
        m.put("dutyFreeAllowance",commonDTO.getDutyFreeAllowance());//免税津贴
        m.put("annuity",commonDTO.getAnnuity());//企业年金个人部分
        m.put("incomeDutyfree",commonDTO.getIncomeDutyfree());//免税所得

        Map<String,BigDecimal> tm = droolsService.incomeTotal(m);

        if(tm!=null){
            commonDTO.setIncomeTotal(tm.get("incomeTotal"));//收入额
        }

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
