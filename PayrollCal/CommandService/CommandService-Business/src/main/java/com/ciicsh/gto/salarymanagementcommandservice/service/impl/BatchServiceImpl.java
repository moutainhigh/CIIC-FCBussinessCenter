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
import com.ciicsh.gto.salarymanagementcommandservice.service.BatchService;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

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

    @Override
    public List<BatchCompareEmpBO> compareBatch(String srcBatchCode, Integer srcBatchType,
                                           String tgtBatchCode, Integer tgtBatchType,
                                           List<String> compareKeys,
                                           LinkedHashMap<String, String> compareMap) {

        List<DBObject> srcBatchList = this.getBatchItemList(srcBatchType, srcBatchCode);
        List<DBObject> tgtBatchList = this.getBatchItemList(tgtBatchType, tgtBatchCode);
        if (srcBatchList == null || tgtBatchList == null || srcBatchList.size() == 0 || tgtBatchList.size() == 0) {
            throw new BusinessException("没有批次数据");
        }

        List<BatchCompareEmpBO> batchCompareEmpBOList = new ArrayList<>();

        srcBatchList.forEach(i -> {
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

                if (tgtEmpResultData.isPresent()) {
                    empResultBO.setInWhichBatch(InWitchCompareBatchEnum.BOTH.getValue());

                    DBObject srcCatalog = (DBObject)i.get("catalog");
                    List<DBObject> srcPayItems = (List<DBObject>)srcCatalog.get("pay_items");
                    DBObject tgtCatalog = (DBObject)i.get("catalog");
                    List<DBObject> tgtPayItems = (List<DBObject>)tgtCatalog.get("pay_items");

                    compareMap.forEach((k,v) -> {
                        DBObject srcItem = srcPayItems.stream()
                                .filter(item -> k.equals(item.get("item_name")))
                                .findFirst()
                                .orElse(null);

                        DBObject tgtItem = tgtPayItems.stream()
                                .filter(item -> v.equals(item.get("item_name")))
                                .findFirst()
                                .orElse(null);

                        BatchCompareItemBO itemBO = new BatchCompareItemBO();

                        itemBO.setMappingKey(k);
                        itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
                        itemBO.setTgtValue(this.getItemValueFromDBObject(tgtItem));

                        batchCompareItemBOList.add(itemBO);
                    });
                    tgtBatchList.remove(tgtEmpResultData);
                } else {
                    empResultBO.setInWhichBatch(InWitchCompareBatchEnum.SRC.getValue());

                    DBObject srcCatalog = (DBObject)i.get("catalog");
                    List<DBObject> srcPayItems = (List<DBObject>)srcCatalog.get("pay_items");

                    compareMap.forEach((k,v) -> {
                        DBObject srcItem = srcPayItems.stream()
                                .filter(item -> k.equals(item.get("item_name")))
                                .findFirst()
                                .orElse(null);

                        BatchCompareItemBO itemBO = new BatchCompareItemBO();

                        itemBO.setMappingKey(k);
                        itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
                        itemBO.setTgtValue("");

                        batchCompareItemBOList.add(itemBO);
                    });
                }

                empResultBO.setItemList(batchCompareItemBOList);
                batchCompareEmpBOList.add(empResultBO);
            }
        });

        tgtBatchList.forEach(i -> {
            if (i.get("雇员编号") != null) {
                BatchCompareEmpBO empResultBO = new BatchCompareEmpBO();

                String empId = i.get("雇员编号").toString();
                empResultBO.setEmployeeId(empId);

                List<BatchCompareItemBO> batchCompareItemBOList = new ArrayList<>();

                empResultBO.setInWhichBatch(InWitchCompareBatchEnum.TGT.getValue());

                DBObject tgtCatalog = (DBObject)i.get("catalog");
                List<DBObject> tgtPayItems = (List<DBObject>)tgtCatalog.get("pay_items");

                compareMap.forEach((k,v) -> {
                    DBObject tgtItem = tgtPayItems.stream()
                            .filter(item -> v.equals(item.get("item_name")))
                            .findFirst()
                            .orElse(null);

                    BatchCompareItemBO itemBO = new BatchCompareItemBO();

                    itemBO.setMappingKey(k);
                    itemBO.setSrcValue("");
                    itemBO.setTgtValue(this.getItemValueFromDBObject(tgtItem));

                    batchCompareItemBOList.add(itemBO);
                });

                empResultBO.setItemList(batchCompareItemBOList);
                batchCompareEmpBOList.add(empResultBO);
            }
        });
//
//
//        compareMap.forEach((k, v) -> {
//
//        });

        //query = query.skip(pageindex*size).limit(size);

        return batchCompareEmpBOList;
    }

    private String getItemValueFromDBObject(DBObject dbObject) {
        if (dbObject == null) {
            return "";
        } else {
            Optional<Object> itemValue =  Optional.ofNullable(dbObject.get("item_value"));
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


        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        return batchList;
    }

    private DBObject findRecordFromListByCompareKeys(List<DBObject> records, List<String> keys) {

        //平铺item list
        List<List<DBObject>> itemListList = records.stream()
                .map(i -> (DBObject)i.get("catalog"))
                .map(catalog -> (List<DBObject>)catalog.get("pay_items"))
                .collect(Collectors.toList());

//        Map<String, List<DBObject>> itemListMap = itemListList.stream()
//                .collect(Collectors.groupingBy(list -> list);

//
//                .collect(Collectors.toMap(catalog -> {
//                    List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");
//                    List<String> keyStr = new ArrayList<>();
//                    keys.forEach(i -> {
//                        DBObject name = payItems.stream()
//                                .filter(item -> i.equals(item.get("item_name")))
//                                .findFirst()
//                                .orElse(null);
//
//                    });
//                    return "";
//                }, catalog -> new ArrayList<DBObject>()));

        return null;
    }
}
