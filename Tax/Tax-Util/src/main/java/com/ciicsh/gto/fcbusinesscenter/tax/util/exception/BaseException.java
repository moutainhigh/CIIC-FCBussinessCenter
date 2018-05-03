package com.ciicsh.gto.fcbusinesscenter.tax.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @author yuantongqing on 2018/2/26
 */
public class BaseException {

    private static final Logger logger = LoggerFactory.getLogger(BaseException.class);

    /**
     * 将Exception转换成字符串
     *
     * @param e
     * @return
     */
    public static String exceptionToString(Throwable e) {
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
        } catch (Exception e1) {
            logger.error("exceptionToString error");
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception e1) {
                    logger.error("exceptionToString sw close error");
                }
            }
        }
        String str = sw.toString();
        return str;
    }

}
