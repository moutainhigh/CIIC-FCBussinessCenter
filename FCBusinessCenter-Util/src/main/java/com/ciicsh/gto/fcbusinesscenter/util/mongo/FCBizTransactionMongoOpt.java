package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.fcbusinesscenter.util.entity.BatchTypeEnum;
import com.ciicsh.gto.fcbusinesscenter.util.entity.DistributedTranEntity;
import com.ciicsh.gto.fcbusinesscenter.util.entity.TransactionStatusEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bill on 18/5/29.
 */
@Component
public class FCBizTransactionMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(FCBizTransactionMongoOpt.class);

    public static final String EVENT_SOURCING  = "event_sourcing_table";

    public FCBizTransactionMongoOpt() {
        super(EVENT_SOURCING);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    private void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("batch_code",1);
        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        this.getMongoTemplate().indexOps(EVENT_SOURCING).ensureIndex(indexDefinition);
    }

    /*单个提交更新*/
    public int commitEvent(String batchCode, int bathType, String eventName, int status){
        Criteria criteria = Criteria.where("batch_code").is(batchCode)
                .and("batch_type").is(bathType)
                .and("events").elemMatch(Criteria.where("event_name").is(eventName));
        Query query = Query.query(criteria);
        long totalCount = this.getMongoTemplate().find(query,DBObject.class,EVENT_SOURCING).stream().count();
        if(totalCount > 0) {
            Update update = Update.update("events.$.event_status", status);
            return this.upsert(query, update);
        }else {
            return 0;
        }
    }

    /*批量提交更新*/
    public int commitBatchEvents(List<String> batchCodes, String eventName, int status){
        if(batchCodes == null || batchCodes.size() == 0){
            return 0;
        }
        int rowAffected = 0;
        for (String code : batchCodes) {
            Criteria criteria = Criteria.where("batch_code").is(code)
                    .and("events").elemMatch(Criteria.where("event_name").is(eventName));
            Query query = Query.query(criteria);
            long totalCount = this.getMongoTemplate().find(query, DBObject.class, EVENT_SOURCING).stream().count();
            if (totalCount > 0) {
                Update update = Update.update("events.$.event_status", status);
                rowAffected += this.upsert(query, update);
            }
        }
        return rowAffected;
    }

    public int initDistributedTransaction(DistributedTranEntity tranEntity){

        Criteria criteria = Criteria.where("transaction_name")
                .is(tranEntity.getTransactionName())
                .and("completed").is(0)
                .and("batch_code").is(tranEntity.getBatchCode())
                .and("batch_type").is(tranEntity.getBatchType());
        Query query = Query.query(criteria);
        Update update = Update.update("events",tranEntity.getEvents());
        createIndex();
        return this.upsert(query,update);
    }

    public int getTransactionStatus(String batchCode){

        DBObject query = new BasicDBObject();
        query.put("batch_code",batchCode);
        DBCursor cursor = this.getMongoTemplate().getCollection(EVENT_SOURCING).find(query);
        while (cursor.hasNext()){
            DBObject find =cursor.next();
            List<DBObject> events = (List<DBObject>) find.get("events");
            boolean validate = events.stream().anyMatch(p -> (int)p.get("event_status") == TransactionStatusEnum.COMPLETE.getValue());
            return validate ? TransactionStatusEnum.COMPLETE.getValue() : TransactionStatusEnum.UNCOMPLETE.getValue();
        }
        return 0;

    }
}