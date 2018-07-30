package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareEmpBO;
import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareItemBO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.InWitchCompareBatchEnum;
import com.ciicsh.gto.salarymanagement.entity.po.AdvanceBatchInfoPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.BatchService;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by NeoJiang on 2018/5/2.
 */
@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private PrNormalBatchMapper prNormalBatchMapper;

    @Override
    public List<BatchCompareEmpBO> compareBatch(String srcBatchCode, Integer srcBatchType,
                                                String tgtBatchCode, Integer tgtBatchType,
                                                List<String> compareKeys,
                                                IdentityHashMap<String, String> compareMap) {

        List<DBObject> srcBatchList = this.getBatchItemList(srcBatchType, srcBatchCode);
        List<DBObject> tgtBatchList = this.getBatchItemList(tgtBatchType, tgtBatchCode);
        if (srcBatchList == null || tgtBatchList == null || srcBatchList.size() == 0 || tgtBatchList.size() == 0) {
            throw new BusinessException("没有批次数据");
        }

        List<BatchCompareEmpBO> batchCompareEmpBOList = new ArrayList<>();

        srcBatchList.forEach(i -> {
            compareBatchList(compareMap, tgtBatchList, batchCompareEmpBOList, i, true);
        });

        IdentityHashMap<String, String> tgtCompareMap = new IdentityHashMap<>();
        for (String k : compareMap.keySet()) {
            tgtCompareMap.put(new String(compareMap.get(k)), k);
        }
        tgtBatchList.forEach(i -> {
            compareBatchList(tgtCompareMap, srcBatchList, batchCompareEmpBOList, i, false);
        });
        return batchCompareEmpBOList;
    }

    private void compareBatchList(IdentityHashMap<String, String> compareMap,
                                  List<DBObject> tgtBatchList,
                                  List<BatchCompareEmpBO> batchCompareEmpBOList,
                                  DBObject i,
                                  boolean isSrcBatchFlag) {
        if (i.get("雇员编号") != null) {
            BatchCompareEmpBO empResultBO = new BatchCompareEmpBO();

            String srcEmpId = i.get("雇员编号").toString();
            empResultBO.setEmployeeId(srcEmpId);

            Optional<DBObject> tgtEmpResultData = tgtBatchList.stream()
                    .filter(j -> {
                        String tgtEmpId = j.get("雇员编号").toString();
                        return srcEmpId.equals(tgtEmpId);
                    })
                    .findFirst();

            List<BatchCompareItemBO> batchCompareItemBOList = new ArrayList<>();

            empResultBO.setInWhichBatch(tgtEmpResultData.isPresent() ?
                    InWitchCompareBatchEnum.BOTH.getValue() : InWitchCompareBatchEnum.SRC.getValue());

            DBObject srcCatalog = (DBObject) i.get("catalog");
            List<DBObject> srcPayItems = (List<DBObject>) srcCatalog.get("pay_items");
            DBObject tgtCatalog = (DBObject) tgtBatchList.get(0).get("catalog");
            List<DBObject> tgtPayItems = (List<DBObject>) tgtCatalog.get("pay_items");

            comparePayItems(compareMap, batchCompareItemBOList, srcPayItems, tgtPayItems, isSrcBatchFlag);
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);
        }
    }

    private void comparePayItems(IdentityHashMap<String, String> compareMap,
                                 List<BatchCompareItemBO> batchCompareItemBOList,
                                 List<DBObject> srcPayItems,
                                 List<DBObject> tgtPayItems,
                                 boolean isSrcBatchFlag) {
        compareMap.forEach((k, v) -> {
            DBObject srcItem = getItemsFromDbObject(srcPayItems, k);

            DBObject tgtItem = getItemsFromDbObject(tgtPayItems, v);

            BatchCompareItemBO itemBO = new BatchCompareItemBO();

            itemBO.setMappingKey(isSrcBatchFlag ? k : v);
            itemBO.setSrcBatchFlag(isSrcBatchFlag);
            itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
            itemBO.setTgtValue(this.getItemValueFromDBObject(tgtItem));

            batchCompareItemBOList.add(itemBO);
        });
    }

    private DBObject getItemsFromDbObject(List<DBObject> srcPayItems, String k) {
        return srcPayItems.stream()
                .filter(item -> k.equals(item.get("item_name")))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int updateAdvancedBatch(AdvanceBatchInfoPO advanceBatchInfoPO) {
        return prNormalBatchMapper.updateAdvancedBatch(advanceBatchInfoPO);
    }

    private String getItemValueFromDBObject(DBObject dbObject) {
        if (dbObject == null) {
            return "";
        } else {
            Optional<Object> itemValue = Optional.ofNullable(dbObject.get("item_value"));
            return itemValue.map(Object::toString).orElse("");
        }
    }

    private List<DBObject> getBatchItemList(Integer batchType, String batchCode) {

        List<DBObject> batchList = null;
        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN)
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
        ;


        if (batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query, DBObject.class,
                    NormalBatchMongoOpt.PR_NORMAL_BATCH);
        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query, DBObject.class,
                    AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        } else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query, DBObject.class,
                    BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        return batchList;
    }

}
