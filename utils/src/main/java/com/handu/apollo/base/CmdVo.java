package com.handu.apollo.base;

import com.handu.apollo.utils.StringPool;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by markerking on 14/8/18.
 */
@Data
public class CmdVo implements Serializable {
    private Class cmdClass;
    private Long misId;//系统Id
    private String name; //Command名称
    private String description; //Command描述
    private String group; //中文名
    private String groupName; //英文名
    private String since; //起始版本，MVC中默认为0.1.0
    private String usage; //使用方法，MVC中没有
    private String type; // 分为 命令与分组两种
    private String extend;//继承cmd
    private Set<String> includes; //依赖cmd
    private Set<String> excludes;

    public String getId(){
        if(misId==null)
            return name;
        return misId + StringPool.COLON + name;
    }


    public String getParentId(){
        if(misId==null||"0".equals(groupName))
            return groupName;
        return misId + StringPool.COLON + groupName;
    }
}
