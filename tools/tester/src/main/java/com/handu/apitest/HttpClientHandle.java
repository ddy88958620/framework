package com.handu.apitest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

/**
 * Created by wangfei on 2014/6/3.
 */
public class HttpClientHandle {
    private HttpClient httpclient;
    private String sessionkey;

    public boolean initHttpClient(){
        httpclient = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("127.0.0.1:8080")
                    .setPath("/client/api")
                    .setParameter("command", "login")
                    .setParameter("username", "admin")
                    .setParameter("password", "password")
                    .build();

            HttpPost httpPost = new HttpPost(uri);
            httpclient.execute(httpPost);

            return true;
        } catch (Exception e) {
            System.out.println("登录失败"+e);
        }
        return false;
    }

    public String getAllCmds(){
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("127.0.0.1:8080")
                    .setPath("/client/api")
                    .setParameter("command", "listAllCmds")
                    .build();

            HttpPost httpPost = new HttpPost(uri);
            HttpResponse response = httpclient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            return null;
        }
    }

    public String httpPost(URI uri,String expect){
        HttpPost httpPost = new HttpPost(uri);
        try {
            HttpResponse response = httpclient.execute(httpPost);
            String responseStr = EntityUtils.toString(response.getEntity());
            //记录本次命令执行的返回状态
            String status = "failed";

            if(response.getStatusLine().getStatusCode() == 200){
                status = "success";
                System.out.println("\t命令执行[success]，本用例期望状态为["+expect+"],命令返回结果："+responseStr);
            }else{
                System.out.println("\t命令执行[failed]，本用例期望状态为["+expect+"],命令返回结果："+responseStr);
            }

            if(expect.equals(status)){
                System.out.println("\t测试结果为[成功]！");
                return responseStr;
            }else{
                System.out.println("\t测试结果为[失败]！");
                return null;
            }
        } catch (IOException e) {
            System.out.println("\t执行失败，返回结果：" + e);
        }
        return null;
    }
}
