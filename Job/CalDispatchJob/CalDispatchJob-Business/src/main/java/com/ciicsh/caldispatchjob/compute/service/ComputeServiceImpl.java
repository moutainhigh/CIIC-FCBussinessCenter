package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.caldispatchjob.compute.messageBus.KafkaSender;
import com.ciicsh.caldispatchjob.compute.util.CustomAgendaFilter;
import com.ciicsh.caldispatchjob.compute.util.JavaScriptEngine;
import com.ciicsh.caldispatchjob.entity.Arith;
import com.ciicsh.caldispatchjob.entity.DroolsContext;
import com.ciicsh.caldispatchjob.entity.EmpPayItem;
import com.ciicsh.caldispatchjob.entity.FuncEntity;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DecimalProcessTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrAdjustBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrBackTrackingBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.mongodb.DBObject;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.mvel2.util.Make;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bill on 17/12/31.
 */
@Service
public class ComputeServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchServiceImpl.class);

    private final static Pattern PAY_ITEM_REGEX = Pattern.compile("\\[([^\\[\\]]+)\\]");
    private final static Pattern FUNCTION_REGEX = Pattern.compile("\\{([^\\{\\}]+)\\}");
    private final static Pattern DIGEST_REGEX = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    private final static Pattern PARAMETER_REGEX = Pattern.compile("\\(([^\\[\\]]+)\\)");


    private final static String FUNCTION_LEFT_SIDE_PREFIX = "{";
    private final static String FUNCTION_RIGHT_SIDE_PREFIX = "}";

    private final static String CONDITION_FOMULAR_SIPE = ";";

    private final static String CONDITION_FUNCTION_SPLIT = "_";



    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.ciicsh.gt1", artifactId = "payroll_common_function", version = "1.0.0-SNAPSHOT")
    private KieSession kSession;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    @Autowired
    private PrAdjustBatchMapper adjustBatchMapper;


    @Autowired
    private PrBackTrackingBatchMapper backTrackingBatchMapper;


    @Autowired
    private KafkaSender sender;

    public void processCompute(String batchCode,int batchType) throws Exception{

        DroolsContext context = new DroolsContext();

        List<DBObject> batchList = null;
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.list(
                    Criteria.where("batch_code").is(batchCode)
                            .and("catalog.emp_info.is_active").is(true)
            );
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.list(
                    Criteria.where("batch_code").is(batchCode)
                            .and("catalog.emp_info.is_active").is(true)
            );
        }else {
            batchList = backTraceBatchMongoOpt.list(
                    Criteria.where("batch_code").is(batchCode)
                            .and("catalog.emp_info.is_active").is(true)
            );
        }

        batchList.stream().forEach(dbObject -> {
            DBObject calalog = (DBObject)dbObject.get("catalog");
            //DBObject empInfo = (DBObject)calalog.get("emp_info");
            String empCode = (String) dbObject.get(PayItemName.EMPLOYEE_CODE_CN);
            List<DBObject> items = (List<DBObject>)calalog.get("pay_items");

            //set drools context
            EmpPayItem empPayItem = new EmpPayItem();
            empPayItem.setItems(listToHashMap(items));
            empPayItem.setEmpCode(empCode);
            context.setEmpPayItem(empPayItem);

            context.getFuncEntityList().clear(); // set each employee function result null
            //end

            items.forEach(item -> {
                int itemType = item.get("item_type") == null ? 0 : (int)item.get("item_type"); // 薪资项类型

                //处理计算项
                if(itemType == ItemTypeEnum.CALC.getValue()){
                    int dataType = item.get("data_type") == null ? 1: (int)item.get("data_type"); // 数据格式: 1-文本,2-数字,3-日期,4-布尔
                    int calPriority = (int)item.get("cal_priority");
                    String itemName = item.get("item_name") == null ? "" : (String) item.get("item_name");
                    logger.info(String.format("薪资项目－%s | 计算优先级－%d",itemName,calPriority));

                    int processType = item.get("decimal_process_type") == null ? DecimalProcessTypeEnum.ROUND.getValue() : (int)item.get("decimal_process_type");//小数处理方式 1 - 四舍五入 2 - 简单去位

                    String condition = item.get("item_condition") == null ? "" : (String)item.get("item_condition");//计算条件
                    String formulaContent = item.get("formula_content") == null ? "" : (String)item.get("formula_content"); //计算公式
                    if(StringUtils.isEmpty(formulaContent)) return;

                    condition = Special2Normal(condition); //特殊字符转化
                    formulaContent = Special2Normal(formulaContent); //特殊字符转化
                    String conditionFormula = replaceFormula(condition,formulaContent,context);//处理计算项的公式
                    String finalResult = replacePayItem(conditionFormula,PAY_ITEM_REGEX,context);//处理薪资项的公式
                    try {
                        BigDecimal computeResult = JavaScriptEngine.compute(finalResult); // 执行JS 方法

                        Object result = null;
                        if(processType == DecimalProcessTypeEnum.ROUND_DOWN.getValue()){ // 简单去位
                            result = Arith.round(computeResult.doubleValue(),0);

                        }else { // 四舍五入
                            result = Arith.round(computeResult.doubleValue(),2);
                        }
                        item.put("item_value",result);
                        context.getEmpPayItem().getItems().put(itemName,result); //设置上下文计算项的值

                    }catch (ScriptException se){
                        logger.error(String.format("雇员编号－%s | 计算失败－%s",item.get(PayItemName.EMPLOYEE_CODE_CN),se.getMessage()));
                    }
                }
            });

            if(batchType == BatchTypeEnum.NORMAL.getValue()) {
                normalBatchMongoOpt.batchUpdate(
                        Criteria.where("batch_code").is(batchCode)
                                .and("catalog.emp_info.is_active").is(true)
                                .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                        "catalog.pay_items",items);

            }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
                adjustBatchMongoOpt.batchUpdate(
                        Criteria.where("batch_code").is(batchCode)
                                .and("catalog.emp_info.is_active").is(true)
                                .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                        "catalog.pay_items",items);


            }else {
                backTraceBatchMongoOpt.batchUpdate(
                        Criteria.where("batch_code").is(batchCode)
                                .and("catalog.emp_info.is_active").is(true)
                                .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode),
                        "catalog.pay_items",items);
            }

        });

        int rowAffected = 0;
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = normalBatchMapper.updateBatchStatus(batchCode, BatchStatusEnum.COMPUTED.getValue(), "system");
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchMapper.updateBatchStatus(batchCode, BatchStatusEnum.COMPUTED.getValue(), "system");

        }else {
            rowAffected = backTrackingBatchMapper.updateBatchStatus(batchCode, BatchStatusEnum.COMPUTED.getValue(), "system");
        }
        if(rowAffected > 0) { // 数据库更新成功后发送消息
            ComputeMsg computeMsg = new ComputeMsg();
            computeMsg.setBatchCode(batchCode);
            computeMsg.setBatchType(batchType);
            computeMsg.setComputeStatus(BatchStatusEnum.COMPUTED.getValue()); // send kafka compute's complete status
            sender.SendComputeStatus(computeMsg);
        }

    }

    /**
     * transfer List to HashMap
     * @param payItems
     * @return
     */
    private HashMap<String, Object> listToHashMap(List<DBObject> payItems){

        HashMap<String, Object> map = payItems.stream().collect(
                HashMap<String, Object>::new,
                (m,c) -> m.put((String)c.get("item_name"), c.get("item_value")),
                (m,u)-> {}
                );

        return map;
    }

    /**
     * 找出formulaContent包含函数名称的列表 -- 函数存在规则引擎里面
     * @param formulaContent example: [a]+[c]; [d]*[f] -- a,c,d,f 可能含有函数
     * @return 该公式里面包含函数名称的列表
     */
    private List<FuncEntity> getCommonFuncParams(String formulaContent, Pattern pattern){
        List<FuncEntity> funcs = new ArrayList<>();
        Matcher m = pattern.matcher(formulaContent);
        FuncEntity funcEntity = null;
        while(m.find()) {
            funcEntity = new FuncEntity();
            funcEntity.setFuncName(m.group(1));
            funcs.add(funcEntity);
        }
        return funcs;
    }

    /**
     * 替换公式值：函数名称替换成函数值
     * @param formulaContent   [a]+[c]; [d]*[f]
     * @param context  包含 当前雇员函数列表 与 当前雇员薪资项列表
     * @return
     */
    private String getFormulaContent(String condition, String formulaContent, DroolsContext context){

        condition = StringUtils.removeEnd(condition,CONDITION_FOMULAR_SIPE);      //去除字符窜最后;
        formulaContent = StringUtils.removeEnd(formulaContent,CONDITION_FOMULAR_SIPE); //去除字符窜最后;

        String[] conditions = condition.split(CONDITION_FOMULAR_SIPE);
        String[] formulas = formulaContent.split(CONDITION_FOMULAR_SIPE);

        String condition_formula = null;

        if(formulas == null || formulas.length == 0){ // 无条件
            logger.error(String.format("emp_code is %s no function", context.getEmpPayItem().getEmpCode()));
            return "";
        } else if((conditions == null || (conditions.length == 1 && conditions[0].indexOf(CONDITION_FOMULAR_SIPE) < 0)) && formulas.length ==1){ // IF ELSE 单个条件执行
            condition_formula = formulas[0]; // 执行结果运算中如果调用了函数，则把该函数替换成Drools规则触发的值
        }else {
            condition_formula = condition + CONDITION_FUNCTION_SPLIT + formulaContent;
        }
        FuncEntity funcEntity = null;
        Matcher m = FUNCTION_REGEX.matcher(condition_formula);
        while (m.find()) {
            funcEntity = new FuncEntity();
            String func = m.group(1); // 工龄([入职日期],[离职日期])
            func = func.replaceAll("\\(",",").replaceAll("\\)","")
                        .replaceAll("\\[","").replaceAll("\\]","");
            String[] funcs = func.split(",");
            String funcName = "";
            if(funcs.length > 1) { // 分析函数包含参数
                funcName = funcs[0];
                List<String> parameters = new ArrayList<>();
                for (int i = 1; i < funcs.length; i++) {
                    parameters.add(funcs[i]);
                }
                funcEntity.setParameters(parameters);
            }else { // 分析函数无参数
                funcName = func;
            }

            funcEntity.setFuncName(funcName);
            context.getFuncEntityList().add(funcEntity);
        }
        if (context.getFuncEntityList().size() == 0) { // 当前没有计算函数触发
            return condition_formula;
        }

        int fireCount = fireRules(context); //fire 规则引擎
        if (fireCount == 0) { // 无规则触发
            return condition_formula;
        }
        for (FuncEntity fun : context.getFuncEntityList()) {
            //公式的函数名 替换成 公式的函数值
            condition_formula = condition_formula.replace(FUNCTION_LEFT_SIDE_PREFIX + fun.toString() + FUNCTION_RIGHT_SIDE_PREFIX, fun.getResult().toString());
        }
        return condition_formula;

    }

    /**
     * 根据薪资项中计算项的条件和内容，拼接最终的计算结果
     * @param condition       example: [a] > 3, [b] == 4
     * @param formulaContent  example: [a]+[c], [d]*[f]
     * @return 最终结果的字符串
     */
    private String replaceFormula(String condition, String formulaContent, DroolsContext context){

        String formula_Content = getFormulaContent(condition,formulaContent,context);// 条件和执行结果运算中如果调用了函数，则把该函数替换成Drools规则触发的值

        if(StringUtils.isEmpty(formula_Content)){ // 无条件
            return "";
        }
        else { // 多个条件执行
            StringBuilder sb = new StringBuilder();
            String[] condition_formula = formula_Content.split(CONDITION_FUNCTION_SPLIT);
            if(condition_formula.length == 1){
                sb.append(condition_formula[0]); // 说明只有一个方法，没有执行条件
            }
            else { // 多个执行条件，多个方法，一个条件一个方法
                String[] conditions = condition_formula[0].split(CONDITION_FOMULAR_SIPE);//aa; split(';') 后 aa
                String[] formulas = condition_formula[1].split(CONDITION_FOMULAR_SIPE);
                if(formulas.length == 2 && conditions.length == 1){
                    String con = StringUtils.removeEnd(conditions[0],CONDITION_FOMULAR_SIPE);
                    sb.append("if ( " + con + " )");
                    sb.append("{ " + formulas[0] + " }");
                    sb.append(" else ");
                    sb.append(" { " + formulas[1] + " }");

                }else {
                    for (int i = 0; i < conditions.length; i++) {
                        sb.append("if ( " + conditions[i] + " )");
                        sb.append("{ " + formulas[i] + " }");
                    }
                }
            }
            return sb.toString();
        }
    }

    /**
     * 替换 公式薪资项名为薪资项的值
     * @param content
     * @param pattern
     * @return 最终公式结果
     */
    private String replacePayItem(String content, Pattern pattern, DroolsContext context){

        HashMap<String,Object> payItems = context.getEmpPayItem().getItems();
        Matcher m = pattern.matcher(content);
        while(m.find()) {
            String payItemName = m.group(1);
            Object val = payItems.get(payItemName);
            content = content.replace("[" + payItemName +"]",val == null ? "": Object2String(val));
        }
        return content;
    }

    /**
     * 对象转化字符串
     * @param obj
     * @return
     */
    private String Object2String(Object obj){
        if(obj instanceof String){
            Matcher m = DIGEST_REGEX.matcher((String)obj);
            while (m.find()) {
                return obj.toString();
            }
            return "\"" + obj.toString() + "\"";
        }
        return obj.toString();
    }

    /**
     * 根据正则表达式匹配触发一个或者多个规则名
     * @param context 函数名称列表对应规则名称
     * @return
     */
    private int fireRules(DroolsContext context){
        if(context.getFuncEntityList() ==null || context.getFuncEntityList().size() == 0){
            return 0;
        }
        Set set = new HashSet();
        for (FuncEntity func : context.getFuncEntityList()){
            set.add(func.getFuncName());
        }
        FactHandle factHandle =  kSession.insert(context);
        int count = kSession.fireAllRules(new CustomAgendaFilter(set));
        kSession.delete(factHandle);
        logger.info(String.format("emp_code: %s, total excute rule counts: %d", context.getEmpPayItem().getEmpCode(),count));
        return count;
    }

    /**
     * 常规计算中的符号替换：半角 转 全角
     * @param inputStr
     * @return
     */
    private String Special2Normal(String inputStr){
        inputStr = inputStr
                .replaceAll("［","[").replaceAll("］","]")
                .replaceAll("｛","{").replaceAll("｝","}")
                .replaceAll("（","(").replaceAll("）",")")
                .replaceAll("＋","+").replaceAll("－","-")
                .replaceAll("＊","*").replaceAll("／","/")
                .replaceAll("，",",");
        return inputStr;
    }


    public void fire(){
        //FactHandle f =  kSession.insert(e);
        int count = kSession.fireAllRules();
        //kSession.delete(f);
    }

}
