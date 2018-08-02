package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareEmpBO;
import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareItemBO;
import com.ciicsh.gto.salarymanagement.entity.dto.BatchCompareRequestDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.CurrentBatchEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.InWitchCompareBatchEnum;
import com.ciicsh.gto.salarymanagement.entity.po.AdvanceBatchInfoPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.BatchService;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<BatchCompareEmpBO> compareBatch(BatchCompareRequestDTO compareDTO) {
        // 源批次code
        String srcBatchCode = compareDTO.getSrc();
        // 源批次类型
        Integer srcBatchType = compareDTO.getSrcBatchType();
        // 对比批次1code
        String tgtBatchCode = compareDTO.getTgt();
        // 对比批次1类型
        Integer tgtBatchType = compareDTO.getTgtBatchType();
        // 对比批次2code
        String tgtTwoBatchCode = compareDTO.getTgtTwo();
        // 对比批次2类型
        Integer tgtTwoBatchType = compareDTO.getTgtTwoBatchType();
        // 对比keys列表,暂时不使用保留今后使用
        List<String> compareKeys = Arrays.asList(compareDTO.getCompareKeysStr().split(","));
        // 源批次与对比批次的对比列映射List
        List<Map<String, String>> compareMappingList = compareDTO.getCompareMappingList();
        // 从MongoDB中查询源批次信息
        List<DBObject> srcBatchList = this.getBatchItemList(srcBatchType, srcBatchCode);
        // 从MongoDB中查询对比批次1信息
        List<DBObject> tgtBatchList = this.getBatchItemList(tgtBatchType, tgtBatchCode);
        // 从MongoDB中查询对比批次2信息
        List<DBObject> tgtTwoBatchList =
                tgtTwoBatchCode != null ? this.getBatchItemList(tgtTwoBatchType, tgtTwoBatchCode) : new ArrayList<>();
        // 对比批次数量
        int batchCount = compareDTO.getBatchCount();

        if (srcBatchList == null
                || tgtBatchList == null
                || srcBatchList.size() == 0
                || tgtBatchList.size() == 0
                || (StringUtils.isNotEmpty(tgtTwoBatchCode) && (tgtTwoBatchList == null || tgtTwoBatchList.size() == 0))) {
            throw new BusinessException("存在没有数据的批次,请检查批次信息!");
        }

        List<BatchCompareEmpBO> batchCompareEmpBOList = new ArrayList<>();

        // 源批次
        srcBatchList.forEach(i -> {
            compareBatchList(compareMappingList, srcBatchList, tgtBatchList, tgtTwoBatchList,
                    batchCompareEmpBOList, i, CurrentBatchEnum.SRC_BATCH.getValue(), batchCount);
        });

        // 对比批次1
        tgtBatchList.forEach(i -> {
            compareBatchList(compareMappingList, srcBatchList, tgtBatchList, tgtTwoBatchList,
                    batchCompareEmpBOList, i, CurrentBatchEnum.TGT_BATCH.getValue(), batchCount);
        });

        // 对比批次2
        tgtTwoBatchList.forEach(i -> {
            compareBatchList(compareMappingList, srcBatchList, tgtBatchList, tgtTwoBatchList,
                    batchCompareEmpBOList, i, CurrentBatchEnum.TGTTWO_BATCH.getValue(), batchCount);
        });

        return batchCompareEmpBOList;
    }

    private void compareBatchList(List<Map<String, String>> compareMappingList,
                                  List<DBObject> srcBatchList,
                                  List<DBObject> tgtBatchList,
                                  List<DBObject> tgtTwoBatchList,
                                  List<BatchCompareEmpBO> batchCompareEmpBOList,
                                  DBObject i,
                                  int currentBatch,
                                  int batchCount) {
        // 当前循环批次中的当前循环薪资结果列表中的雇员信息
        String currentEmpId = i.get(PayItemName.EMPLOYEE_CODE_CN).toString();
        String currentCompanyId = i.get(PayItemName.EMPLOYEE_COMPANY_ID).toString();

        BatchCompareEmpBO empResultBO = new BatchCompareEmpBO();
        // 赋值当前雇员Id和公司编号
        empResultBO.setEmployeeId(currentEmpId);
        empResultBO.setCompanyId(currentCompanyId);
        if (currentEmpId != null && currentCompanyId != null) {
            // 如果当前循环批次为源批次
            if (currentBatch == CurrentBatchEnum.SRC_BATCH.getValue()) {
                processCurrentBatch(compareMappingList, srcBatchList, tgtBatchList, tgtTwoBatchList,
                        batchCompareEmpBOList, currentBatch, currentEmpId, currentCompanyId, empResultBO);

            } else if (currentBatch == CurrentBatchEnum.TGT_BATCH.getValue()) {
                // 如果当前循环批次为对比批次1
                processCurrentBatch(compareMappingList, srcBatchList, tgtBatchList, tgtTwoBatchList,
                        batchCompareEmpBOList, currentBatch, currentEmpId, currentCompanyId, empResultBO);

            } else if (currentBatch == CurrentBatchEnum.TGTTWO_BATCH.getValue()) {
                // 如果当前循环批次为对比批次2
                processCurrentBatch(compareMappingList, srcBatchList, tgtBatchList, tgtTwoBatchList,
                        batchCompareEmpBOList, currentBatch, currentEmpId, currentCompanyId, empResultBO);

            }
        }
    }

    private void processCurrentBatch(List<Map<String, String>> compareMappingList,
                                     List<DBObject> srcBatchList,
                                     List<DBObject> tgtBatchList,
                                     List<DBObject> tgtTwoBatchList,
                                     List<BatchCompareEmpBO> batchCompareEmpBOList,
                                     int currentBatch,
                                     String currentEmpId,
                                     String currentCompanyId,
                                     BatchCompareEmpBO empResultBO) {
        // 源批次中的当前雇员薪资项信息
        Optional<DBObject> srcEmpResultData = srcBatchList.stream()
                .filter(payItems -> {
                    String srcEmpId = payItems.get(PayItemName.EMPLOYEE_CODE_CN).toString();
                    String srcCompanyId = payItems.get(PayItemName.EMPLOYEE_COMPANY_ID).toString();
                    return currentEmpId.equals(srcEmpId) && currentCompanyId.equals(srcCompanyId);
                })
                .findFirst();

        // 对比批次1中的当前雇员薪资项信息
        Optional<DBObject> tgtEmpResultData = tgtBatchList.stream()
                .filter(payItems -> {
                    String tgtEmpId = payItems.get(PayItemName.EMPLOYEE_CODE_CN).toString();
                    String tgtCompanyId = payItems.get(PayItemName.EMPLOYEE_COMPANY_ID).toString();
                    return currentEmpId.equals(tgtEmpId) && currentCompanyId.equals(tgtCompanyId);
                })
                .findFirst();

        // 对比批次2中的当前雇员薪资项信息
        Optional<DBObject> tgtTwoEmpResultData = tgtTwoBatchList.stream()
                .filter(payItems -> {
                    String tgtTwoEmpId = payItems.get(PayItemName.EMPLOYEE_CODE_CN).toString();
                    String tgtTwoCompanyId = payItems.get(PayItemName.EMPLOYEE_COMPANY_ID).toString();
                    return currentEmpId.equals(tgtTwoEmpId) && currentCompanyId.equals(tgtTwoCompanyId);
                })
                .findFirst();

        List<BatchCompareItemBO> batchCompareItemBOList = new ArrayList<>();

        // 如果当前雇员在三个批次中都存在
        if (srcEmpResultData.isPresent()
                && tgtEmpResultData.isPresent()
                && tgtTwoEmpResultData.isPresent()) {

            empResultBO.setInWhichBatchForEmpId(InWitchCompareBatchEnum.ALL.getValue());
            // 取出源批次的薪资项列表pay_items
            DBObject srcCatalog = (DBObject) srcEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> srcPayItems = (List<DBObject>) srcCatalog.get(PayItemName.PAY_ITEMS);

            // 取出对比批次1的薪资项列表pay_items
            DBObject tgtCatalog = (DBObject) tgtEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> tgtPayItems = (List<DBObject>) tgtCatalog.get(PayItemName.PAY_ITEMS);

            //对比批次2的薪资项列表pay_items
            DBObject tgtTwoCatalog = (DBObject) tgtTwoEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> tgtTwoPayItems = (List<DBObject>) tgtTwoCatalog.get(PayItemName.PAY_ITEMS);

            compareMappingList.forEach(compareKeyMap -> {
                // 取出源批次薪资项
                DBObject srcItem = getItemsFromDbObject(srcPayItems, compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));

                // 取出对比批次1薪资项
                DBObject tgtItem = getItemsFromDbObject(tgtPayItems, compareKeyMap.get(PayItemName.TGT_COMPANY_KEY));

                // 取出对比批次2薪资项
                DBObject tgtTwoItem = getItemsFromDbObject(tgtTwoPayItems, compareKeyMap.get(PayItemName.TGTTWO_COMPANY_KEY));

                // 三个批次的薪资项分别放入对应的对比结果BO中,用于页面对比展示
                BatchCompareItemBO itemBO = new BatchCompareItemBO();
                itemBO.setMappingKey(compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));
                // 当前批次
                itemBO.setCurrentBatch(currentBatch);
                itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
                itemBO.setTgtValue(this.getItemValueFromDBObject(tgtItem));
                itemBO.setTgtTwoValue(this.getItemValueFromDBObject(tgtTwoItem));
                batchCompareItemBOList.add(itemBO);
            });
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);

        } else if (srcEmpResultData.isPresent()
                && tgtEmpResultData.isPresent()
                && !tgtTwoEmpResultData.isPresent()) {
            // 如果当前雇员只存在于源批次和对比批次1中
            empResultBO.setInWhichBatchForEmpId(InWitchCompareBatchEnum.SRC_TGT.getValue());

            // 取出源批次的薪资项列表
            DBObject srcCatalog = (DBObject) srcEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> srcPayItems = (List<DBObject>) srcCatalog.get(PayItemName.PAY_ITEMS);

            // 取出对比批次1的薪资项列表
            DBObject tgtCatalog = (DBObject) tgtEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> tgtPayItems = (List<DBObject>) tgtCatalog.get(PayItemName.PAY_ITEMS);

            compareMappingList.forEach(compareKeyMap -> {
                // 取出源批次薪资项
                DBObject srcItem = getItemsFromDbObject(srcPayItems, compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));

                // 取出对比批次1薪资项
                DBObject tgtItem = getItemsFromDbObject(tgtPayItems, compareKeyMap.get(PayItemName.TGT_COMPANY_KEY));

                // 源批次和对比批次1的薪资项分别放入对应的对比结果BO中,对比批次2中无此雇员, 故为"",用于页面对比展示
                BatchCompareItemBO itemBO = new BatchCompareItemBO();
                itemBO.setMappingKey(compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));
                // 当前批次
                itemBO.setCurrentBatch(currentBatch);
                itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
                itemBO.setTgtValue(this.getItemValueFromDBObject(tgtItem));
                itemBO.setTgtTwoValue("");
                batchCompareItemBOList.add(itemBO);
            });
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);

        } else if (srcEmpResultData.isPresent()
                && !tgtEmpResultData.isPresent()
                && tgtTwoEmpResultData.isPresent()) {
            // 如果当前雇员只存在于源批次和对比批次2中
            empResultBO.setInWhichBatchForEmpId(InWitchCompareBatchEnum.SRC_TGTTWO.getValue());

            // 取出源批次的薪资项列表
            DBObject srcCatalog = (DBObject) srcEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> srcPayItems = (List<DBObject>) srcCatalog.get(PayItemName.PAY_ITEMS);

            //对比批次2的薪资项列表
            DBObject tgtTwoCatalog = (DBObject) tgtTwoEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> tgtTwoPayItems = (List<DBObject>) tgtTwoCatalog.get(PayItemName.PAY_ITEMS);

            compareMappingList.forEach(compareKeyMap -> {
                // 取出源批次薪资项
                DBObject srcItem = getItemsFromDbObject(srcPayItems, compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));

                // 取出对比批次2薪资项
                DBObject tgtTwoItem = getItemsFromDbObject(tgtTwoPayItems, compareKeyMap.get(PayItemName.TGTTWO_COMPANY_KEY));

                // 源批次和对比批次2的薪资项分别放入对应的对比结果BO中,对比批次1中无此雇员, 故为"",用于页面对比展示
                BatchCompareItemBO itemBO = new BatchCompareItemBO();
                itemBO.setMappingKey(compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));
                // 当前批次
                itemBO.setCurrentBatch(currentBatch);
                itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
                itemBO.setTgtValue("");
                itemBO.setTgtTwoValue(this.getItemValueFromDBObject(tgtTwoItem));
                batchCompareItemBOList.add(itemBO);
            });
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);

        } else if (srcEmpResultData.isPresent()
                && !tgtEmpResultData.isPresent()
                && !tgtTwoEmpResultData.isPresent()) {
            // 如果当前雇员只存在于源批次中
            empResultBO.setInWhichBatchForEmpId(InWitchCompareBatchEnum.SRC_ONLY.getValue());

            // 取出源批次的薪资项列表
            DBObject srcCatalog = (DBObject) srcEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> srcPayItems = (List<DBObject>) srcCatalog.get(PayItemName.PAY_ITEMS);

            compareMappingList.forEach(compareKeyMap -> {
                // 取出源批次薪资项
                DBObject srcItem = getItemsFromDbObject(srcPayItems, compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));

                // 源批次的薪资项放入对应的对比结果BO中,对比批次1和对比批次2中无此雇员, 故为"",用于页面对比展示
                BatchCompareItemBO itemBO = new BatchCompareItemBO();
                itemBO.setMappingKey(compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));
                // 当前批次
                itemBO.setCurrentBatch(currentBatch);
                itemBO.setSrcValue(this.getItemValueFromDBObject(srcItem));
                itemBO.setTgtValue("");
                itemBO.setTgtTwoValue("");
                batchCompareItemBOList.add(itemBO);
            });
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);
        } else if (!srcEmpResultData.isPresent()
                && tgtEmpResultData.isPresent()
                && !tgtTwoEmpResultData.isPresent()) {
            // 如果当前雇员只存在于对比批次1中
            empResultBO.setInWhichBatchForEmpId(InWitchCompareBatchEnum.TGT_ONLY.getValue());

            // 取出对比批次1的薪资项列表
            DBObject tgtCatalog = (DBObject) tgtEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> tgtPayItems = (List<DBObject>) tgtCatalog.get(PayItemName.PAY_ITEMS);

            compareMappingList.forEach(compareKeyMap -> {
                // 取出对比批次1薪资项
                DBObject tgtItem = getItemsFromDbObject(tgtPayItems, compareKeyMap.get(PayItemName.TGT_COMPANY_KEY));

                // 对比批次1的薪资项放入对应的对比结果BO中,源批次和和对比批次2中无此雇员, 故为"",用于页面对比展示
                BatchCompareItemBO itemBO = new BatchCompareItemBO();
                itemBO.setMappingKey(compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));
                // 当前批次
                itemBO.setCurrentBatch(currentBatch);
                itemBO.setSrcValue("");
                itemBO.setTgtValue(this.getItemValueFromDBObject(tgtItem));
                itemBO.setTgtTwoValue("");
                batchCompareItemBOList.add(itemBO);
            });
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);

        } else if (!srcEmpResultData.isPresent()
                && !tgtEmpResultData.isPresent()
                && tgtTwoEmpResultData.isPresent()) {
            // 如果当前雇员只存在于对比批次2中
            empResultBO.setInWhichBatchForEmpId(InWitchCompareBatchEnum.TGTTWO_ONLY.getValue());

            // 对比批次2的薪资项列表
            DBObject tgtTwoCatalog = (DBObject) tgtTwoEmpResultData.get().get(PayItemName.CATALOG);
            List<DBObject> tgtTwoPayItems = (List<DBObject>) tgtTwoCatalog.get(PayItemName.PAY_ITEMS);

            compareMappingList.forEach(compareKeyMap -> {
                // 取出对比批次2薪资项
                DBObject tgtTwoItem = getItemsFromDbObject(tgtTwoPayItems, compareKeyMap.get(PayItemName.TGTTWO_COMPANY_KEY));

                // 对比批次2的薪资项放入对应的对比结果BO中,源批次和对比批次1中无此雇员, 故为"",用于页面对比展示
                BatchCompareItemBO itemBO = new BatchCompareItemBO();
                itemBO.setMappingKey(compareKeyMap.get(PayItemName.SRC_COMPANY_KEY));
                // 当前批次
                itemBO.setCurrentBatch(currentBatch);
                itemBO.setSrcValue("");
                itemBO.setTgtValue("");
                itemBO.setTgtTwoValue(this.getItemValueFromDBObject(tgtTwoItem));
                batchCompareItemBOList.add(itemBO);
            });
            empResultBO.setItemList(batchCompareItemBOList);
            batchCompareEmpBOList.add(empResultBO);

        }
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
                .include(PayItemName.EMPLOYEE_COMPANY_ID)
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value");
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
