package com.ciicsh.caldispatchjob.compute.Cal;

import com.ciicsh.caldispatchjob.compute.util.CustomAgendaFilter;
import com.ciicsh.caldispatchjob.compute.util.JavaScriptEngine;
import com.ciicsh.caldispatchjob.entity.*;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.*;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrAdjustBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrBackTrackingBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import javax.script.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.StampedLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by bill on 17/12/31.
 */
@Service
public class ComputeServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(ComputeServiceImpl.class);

    private final static Pattern PAY_ITEM_REGEX = Pattern.compile("\\_\\]");//Pattern.compile("\\[([^\\[\\]]+)\\]");
    private final static Pattern FUNCTION_REGEX = Pattern.compile("\\{([^\\{\\}]+)\\}");
    private final static Pattern DIGEST_REGEX = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    private final static Pattern PARAMETER_REGEX = Pattern.compile("\\(([^\\[\\]]+)\\)");
    private final static Pattern DATE_REGEX_PATTERN = Pattern.compile("\\$\\d{4}[-.]\\d{1,2}[-.]\\d{1,2}\\$");
    private final static Pattern XZX_REGEX_PATTERN = Pattern.compile("(XZX_GL)\\d{7}(_)\\d{5}");

    private final static String FUNCTION_LEFT_SIDE_PREFIX = "{";
    private final static String FUNCTION_RIGHT_SIDE_PREFIX = "}";
    private final static String REPLACE_FUNC_PREFIX="func";

    private final static String CONDITION_FOMULAR_SIPE = ";";

    private final static String CONDITION_FUNCTION_SPLIT = "___";

    private final StampedLock sl = new StampedLock();


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
    private ComputeSender sender;

    public void processCompute(String batchCode,int batchType, String userId){

        long start = System.currentTimeMillis(); //begin

        List<DBObject> batchList = null;
        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN)
                .include(PayItemName.EMPLOYEE_COMPANY_ID)
                .include("catalog.batch_info.actual_period")
                .include("catalog.pay_items.item_type")
                .include("catalog.pay_items.data_type")
                .include("catalog.pay_items.cal_priority")
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
                .include("catalog.pay_items.canLock")
                .include("catalog.pay_items.isLocked")
                .include("catalog.pay_items.decimal_process_type")
                .include("catalog.pay_items.item_condition")
                .include("catalog.pay_items.formula_content")
                .include("catalog.pay_items.display_priority")
        ;

        //query = query.skip(pageindex*size).limit(size);

        int cores = Runtime.getRuntime().availableProcessors() - 1;
        final ForkJoinPool pool = new ForkJoinPool(cores);

        long start1 = System.currentTimeMillis(); //begin

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }
        logger.info("查询mogodb时间 : " + String.valueOf((System.currentTimeMillis() - start1)));

        List<DBObject> finalBatchList = batchList;

        try {
            List<DBObject> others = pool.submit(()-> finalBatchList.parallelStream().map(dbObject -> runCompute(dbObject,batchCode,batchType)).collect(Collectors.toList())).get();
            logger.info("get total: " + String.valueOf(others.size()));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        int rowAffected = 0;
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = normalBatchMapper.auditBatch(batchCode,"", BatchStatusEnum.COMPUTED.getValue(), "system","", null);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchMapper.auditBatch(batchCode, "", BatchStatusEnum.COMPUTED.getValue(), "system","",null);

        }else {
            rowAffected = backTrackingBatchMapper.auditBatch(batchCode,"", BatchStatusEnum.COMPUTED.getValue(), "system","", null);
        }
        if(rowAffected > 0) { // 数据库更新成功后发送消息
            ComputeMsg computeMsg = new ComputeMsg();
            computeMsg.setBatchCode(batchCode);
            computeMsg.setBatchType(batchType);
            computeMsg.setOptID(userId);
            computeMsg.setComputeStatus(BatchStatusEnum.COMPUTED.getValue()); // send kafka compute's complete status
            sender.SendComputeStatus(computeMsg);
        }

        long end = System.currentTimeMillis();

        //logger.info("get total: " + String.valueOf(others.size()));
        logger.info("actual total: " + String.valueOf(finalBatchList.size()));
        logger.info("获取计算核数量 cores " + String.valueOf(cores));


        batchList = null;
        logger.info("薪资计算总共时间 : " + String.valueOf((end - start)/1000.00) + " 秒");

    }

    private DBObject runCompute(DBObject dbObject,String batchCode,int batchType) {

        try {

            DroolsContext context = new DroolsContext();
            Map<String, CompiledScript> scripts = null;

            if (scripts == null) {
                scripts = new HashMap<>();
            }
            DBObject catalog = (DBObject) dbObject.get("catalog");
            String empCode = (String) dbObject.get(PayItemName.EMPLOYEE_CODE_CN);
            String companyId = (String) dbObject.get(PayItemName.EMPLOYEE_COMPANY_ID);

            List<DBObject> items = (List<DBObject>)catalog.get("pay_items");

            /*获取计算期间，并传递给drools context*/
            DBObject batch = (DBObject) catalog.get("batch_info");
            String period = batch.get("actual_period") == null ? "" : (String) batch.get("actual_period");
            BatchContext batchContext = new BatchContext();
            batchContext.setPeriod(period);
            context.setBatchContext(batchContext);
            //end

            Bindings bindings = new SimpleBindings();

            //set drools context
            EmpPayItem empPayItem = new EmpPayItem();
            empPayItem.setItems(bindings);
            empPayItem.setEmpCode(empCode);
            empPayItem.setCompanyId(companyId);
            context.setEmpPayItem(empPayItem); //用于规则引擎计算

            context.getFuncEntityList().clear(); // set each employee function result null
            //end

            CompiledScript compiled = null;

            for (DBObject item : items) {

                try {

                    //计算之前过滤掉 锁住的薪资项
                    boolean canLock = item.get("canLock") == null ? false : Boolean.valueOf(item.get("canLock").toString());
                    boolean isLocked = item.get("isLocked") == null ? false : Boolean.valueOf(item.get("isLocked").toString());

                    if(canLock && isLocked){
                        continue;
                    }

                    int dataType = item.get("data_type") == null ? 1 : (int) item.get("data_type"); // 数据格式: 1-文本,2-数字,3-日期,4-布尔

                    String itemCode = (String) item.get("item_code");

                    if (dataType == DataTypeEnum.NUM.getValue()) {
                        bindings.put(itemCode, new BigDecimal(String.valueOf(item.get("item_value") == null ? 0 : item.get("item_value")))); //设置导入项和固定项的值
                    } else {
                        bindings.put(itemCode, item.get("item_value")); //设置导入项和固定项的值
                    }

                    int itemType = item.get("item_type") == null ? 0 : (int) item.get("item_type"); // 薪资项类型

                    //处理计算项
                    if (itemType == ItemTypeEnum.CALC.getValue()) {

                        int calPriority = (int) item.get("cal_priority");
                        String itemName = item.get("item_name") == null ? "" : (String) item.get("item_name");
                        logger.info(String.format("薪资项目－%s | 计算优先级－%d", itemName, calPriority));

                        int processType = item.get("decimal_process_type") == null ? DecimalProcessTypeEnum.ROUND.getValue() : (int) item.get("decimal_process_type");//小数处理方式 1 - 四舍五入 2 - 简单去位

                        String condition = item.get("item_condition") == null ? "" : (String) item.get("item_condition");//计算条件
                        String formulaContent = item.get("formula_content") == null ? "" : (String) item.get("formula_content"); //计算公式
                        if (StringUtils.isEmpty(formulaContent)) continue;

                        condition = Special2Normal(condition); //特殊字符转化
                        formulaContent = Special2Normal(formulaContent); //特殊字符转化
                        String conditionFormula = replaceFormula(condition, formulaContent, context);//处理计算项的公式
                        try {
                            if (scripts.get(itemCode) == null) {
                                compiled = ((Compilable) JavaScriptEngine.getEngine()).compile(conditionFormula);
                                scripts.put(itemCode, compiled);
                            } else {
                                compiled = scripts.get(itemCode);
                            }

                            for (FuncEntity fun : context.getFuncEntityList()) {
                                //公式的函数名 替换成 公式的函数值
                                bindings.put(REPLACE_FUNC_PREFIX + fun.getFuncName(), fun.getResult());
                            }

                            Object compiledResult = compiled.eval(bindings); // run JS method

                            BigDecimal computeResult = context.getBigDecimal(compiledResult);
                            double result = 0.0;

                            if(compiledResult == null){
                                item.put("item_value", result);
                            }
                            else {
                                if (processType == DecimalProcessTypeEnum.ROUND_DOWN.getValue()) { // 简单去位
                                    result = Arith.round(computeResult.doubleValue(), 0);

                                } else { // 四舍五入
                                    result = Arith.round(computeResult.doubleValue(), 2);
                                }

                                if (StringUtils.isNotEmpty(itemName) && itemName.equals(PayItemName.EMPLOYEE_NET_PAY) && result < 0) {
                                    item.put("item_value", 0.0);
                                } else {
                                    item.put("item_value", result);
                                }
                            }
                            bindings.put(itemCode, result); // 设置计算项的值
                            context.getFuncEntityList().clear(); // 清除FIRE 过的函数

                        } catch (Exception se) {
                            context.getFuncEntityList().clear(); // 清除FIRE 过的函数
                            logger.error(String.format("雇员编号－%s | 计算失败－%s", empCode, se.getMessage()));
                        }

                    }
                }catch (Exception ex){
                    logger.info(ex.getMessage());
                }
            }

            int rowAffected = 0;
            Criteria criteria = Criteria.where("batch_code").is(batchCode)
                    .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode).and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId);
            Update update = Update.update("catalog.pay_items",items);
            Query query = Query.query(criteria);

            if (batchType == BatchTypeEnum.NORMAL.getValue()) {
                rowAffected = normalBatchMongoOpt.upsert(query, update);
            } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
                rowAffected = adjustBatchMongoOpt.upsert(query, update);;
            } else {
                rowAffected = backTraceBatchMongoOpt.upsert(query, update);;
            }
            logger.info("row affected " + rowAffected);
            return dbObject;
        }catch (Exception ex){
            logger.info(ex.getMessage());
        }

        return null;
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


        String[] conditions = condition.split(CONDITION_FOMULAR_SIPE);
        String[] formulas = formulaContent.split(CONDITION_FOMULAR_SIPE);

        String condition_formula = null;

        if(formulas.length == 0){ // 无条件
            logger.error(String.format("emp_code is %s no function", context.getEmpPayItem().getEmpCode()));
            return "";
        }else if(StringUtils.isEmpty(condition) && StringUtils.isNotEmpty(formulaContent)){ // 无条件执行
            condition_formula = formulaContent;
        }
        else if(conditions.length == 1  && formulas.length ==1){
            condition_formula = StringUtils.removeEnd(conditions[0],CONDITION_FOMULAR_SIPE) + CONDITION_FUNCTION_SPLIT + formulas[0];
        }else {
            condition_formula = StringUtils.removeEnd(condition,CONDITION_FOMULAR_SIPE) + CONDITION_FUNCTION_SPLIT + formulaContent;
        }
        FuncEntity funcEntity = null;
        Matcher m = FUNCTION_REGEX.matcher(condition_formula);
        while (m.find()) {
            funcEntity = new FuncEntity();
            String func = m.group(1);
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
                funcName = StringUtils.removeEnd(func,",");
            }

            if(!context.existFunction(funcName)) {
                funcEntity.setFuncName(funcName);
                context.getFuncEntityList().add(funcEntity);
            }

        }
        if (context.getFuncEntityList().size() == 0) { // 当前没有计算函数触发
            return condition_formula;
        }

        int fireCount = fireRules(context); //fire 规则引擎
        if (fireCount == 0) { // 无规则触发
            return condition_formula;
        }
        for (FuncEntity fun : context.getFuncEntityList()) {
            //公式名替换
            String funcExpress = FUNCTION_LEFT_SIDE_PREFIX + fun.toString() + FUNCTION_RIGHT_SIDE_PREFIX;
            condition_formula = condition_formula.replace(funcExpress, REPLACE_FUNC_PREFIX + fun.getFuncName());
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
            if(condition_formula.length == 1 && StringUtils.isEmpty(condition)){
                sb.append(condition_formula[0]); // 说明只有一个方法，没有执行条件
            }else if(condition_formula.length == 2){
                String formula = condition_formula[1];
                String cond = condition_formula[0];
                Matcher dateMatcher = DATE_REGEX_PATTERN.matcher(cond);
                if (dateMatcher.find()) {
                    Matcher xzxMatcher = XZX_REGEX_PATTERN.matcher(cond);
                    cond = processDateType(dateMatcher, xzxMatcher, cond);
                }

                int formulaCount = formula.split(CONDITION_FOMULAR_SIPE).length;
                int conditionCount = cond.split(CONDITION_FOMULAR_SIPE).length;
                if(conditionCount == 1 && formulaCount == 2){
                    sb.append("if (" + cond + ")");
                    sb.append("{ " + formula.split(CONDITION_FOMULAR_SIPE)[0] + " }");
                    sb.append(" else ");
                    sb.append("{ " + formula.split(CONDITION_FOMULAR_SIPE)[1] + " }");
                }
                else if(conditionCount == 1 && formulaCount == 1){
                    sb.append("if (" + cond + ")");
                    sb.append("{ " + formula + " }");
                }
                else { // 多个执行条件，多个方法，一个条件一个方法
                    String[] conditions = cond.split(CONDITION_FOMULAR_SIPE);
                    String[] formulas = formula.split(CONDITION_FOMULAR_SIPE);
                    if (conditions.length == formulas.length) { // 倒序实行条件
                        for (int i = conditions.length - 1; i >= 0; i--) {
                            if (i == conditions.length - 1) {
                                sb.append("if (" + conditions[i] + ")");
                                sb.append("{ " + formulas[i] + " }");
                            } else {
                                sb.append("else if (" + conditions[i] + ")");
                                sb.append("{ " + formulas[i] + " }");
                            }
                        }
                    } else if (conditions.length + 1 == formulas.length) { // 倒序实行条件
                        for (int i = conditions.length - 1; i >= 0; i--) {
                            if (i == conditions.length - 1) {
                                sb.append("if (" + conditions[i] + ")");
                                sb.append("{ " + formulas[i] + " }");
                            } else {
                                sb.append("else if (" + conditions[i] + ")");
                                sb.append("{ " + formulas[i] + " }");
                            }
                        }
                        sb.append(" else");
                        sb.append(" { " + formulas[conditions.length] + " }");
                    }
                }
            }
            return sb.toString();
        }
    }

    /**
     * 处理条件中日期类型
     * @param cond
     * @return
     */
    private String processDateType(Matcher dateMatcher, Matcher xzxMatcher, String cond){
        String result = "";
        if (dateMatcher.find()) {
            String ori = cond.substring(dateMatcher.start(), dateMatcher.end());
            String update = "new Date('" + ori.replaceAll("\\$","") + "')";
            result = cond.replace(ori, update);
        }
        //todo: 假设条件有日期型值的话，条件里所有薪资项都是日期型，以后可能要优化。11/12/2018
        if(xzxMatcher.find()) {
            String ori = result.substring(xzxMatcher.start(), xzxMatcher.end());
            String update = "new Date(" + ori + ")";
            result = result.replace(ori, update);
        }
        return result;
    }

    /**
     * 替换 公式薪资项名为薪资项的值
     * @param content
     * @param pattern
     * @return 最终公式结果
     */
    private void setPayItemBindings(String content, Pattern pattern, DroolsContext context,Bindings bindings){

        Matcher m = pattern.matcher(content);
        while(m.find()) {
            String itemName = m.group(1);
            Object val = context.getItemValByCode(itemName);
            //content = content.replace("[" + payItemName +"]",val == null ? "": Object2String(val));
            bindings.put(itemName, val);
        }
        //return content;
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
        long stamp = sl.writeLock();
        try {
            FactHandle factHandle = kSession.insert(context); // 插入上下文信息
            int count = kSession.fireAllRules(new CustomAgendaFilter(set));
            kSession.delete(factHandle);
            logger.info(String.format("emp_code: %s, total excute rule counts: %d", context.getEmpPayItem().getEmpCode(), count));
            return count;
        }catch (Exception ex){
            logger.info(ex.getMessage());
            context.getFuncEntityList().clear();
            return 0;
        }
        finally {
            sl.unlockWrite(stamp);
        }
    }

    /**
     * 常规计算中的符号替换：半角 转 全角
     * @param inputStr
     * @return
     */
    private String Special2Normal(String inputStr){
        if(StringUtils.isEmpty(inputStr)) return "";
        inputStr = inputStr
                .replaceAll("［","[").replaceAll("］","]")
                .replaceAll("｛","{").replaceAll("｝","}")
                //.replaceAll("（","(").replaceAll("）",")")
                .replaceAll("＋","+").replaceAll("－","-")
                .replaceAll("＊","*").replaceAll("／","/")
                .replaceAll("，",",").replaceAll("'","'")
                .replaceAll("“","\"");
        inputStr = StringUtils.removeEnd(inputStr,";").trim(); // 去除 ；号结尾
        return inputStr;
    }


    public void fire(HashSet hashSet, DroolsContext context){
        int count = 0;
        FactHandle factHandle = kSession.insert(context); // 插入上下文信息
        if(hashSet == null) {
            count = kSession.fireAllRules();
        }else {
            count = kSession.fireAllRules(new CustomAgendaFilter(hashSet));
        }
        kSession.delete(factHandle);
        logger.info(String.format("emp_code: %s, total excute rule counts: %d", context.getEmpPayItem().getEmpCode(), count));
        //kSession.delete(f);
    }
}