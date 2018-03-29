package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrAdjustBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by bill on 18/1/13.
 */
@Service
public class PrAdjustBatchServiceImpl implements PrAdjustBatchService {

    private final static Logger logger = LoggerFactory.getLogger(PrAdjustBatchServiceImpl.class);


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
        return adjustBatchMapper.updateHasAdvance(batchCode,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return adjustBatchMapper.updateHasMoneny(batchCode,hasMoney,modifiedBy);
    }

    @Override
    public int insert(PrAdjustBatchPO adjustBatchPO) {

        List<DBObject> list = adjustBatchMongoOpt.list(Criteria.where("batch_code").is(adjustBatchPO.getAdjustBatchCode()));

        int rowAffected = 0;
        if (list == null || list.size() == 0) {
            PrNormalBatchPO normalBatchPO = new PrNormalBatchPO();
            normalBatchPO.setCode(adjustBatchPO.getOriginBatchCode());
            normalBatchPO = normalBatchMapper.selectOne(normalBatchPO);
            String jsonResult = normalBatchPO.getResultData();
            list = (List<DBObject>) JSON.parse(jsonResult);
            if (list != null && list.size() > 0) {

                list.forEach(dbObject -> {
                    dbObject.put("_id", new ObjectId());
                    dbObject.put("batch_code", adjustBatchPO.getAdjustBatchCode()); // update origin code to adjust code
                    dbObject.put("origin_code",adjustBatchPO.getOriginBatchCode());
                    /*DBObject catalog = (DBObject) dbObject.get("catalog");
                    List<DBObject> payItems = (List<DBObject>) catalog.get("pay_items");

                    payItems.forEach(item -> {
                        int itemType = item.get("item_type") == null ? 0 : (int) item.get("item_type"); // 薪资项类型
                        if (itemType == ItemTypeEnum.CALC.getValue()) {
                            item.put("item_value", 0.0); //清除计算项结果
                        }
                    });*/
                });
            }
            adjustBatchMongoOpt.createIndex();
            rowAffected = adjustBatchMongoOpt.batchInsert(list); // batch insert to mongodb
            if (rowAffected > 0) {
                rowAffected += adjustBatchMapper.insert(adjustBatchPO);
            }else {
                Map<String,Object> map = new HashMap<>();
                map.put("origin_batch_code",adjustBatchPO.getOriginBatchCode());
                map.put("adjust_batch_code",adjustBatchPO.getAdjustBatchCode());
                adjustBatchMapper.deleteByMap(map);
                rowAffected = 0;
            }
            logger.info(String.format("adjust batch row affected %d", rowAffected));
        }

        return rowAffected;
    }

    @Override
    public List<DBObject> getAdjustBatch(String adjustBatchCode, String originCode) {
        return adjustBatchMongoOpt.list(Criteria.where("batch_code").is(adjustBatchCode));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result) {
        if(status == BatchStatusEnum.COMPUTING.getValue()){
            return adjustBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,result);
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

        return adjustBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,result);
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
