package com.ciicsh.caldispatchjob.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by bill on 18/1/16.
 */

/**
 * 通用函数实体定义 函数名称，函数参数列表,函数结果
 * 无参数调用 funcName
 * 有参数调用 funcName([para1],[para2],...)
 */
public class FuncEntity {

    private String funcName;

    private List<String> parameters;

    private BigDecimal result;

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(funcName);
        sb.append("(");
        if(this.parameters != null && this.parameters.size() > 0) {
            int total = this.parameters.size();
            int count = 0;
            for (String para : this.parameters) {
                count++;
                sb.append(para);
                if(count < total) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return sb.toString(); // 无参数 funcName, 有参数 funcName([para1],[para2],...)
    }

}
