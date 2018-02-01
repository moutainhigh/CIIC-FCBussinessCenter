package com.ciicsh.caldispatchjob.compute.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;

/**
 * Created by jiangtianning on 2017/9/14.
 */
public class JavaScriptEngine {

    private static ScriptEngineManager factory = new ScriptEngineManager();
    private static ScriptEngine engine = factory.getEngineByName("nashorn");

    public static BigDecimal compute(String formula) throws ScriptException{

        Object o = engine.eval(formula);
        BigDecimal result = new BigDecimal(o.toString());
        return result;

    }
}
