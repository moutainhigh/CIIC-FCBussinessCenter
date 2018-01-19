package com.ciicsh.caldispatchjob.compute.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;


/**
 * Created by jiangtianning on 2017/8/29.
 */
@Service
public class Calculation {



    private final static String PAY_ITEM_REGEX = "\\[([^\\[\\]]+)\\]";
    private final static String FUNCTION_REGEX = "\\{([^\\{\\}]+)\\}";


    private final StampedLock sl = new StampedLock();

    /*
        NOTICE: 所有参与计算的数字必须转化为double,扣减项目必须前面加负号
    * */
    public List<Object> doAction() {

        long startTime = System.currentTimeMillis();   //获取开始时间

        ArrayList<Object> employees = null;
        int sum = employees.size();
        int SIZE = 1000;
        int BATCH_SIZE = sum % SIZE == 0 ? sum/SIZE :(sum/SIZE + 1);

        //kieSession.setGlobal("utility", "");
        //kieSession.setGlobal("arith", "");

        /*StreamOpt.doPartition(employees.stream(),SIZE,BATCH_SIZE)
                .parallel()
                .forEach(
                        bath -> doComputeList(bath)
                );

        StreamOpt.partition(employees.stream(),SIZE).parallel().forEach(e->doCompute(e));

        StreamOpt.doPartition(employees.stream(),SIZE).parallel().forEach(e -> doComputeList(e));

        long gapTime = System.currentTimeMillis() - startTime; //获取结束时间

        System.out.println("cost time : " + gapTime);*/

        return employees;
    }

    /*
    //计算每个雇员薪资
    private void doCompute(Object employee){

        long stamp = sl.writeLock();
        try {
            //处理排序好的薪资计算项目列表（一般计算项，复杂计算项）
            employee.getClass()
                    .stream()
                    .filter(item -> item.getItemType() == PayItemEnum.COMMON || item.getItemType() == PayItemEnum.COMPLEX)
                    .forEach(item -> {
                        if (item.getItemType() == PayItemEnum.COMMON)
                            processSimpleCompute(item, employee);
                        else
                            processComplexCompute(item, employee);
                    });
        }
        finally {
            sl.unlockWrite(stamp);
        }

    }

    private void doComputeList(List<Object> employees) {

        long stamp = sl.writeLock();
        try {
            employees.stream().forEach(emp -> {
                emp.getPayItems()
                        .stream()
                        .filter(item -> item.getPayItemType() == PayItemTypeEnum.CALC.getValue())
                        .forEach(item -> {
                            processCompute(item,emp);
                        });
            });
        }
        finally {
            sl.unlockWrite(stamp);
        }
    }

    // 进行计算
    private void processCompute(PayItem item, Employee emp){

        BigDecimal result = BigDecimal.ZERO;
        if (item.getPayItemType() != PayItemTypeEnum.CALC.getValue()){
            System.out.println("这不是一个计算项 " + item.getName());
            return;
        }

        String[] formulas = item.getFormula().split(",");
        // 内存中拿到解析过条件的薪资项
        item = payItemServiceImpl.getPayItemFromCache(item.getCode());
        String condition = item.getResolvedCondition();
        boolean conditionFlg = condition.length() != 0;
        String[] conditions = {};
        if (conditionFlg) {
            conditions = condition.split("else");
        }

        if (formulas.length == 0) {
            System.out.println("这个计算项没有设置公式：" + item.getName());
            return;
        } else {
            List<String> formulaResultList = new ArrayList<>();
            for (int i=0; i<formulas.length; i++) {
                BigDecimal tmpResult = resolveFormula(formulas[i], emp);
//                formulaResultList.add(tmpResult);
                if (conditionFlg) {
                    String tmpCondition = replaceItem(conditions[i]);
                    Map<String, String> map = new HashMap<>();
                    map.put("exec", tmpResult.toString());
                    StrSubstitutor sub = new StrSubstitutor(map);
                    String partOfQuery = sub.replace(tmpCondition);
                    formulaResultList.add(partOfQuery);
                }
            }
            // 对所有条件分支进行拼接
            String finalResult = String.join(" else ", formulaResultList);

            try {
                // 使用js引擎计算表达式
                result = JavaScriptEngine.compute(finalResult);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        //TODO 插入薪资项的最终计算值
        item.setItemValue(result);
    }

    //处理复杂计算
    private void processComplexCompute(PayItem item, Employee e){

        try {
            String ruleName = item.getRuleName();
            FactHandle f =  kSession.insert(e);
            int count = kSession.fireAllRules(new RuleNameMatchesAgendaFilter(ruleName));
            kSession.delete(f);
            System.out.printf("\n fired " + String.valueOf(count) + " ; emp-code : "+ e.getCode() + " " + ruleName +" : " + item.getItemValue());
        }
        catch (Exception ex){
            System.out.printf("\n employee code: %s, rule nanme %s, payitem value %s, reason: %s",
                    e.getCode(),item.getRuleName(), String.valueOf(item.getItemValue()), ex.getMessage());
        }

    }

    private double fireRule(String ruleName,Employee e){
        try {
            FactHandle f =  kSession.insert(e);
            int count = kSession.fireAllRules(new RuleNameMatchesAgendaFilter(ruleName));
            kSession.delete(f);
            System.out.printf("\n fired " + String.valueOf(count) + " ; emp-code : "+ e.getCode() + " " + ruleName +" : ");
        }
        catch (Exception ex){
            //System.out.printf("\n employee code: %s, rule nanme %s, payitem value %s, reason: %s",
               //     e.getCode(),item.getRuleName(), String.valueOf(item.getItemValue()), ex.getMessage());
        }
        return 0.00;
    }

    //处理简单计算
    private BigDecimal processSimpleCompute(PayItem item, Employee e){

        return null;
    }

    // 解析公式得到计算值
    private BigDecimal resolveFormula(String formula, Employee emp){

        BigDecimal result = BigDecimal.ZERO;
        formula = replaceFuntion(replaceItem(formula), emp);

        try {
            // 使用js引擎计算表达式
            result = JavaScriptEngine.compute(formula);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 匹配所有薪资项名,函数名
    private List<String> getAllPayItems(String formula, String regex){

        List<String> names = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(formula);
        while(m.find()) {
            names.add(m.group(1));
        }
        return names;

    }

    // 替换薪资项
    private String replaceItem(String query) {

        List<String> payItems = getAllPayItems(query, PAY_ITEM_REGEX);

        // 替换薪资项
        if (payItems.size() != 0) {
            for (String itemName : payItems) {
                String value = DataMgr.getOrderedItemList()
                        .parallelStream()
                        .filter(item -> item.getName().equals(itemName))
                        .map(item -> item.getItemValue().toString())
                        .findAny()
                        .orElse("");

                query = query.replace("[" + itemName + "]", value);
            }
        }

        return query;
    }

    //替换函数
    private String replaceFuntion(String query, Employee emp) {

        List<String> functions = getAllPayItems(query, FUNCTION_REGEX);

        // 替换函数
        if (functions.size() != 0) {
            for (String functionName : functions) {
                PrFunction prFunction = DataMgr.getFunctions()
                        .parallelStream()
                        .filter(func -> func.getFunctionCNName().equals(functionName))
                        .findAny()
                        .orElse(null);

                if (prFunction == null) {
                    System.out.println("函数 " + functionName + " 不存在");
                    continue;
                }

                String ruleName = prFunction.getRuleName();
                BigDecimal value = new BigDecimal(fireRule(ruleName,emp));
                query = query.replace("{" + functionName + "}", value.toString());
            }
        }

        return query;
    }
    */

}
