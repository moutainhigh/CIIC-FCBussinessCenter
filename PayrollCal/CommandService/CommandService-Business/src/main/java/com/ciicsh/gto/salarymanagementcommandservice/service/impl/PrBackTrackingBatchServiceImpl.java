package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.*;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrBackTrackingBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bill on 18/1/13.
 */
@Service
public class PrBackTrackingBatchServiceImpl implements PrBackTrackingBatchService {

    private final static Logger logger = LoggerFactory.getLogger(PrBackTrackingBatchServiceImpl.class);

    @Autowired
    private PrBackTrackingBatchMapper backTrackingBatchMapper;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    @Override
    public int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy) {
        return backTrackingBatchMapper.updateHasAdvance(batchCode,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return backTrackingBatchMapper.updateHasAdvance(batchCode,hasMoney,modifiedBy);
    }

    @Override
    public int insert(PrBackTrackingBatchPO prBackTrackingBatchPO) {
        List<DBObject> list = null;
        int rowAffected = 0;
        String jsonResult = "";
        PrNormalBatchPO normalBatchPO = new PrNormalBatchPO();
        normalBatchPO.setCode(prBackTrackingBatchPO.getRootBatchCode());
        PrNormalBatchPO find = normalBatchMapper.selectOne(normalBatchPO);
        if(find == null){ // 正常批次没有找到 ， 说明是基于原回溯批次的新回溯批次
            PrBackTrackingBatchPO origin = new PrBackTrackingBatchPO();
            origin.setBackTrackingBatchCode(prBackTrackingBatchPO.getBackTrackingBatchCode());
            origin = backTrackingBatchMapper.selectOne(origin);
            jsonResult = origin.getBackEmpResult();

        }else { // 正常批次找到 ， 说明是基于原回溯批次的正常批次
            jsonResult = find.getResultData();
        }
        if(StringUtils.isEmpty(jsonResult)){
            logger.error("计算结果不能为空");
            return 0;
        }
        rowAffected = backTrackingBatchMapper.insert(prBackTrackingBatchPO);
        if(rowAffected > 0){

            list = (List<DBObject>) JSON.parse(jsonResult);
            if (list != null && list.size() > 0) {
                list.forEach(dbObject -> {
                    dbObject.put("_id", new ObjectId());
                    dbObject.put("batch_code", prBackTrackingBatchPO.getBackTrackingBatchCode()); // update origin code to adjust code
                    dbObject.put("origin_batch_code",prBackTrackingBatchPO.getOriginBatchCode());
                    dbObject.put("root_batch_code",prBackTrackingBatchPO.getRootBatchCode());
                    DBObject catalog = (DBObject)dbObject.get("catalog");
                    DBObject batchInfo = (DBObject)catalog.get("batch_info");
                    batchInfo.put("payroll_type", BatchTypeEnum.BACK.getValue());

                });
            }
            backTraceBatchMongoOpt.createIndex();
            rowAffected = backTraceBatchMongoOpt.batchInsert(list); // batch insert to mongodb
        }

        logger.info(String.format("back trace batch row affected %d", rowAffected));

        return rowAffected;
    }

    @Override
    public List<DBObject> getBackTrackingBatch(String backTraceBatchCode) {
        List<DBObject> list = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(backTraceBatchCode));
        return list;
    }

    @Override
    public int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result) {
        if(status == BatchStatusEnum.COMPUTING.getValue()){
            return backTrackingBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,result);
        }
        ApprovalHistoryPO historyPO = new ApprovalHistoryPO();
        int approvalResult = 0;
        if(status == BatchStatusEnum.NEW.getValue()){
            approvalResult = ApprovalStatusEnum.DRAFT.getValue();
        }else if(status == BatchStatusEnum.PENDING.getValue()){
            approvalResult = ApprovalStatusEnum.AUDITING.getValue();
        }else if(status == BatchStatusEnum.APPROVAL.getValue() || status == BatchStatusEnum.CLOSED.getValue()){
            approvalResult = ApprovalStatusEnum.APPROVE.getValue();
        }else if(status == BatchStatusEnum.REJECT.getValue()){
            approvalResult = ApprovalStatusEnum.DENIED.getValue();
        }
        historyPO.setApprovalResult(approvalResult);
        historyPO.setBizCode(batchCode);
        historyPO.setBizType(BizTypeEnum.NORMAL_BATCH.getValue());
        historyPO.setComments(comments);
        approvalHistoryService.addApprovalHistory(historyPO);

        return backTrackingBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,result);
    }

    @Override
    public Integer deleteBackTraceBatchByCodes(List<String> codes) {
        return backTrackingBatchMapper.deleteBatchByCodes(codes);
    }

    @Override
    public int checkBackTraceBatch(String originBatchCode) {
        return backTrackingBatchMapper.checkBackTraceBatch(originBatchCode);
    }

    @Override
    public PrBackTrackingBatchPO getPrBackTrackingBatchPO(PrBackTrackingBatchPO prBackTrackingBatchPO){
        return backTrackingBatchMapper.selectOne(prBackTrackingBatchPO);
    }

}
