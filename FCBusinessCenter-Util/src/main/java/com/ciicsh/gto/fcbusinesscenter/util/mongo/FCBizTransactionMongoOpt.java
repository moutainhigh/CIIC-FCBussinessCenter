package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.fcbusinesscenter.util.entity.DistributedTranEntity;
import com.mongodb.BasicDBObject;
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
        mongoTemplate.indexOps(EVENT_SOURCING).ensureIndex(indexDefinition);
    }

    public int commitEvent(String batchCode, int bathType, String eventName, int status){
        Criteria criteria = Criteria.where("batch_code").is(batchCode)
                .and("batch_type").is(bathType)
                .and("events").elemMatch(Criteria.where("event_name").is(eventName));
        Query query = Query.query(criteria);
        Update update = Update.update("events.$.event_status",status);
        return this.upsert(query,update);
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

    public int getStatus(String batchCode){
        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = Query.query(criteria);
        DBObject find = mongoTemplate.findOne(query,DBObject.class);
        int status = (Integer) find.get("completed");
        /*List<DBObject> events = (List<DBObject>) find.get("events");
        for (DBObject event: events) {
            event.get("event_name")
        }*/
        return status;
    }
}