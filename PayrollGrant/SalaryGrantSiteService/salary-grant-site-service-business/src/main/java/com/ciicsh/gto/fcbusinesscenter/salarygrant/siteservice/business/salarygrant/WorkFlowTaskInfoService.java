package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.WorkFlowTaskInfoPO;
import com.ciicsh.gto.sheetservice.api.dto.ProcessCompleteMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCompleteMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCreateMsgDTO;

/**
 * <p>
 * 工作流任务日志表 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-09
 */
public interface WorkFlowTaskInfoService extends IService<WorkFlowTaskInfoPO> {
    /**
     * 保存工作流信息
     * @author chenpb
     * @since 2018-06-27
     * @param taskCreateMsgDTO
     */
    void createMessage(TaskCreateMsgDTO taskCreateMsgDTO);

    /**
     * 任务完成
     * @author chenpb
     * @since 2018-06-27
     * @param taskCompleteMsgDTO
     */
    void taskComplete(TaskCompleteMsgDTO taskCompleteMsgDTO);

    /**
     * 流程结束
     * @author chenpb
     * @since 2018-07-13
     * @param processCompleteMsgDTO
     */
    void processComplete(ProcessCompleteMsgDTO processCompleteMsgDTO);

    /**
     * taskId去重
     * @author chenpb
     * @since 2018-06-27
     * @param taskId
     * @return
     */
    Integer selectWfTaskInfoByTaskId(String taskId);
}
