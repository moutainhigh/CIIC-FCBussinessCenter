package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.EmployeeReturnTicketDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/31 0031
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantEmployeeCommandServiceTest {
    @Autowired
    private SalaryGrantEmployeeCommandService employeeCommandService;

    @Test
    public void updateForRefund() {
        String taskCode = "LTB20180328000000002";
        List<EmployeeReturnTicketDTO > employeeReturnTicketDTOList = new ArrayList<>();
        EmployeeReturnTicketDTO employeeReturnTicketDTO = new EmployeeReturnTicketDTO();
        employeeReturnTicketDTO.setCompanyId("c001");
        employeeReturnTicketDTO.setEmployeeId("000000001");
        employeeReturnTicketDTO.setEmployeeBankAccount("1234567890");
        employeeReturnTicketDTO.setPayAmount(new BigDecimal(0));
        employeeReturnTicketDTOList.add(employeeReturnTicketDTO);

        employeeReturnTicketDTO = new EmployeeReturnTicketDTO();
        employeeReturnTicketDTO.setCompanyId("c002");
        employeeReturnTicketDTO.setEmployeeId("000000003");
        employeeReturnTicketDTO.setEmployeeBankAccount("1234567892");
        employeeReturnTicketDTO.setPayAmount(new BigDecimal(0));
        employeeReturnTicketDTOList.add(employeeReturnTicketDTO);

        employeeCommandService.updateForRefund(taskCode, employeeReturnTicketDTOList);
    }
}
