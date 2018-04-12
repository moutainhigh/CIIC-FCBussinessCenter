package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/12/7.
 * @author jiangtianning
 */
@RestController
public class BaseController {

    @Autowired
    protected CodeGenerator codeGenerator;

    public Hashtable<String,String> getManagement(){
        Hashtable<String,String> hashtable = new Hashtable<>();
        hashtable.put("GL170001","蓝天科技");

        hashtable.put("glf-0009","微软中国（上海）");
        hashtable.put("glf-00091","微软中国（北京）");
        hashtable.put("glf-00092","微软中国（深圳）");
        hashtable.put("ymx-0001","亚马逊（上海）");
        hashtable.put("ymx-0002","亚马逊（北京）");
        hashtable.put("ymx-0003","亚马逊（深圳）");
        return hashtable;
    }

    public Hashtable<Integer,String> getDepartment(){
        Hashtable<Integer,String> hashtable = new Hashtable<>();
        hashtable.put(1,"研发部");
        hashtable.put(2,"市场部");
        hashtable.put(3,"销售部");
        hashtable.put(4,"公关部");
        return hashtable;
    }

    public Hashtable<Integer,String> getPosition(){
        Hashtable<Integer,String> hashtable = new Hashtable<>();
        hashtable.put(1,"测试工程");
        hashtable.put(2,"开发工程师");
        hashtable.put(3,"架构师");
        hashtable.put(4,"研发经理");
        hashtable.put(6,"产品");
        hashtable.put(4,"研发经理");
        return hashtable;
    }


//    public Hashtable<String,String> getPayrollGroup(){
//        Hashtable<String,String> hashtable = new Hashtable<>();
//        hashtable.put("glf-0009","XZZ-glf-00090-00046");
//        hashtable.put("glf-00091","XZZ-glf-00091-00047");
//        hashtable.put("glf-00092","XZZ-glf-00092-00048");
//        hashtable.put("ymx-0001","XZZ-ymx-0001-00049");
//        hashtable.put("ymx-0002","XZZ-ymx-0002-00050");
//        hashtable.put("ymx-0003","XZZ-ymx-0003-00051");
//        hashtable.put("GL170001","XZZ-GL170001-00055");
//
//        return hashtable;
//    }
//
//    public List<PrPayrollItemPO> getItems(){
//      List<PrPayrollItemPO> items = new ArrayList<>();
//
//      PrPayrollItemPO itemPO = null;
//
//      itemPO = new PrPayrollItemPO();
//      itemPO.setItemName("连续工龄");
//      itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//      itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//      itemPO.setDefaultValueStyle(2);
//      itemPO.setDefaultValue("0.00");
//      itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//      itemPO.setCalPrecision(0);
//      items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("入职日期");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(3); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("离职日期");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(3); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("工龄");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("报税名");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(1); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("公司编号");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(1); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("国籍");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(1); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("基本薪资");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("职位薪资");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("职务薪资");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("年假结算A");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("劳务费");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("加班薪资基数");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("工作日加班小时数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("周末加班小时数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("节日加班小时数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("加班薪资");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("调整工资");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("语言津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("补发工资");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("其他加");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("其他扣");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("试用期扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("当月实际工作天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("缺勤天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("新进、离职折算扣款");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("最低工资补足");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("广州病假扣减基数");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("特殊补贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("郊区津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("民族津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("激励津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("派驻津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("技术津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("见习津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("综合津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("私车公用补贴基数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("离职结算私车公用补贴缺勤天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("私车公用补贴");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("一次性福利");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("四班三运转津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("出差津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("早班");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("中班");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("晚班");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("班值津贴");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("生理假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("生理假津贴");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("全勤奖");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("培训生津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("交通津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("职务津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("担当津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("劳务工津贴");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("宿舍扣款");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("迟到早退累计小时数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("迟到早退扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("上半年奖金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("下半年奖金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("销售上半年奖金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("销售下半年奖金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("年度奖金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("年假结算E");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("销售月奖");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("特别奖金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("事假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("产假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("缺勤小时数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("事假产假旷工扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("病假扣除比例");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("病假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("长病假扣除比例");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("长病假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("病假扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("丧假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("婚假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("工伤天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("婚丧假工伤扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("产前假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("哺乳假天数");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("产前假哺乳假扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("缺勤扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("工会费");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("工会会员");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("高温费");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("实发负数调零项");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("应发工资");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣养老金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("调整养老金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("基本养老保险费合计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣失业金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("调整失业金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("失业保险费合计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣社保小计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣公积金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("调整公积金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣补充公积金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("调整补充公积金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣补充公积金小计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("扣公积金小计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("住房公积金合计（报税用）");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("四金合计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("计税不发补充公积金-个人部分");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("计税不发补充公积金-公司部分");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("工资调整");
//        itemPO.setItemType(2); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("薪金个税");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("薪金税调整");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("劳务个税");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("年终奖税");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("离职金");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("离职金税");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("个税合计");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("税后报销");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("大额大病医疗扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("税后扣款（日本人房租）");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("税后扣款（产假工资）");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("税后扣款（其他扣款）");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("税后扣款");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("独生子女奖励费");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("实发工资");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//        itemPO = new PrPayrollItemPO();
//        itemPO.setItemName("实发");
//        itemPO.setItemType(3); /**1: 固定项 2：输入项  3：计算项*/
//        itemPO.setDataType(2); /**1: 文本 2：数字  3：日期 4：布尔*/
//        itemPO.setDefaultValueStyle(2);
//        itemPO.setDefaultValue("0.00");
//        itemPO.setDecimalProcessType(1);/**1: 四舍五入 2：简单去位*/
//        itemPO.setCalPrecision(0);
//        items.add(itemPO);
//
//
//
//
//      return items;
//    }
}
