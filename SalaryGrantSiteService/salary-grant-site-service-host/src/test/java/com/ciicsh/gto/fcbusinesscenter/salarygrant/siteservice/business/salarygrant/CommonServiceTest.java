package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * <p>Test CommonService</p>
 *
 * @author gaoyang
 * @version 1.0
 * @date 2018/5/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CommonServiceTest {
    @Autowired
    private CommonService commonService;

    @Test
    public void queryNameByValue(){
        String nameStr = commonService.getNameByValue("sgGrantMode","4");
        System.out.println("字典名称: " + nameStr);
    }

    @Test
    public void queryNameByValueForList(){
        List nameList = commonService.getNameByValueForList("sgGrantMode");
        nameList.forEach(item ->{
            System.out.println("字典名称: " + item);
        });
    }

    @Test
    public void queryCountryName(){
        String countryName = commonService.getCountryName("CN");
        System.out.println("国家名称: " + countryName);
    }
}
