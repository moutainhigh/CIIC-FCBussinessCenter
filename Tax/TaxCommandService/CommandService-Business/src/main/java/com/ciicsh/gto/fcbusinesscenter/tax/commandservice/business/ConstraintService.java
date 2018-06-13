package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

/**
 * 约束类
 * @author wuhua
 */
public interface ConstraintService {

    String TASK_MAIN = "TM";//个税主任务
    String TASK_SUB_DECLARE = "TSSB";//申报子任务
    String TASK_SUB_TRANSFER = "TSHK";//划款子任务
    String TASK_SUB_PAYMENT = "TSJN";//缴纳子任务
    String TASK_SUB_SUPPLIER = "TSSU";//供应商处理子任务
    String TASK_MAIN_PROOF = "TMP";//完税凭证主任务
    String TASK_SUB_PROOF = "TSP";//完税凭证子任务

    /**
     * 检查批次约束
     * @param Ids
     * @return
     */
    int checkBatch(String[] Ids);
    /**
     * 检查任务约束
     * @param taskIds
     * @param taskType
     * @return
     */
    int checkTask(String[] taskIds , String taskType);

}
