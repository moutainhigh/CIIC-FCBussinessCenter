package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl.SalaryGrantSupplierSubTaskServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/23 0023
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantSupplierSubTaskServiceTest {
    @Autowired
    private SalaryGrantSupplierSubTaskService supplierSubTaskService;
    @Autowired
    private SalaryGrantSupplierSubTaskServiceImpl supplierSubTaskServiceImpl;

    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     */
    @Test
    public void querySupplierSubTaskForSubmitPage() {
        Page<SalaryGrantTaskBO> page = new Page<>();
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        salaryGrantTaskBO.setManagementId("GL000007");
        salaryGrantTaskBO.setBatchCode("GL000007_201802_0000000175");
        salaryGrantTaskBO.setTaskCode("LT201803280000000003");
        page = supplierSubTaskService.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO);
        System.out.println("查询供应商任务单列表 page: " + page + " 记录: " + page.getRecords());
    }

    /**
     * 查询供应商任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回 角色=操作员、审核员（查历史表）
     */
    @Test
    public void querySupplierSubTaskForRejectPage() {
        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(3);
        page.setSize(2);

        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
//        salaryGrantTaskBO.setCurrentUserId("001");
//        salaryGrantTaskBO.setTaskCode("ST201803280000000001");
//        salaryGrantTaskBO.setManagementId("001");
//        salaryGrantTaskBO.setManagementName("星巴克");
//        salaryGrantTaskBO.setBatchCode("CMY000001-20180328-0001");
//        salaryGrantTaskBO.setGrantAccountCode("sp001");

        Page<SalaryGrantTaskBO> taskBOPage = supplierSubTaskService.querySupplierSubTaskForRejectPage(page, salaryGrantTaskBO);
        System.out.println("查询供应商任务单列表 page: " + taskBOPage + " 记录: " + taskBOPage.getRecords());
    }

    /**
     * BO PAGE TO DTO PAGE
     */
    @Test
    public void boPageToDTOPage() {
        SalaryGrantTaskDTO salaryGrantTaskDTO = new SalaryGrantTaskDTO();
        salaryGrantTaskDTO.setCurrent(1);
        salaryGrantTaskDTO.setSize(2);

        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);

        page = supplierSubTaskService.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO);
        System.out.println("BO PAGE TO DTO PAGE BO page: " + page + " 记录: " + page.getRecords());

//        String boJsonStr = JSONArray.toJSONString(page.getRecords());
//
//        // 复制分页信息
//        Page<SalaryGrantTaskDTO> taskDTOPage = new Page<>();
//        BeanUtils.copyProperties(page, taskDTOPage);
//        taskDTOPage.setRecords(JSONArray.parseArray(boJsonStr, SalaryGrantTaskDTO.class));
//        System.out.println("BO PAGE TO DTO PAGE DTO page: " + taskDTOPage + " 记录: " + taskDTOPage.getRecords());

        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);
        System.out.println("BO PAGE TO DTO PAGE DTO page: " + taskDTOPage + " 记录: " + taskDTOPage.getRecords());

    }

    @Test
    public void insertChangeLog() {
        SalaryGrantEmployeePO currentEmployeePO = new SalaryGrantEmployeePO();
        currentEmployeePO.setSalaryGrantEmployeeId(216L);
        SalaryGrantEmployeePO newestEmployeePO = new SalaryGrantEmployeePO();

        //国籍 country_code
        currentEmployeePO.setCountryCode("CN"); //CN 中国
        newestEmployeePO.setCountryCode("CO"); //CO	哥伦比亚

        //银行卡种类 bankcard_type 银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
        currentEmployeePO.setBankcardType(1);
        newestEmployeePO.setBankcardType(2);

        //收款行名称 deposit_bank
        currentEmployeePO.setDepositBank("收款行名称1");
        newestEmployeePO.setDepositBank("收款行名称2");

        //收款行行号 bank_code
        currentEmployeePO.setBankCode("收款行行号1");
        newestEmployeePO.setBankCode("收款行行号2");

        //收款人姓名 account_name
        currentEmployeePO.setAccountName("收款人姓名1");
        newestEmployeePO.setAccountName("收款人姓名2");

        //收款人账号 card_num
        currentEmployeePO.setCardNum("收款人账号1");
        newestEmployeePO.setCardNum("收款人账号2");

        //银行卡币种 currency_code
        currentEmployeePO.setCurrencyCode("银行卡币种1");
        newestEmployeePO.setCurrencyCode("银行卡币种2");

        //银行卡汇率 exchange
        currentEmployeePO.setExchange(new BigDecimal(0.02));
        newestEmployeePO.setExchange(new BigDecimal(0.03));

        //银行国际代码 swift_code
        currentEmployeePO.setSwiftCode("银行国际代码1");
        newestEmployeePO.setSwiftCode("银行国际代码2");

        //国际银行账户号码 iban
        currentEmployeePO.setIban("国际银行账户号码1");
        newestEmployeePO.setIban("国际银行账户号码2");

        //是否默认卡 is_default_card
        currentEmployeePO.setDefaultCard(false);
        newestEmployeePO.setDefaultCard(true);

        //银行卡编号 bankcard_id
        currentEmployeePO.setBankcardId(123L);
        newestEmployeePO.setBankcardId(456L);

        //发放币种 currency_code 发放币种:CNY-人民币，USD-美元，EUR-欧元
        currentEmployeePO.setCurrencyCode("CNY");
        newestEmployeePO.setCurrencyCode("USD");

        //规则金额 rule_amount
        currentEmployeePO.setRuleAmount(new BigDecimal(100));
        newestEmployeePO.setRuleAmount(new BigDecimal(101));

        //规则比例 rule_ratio
        currentEmployeePO.setRuleRatio(new BigDecimal(0.01));
        newestEmployeePO.setRuleRatio(new BigDecimal(0.02));

        //规则类型 rule_type 规则类型:0-固定金额、1-固定比例
        currentEmployeePO.setRuleType(0);
        newestEmployeePO.setRuleType(1);

        //发放方式 grant_mode 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
        currentEmployeePO.setGrantMode(1);
        newestEmployeePO.setGrantMode(2);

        //薪资发放日期 grant_date
        currentEmployeePO.setGrantDate("2018-07-01");
        newestEmployeePO.setGrantDate("2018-07-02");

        //薪资发放时段 grant_time 薪资发放时段:1-上午，2-下午
        currentEmployeePO.setGrantTime(1);
        newestEmployeePO.setGrantTime(2);

        supplierSubTaskServiceImpl.compareAndUpdateEmployeeNewestInfo(currentEmployeePO, newestEmployeePO);
    }
}
