package com.ciicsh.gt1.fcbusinesscenter.compute.Controller;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.entity.BatchContext;
import com.ciicsh.caldispatchjob.entity.DroolsContext;
import com.ciicsh.caldispatchjob.entity.EmpPayItem;
import com.ciicsh.caldispatchjob.entity.FuncEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Created by bill on 18/5/25.
 */
@RestController
public class TestController {

    @Autowired
    private ComputeServiceImpl computeService;

    /**
     * 最低工资标准
     *
     * @param city 城市
     * @return 城市对应的工资标准
     */
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
     * @param firstWorkDate 首次参加工作日期w
     * @param leaveDate     离职日期
     * @return 连续工龄
     */
    @PostMapping("/api/successiveWorkAgeFireRules")
    public String successiveWorkAgeFireRules(@RequestParam String firstWorkDate, @RequestParam String leaveDate) {
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
        payItems.put("离职日期", leaveDate); //薪资项名称 和 值
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
     * @param leaveDate   离职日期
     * @return 工龄
     */
    @PostMapping("/api/seniorityFireRules")
    public String seniorityFireRules(@RequestParam String onboardDate, @RequestParam String leaveDate) {
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
        payItems.put("离职日期", leaveDate); //薪资项名称 和 值
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
     * @param nation 国家
     * @return 国家对应的免抵额
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
     * 本月日历天数
     *
     * @return 计薪月的日历天数
     */
    @PostMapping("/api/daysOfMonthFireRules")
    public String daysOfMonthFireRules() {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("本月日历天数");
        context.getFuncEntityList().add(funcEntity);
        // 设置计薪周期
        BatchContext batchContext = new BatchContext();
        batchContext.setPeriod("201806");
        context.setBatchContext(batchContext);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("本月日历天数", 0); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("本月日历天数");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 本月计薪天数
     *
     * @return 计薪月的计薪天数
     */
    @PostMapping("/api/paidDaysOfMonthFireRules")
    public String paidDaysOfMonthFireRules() {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("本月计薪天数");
        context.getFuncEntityList().add(funcEntity);
        // 设置计薪周期
        BatchContext batchContext = new BatchContext();
        batchContext.setPeriod("201806");
        context.setBatchContext(batchContext);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("本月计薪天数", 0); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("本月计薪天数");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 实际工作年限数
     *
     * @param dateOnBoard 入职日期
     * @param leaveDate   离职日期
     * @return 实际工作年限数
     * <p>
     * >0&&<=0.5 0.5
     * >0.5&&<=1 1.0
     * >1&&<=1.5 1.5
     * >11.5&&<=12 12.0
     * >12&&<=12.5 12.5
     */
    @PostMapping("/api/actualWorkingYearsFireRules")
    public String actualWorkingYearsFireRules(@RequestParam String dateOnBoard, @RequestParam String leaveDate) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("实际工作年限数");
        List<String> list = new ArrayList<>();
        list.add("入职日期");
        list.add("离职日期");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置计薪周期
        BatchContext batchContext = new BatchContext();
        batchContext.setPeriod("201806");
        context.setBatchContext(batchContext);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("入职日期", dateOnBoard); //薪资项名称 和 值
        payItems.put("离职日期", leaveDate); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("实际工作年限数");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 工龄折算率
     *
     * @param successiveService 工作年限_连续工龄
     * @return 工龄折算率
     */
    @PostMapping("/api/workAgeConversionRateFireRules")
    public String workAgeConversionRateFireRules(@RequestParam double successiveService) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("工龄折算率");
        List<String> list = new ArrayList<>();
        list.add("工作年限_连续工龄");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("工作年限_连续工龄", successiveService); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("工龄折算率");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 长病假工龄折算率
     *
     * @param successiveService 工作年限_连续工龄
     * @return 长病假工龄折算率
     */
    @PostMapping("/api/longSickDaysWorkAgeConversionRateFireRules")
    public String longSickDaysWorkAgeConversionRateFireRules(@RequestParam double successiveService) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("长病假工龄折算率");
        List<String> list = new ArrayList<>();
        list.add("工作年限_连续工龄");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("工作年限_连续工龄", successiveService); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("长病假工龄折算率");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 劳务税
     *
     * @param serviceFee 劳务费
     * @return 劳务税
     */
    @PostMapping("/api/serviceFeeFireRules")
    public String serviceFeeFireRules(@RequestParam Double serviceFee) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("劳务税");
        List<String> list = new ArrayList<>();
        list.add("劳务费");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("劳务费", serviceFee); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("劳务税");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 病假扣除比例
     *
     * @param city              城市
     * @param workAge           工龄
     * @param successiveWorkAge 连续工龄
     * @param sickDays          病假天数
     * @param totalSickDays     当年度累计病假天数
     * @param baseOfSickLeave   病假基数
     * @param minimusWage       最低工资标准
     * @return 病假扣除比例
     */
    @PostMapping("/api/sickLeaveDeductionRatioFireRules")
    public String sickLeaveDeductionRatioFireRules(@RequestParam String city, @RequestParam Double workAge,
                                                   @RequestParam Double successiveWorkAge,
                                                   @RequestParam Double sickDays,
                                                   @RequestParam Double totalSickDays,
                                                   @RequestParam Double baseOfSickLeave,
                                                   @RequestParam Double minimusWage) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("病假扣除比例");
        List<String> list = new ArrayList<>();
        list.add("城市");
        list.add("工龄");
        list.add("连续工龄");
        list.add("病假天数");
        list.add("当年度累计病假天数");
        list.add("病假基数");
        list.add("最低工资标准");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("城市", city); //薪资项名称 和 值
        payItems.put("工龄", workAge); //薪资项名称 和 值
        payItems.put("连续工龄", successiveWorkAge); //薪资项名称 和 值
        payItems.put("病假天数", sickDays); //薪资项名称 和 值
        payItems.put("当年度累计病假天数", totalSickDays); //薪资项名称 和 值
        payItems.put("病假基数", baseOfSickLeave); //薪资项名称 和 值
        payItems.put("最低工资标准", minimusWage); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("病假扣除比例");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 长病假扣除比例
     *
     * @param city              城市
     * @param workAge           工龄
     * @param successiveWorkAge 连续工龄
     * @return 长病假扣除比例
     */
    @PostMapping("/api/longSickLeaveDeductionRatioFireRules")
    public String longSickLeaveDeductionRatioFireRules(@RequestParam String city, @RequestParam Double workAge,
                                                       @RequestParam Double successiveWorkAge) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("长病假扣除比例");
        List<String> list = new ArrayList<>();
        list.add("城市");
        list.add("工龄");
        list.add("连续工龄");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("城市", city); //薪资项名称 和 值
        payItems.put("工龄", workAge); //薪资项名称 和 值
        payItems.put("连续工龄", successiveWorkAge); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("长病假扣除比例");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 实际工龄
     *
     * @param dateOnBoard 入职日期
     * @param leaveDate   离职日期
     * @return 实际工龄
     */
    @PostMapping("/api/actualLengthOfServiceFireRules")
    public String actualLengthOfServiceFireRules(@RequestParam String dateOnBoard, @RequestParam String leaveDate) {
        DroolsContext context = new DroolsContext();

        // 设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("实际工龄");
        List<String> list = new ArrayList<>();
        list.add("入职日期");
        list.add("离职日期");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        // end

        // 设置计薪周期
        BatchContext batchContext = new BatchContext();
        batchContext.setPeriod("201806");
        context.setBatchContext(batchContext);
        // end

        // 设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("入职日期", dateOnBoard); //薪资项名称 和 值
        payItems.put("离职日期", leaveDate); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        // end

        // 设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("实际工龄");
        // end

        // 触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 一般个人所得税
     *
     * @param taxableIncome 应纳税所得额
     * @return 一般个人所得税
     */
    @PostMapping("/api/generalIncomeTaxFireRules")
    public String generalIncomeTaxFireRules(@RequestParam Double taxableIncome) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("一般个人所得税");
        List<String> list = new ArrayList<>();
        list.add("应纳税所得额");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("应纳税所得额", taxableIncome); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("一般个人所得税");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 税率
     *
     * @param taxableIncome 应纳税所得额
     * @return 税率
     */
    @PostMapping("/api/taxRateFireRules")
    public String taxRateFireRules(@RequestParam Double taxableIncome) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("税率");
        List<String> list = new ArrayList<>();
        list.add("应纳税所得额");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("应纳税所得额", taxableIncome); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("税率");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

    /**
     * 速扣数
     *
     * @param taxableIncome 应纳税所得额
     * @return 速扣数
     */
    @PostMapping("/api/speedButtonNumFireRules")
    public String speedButtonNumFireRules(@RequestParam Double taxableIncome) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("速扣数");
        List<String> list = new ArrayList<>();
        list.add("应纳税所得额");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("应纳税所得额", taxableIncome); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("速扣数");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }


    public static void main(String[] args) {

        getYears("", "");
    }

    private static BigDecimal getYears(String dateOnBoard, String leaveDate) {
        // 离职－入职（精确到天）
        //String dateOnBoard = String.valueOf($context.getItemValByCode(para1)); // 入职日期
        //String leaveDate = String.valueOf($context.getItemValByCode(para2)); // 离职日期
        dateOnBoard = "2018-01-01";
        leaveDate = "2019-01-01";

        LocalDate localDateBegin = LocalDate.parse(dateOnBoard);
        LocalDate localDateEnd = LocalDate.now();
        // 离职日期不为空，天数差=离职-入职
        if (StringUtils.isNotBlank(leaveDate)) {
            localDateEnd = LocalDate.parse(leaveDate);
        } else {
            // 离职日期为空，天数差=计薪月最后一天-入职
            // String period = $context.getBatchContext().getPeriod();
            String period = "201801";
            StringBuilder builder = new StringBuilder();
            period = builder.append(period.substring(0, 4)).append("-").append(period.substring(4)).append("-01").toString();
            // 计薪月第一天,格式为yyyy-MM-01
            LocalDate periodBegin = LocalDate.parse(period);
            // 计薪月最后一天,格式为yyyy-MM-dd
            localDateEnd = periodBegin.with(TemporalAdjusters.lastDayOfMonth());
        }
        // 得到入职和离职(或者是计薪月最后一天)的天数差
        long days = ChronoUnit.DAYS.between(localDateBegin, localDateEnd);
        System.out.println("days:" + days);
        BigDecimal workYears = new BigDecimal(days).divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP);
        System.out.println("workYears:" + workYears);

        return workYears;
    }


    @PostMapping("/api/personalTax")
    public String personalTax(@RequestParam Double paras) {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("速扣数");
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("应纳税所得额");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("应纳税所得额", paras); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("速扣数");
        //end

        //触发规则
        computeService.fire(hashSet, context);

        return String.valueOf(funcEntity.getResult());
    }

}
