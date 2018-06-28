package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gt1.exchangerate.Currencies;
import com.ciicsh.gt1.exchangerate.ExchangeManager;
import org.junit.Test;

import java.math.BigDecimal;

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
        BigDecimal exchangeRate = ExchangeManager.exchange(Currencies.CNY, Currencies.USD);
        System.out.println("人民币 对 美元 汇率：" + exchangeRate);

        exchangeRate = ExchangeManager.exchange(Currencies.USD, Currencies.CNY);
        System.out.println("美元 对 人民币 汇率：" + exchangeRate);
    }
}
