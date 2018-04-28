package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.test;
import com.ciicsh.gt1.config.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class PlayMongo {
    @Autowired
    private static MongoConfig mongoConfig;


    public static void main(String[] args) {

        Document doc = new Document("batch_id", "GL000007_201801_0000000179");

        MongoCursor<Document> cursor = mongoConfig.mongoClient().getDatabase("payroll_db").getCollection("fc_payroll_calc_result_table").find(doc).iterator();

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
                break;
            }
        } finally {
            cursor.close();
        }


    }
}