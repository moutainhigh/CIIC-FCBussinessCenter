package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gt1.exchangerate.Currencies;
import com.ciicsh.gt1.exchangerate.ExchangeManager;
import com.ciicsh.gt1.exchangerate.RateExchanger;
import com.ciicsh.gt1.exchangerate.exchangers.BocExchanger;
import com.ciicsh.gt1.exchangerate.exchangers.IP138Exchanger;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/6/27 0027
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = App.class)
public class ExchangeManagerTest {

    @Test
    public void exchange() {


        BigDecimal exchangeRate = ExchangeManager.exchange(Currencies.USD, Currencies.CNY);
        System.out.println("美元 对 人民币 汇率：" + exchangeRate);

//        exchangeRate = ExchangeManager.exchange(Currencies.CNY, Currencies.USD);
//        System.out.println("人民币 对 美元 汇率：" + exchangeRate);

        exchangeRate = ExchangeManager.exchange(Currencies.EUR, Currencies.CNY);
        System.out.println("欧元 对 人民币 汇率：" + exchangeRate);

        RateExchanger rateExchanger = new BocExchanger();
        System.out.println("Boc 美元 对 人民币 汇率：" + rateExchanger.exchange(Currencies.USD, Currencies.CNY));
        System.out.println("Boc 欧元 对 人民币 汇率：" + rateExchanger.exchange(Currencies.EUR, Currencies.CNY));

        rateExchanger = new IP138Exchanger();
        System.out.println("IP138 美元 对 人民币 汇率：" + rateExchanger.exchange(Currencies.USD, Currencies.CNY));
        System.out.println("IP138 欧元 对 人民币 汇率：" + rateExchanger.exchange(Currencies.EUR, Currencies.CNY));
    }

    @Test
    public void fastJson() {
        Map<String, String> outerMap = new HashMap<>();

        //国籍
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("old_value", 1);
        innerMap.put("old_date", new Date());
        innerMap.put("new_value", 2);
        innerMap.put("new_date", new Date());
        innerMap.put("type", "emp_employee");

        outerMap.put("country_code", JSONObject.toJSONString(innerMap));

        //银行卡种类
        innerMap = new HashMap<>();
        innerMap.put("old_value", 3);
        innerMap.put("old_date", new Date());
        innerMap.put("new_value", 4);
        innerMap.put("new_date", new Date());
        innerMap.put("type", "emp_fc_bankcard");

        outerMap.put("bankcard_type", JSONObject.toJSONString(innerMap));

        String changeLogMapJsonStr = JSONObject.toJSONString(outerMap);
        System.out.println("变更日志: " + changeLogMapJsonStr);

        Map restoreMap = JSONObject.parseObject(changeLogMapJsonStr, Map.class);
        System.out.println("恢复Map: " + restoreMap);

        SalaryGrantEmployeePO oldEmployeePO = new SalaryGrantEmployeePO();
        SalaryGrantEmployeePO newEmployeePO = new SalaryGrantEmployeePO();

        //国籍
        Object countryCodeObject = restoreMap.get("country_code");
        if (!ObjectUtils.isEmpty(countryCodeObject)) {
            Map countryCodeMap = JSONObject.parseObject((String) countryCodeObject, Map.class);
            System.out.println("countryCodeMap：" + countryCodeMap);

            oldEmployeePO.setCountryCode((String) countryCodeMap.get("old_value"));
            newEmployeePO.setCountryCode((String) countryCodeMap.get("new_value"));
        }
    }

}
