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

        BigDecimal taxReal = commonDTO.getTaxReal()==null?BigDecimal.ZERO:commonDTO.getTaxReal();
        BigDecimal preTaxAggregate = commonDTO.getPreTaxAggregate()==null?BigDecimal.ZERO:commonDTO.getPreTaxAggregate();
        BigDecimal deduction = commonDTO.getDeduction()==null?BigDecimal.ZERO:commonDTO.getDeduction();
        BigDecimal donation = commonDTO.getDonation()==null?BigDecimal.ZERO:commonDTO.getDonation();
        BigDecimal deductTakeoff = commonDTO.getDeductTakeoff()==null?BigDecimal.ZERO:commonDTO.getDeductTakeoff();
        BigDecimal others = commonDTO.getOthers()==null?BigDecimal.ZERO:commonDTO.getOthers();
        BigDecimal businessHealthInsurance = commonDTO.getBusinessHealthInsurance()==null?BigDecimal.ZERO:commonDTO.getBusinessHealthInsurance();
        BigDecimal deductHouseFund = commonDTO.getDeductHouseFund()==null?BigDecimal.ZERO:commonDTO.getDeductHouseFund();
        BigDecimal deductDlenessInsurance = commonDTO.getDeductDlenessInsurance()==null?BigDecimal.ZERO:commonDTO.getDeductDlenessInsurance();
        BigDecimal deductMedicalInsurance = commonDTO.getDeductMedicalInsurance()==null?BigDecimal.ZERO:commonDTO.getDeductMedicalInsurance();
        BigDecimal deductRetirementInsurance = commonDTO.getDeductRetirementInsurance()==null?BigDecimal.ZERO:commonDTO.getDeductRetirementInsurance();
        BigDecimal dutyFreeAllowance = commonDTO.getDutyFreeAllowance()==null?BigDecimal.ZERO:commonDTO.getDutyFreeAllowance();
        BigDecimal annuity = commonDTO.getAnnuity()==null?BigDecimal.ZERO:commonDTO.getAnnuity();
        BigDecimal incomeDutyfree = commonDTO.getIncomeDutyfree()==null?BigDecimal.ZERO:commonDTO.getIncomeDutyfree();


        m.put("taxReal",taxReal);//税金
        m.put("preTaxAggregate",preTaxAggregate);//税前合计
        m.put("deduction",deduction);//免抵税额
        m.put("donation",donation);//准予扣除的捐赠额
        m.put("deductTakeoff",deductTakeoff);//允许扣除的税费
        m.put("others",others);//其它扣除
        m.put("businessHealthInsurance",businessHealthInsurance);//商业保险
        m.put("deductHouseFund",deductHouseFund);//住房公积金
        m.put("deductDlenessInsurance",deductDlenessInsurance);//失业保险费
        m.put("deductMedicalInsurance",deductMedicalInsurance);//基本医疗保险费
        m.put("deductRetirementInsurance",deductRetirementInsurance);//基本养老保险费
        m.put("dutyFreeAllowance",dutyFreeAllowance);//免税津贴
        m.put("annuity",annuity);//企业年金个人部分
        m.put("incomeDutyfree",incomeDutyfree);//免税所得

        Map<String,BigDecimal> tm = droolsService.incomeTotal(m);

        if(tm!=null){
            commonDTO.setIncomeTotal(tm.get("incomeTotal"));//收入额
            commonDTO.setTaxRate(tm.get("taxRate"));//税率
            commonDTO.setQuickCalDeduct(tm.get("quickCalDeduct"));//速算扣除数

            //合计（税前扣除项目）= 基本养老保险费 + 基本医疗保险费 + 失业保险费 + 住房公积金 + 财产原值(空) + 允许扣除的税费 + 其他（税前扣除项目）{其它扣除 + 商业保险 + 免税津贴 + 企业年金个人部分}
            BigDecimal deductTotal = BigDecimal.ZERO;
            deductTotal = deductTotal.add(deductRetirementInsurance).add(deductMedicalInsurance).add(deductDlenessInsurance)
                        .add(deductHouseFund).add(deductTakeoff).add(others).add(businessHealthInsurance)
                        .add(dutyFreeAllowance).add(annuity);

                commonDTO.setDeductTotal(deductTotal);//合计（税前扣除项目）
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
