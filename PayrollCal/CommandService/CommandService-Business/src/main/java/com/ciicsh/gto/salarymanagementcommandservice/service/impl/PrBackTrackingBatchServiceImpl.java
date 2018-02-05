package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrBackTrackingBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
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
        return backTrackingBatchMapper.insert(prBackTrackingBatchPO);
    }

    @Override
    public List<DBObject> getBackTrackingBatch(String backTraceBatchCode, String originCode) {
        List<DBObject> list = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(backTraceBatchCode));
        if(list == null || list.size() == 0) {
            PrNormalBatchPO normalBatchPO = new PrNormalBatchPO();
            normalBatchPO.setCode(originCode);
            normalBatchPO = normalBatchMapper.selectOne(normalBatchPO);
            String jsonResult = normalBatchPO.getResultData();
            list = (List<DBObject>) JSON.parse(jsonResult);
            if(list != null && list.size() > 0){

                list.forEach(dbObject -> {
                    dbObject.put("batch_code",backTraceBatchCode); // update origin code to adjust code
                    DBObject catalog = (DBObject)dbObject.get("catalog");
                    List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");

                    payItems.forEach(item ->{
                        int itemType = item.get("item_type") == null ? 0 : (int)item.get("item_type"); // 薪资项类型
                        if(itemType == ItemTypeEnum.CALC.getValue()) {
                            item.put("item_value", 0.0); //清除计算项结果
                        }
                    });
                });
            }
            backTraceBatchMongoOpt.createIndex();
            int rowAffected = backTraceBatchMongoOpt.batchInsert(list); // batch insert to mongodb
            logger.info(String.format("adjust batch row affected %d", rowAffected));
        }
        return list;
    }

    @Override
    public int updateBatchStatus(String batchCode, int status, String modifiedBy) {
        return backTrackingBatchMapper.updateBatchStatus(batchCode,status,modifiedBy);
    }

    @Override
    public Integer deleteBackTraceBatchByCodes(List<String> codes) {
        return backTrackingBatchMapper.deleteBatchByCodes(codes);
    }

    @Override
    public int checkBackTraceBatch(String originBatchCode) {
        return backTrackingBatchMapper.checkBackTraceBatch(originBatchCode);
    }
}
