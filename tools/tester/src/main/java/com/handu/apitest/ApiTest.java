package com.handu.apitest;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by wangfei on 2014/6/3.
 */
public class ApiTest {

    private static final String LINE = "=================================================================================";

    public static void main(String[] args) throws Exception {
        //用于记录所有出错的CMD
        List errorcmdlist = new ArrayList();
        int allCmdNum = 0;
        int testCmdNum = 0;
        //记录系统中所有的CMD
        Map<String, String> allCmdMap = new HashMap<String, String>();

        FileToJson ftj = new FileToJson();
        HttpClientHandle hch = new HttpClientHandle();
        //初始化HTTPCLIENT，并登录到apollo系统
        hch.initHttpClient();
        //获取系统中的所有命令，包括分组
        String jsonstr = hch.getAllCmds();

        Map<String, Object> cmdlistmap = ftj.string2Json(jsonstr);
        List allcmdlist = (List) cmdlistmap.get("list");
        for (int i = 0; i < allcmdlist.size(); i++) {
            Map<String, String> cmdmap = (Map<String, String>) allcmdlist.get(i);
            //排除命令分组，将命令记入allCmdMap
            if (!cmdmap.get("type").equals("group")) {
                allCmdMap.put(cmdmap.get("id"), cmdmap.get("id"));
                allCmdNum++;
            }
        }
        //系统初始化完成


        FileViewer fv = new FileViewer();
        //获取所有测试数据文件
        //List filelist = fv.getListFiles("tools/tester/src/main/resources/newdata/", "json");
        List filelist = fv.getListFiles("src/main/resources/data/", "json");

        for (int i = 0; i < filelist.size(); i++) {
            Map jsonmaps = ftj.readJson2Map((String) filelist.get(i));
            if (jsonmaps != null) {
                //取组名
                String group = new String(((String) jsonmaps.get("group")).getBytes(), "utf-8");
                //取是否测试开关
                Boolean test = (Boolean) jsonmaps.get("test");
                if (test != null && test) {
                    //缓存Map，记录需要被缓存的数据
                    Map<String, Map<String, Object>> cacheMap = new HashMap<String, Map<String, Object>>();
                    //记录模块中命令的成功数
                    int successnum = 0;
                    //记录模块中命令的失败数
                    int failednum = 0;

                    System.out.println(LINE);
                    System.out.println("模块[" + group + "] 测试开始");

                    List cmdlist = (List) jsonmaps.get("cmdlist");
                    for (int j = 0; j < cmdlist.size(); j++) {
                        Map<String, Object> cmdMap = (Map<String, Object>) cmdlist.get(j);
                        //command命令
                        String command = cmdMap.get("command").toString();
                        //命令结果是否需要缓存
                        Boolean cache = cmdMap.get("cache") == null ? false : Boolean.valueOf(cmdMap.get("cache") + "");
                        //命令结果是否需要缓存
                        String expect = cmdMap.get("expect") == null ? "success" : (cmdMap.get("expect") + "");

                        //组装Url
                        URIBuilder ub = new URIBuilder()
                                .setScheme("http")
                                .setHost("127.0.0.1:8080")
                                .setPath("/client/api")
                                .setParameter("command", command);
                        //设置URL参数
                        Map<String, String> paramMap = (Map<String, String>) cmdMap.get("params");
                        Iterator<String> paramIter = paramMap.keySet().iterator();
                        while (paramIter.hasNext()) {
                            String paramname = paramIter.next();
                            //根据value判断，是否需要从缓存中取数据
                            String pValue = paramMap.get(paramname);
                            try {
                                if (pValue.indexOf('#') == 0) {
                                    pValue = pValue.substring(1);
                                    String[] pvs = pValue.split("\\.");
                                    Map<String, Object> itemCacheMap = cacheMap.get(pvs[0]);
                                    pValue = ""+itemCacheMap.get(pvs[1]);
                                    //System.out.println("发现需要替换的字符串，paramname =["+pValue+"]");
                                }
                                ub.setParameter(paramname, pValue);
                            } catch (Exception e) {
                                System.out.println("解析参数异常，忽略该参数，----" + paramname);
                            }
                        }
                        try {
                            //执行测试
                            URI uri = ub.build();
                            System.out.println("开始测试命令，command：[" + command + "]");
                            String responseString = hch.httpPost(uri, expect);
                            if (responseString != null) {
                                successnum++;
                                if (cache) {
                                    Map<String, Object> responsemap = ftj.string2Json(responseString);
                                    String cachekey = cmdMap.get("cachekey") == null ? command : (String) cmdMap.get("cachekey");
                                    cacheMap.put(cachekey, responsemap);
                                }
                            } else {
                                failednum++;
                                errorcmdlist.add(group + ":" + command);
                            }
                            testCmdNum++;
                            System.out.println(LINE);
                            allCmdMap.remove(command);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("模块[" + group + "] 测试结束，共测试command[ " + (successnum + failednum) + " ]个,成功[ " + successnum + " ]个，失败[ " + failednum + " ]个");
                } else {
                    System.out.println("跳过测试模块[" + group + "] ");
                }
            }
        }

        System.out.println();
        System.out.println("-----------------------------测试总结-----------------------------");
        System.out.println("系统中共有COMMAND数：" + allCmdNum);
        System.out.println("本次测试的COMMAND数：" + testCmdNum);
        System.out.println("以下COMMAND没有被测试：");
        Iterator<String> iter = allCmdMap.keySet().iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        System.out.println();
        System.out.println("-----------------------------测试结果-----------------------------");
        if (errorcmdlist.size() > 0) {
            System.out.println("Command测试完成，在以下命令的执行过程中发现错误！");
            for (int i = 0; i < errorcmdlist.size(); i++) {
                System.out.println(i + " : " + errorcmdlist.get(i));
            }
            throw new Exception("Command测试过程中发现错误！");
        } else {
            System.out.println("Command测试完成，没有发现错误！");
        }
    }
}
