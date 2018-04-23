package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.*;
import com.ciicsh.gto.salarymanagement.entity.message.AdjustBatchMsg;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrAdjustBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by bill on 18/1/13.
 */
@Service
public class PrAdjustBatchServiceImpl implements PrAdjustBatchService {

    private final static Logger logger = LoggerFactory.getLogger(PrAdjustBatchServiceImpl.class);


    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private PrAdjustBatchMapper adjustBatchMapper;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    @Override
    public int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy) {
        return adjustBatchMapper.updateHasAdvance(batchCode, hasAdvance, modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return adjustBatchMapper.updateHasMoneny(batchCode, hasMoney, modifiedBy);
    }

    @Override
    public int insert(PrAdjustBatchPO adjustBatchPO) {

        AdjustBatchMsg batchMsg = new AdjustBatchMsg();

        int rowAffected = 0;
        PrNormalBatchPO normalBatchPO = new PrNormalBatchPO();
        normalBatchPO.setCode(adjustBatchPO.getRootBatchCode());
        PrNormalBatchPO find = normalBatchMapper.selectOne(normalBatchPO);
        if (find == null) { // 正常批次没有找到 ， 说明是基于原调整批次的新调整批次
            batchMsg.setRootBatchCode(adjustBatchPO.getOriginBatchCode());
        } else { // 正常批次找到 ， 说明是基于原调整批次的正常批次
            batchMsg.setRootBatchCode(find.getCode());
        }

        rowAffected = adjustBatchMapper.insert(adjustBatchPO);
        if(rowAffected > 0){
            batchMsg.setOriginBatchCode(adjustBatchPO.getOriginBatchCode());
            batchMsg.setAdjustBatchCode(adjustBatchPO.getAdjustBatchCode());
            batchMsg.setOperateTypeEnum(OperateTypeEnum.ADD);
            kafkaSender.SendAdjustBatch(batchMsg);
        }
        return rowAffected;
    }

    @Override
    public PrAdjustBatchPO getAdjustBatchPO(PrAdjustBatchPO adjustBatchPO) {
        return adjustBatchMapper.selectOne(adjustBatchPO);
    }

    @Override
    public List<DBObject> getAdjustBatch(String adjustBatchCode) {
        return adjustBatchMongoOpt.list(Criteria.where("batch_code").is(adjustBatchCode));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result) {
        if (status == BatchStatusEnum.COMPUTING.getValue()) {
            return adjustBatchMapper.auditBatch(batchCode, comments, status, modifiedBy, result);
        }
        ApprovalHistoryPO historyPO = new ApprovalHistoryPO();
        int approvalResult = 0;
        if (status == BatchStatusEnum.NEW.getValue()) {
            approvalResult = ApprovalStatusEnum.DRAFT.getValue();
        } else if (status == BatchStatusEnum.PENDING.getValue()) {
            approvalResult = ApprovalStatusEnum.AUDITING.getValue();
        } else if (status == BatchStatusEnum.APPROVAL.getValue() || status == BatchStatusEnum.CLOSED.getValue()) {
            approvalResult = ApprovalStatusEnum.APPROVE.getValue();
        } else if (status == BatchStatusEnum.REJECT.getValue()) {
            approvalResult = ApprovalStatusEnum.DENIED.getValue();
        }
        historyPO.setApprovalResult(approvalResult);
        historyPO.setBizCode(batchCode);
        historyPO.setBizType(BizTypeEnum.NORMAL_BATCH.getValue());
        historyPO.setComments(comments);
        approvalHistoryService.addApprovalHistory(historyPO);

        return adjustBatchMapper.auditBatch(batchCode, comments, status, modifiedBy, result);
    }

    @Override
    public Integer deleteAdjustBatchByCodes(List<String> codes) {
        return adjustBatchMapper.deleteBatchByCodes(codes);
    }

    @Override
    public int checkAdjustBatch(String originBatchCode) {
        return adjustBatchMapper.checkAdjustBatch(originBatchCode);
    }
}
