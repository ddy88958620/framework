package com.handu.apollo.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.handu.apollo.base.CmdVo;
import com.handu.apollo.core.CmdList;
import com.handu.apollo.utils.ClassUtil;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by markerking on 14-5-12.
 */
@Component
public class ApiCmdList implements CmdList {

    private List<CmdVo> cmdList = Lists.newArrayList();
    private static final Log LOG = Log.getLog(ApiCmdList.class);

    @Autowired
    Environment env;

    @PostConstruct
    public void init() {
        List<Class<?>> classes = ClassUtil.getClasses(getPackage());
        Map<String, String> groupMap = Maps.newHashMap();
        for (Class<?> aClass : classes) {
            ApiCommand at = aClass.getAnnotation(ApiCommand.class);
            if (at != null) {
                CmdVo vo = new CmdVo();

                vo.setCmdClass(aClass);
                vo.setName(at.name());
                vo.setDescription(at.description());
                vo.setGroup(at.group());
                vo.setGroupName(at.groupName());
                vo.setSince(at.since());
                vo.setUsage(at.usage());
                vo.setType("cmd");
                vo.setMisId(getMisId());

                cmdList.add(vo);
                groupMap.put(at.groupName(), at.group());
            } else {
                LOG.warn(String.format("扫描路径[%s]中发现非ApiCommand注解的类[%s]，但它没有ApiCommand注解", getPackage(), aClass.getName()));
            }
        }

        //分组信息处理
        Iterator<String> iter = groupMap.keySet().iterator();
        while (iter.hasNext()) {
            String groupName = iter.next();

            CmdVo vo = new CmdVo();

            vo.setName(groupName);
            vo.setDescription(groupMap.get(groupName));
            vo.setGroup("0");
            vo.setGroupName("0");
            vo.setSince("");
            vo.setUsage("");
            vo.setType("group");
            vo.setMisId(getMisId());

            cmdList.add(vo);
        }
    }

    private Long misId;

    @Override
    public Long getMisId() {
        if (misId == null && env != null) {
            misId = env.getProperty(StringPool.MIS_ID, Long.class, 1L);
        }
        return misId;
    }

    public void setMisId(Long misId) {
        this.misId = misId;
    }

    public String getPackage() {
        return "com.handu.apollo.api.command";
    }

    public List<CmdVo> getCommands() {
        return cmdList;
    }
}
