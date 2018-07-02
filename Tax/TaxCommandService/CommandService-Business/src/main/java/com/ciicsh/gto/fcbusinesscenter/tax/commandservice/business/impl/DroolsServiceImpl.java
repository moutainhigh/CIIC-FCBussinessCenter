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
    public Map<String,BigDecimal> preTaxRevenue(BigDecimal tax) {

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
        Object o1 = factType.get(cc,"incomeForTax");
        Object o2 = factType.get(cc,"taxRate");
        Object o3 = factType.get(cc,"quickCalDeduct");

        Map<String,BigDecimal> rm = new HashMap<>();
        rm.put("incomeForTax",(BigDecimal)o1);
        rm.put("taxRate",(BigDecimal)o2);
        rm.put("quickCalDeduct",(BigDecimal)o3);
        return rm;
    }

    @Override
    public Map<String,BigDecimal> incomeTotal(Map<String, BigDecimal> item) {

        Map<String,BigDecimal> rm = new HashMap<>();

        //应纳税所得额
        BigDecimal incomeForTax = null;
        //收入额
        BigDecimal incomeTotal=new BigDecimal(0);
        //税率
        BigDecimal taxRate=null;
        //速算扣除数
        BigDecimal quickCalDeduct=null;

        //税金
        BigDecimal taxReal=new BigDecimal(0);
        if(item.containsKey("taxReal")){
            taxReal = item.get("taxReal");
        }
        //税前合计
        BigDecimal preTaxAggregate=new BigDecimal(0);
        if(item.containsKey("preTaxAggregate")){
            preTaxAggregate = item.get("preTaxAggregate");
        }
        //免抵税额
        BigDecimal deduction=new BigDecimal(0);
        if(item.containsKey("deduction")){
            deduction = item.get("deduction");
        }
        //准予扣除的捐赠额
        BigDecimal donation=new BigDecimal(0);
        if(item.containsKey("donation")){
            donation = item.get("donation");
        }
        //允许扣除的税费
        BigDecimal deductTakeoff=new BigDecimal(0);
        if(item.containsKey("deductTakeoff")){
            deductTakeoff = item.get("deductTakeoff");
        }
        //其它扣除
        BigDecimal otherDeductions=new BigDecimal(0);
        if(item.containsKey("others")){
            otherDeductions = item.get("others");
        }
        //商业保险
        BigDecimal businessHealthInsurance=new BigDecimal(0);
        if(item.containsKey("businessHealthInsurance")){
            businessHealthInsurance = item.get("businessHealthInsurance");
        }
        //住房公积金
        BigDecimal deductHouseFund=new BigDecimal(0);
        if(item.containsKey("deductHouseFund")){
            deductHouseFund = item.get("deductHouseFund");
        }
        //失业保险费
        BigDecimal deductDlenessInsurance=new BigDecimal(0);
        if(item.containsKey("deductDlenessInsurance")){
            deductDlenessInsurance = item.get("deductDlenessInsurance");
        }
        //基本医疗保险费
        BigDecimal deductMedicalInsurance=new BigDecimal(0);
        if(item.containsKey("deductMedicalInsurance")){
            deductMedicalInsurance = item.get("deductMedicalInsurance");
        }
        //基本养老保险费
        BigDecimal deductRetirementInsurance=new BigDecimal(0);
        if(item.containsKey("deductRetirementInsurance")){
            deductRetirementInsurance = item.get("deductRetirementInsurance");
        }
        //免税津贴
        BigDecimal dutyFreeAllowance=new BigDecimal(0);
        if(item.containsKey("dutyFreeAllowance")){
            dutyFreeAllowance = item.get("dutyFreeAllowance");
        }
        //企业年金个人部分
        BigDecimal annuity=new BigDecimal(0);
        if(item.containsKey("annuity")){
            annuity = item.get("annuity");
        }
        //免税所得
        BigDecimal incomeDutyfree=new BigDecimal(0);
        if(item.containsKey("incomeDutyfree")){
            incomeDutyfree = item.get("incomeDutyfree");
        }

        //税金等于0
        if(taxReal.compareTo(BigDecimal.ZERO)==0){

            //税前合计小于免抵税额
            if(preTaxAggregate.compareTo(deduction)==-1){
                incomeTotal=preTaxAggregate.add(donation).add(deductTakeoff).add(otherDeductions).add(businessHealthInsurance).add(deductHouseFund).add(deductDlenessInsurance)
                        .add(deductMedicalInsurance).add(deductRetirementInsurance).add(dutyFreeAllowance).add(annuity).add(incomeDutyfree);
            }else{
                incomeTotal=deduction.add(donation).add(deductTakeoff).add(otherDeductions).add(businessHealthInsurance).add(deductHouseFund).add(deductDlenessInsurance)
                        .add(deductMedicalInsurance).add(deductRetirementInsurance).add(dutyFreeAllowance).add(annuity).add(incomeDutyfree);
            }
        //税金大于0
        }else if(taxReal.compareTo(BigDecimal.ZERO)==1){

            //倒推应纳税所得额、税率、速算扣除数
            Map<String,BigDecimal> m = this.preTaxRevenue(taxReal);

            //应纳税所得额
            incomeForTax = m.get("incomeForTax");

            //收入额
            incomeTotal=incomeForTax.add(donation).add(deductTakeoff).add(otherDeductions).add(businessHealthInsurance).add(deductHouseFund).add(deductDlenessInsurance)
                    .add(deductMedicalInsurance).add(deductRetirementInsurance).add(dutyFreeAllowance).add(annuity).add(incomeDutyfree);

            //税率
            taxRate=m.get("taxRate");
            //速算扣除数
            quickCalDeduct=m.get("quickCalDeduct");

        }

        rm.put("incomeForTax",incomeForTax);
        rm.put("incomeTotal",incomeTotal);
        rm.put("taxRate",taxRate);
        rm.put("quickCalDeduct",quickCalDeduct);


        return rm;
    }
}
