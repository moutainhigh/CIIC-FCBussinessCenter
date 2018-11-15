package com.ciicsh.gt1.fcbusinesscenter.jobHandler;

import com.ciicsh.gt1.fcbusinesscenter.engine.ComputeEngine;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@JobHandler(value = "fullComputeJobHandler")
public class FullJobHandler extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(FullJobHandler.class);

    @Autowired
    private ComputeEngine computeEngine;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        if(StringUtils.isEmpty(param)){
            throw new Exception("输入的参数不能为空！");
        }

        String batchCode = param.split("=")[1];
        logger.info(String.format("批次ID号：%s", batchCode));

        computeEngine.processCompute(batchCode);

        return SUCCESS;
    }
}
