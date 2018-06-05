package com.ciicsh.gt1.fcbusinesscenter.compute.Controller;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.entity.DroolsContext;
import com.ciicsh.caldispatchjob.entity.EmpPayItem;
import com.ciicsh.caldispatchjob.entity.FuncEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by bill on 18/5/25.
 */
@RestController
public class TestController {

    @Autowired
    private ComputeServiceImpl computeService;

    @PostMapping("/api/fireRules")
    public String fireRules(@RequestParam String city) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("最低工资标准");
        List<String> list = new ArrayList<>();
        list.add("城市");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("城市", city); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("最低工资标准");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 连续工龄
     *
     * @param firstWorkDate 首次参加工作日期
     * @param tremDate      离职日期
     * @return
     */
    @PostMapping("/api/successiveSeniorityFireRules")
    public String successiveSeniorityFireRules(@RequestParam String firstWorkDate, @RequestParam String tremDate) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("连续工龄");
        List<String> list = new ArrayList<>();
        list.add("首次参加工作日期");
        list.add("离职日期");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("首次参加工作日期", firstWorkDate); //薪资项名称 和 值
        payItems.put("离职日期", tremDate); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("连续工龄");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 工龄
     *
     * @param onboardDate 入职日期
     * @param tremDate    离职日期
     * @return
     */
    @PostMapping("/api/seniorityFireRules")
    public String seniorityFireRules(@RequestParam String onboardDate, @RequestParam String tremDate) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("工龄");
        List<String> list = new ArrayList<>();
        list.add("入职日期");
        list.add("离职日期");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("入职日期", onboardDate); //薪资项名称 和 值
        payItems.put("离职日期", tremDate); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("工龄");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 免抵额
     *
     * @param nation    国家
     * @return
     */
    @PostMapping("/api/freeAmountFireRules")
    public String freeAmountFireRules(@RequestParam String nation) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("免抵额");
        List<String> list = new ArrayList<>();
        list.add("国家");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("国家", nation); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("免抵额");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }


    /**
     * 偶然所得税
     *
     * @param accidentTax
     * @return
     */
    @PostMapping("/api/accidentTaxFireRules")
    public String accidentTaxFireRules(@RequestParam BigDecimal accidentTax) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("偶然所得税");
        List<String> list = new ArrayList<>();
        list.add("偶然所得税");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("偶然所得税", accidentTax); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("偶然所得税");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }


    /**
     * 股票期权税
     *
     * @param para1 入职日期
     * @param para2    离职日期
     * @return
     */
    @PostMapping("/api/taxFireRules")
    public String taxFireRules(@RequestParam long para1, @RequestParam long para2) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("股票期权税");
        List<String> list = new ArrayList<>();
        list.add("应纳股票期权税");
        list.add("已纳股票期权税");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("应纳股票期权税", para1); //薪资项名称 和 值
        payItems.put("已纳股票期权税", para2); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("股票期权税");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

}
