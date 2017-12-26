package com.ciicsh.gto.salarymanagementcommandservice.util;

import com.ciicsh.gto.afsystemmanagecenter.basicdataservice.client.EntityIdClient;
import com.ciicsh.gto.afsystemmanagecenter.basicdataservice.tips.IdsTip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by jiangtianning on 2017/11/13.
 */
@Component
public class PrEntityIdClient {

    @Autowired
    private EntityIdClient client;

    @Autowired
    private CacheManager cacheManager;

    public static final String CACHE_NAME = "ENTITY_CACHE";

    public static final String PR_GROUP_CAT_ID = "EC0028";
    public static final String PR_GROUP_TEMPLATE_CAT_ID = "EC0026";
    public static final String PR_ITEM_CAT_ID = "EC0027";
    public static final String PR_ACCOUNT_SET_CAT_ID = "EC0029";
    public static final String PR_EMPLOYEE_CAT_ID = "EC0030";

    private static final String[] CAT_ARRAY = {PR_GROUP_CAT_ID, PR_GROUP_TEMPLATE_CAT_ID, PR_ITEM_CAT_ID,
            PR_ACCOUNT_SET_CAT_ID, PR_EMPLOYEE_CAT_ID};

    public String getEntityId(String catId) {

        if (!Arrays.asList(CAT_ARRAY).contains(catId)) {
            return "";
        }

        String result = getEntityIdFromList(catId);

        return result;
    }

    private IdsTip generateEntityId(String catId) {
//        return client.getEntityIds("EC0009", 10);mvn
        return client.getEntityIds(catId, 5);
    }

    private LinkedList<String> getEntityIdList(String catId) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        LinkedList<String> entityIdList = new LinkedList<>();
        Cache.ValueWrapper cacheWrapper = cache.get(catId);

        if (cacheWrapper == null) {
            entityIdList.addAll(generateEntityId(catId).getData());
            cache.put(catId, entityIdList);
            return entityIdList;
        }

        entityIdList = (LinkedList) cacheWrapper.get();
        if (entityIdList.size() == 0) {
            entityIdList.addAll(generateEntityId(catId).getData());
            cache.put(catId, entityIdList);
        }

        return entityIdList;
    }

    private String getEntityIdFromList(String catId) {
        String entityId = getEntityIdList(catId).pop();
        return entityId;
    }
}
