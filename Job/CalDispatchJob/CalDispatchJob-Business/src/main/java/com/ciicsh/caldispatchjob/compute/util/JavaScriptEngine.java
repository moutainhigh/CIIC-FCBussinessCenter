package com.ciicsh.caldispatchjob.compute.util;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;

/**
 * Created by jiangtianning on 2017/9/14.
 */
public class JavaScriptEngine {

    //private static ScriptEngineManager factory = new ScriptEngineManager();
    private static ScriptEngine engine;

    public static void setEngine(ScriptEngine scriptEngine){
        engine = scriptEngine;
    }

    public static ScriptEngine getEngine(){
        return engine;
    }

    public static BigDecimal compute(String formula) throws ScriptException{

        Object o = engine.eval(formula);
        BigDecimal result = new BigDecimal(o.toString());
        return result;

    }

    public static ScriptEngine createEngine(){
        ScriptEngineManager sm = new ScriptEngineManager();

        NashornScriptEngineFactory factory = null;
        for (ScriptEngineFactory f : sm.getEngineFactories()) {
            if (f.getEngineName().equalsIgnoreCase("Oracle Nashorn")) {
                factory = (NashornScriptEngineFactory)f;
                break;
            }
        }
        String[] stringArray = new String[]{"-doe", "--global-per-engine"};
        ScriptEngine engine = factory.getScriptEngine(stringArray);

        //ScriptEngineManager factory = new ScriptEngineManager();
        //ScriptEngine engine = factory.getEngineByName("nashorn");
        return engine;
    }
}
