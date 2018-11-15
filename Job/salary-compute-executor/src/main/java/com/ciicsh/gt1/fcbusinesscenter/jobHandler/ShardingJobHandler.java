package com.ciicsh.gt1.fcbusinesscenter.jobHandler;

import com.ciicsh.gt1.fcbusinesscenter.engine.ComputeEngine;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分片广播任务
 *
 * @author bill
 */
@JobHandler(value="shardingComputeJobHandler")
@Service
public class ShardingJobHandler extends IJobHandler {

	private Logger logger = LoggerFactory.getLogger(ShardingJobHandler.class);

	@Autowired
	private ComputeEngine computeEngine;


	/**
	 *
	 * @param param example: batchCode=GLP0001
	 * @return
	 * @throws Exception
	 */
	@Override
	public ReturnT<String> execute(String param) throws Exception {

		if(StringUtils.isEmpty(param)){
			throw new Exception("输入的参数不能为空！");
		}
		String batchCode = param.split("=")[1];
		logger.info(String.format("批次ID号：%s", batchCode));

		// 分片参数
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		XxlJobLogger.log("分片参数：当前分片序号 = {0}, 总分片数 = {1}", shardingVO.getIndex(), shardingVO.getTotal());
		logger.info(String.format("分片参数：当前分片序号 = %d, 总分片数 = %d", shardingVO.getIndex(), shardingVO.getTotal()));

		try {
			// 薪资计算业务逻辑
			//computeEngine.processShardingCompute(batchCode, shardingVO.getIndex());

		}catch (Exception ex){
			return FAIL;
		}

		return SUCCESS;
	}

}
