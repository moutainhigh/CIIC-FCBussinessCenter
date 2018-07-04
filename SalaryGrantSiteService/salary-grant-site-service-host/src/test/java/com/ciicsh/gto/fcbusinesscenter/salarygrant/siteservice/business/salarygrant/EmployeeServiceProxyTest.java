package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.FcEmpInfoRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.FcEmpInfoResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/6/28 0028
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EmployeeServiceProxyTest {
    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;

    /**
     * 获取雇员最新信息
     */
    @Test
    public void getFcEmpDetail() {
        SalaryGrantEmployeePO newestEmployeePO = null;

        FcEmpInfoRequestDTO fcEmpInfoRequestDTO = new FcEmpInfoRequestDTO();
//        fcEmpInfoRequestDTO.setCompanyId("KH18000355"); //公司编号
        fcEmpInfoRequestDTO.setCompanyId("1"); //公司编号
        fcEmpInfoRequestDTO.setEmployeeId("18023890"); //雇员编号
        fcEmpInfoRequestDTO.setBankcardId("610"); //银行卡编号
        fcEmpInfoRequestDTO.setRuleId("119"); //薪资发放规则编号
        JsonResult<FcEmpInfoResponseDTO> jsonResult = employeeServiceProxy.getFcEmpDetail(fcEmpInfoRequestDTO);
        if (!ObjectUtils.isEmpty(jsonResult)) {
            if (true == jsonResult.isSuccess()) {
                FcEmpInfoResponseDTO retEmpInfoResponseDTO = jsonResult.getData();
                System.out.println("retEmpInfoResponseDTO: " + retEmpInfoResponseDTO);
            } else {
                System.out.println("获取雇员最新信息返回失败 错误码: " + jsonResult.getErrCode() + " 错误消息: " + jsonResult.getMessage());
            }
        } else {
            System.out.println("获取雇员最新信息接口调用失败");
        }
    }

    public SalaryGrantEmployeePO fcEmpInfoRequestDTO2SalaryGrantEmployeePO(FcEmpInfoResponseDTO fcEmpInfoResponseDTO){
        SalaryGrantEmployeePO newestEmployeePO = new SalaryGrantEmployeePO();

        //雇员基本信息表
        newestEmployeePO.setCountryCode(fcEmpInfoResponseDTO.getBaseInfo().getCountryCode()); //国籍

        //雇员银行卡信息
        newestEmployeePO.setBankcardType(fcEmpInfoResponseDTO.getBankCardInfo().getBankcardType()); //银行卡种类
        newestEmployeePO.setDepositBank(fcEmpInfoResponseDTO.getBankCardInfo().getDepositBank()); //收款行名称
        newestEmployeePO.setBankCode(fcEmpInfoResponseDTO.getBankCardInfo().getBankCode()); //收款行行号
        newestEmployeePO.setAccountName(fcEmpInfoResponseDTO.getBankCardInfo().getAccountName()); //收款人姓名
        newestEmployeePO.setCardNum(fcEmpInfoResponseDTO.getBankCardInfo().getCardNum()); //收款人账号
        newestEmployeePO.setCurrencyCode(fcEmpInfoResponseDTO.getBankCardInfo().getCurrencyCode()); //银行卡币种
        newestEmployeePO.setExchange(fcEmpInfoResponseDTO.getBankCardInfo().getExchange()); //银行卡汇率
        newestEmployeePO.setSwiftCode(fcEmpInfoResponseDTO.getBankCardInfo().getSwiftCode()); //银行国际代码
        newestEmployeePO.setIban(fcEmpInfoResponseDTO.getBankCardInfo().getIban()); //国际银行账户号码
        newestEmployeePO.setDefaultCard(1 == fcEmpInfoResponseDTO.getBankCardInfo().getDefult()); //是否默认卡

        //薪资发放规则信息
        newestEmployeePO.setBankcardId(Long.parseLong(fcEmpInfoResponseDTO.getSalGrantRule().getBankcardId())); //银行卡编号
        newestEmployeePO.setCurrencyCode(fcEmpInfoResponseDTO.getSalGrantRule().getCurrency()); //发放币种
        newestEmployeePO.setRuleAmount(fcEmpInfoResponseDTO.getSalGrantRule().getAmount()); //规则金额
        newestEmployeePO.setRuleRatio(fcEmpInfoResponseDTO.getSalGrantRule().getPercent()); //规则比例
        newestEmployeePO.setRuleType(fcEmpInfoResponseDTO.getSalGrantRule().getRuleType()); //规则类型

        //雇员服务协议
        newestEmployeePO.setGrantMode(Integer.parseInt(fcEmpInfoResponseDTO.getEmpServiceAgreement().getSalaryGrantInfo())); //发放方式

        //服务周期规则表
        newestEmployeePO.setGrantDate(fcEmpInfoResponseDTO.getVariables().get("cycleRule").get("SalaryDayDate").toString()); //薪资发放日期
        newestEmployeePO.setGrantTime(Integer.parseInt(fcEmpInfoResponseDTO.getVariables().get("cycleRule").get("SalaryDayTime").toString())); //薪资发放时段

        return newestEmployeePO;
    }
}
