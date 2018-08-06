package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.EmployeeReturnTicketDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private CommonService commonService;

    @Test
    public void updateForRefund() {
        List<EmployeeReturnTicketDTO > employeeReturnTicketDTOList = new ArrayList<>();
        EmployeeReturnTicketDTO employeeReturnTicketDTO = new EmployeeReturnTicketDTO();
        employeeReturnTicketDTO.setBatchCode("GL1800376_201807_0000001135");
        employeeReturnTicketDTO.setSequenceNo("LTB18073000000000001");
        employeeReturnTicketDTO.setCompanyId("KH18000516");
        employeeReturnTicketDTO.setEmployeeId("18026414");
        employeeReturnTicketDTO.setEmployeeBankAccount("6333000040035");
        employeeReturnTicketDTO.setPayAmount(new BigDecimal("56221.00"));
        employeeReturnTicketDTOList.add(employeeReturnTicketDTO);

        employeeReturnTicketDTO = new EmployeeReturnTicketDTO();
        employeeReturnTicketDTO.setBatchCode("GL1800376_201807_0000000994");
        employeeReturnTicketDTO.setSequenceNo("LTB18072000000000003");
        employeeReturnTicketDTO.setCompanyId("KH18000516");
        employeeReturnTicketDTO.setEmployeeId("18026318");
        employeeReturnTicketDTO.setEmployeeBankAccount("6333000027035");
        employeeReturnTicketDTO.setPayAmount(new BigDecimal("56221.00"));
        employeeReturnTicketDTOList.add(employeeReturnTicketDTO);

        employeeCommandService.updateForRefund(employeeReturnTicketDTOList);
    }

    /**
     * 添加调整信息
     */
    @Test
    public void adjustCompareInfo() {
        SalaryGrantEmployeePO adjustDiffEmployeePO = employeeCommandService.selectById(215);

        SalaryGrantEmployeePO adjustBeforeEmployeePO = employeeCommandService.selectById(216);

        //生成调整后数据
        SalaryGrantEmployeePO adjustAfterEmployeePO = new SalaryGrantEmployeePO();
        BeanUtils.copyProperties(adjustDiffEmployeePO, adjustAfterEmployeePO);
        //发放金额 = 调整差异记录发放金额 + 调整前记录发放金额
        adjustAfterEmployeePO.setPaymentAmount(adjustDiffEmployeePO.getPaymentAmount().add(adjustBeforeEmployeePO.getPaymentAmount()));

        //生成调整信息
        List<SalaryGrantEmployeeBO> adjustCompareInfo = new ArrayList<>();
        SalaryGrantEmployeeBO adjustBeforeEmployeeBO = new SalaryGrantEmployeeBO();
        BeanUtils.copyProperties(adjustBeforeEmployeePO, adjustBeforeEmployeeBO);
        adjustBeforeEmployeeBO.setAdjustInfo("调整前信息");
        //发放币种名称
        adjustBeforeEmployeeBO.setCurrencyName(commonService.getNameByValue("currency", String.valueOf(adjustBeforeEmployeeBO.getCurrencyCode())));
        //国籍中文
        adjustBeforeEmployeeBO.setCountryName(commonService.getCountryName(adjustBeforeEmployeeBO.getCountryCode()));
        adjustCompareInfo.add(adjustBeforeEmployeeBO); //调整前记录

        SalaryGrantEmployeeBO adjustDiffEmployeeBO = new SalaryGrantEmployeeBO();
        BeanUtils.copyProperties(adjustDiffEmployeePO, adjustDiffEmployeeBO);
        adjustDiffEmployeeBO.setAdjustInfo("调整差异信息");
        //发放币种名称
        adjustDiffEmployeeBO.setCurrencyName(commonService.getNameByValue("currency", String.valueOf(adjustDiffEmployeeBO.getCurrencyCode())));
        //国籍中文
        adjustDiffEmployeeBO.setCountryName(commonService.getCountryName(adjustDiffEmployeeBO.getCountryCode()));
        adjustCompareInfo.add(adjustDiffEmployeeBO); //调整差异记录

        SalaryGrantEmployeeBO adjustAfterEmployeeBO = new SalaryGrantEmployeeBO();
        BeanUtils.copyProperties(adjustAfterEmployeePO, adjustAfterEmployeeBO);
        adjustAfterEmployeeBO.setAdjustInfo("调整后信息");
        //发放币种名称
        adjustAfterEmployeeBO.setCurrencyName(commonService.getNameByValue("currency", String.valueOf(adjustAfterEmployeeBO.getCurrencyCode())));
        //国籍中文
        adjustAfterEmployeeBO.setCountryName(commonService.getCountryName(adjustAfterEmployeeBO.getCountryCode()));
        adjustCompareInfo.add(adjustAfterEmployeeBO); //调整后记录

        //更新调整信息到调整差异记录
        SalaryGrantEmployeePO updateEmployeePO = new SalaryGrantEmployeePO();
        updateEmployeePO.setSalaryGrantEmployeeId(adjustDiffEmployeePO.getSalaryGrantEmployeeId());
        updateEmployeePO.setAdjustCompareInfo(JSONObject.toJSONString(adjustCompareInfo));
        employeeCommandService.updateById(updateEmployeePO);
    }
}
