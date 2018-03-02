package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common;

import com.ciicsh.gto.fcbusinesscenter.tax.util.exception.BaseException;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 异常工具类
 *
 * @author yuantongqing on 2018/3/2
 */
@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Value("${app.id}")
    public String appId;

    @Autowired
    private LogServiceProxy logServiceProxy;


    /**
     * 日志信息打印
     *
     * @param content
     * @param title
     * @param source
     * @param logType
     * @param tags
     */
    public void info(String content, String title, String source, LogType logType, Map<String, String> tags) {
        LogDTO logDTO = new LogDTO(appId, title, content, source, logType, tags);
        logServiceProxy.info(logDTO);
    }

    /**
     * 错误日志打印
     *
     * @param e
     * @param title
     * @param source
     * @param logType
     * @param tags
     */
    public void error(Exception e, String title, String source, LogType logType, Map<String, String> tags) {
        try {
            String str = BaseException.exceptionToString(e);
            logger.error(str);
            LogDTO logDTO = new LogDTO(appId, title, str, source, logType, tags);
            logServiceProxy.error(logDTO);
        } catch (Exception e1) {
            String str = BaseException.exceptionToString(e1);
            logger.error(str);
        }
    }


}
