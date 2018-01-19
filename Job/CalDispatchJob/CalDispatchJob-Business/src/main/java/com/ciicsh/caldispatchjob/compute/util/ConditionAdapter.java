package com.ciicsh.caldispatchjob.compute.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.text.StrSubstitutor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/9/12.
 */
public class ConditionAdapter {

    private static final String LOGICAL_OP = "logicalOperator";
    private static final String CHILDREN = "children";
    private static final String TYPE = "type";
    private static final String SINGLE_RULE = "query-builder-rule";
    private static final String GROUP_RULE = "query-builder-group";
    private static final String QUERY = "query";
    private static final String OP = "selectedOperator";
    private static final String ITEM_ID = "rule";
    private static final String ITEM = "selectedOperand";
    private static final String VALUE = "value";
    private static final String CONDITION_TEMPLATE = "if (${condition}){ ${exec} }";

    private static final String ALL = "所有";
    private static final String OR = "任意";

    private static final String EQU_OP = "=";

    /**
     * 解析公式条件列表
     * @param conditionsStr 条件
     * @return 条件文字列
     */
    public static String getConditionStr(String conditionsStr){

        List<JSONObject> conditions =
                Arrays.asList(conditionsStr.split(";"))
                        .stream()
                        .map(con -> JSON.parseObject(con))
                        .collect(Collectors.toList());

        List<String> resolvedQueryList = new ArrayList<>();
        Map<String, String> resolvedQueryMap = new HashMap<>();

        // 遍历条件数组进行解析
        for (JSONObject o : conditions) {
            // 调用解析方法
            String result = resolveSingleCondition(o);

            // 将解析好的Query String对template中进行对应替换

            if(result.length() != 0){
                resolvedQueryMap.put("condition", result);
                StrSubstitutor sub = new StrSubstitutor(resolvedQueryMap);
                String resolvedString = sub.replace(CONDITION_TEMPLATE);
                resolvedQueryList.add(resolvedString);
            }
        }
        // 对所有条件分支进行拼接
        String finalResult = String.join(" else ", resolvedQueryList);

        return finalResult;

    }

    private static String resolveSingleCondition(JSONObject condition){

        // 判断是否存在逻辑运算
        if (condition.containsKey(LOGICAL_OP)) {

            JSONArray subCons = condition.getJSONArray(CHILDREN);
            if (subCons.size() == 0) {
               return "";
            }
            List<String> logicalList = new ArrayList<>();

            // 对逻辑运算符中的各部分进行解析
            for (Iterator ite = subCons.iterator(); ite.hasNext();) {
                JSONObject o = (JSONObject)ite.next();
                logicalList.add(resolveSingleCondition(o));
            }
            String delimiter;
            if (ALL.equals(condition.getString(LOGICAL_OP))) {
                delimiter = " && ";
            } else if (OR.equals(condition.getString(LOGICAL_OP))) {
                delimiter = " || ";
            } else {
                delimiter = "";
            }
            String logicQuery = String.join(delimiter, logicalList);
            return logicQuery;
        } else {
            if (SINGLE_RULE.equals(condition.getString(TYPE))) {
                // 单个条件直接进行解析
                JSONObject query = condition.getJSONObject(QUERY);
                // A = B
                String op = EQU_OP.equals(query.getString(OP)) ? "==" : query.getString(OP);
                String queryStr = query.getString(ITEM) + op + query.getString(VALUE);
                return queryStr;
            } else if (GROUP_RULE.equals(condition.getString(TYPE))) {
                // 多个条件的再次递归
                JSONObject query = condition.getJSONObject(QUERY);
                return "(" + resolveSingleCondition(query) + ")";
            } else {
                return "";
            }
        }
    }
}
