package com.ciicsh.gto.fcbusinesscenter.util.common;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class UrlUtility {

    /**
     *  url utility
     * @param url : http://localhost:8080/ciicsh-job-admin/jobinfo/doCompute?batchCode=GL0009&&jobHandler=computeShardingJobHandler
     * @return
     */
    public static boolean XXLJobRemoteCall(String url) throws IOException{

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            //StringEntity entity = new StringEntity();
            //post.setEntity(entity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        }
        finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return false;
    }
}
