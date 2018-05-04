package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log;

import com.ciicsh.gto.logservice.api.dto.LogType;

import java.util.Map;

/**
 * 日志任务接口
 * @author wuhua
 */
public interface ILogTask {


    void info(final String content, final String title, final String source
            , final LogType logType, final Map<String, String> tags);

    void error(final Throwable e, final String title, final String source
            , final LogType logType, final Map<String, String> tags);
}
