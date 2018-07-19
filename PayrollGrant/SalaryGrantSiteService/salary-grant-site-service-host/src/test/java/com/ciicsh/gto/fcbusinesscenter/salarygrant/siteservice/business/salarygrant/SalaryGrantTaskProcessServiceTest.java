package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gt1.exchangerate.Currencies;
import com.ciicsh.gt1.exchangerate.ExchangeManager;
import com.ciicsh.gto.companycenter.webcommandservice.api.CycleRuleProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmpFcBankProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeGrantRuleProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.EmployeeGrantRuleDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.CmyFcBankCardRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmployeeGrantRuleRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.CmyFcBankCardResponseDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.CycleRuleResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.PayrollCalcResultDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.settlementcenter.gathering.queryapi.ExchangeProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Test SalaryGrantTaskProcessService</p>
 *
 * @author gaoyang
 * @version 1.0
 * @date 2018/6/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantTaskProcessServiceTest {
    @Autowired
    private SalaryGrantTaskProcessService salaryGrantTaskProcessService;
    @Autowired
    CycleRuleProxy cycleRuleProxy;
    @Autowired
    EmpFcBankProxy empFcBankProxy;
    @Autowired
    EmployeeGrantRuleProxy employeeGrantRuleProxy;
    @Autowired
    ExchangeProxy exchangeProxy;

    @Test
    public void toConvertToPayrollCalcResultDTO(){
        Map batchParam = new HashMap();
        batchParam.put("batchCode","GL1800255_201806_0000000299");
        batchParam.put("batchType",1);
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = salaryGrantTaskProcessService.listPayrollCalcResult(batchParam);
    }

    @Test
    public void toClosing(){
        ClosingMsg closingMsg = new ClosingMsg();
        /*closingMsg.setBatchCode("GL1800255_201805_0000000329");
        closingMsg.setBatchType(1);
        closingMsg.setVersion(11);*/
        /*closingMsg.setBatchCode("GL1800255_201807_0000000692");
        closingMsg.setBatchType(1);
        closingMsg.setVersion(1);*/
        closingMsg.setBatchCode("GL1800255_201806_0000000299");
        closingMsg.setBatchType(1);
        closingMsg.setVersion(9);
        salaryGrantTaskProcessService.toClosing(closingMsg);
    }

    @Test
    public void getCycleRule(){
        Integer cycleRuleId = 43;
        com.ciicsh.common.entity.JsonResult<CycleRuleResponseDTO> cmyFcCycleRuleResponseDTOResult = cycleRuleProxy.selectById(String.valueOf(cycleRuleId));
        if(!ObjectUtils.isEmpty(cmyFcCycleRuleResponseDTOResult.getData())){
            CycleRuleResponseDTO cycleRuleResponseDTO = cmyFcCycleRuleResponseDTOResult.getData();
            System.out.println("salary_day_date:" + cycleRuleResponseDTO.getSalaryDayDate()); //31
            System.out.println("salary_day_time:" + cycleRuleResponseDTO.getSalaryDayTime());
        }
    }

    @Test
    public void listEmployeeBankcardInfo(){
        CmyFcBankCardRequestDTO cmyFcBankCardRequestDTO = new CmyFcBankCardRequestDTO();
        cmyFcBankCardRequestDTO.setEmployeeId("000000031");
        JsonResult<List<CmyFcBankCardResponseDTO>> bankCardInfoResult = empFcBankProxy.getFcBankCardInfo(cmyFcBankCardRequestDTO);
        List<CmyFcBankCardResponseDTO>  cmyFcBankCardResponseDTOList = bankCardInfoResult.getData();
        if(!CollectionUtils.isEmpty(cmyFcBankCardResponseDTOList)){
            cmyFcBankCardResponseDTOList.forEach(cmyFcBankCardResponseDTO ->{
                System.out.println("bankcard_id:" + cmyFcBankCardResponseDTO.getBankcardId()); // 464
            });
        }
    }

    @Test
    public void listSalaryGrantRuleInfo(){
        EmployeeGrantRuleRequestDTO employeeGrantRuleRequestDTO = new EmployeeGrantRuleRequestDTO();
        employeeGrantRuleRequestDTO.setBankcardId("464");
        //employeeGrantRuleRequestDTO.setRuleId((String)paramMap.get("salaryGrantRuleId"));
        com.ciicsh.common.entity.JsonResult<List<EmployeeGrantRuleDTO>> grantRuleResult = employeeGrantRuleProxy.getGrantRule(employeeGrantRuleRequestDTO);
        List<EmployeeGrantRuleDTO> employeeGrantRuleDTOList = grantRuleResult.getData();
        if(!CollectionUtils.isEmpty(employeeGrantRuleDTOList)){
            employeeGrantRuleDTOList.forEach(employeeGrantRuleDTO ->{
                System.out.println("grant_rule_id:" + employeeGrantRuleDTO.getCmyFcEmpSalaryGrantRuleId()); //67
            });
        }
    }

    @Test
    public void getExchangeInfo(){
        String currencyCode = "USD";
        com.ciicsh.gto.settlementcenter.gathering.queryapi.result.JsonResult<com.ciicsh.gto.settlementcenter.gathering.queryapi.dto.ExchangeDTO>  exchangeResult = exchangeProxy.getExchange(currencyCode);
        com.ciicsh.gto.settlementcenter.gathering.queryapi.dto.ExchangeDTO exchangeDTO = exchangeResult.getRecord();
        if(!ObjectUtils.isEmpty(exchangeDTO)){
            System.out.println("exchange_USD:" + exchangeDTO.getExchange()); //6.6
        }
    }

    @Test
    public void getCurrentExchangeInfo(){
        BigDecimal exchange = ExchangeManager.exchange(Currencies.USD,Currencies.CNY);
        System.out.println("exchange_USD:" + exchange); //6.6157
    }
}
