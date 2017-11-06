package com.ciicsh.zorro.leopardwebservice.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

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

        if(StringUtils.isEmpty(conditionsStr)){
            return "";
        }

        List<String> resolvedQueryList =
                Arrays.asList(conditionsStr.split(";"))
                        .stream()
                        .map(c -> resolveSingleCondition(JSON.parseObject(c)))   // 调用解析方法
                        .filter(rc -> rc.length() != 0)
                        .map(rc -> {
                            // 将解析好的Query String对template中进行对应替换
                            Map<String, String> queryMap = new HashMap<String, String>() {{
                                put("condition", rc);
                            }};
                            StrSubstitutor sub = new StrSubstitutor(queryMap);
                            return sub.replace(CONDITION_TEMPLATE);
                        })
                        .collect(Collectors.toList());

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
