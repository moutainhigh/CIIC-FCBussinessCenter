package com.ciicsh.caldispatchjob.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by bill on 18/1/16.
 */

/**
 * 通用函数实体定义 函数名称，函数结果
 */
public class FuncEntity {

    private String funcName;

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

}
