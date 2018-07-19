package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryBatchDTO;
import com.google.gson.JsonObject;
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

    @Test
    public void getUserNameById(){
        String userName = commonService.getUserNameById("XTY00699");
        System.out.println("userName: " + userName);
    }

    /**
     * 查询管理方的发放批次信息
     */
    @Test
    public void getBatchListByManagementId() {
        List<PrNormalBatchDTO> batchDTOList =  commonService.getBatchListByManagementId("GL170001");
        System.out.println("batchDTOList: \n" + JSONObject.toJSONString(batchDTOList));
    }

    /**
     * 暂缓池删除接口
     */
    @Test
    public void delDeferredEmp() {
        SalaryGrantMainTaskPO mainTaskPO = new SalaryGrantMainTaskPO();
        mainTaskPO.setBatchCode("123");
        mainTaskPO.setSalaryGrantMainTaskCode("123");
        boolean result = commonService.delDeferredEmp(mainTaskPO);
        System.out.println("暂缓池删除 result: " + result);
    }

}
