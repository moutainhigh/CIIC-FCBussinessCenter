package com.ciicsh.caldispatchjob.compute.messageBus;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.compute.service.*;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeContractProxy;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.AdjustBatchMsg;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bill on 17/10/21.
 */
@EnableBinding(value = PayrollSink.class)
@Component
public class BatchSyncReceiver {

    private final static Logger logger = LoggerFactory.getLogger(BatchSyncReceiver.class);

    @Autowired
    private EmployeeContractProxy employeeContractProxy;

    @Autowired
    private EmpGroupServiceImpl empGroupService;

    @Autowired
    private NormalBatchServiceImpl batchService;

    @Autowired
    private AdjustBatchServiceImpl adjustBatchService;

    @Autowired
    private BackTraceBatchServiceImpl backTraceBatchService;

    @Autowired
    private ComputeServiceImpl computeService;

    @Autowired
    private CompleteComputeServiceImpl completeComputeService;

    @StreamListener(PayrollSink.PR_NORMAL_BATCH_INPUT)
    public void receive(PayrollMsg message) {
        logger.info("received from normal batchCode : " + message.getBatchCode()
                + " BatchType: " + message.getBatchType());
        processBatchInfo(message.getBatchCode(),
                message.getOriginBatchCode(),
                message.getOperateType(),
                message.getBatchType());
    }

    @StreamListener(PayrollSink.EMP_GROUP_INPUT)
    public void receive(PayrollEmpGroup message) {
        //logger.info("received employee group from message: " + message.getIds());
        processEmployees(message);
    }


    @StreamListener(PayrollSink.PR_COMPUTE_COMPLTE_INPUT)
    public void receiveComplete(ComputeMsg computeMsg) {
        logger.info("received payroll compute from message: " + computeMsg);
        if (computeMsg.getBatchType() > 0) {
            completeComputeService.processCompleteCompute(computeMsg.getBatchCode(), computeMsg.getBatchType(), computeMsg.getOptID(), computeMsg.getOptName());
        }
    }

    @StreamListener(PayrollSink.PR_ADJUST_BATCH_INPUT)
    public void receiveAdjustBatch(AdjustBatchMsg adjustBatchMsg) {
        logger.info("received adjust batch change from message: " + adjustBatchMsg.getAdjustBatchCode());
        if (adjustBatchMsg.getOperateTypeEnum() == OperateTypeEnum.ADD) {
            String rootBatchCode = adjustBatchMsg.getRootBatchCode();
            String originBatchCode = adjustBatchMsg.getOriginBatchCode();
            String adjustCode = adjustBatchMsg.getAdjustBatchCode();
            adjustBatchService.deleteAdjustBatch(adjustBatchMsg.getAdjustBatchCode());
            adjustBatchService.addAdjustBatch(rootBatchCode,originBatchCode,adjustCode);
        } else {
            adjustBatchService.deleteAdjustBatch(adjustBatchMsg.getAdjustBatchCode());
        }
    }

    /**
     * 订阅雇员组里面的雇员列表变化：新增或者删除
     *
     * @param message
     */
    private void processEmployees(PayrollEmpGroup message) {
        List<String> empIdAndCompanyIds = StringUtils.isNotEmpty(message.getIds()) ? Arrays.asList(message.getIds().split("\\$")) : null;
        List<String> groupIds = StringUtils.isNotEmpty(message.getEmpGroupIds()) ? Arrays.asList(message.getEmpGroupIds().split(",")) : null;

        if (groupIds == null && empIdAndCompanyIds == null) {
            logger.info("groupIds & empIds should not be empty");
            return;
        }
        if (message.getOperateType() == OperateTypeEnum.ADD.getValue()) {
            String empGroupId = groupIds.get(0);
            logger.info("ADD Opt");
            logger.info("emp group id:" + empGroupId);
            empGroupService.batchInsertOrUpdateGroupEmployees(empGroupId, empIdAndCompanyIds);
        } else if (message.getOperateType() == OperateTypeEnum.DELETE.getValue()) {
            logger.info("DELETE Opt");
            empGroupService.batchDelGroupEmployees(groupIds, empIdAndCompanyIds);
        }

    }

    /**
     * 订阅批次：新增或者删除
     *
     * @param batchCode 当前批次code
     * @param originBatchCode 源批次code(导入批次时选择的批次)
     * @param optType 操作类型 1 增加, 2 更新, 3 删除, 4 查询, 5 导入;
     * @param batchType 批次类型 1 正常批次, 2 调整批次, 3 回溯批次, 4 测试批次, 5 导入批次;
     */
    private void processBatchInfo(String batchCode, String originBatchCode, int optType, int batchType) {
        if (batchType == BatchTypeEnum.NORMAL.getValue()) { //正常批次
            if (optType == OperateTypeEnum.ADD.getValue()) {
                batchService.batchInsertOrUpdateNormalBatch(batchCode, batchType);
            } else if (optType == OperateTypeEnum.DELETE.getValue()) {
                // 正常批次删除，导入批次的删除复用此逻辑
                batchService.deleteNormalBatch(batchCode, batchType);
            }
        } else if (batchType == BatchTypeEnum.Test.getValue()) { //测试批次
            if (optType == OperateTypeEnum.ADD.getValue()) {
                batchService.batchInsertOrUpdateNormalBatch(batchCode, batchType);
            } else if (optType == OperateTypeEnum.DELETE.getValue()) {
                batchService.deleteNormalBatch(batchCode, batchType);
            }
        } else if (batchType == BatchTypeEnum.IMPORT.getValue()) {
            // 导入批次
            if (optType == OperateTypeEnum.IMPORT.getValue()) {
                // 导入批次新增
                batchService.batchInsertOrUpdateImportBatch(batchCode, originBatchCode);
            }
        } else { //其它批次
            if (optType == OperateTypeEnum.DELETE.getValue()) {
                backTraceBatchService.deleteBackTraceBatch(batchCode);
            }
        }
    }

}
