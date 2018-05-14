package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.BankFileProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileEmployeeProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileResultFileProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileResultProxyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 调用结算中心接口生成报盘文件</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/14 0014
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class BankFileProxyTest {
    @Autowired
    private BankFileProxy bankFileProxy;

    @Test
    public void getPrivateFile() {
        BankFileProxyDTO bankFileProxyDTO = new BankFileProxyDTO();
        //银行代码：建行1，工行2，招商银行3，中国银行4，其他银行5
        bankFileProxyDTO.setBankCode(1);

        List<BankFileEmployeeProxyDTO> list = new ArrayList<>();
        BankFileEmployeeProxyDTO employeeProxyDTO = new BankFileEmployeeProxyDTO();
        employeeProxyDTO.setEmployeeName("张三");
        employeeProxyDTO.setEmployeeBankAccount("6000000000001");
        employeeProxyDTO.setBankName("建设银行");
        employeeProxyDTO.setCityName("上海");
        employeeProxyDTO.setPayAmount(new BigDecimal(200.00));
        employeeProxyDTO.setProvinceName("上海");
        employeeProxyDTO.setAreaCode("SH");
        employeeProxyDTO.setRemark("AF雇员报销");
        employeeProxyDTO.setBankAccountName("建设银行徐汇支行");
        employeeProxyDTO.setBankAccount("6000000000002");

        list.add(employeeProxyDTO);

        bankFileProxyDTO.setList(list);

        JsonResult<BankFileResultProxyDTO> proxyDTOJsonResult = bankFileProxy.getPrivateFile(bankFileProxyDTO);
        if (!ObjectUtils.isEmpty(proxyDTOJsonResult) && "0".equals(proxyDTOJsonResult.getCode())) {
            BankFileResultProxyDTO fileResultProxyDTO = proxyDTOJsonResult.getData();

            if (!ObjectUtils.isEmpty(fileResultProxyDTO)) {
                List<BankFileResultFileProxyDTO> resultFileProxyDTOList = fileResultProxyDTO.getList();
                if (!CollectionUtils.isEmpty(resultFileProxyDTOList)) {
                    resultFileProxyDTOList.stream().forEach(proxyDTO ->
                            System.out.println("getFilePath: " + proxyDTO.getFilePath() + " getAmount: " + proxyDTO.getAmount() + " getCount: " + proxyDTO.getCount())
                    );
                }
            }
        } else {
            System.out.println("调用接口返回错误");
        }

    }
}
