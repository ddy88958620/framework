package com.handu.apitest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Map;

/**
 * Created by wangfei on 2014/6/3.
 */
public class FileToJson {

    public Map readJson2Map(String filename) {
        Map maps = null;

        StringBuilder json = new StringBuilder();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
           // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if(tempString.trim().indexOf("//") != 0) {
                    json.append(tempString);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            maps = objectMapper.readValue(json.toString(), Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
    }

    public Map<String, Object> string2Json(String jsonstr) {
        Map<String, Object> maps = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            maps = objectMapper.readValue(jsonstr, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
    }
}
