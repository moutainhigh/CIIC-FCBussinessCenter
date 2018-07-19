package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.billcenter.fcmodule.api.ISalaryEmployeeProxy;
import com.ciicsh.gto.billcenter.fcmodule.api.common.JsonResult;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryEmployeeProxyDTO;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryProxyDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 账单中心的接口查询雇员的薪酬服务费</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/24 0024
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class IEmployeeProxyTest {
    @Autowired
    private ISalaryEmployeeProxy salaryEmployeeProxy;

    @Test
    public void getEmployeeSalaryServiceFee() {
        SalaryProxyDTO salaryProxyDTO = new SalaryProxyDTO();
        salaryProxyDTO.setBatchCode("20180411001");

        List<SalaryEmployeeProxyDTO> employeeList = new ArrayList<>();

        SalaryEmployeeProxyDTO employeeProxyDTO = new SalaryEmployeeProxyDTO();
        employeeProxyDTO.setCompanyId("KH1800299");
        employeeProxyDTO.setEmployeeId("XTY00201");
        employeeList.add(employeeProxyDTO);

        JsonResult<SalaryProxyDTO> proxyDTOJsonResult = salaryEmployeeProxy.getSalaryEmployeeServiceFee(salaryProxyDTO);
        if (!ObjectUtils.isEmpty(proxyDTOJsonResult)) {
            if (proxyDTOJsonResult.getCode().intValue() == 0) {
                SalaryProxyDTO proxyDTO = proxyDTOJsonResult.getData();
                if (!ObjectUtils.isEmpty(proxyDTO)) {
                    String batchCode = proxyDTO.getBatchCode();
                    System.out.println("批次号: " + batchCode);
                    List<SalaryEmployeeProxyDTO> proxyDTOEmployeeList = proxyDTO.getEmployeeList();
                    if (!CollectionUtils.isEmpty(proxyDTOEmployeeList)) {
                        proxyDTOEmployeeList.stream().forEach(dto ->
                                System.out.println("公司编号: " + dto.getCompanyId() + " 雇员编号: " + dto.getEmployeeId() + " 薪酬服务费: " + dto.getServiceFeeAmount())
                        );
                    } else {
                        System.out.println("返回雇员信息为空！");
                    }
                }
            } else {
                System.out.println(proxyDTOJsonResult.getMsg());
            }
        }

    }
}
