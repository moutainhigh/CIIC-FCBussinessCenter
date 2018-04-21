package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.mongodb.DBObject;
import org.apache.maven.lifecycle.internal.LifecycleStarter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by bill on 18/2/2.
 */
@Service
public class AdjustBatchServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(AdjustBatchServiceImpl.class);

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    /**
     * 删除批次
     * @param batchCodes
     * @return
     */
    public int deleteAdjustBatch(String batchCodes) {
        String[] codes = batchCodes.split(",");
        if (codes.length > 0) {
            int rowAffected = adjustBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
            logger.info("row affected : " + String.valueOf(rowAffected));
            return rowAffected;
        }
        return 0;
    }

    /**
     * 新增调整批次（正常批次 或者 调整批次来）
     * @param rootCode
     * @param originCode
     * @return
     */
    public int addAdjustBatch(String rootCode, String originCode, String adjustCode){
        int rowAffected = 0;
        Criteria criteria = null;
        Query query = null;
        List<DBObject> list = null;
        if(StringUtils.isEmpty(rootCode)){ // 从调整批次中 COPY
            criteria = Criteria.where("batch_code").is(originCode);
            query = Query.query(criteria);
            list = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else { // 从正常批次中 COPY
            criteria = Criteria.where("batch_code").is(rootCode);
            query = Query.query(criteria);
            list = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }
        if (list != null && list.size() > 0) {
            list.forEach(dbObject -> {
                DBObject catalog = (DBObject) dbObject.get("catalog");
                DBObject batchInfo = (DBObject) catalog.get("batch_info");
                dbObject.put("_id", new ObjectId());
                dbObject.put("batch_code", adjustCode); // update origin code to adjust code
                dbObject.put("origin_batch_code", originCode);
                dbObject.put("root_batch_code", rootCode);
                dbObject.put("show_adj", false); // 调整差异值是否需要显示
                catalog.put("adjust_items", null); // 调整差异字段列表
                Object netPay = findValByName(catalog, PayItemName.EMPLOYEE_NET_PAY);
                dbObject.put("net_pay",netPay); // 存储上个批次的实发工资
                batchInfo.put("payroll_type", BatchTypeEnum.ADJUST.getValue());
            });
        }
        adjustBatchMongoOpt.createIndex();
        rowAffected = adjustBatchMongoOpt.batchInsert(list); // batch insert to mongodb
        logger.info("batch insert counts : " + String.valueOf(rowAffected));
        return rowAffected;
    }

    private Object findValByName(DBObject catalog, String payItemName) {
        List<DBObject> list = (List<DBObject>) catalog.get("pay_items");
        return findValByName(list,payItemName);
    }

    private Object findValByName(List<DBObject> list, String payItemName) {
        Optional<DBObject> find = list.stream().filter(p -> p.get("item_name").equals(payItemName)).findFirst();
        if (find.isPresent()) {
            return find.get().get("item_value");
        }
        return null;
    }

}
