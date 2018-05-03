package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.SpringContextHolder;
import com.ciicsh.gto.fcbusinesscenter.tax.util.exception.BaseException;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.TimerTask;

/**
 * 日志工厂
 * @author wuhua
 */

@Service
@DependsOn("springContextHolder")
public class LogTaskFactory implements ILogTask{

    private static final Logger logger = LoggerFactory.getLogger(LogTaskFactory.class);

    @Value("${app.id}")
    public String appId;

    @Autowired
    private LogServiceProxy logServiceProxy;

    public static ILogTask getLogger() {
        return SpringContextHolder.getBean(ILogTask.class);
    }

    //业务日志
    public void info(final String content,final String title, final String source
                        , final LogType logType, final Map<String, String> tags) {

        LogManager.me().executeLog(new TimerTask() {
            @Override
            public void run() {
                try {
                    LogDTO logDTO = new LogDTO(appId, title, content, source, logType, tags);
                    logServiceProxy.info(logDTO);
                } catch (Exception e) {
                    logger.error("调用日志平台接口异常!", e);
                }
            }
        });
    }

    //错误日志
    public void error(final Throwable e, final String title, final String source
                        , final LogType logType, final Map<String, String> tags) {

        LogManager.me().executeLog(new TimerTask() {
            @Override
            public void run() {
                try {
                    String str = BaseException.exceptionToString(e);
                    LogDTO logDTO = new LogDTO(appId, title, str, source, logType, tags);
                    logServiceProxy.error(logDTO);
                } catch (Exception e) {
                    logger.error("调用日志平台接口异常!", e);
                }finally{
                    logger.error("",e);
                }
            }
        });
    }
}

