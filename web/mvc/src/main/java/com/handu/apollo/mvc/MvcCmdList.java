package com.handu.apollo.mvc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.handu.apollo.base.CmdVo;
import com.handu.apollo.core.CmdList;
import com.handu.apollo.utils.ClassUtil;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by liuhx on 2014/12/12.
 */
public class MvcCmdList implements CmdList {

    private static final Log LOG = Log.getLog(MvcCmdList.class.getName());

    private List<CmdVo> cmdList = Lists.newArrayList();

    @Autowired
    Environment env;

    private Long misId;
    private String since = null;

    private String packages;

    @Override
    public Long getMisId() {
        if (misId == null) {
            misId = env.getProperty(StringPool.MIS_ID, Long.class, 1L);
        }
        return misId;
    }

    @Override
    public String getPackage() {
        return packages;
    }

    @PostConstruct
    @Override
    public void init() {
        String[] packages = getPackage().split(",");

        List<Class<?>> classes = Lists.newArrayList();
        for (String packag : packages) {
            classes.addAll(ClassUtil.getClasses(getPackage()));
        }
        cmdList = getCmdVoList(classes);
    }

    @Override
    public List<CmdVo> getCommands() {
        if (cmdList == null) {
            init();
        }
        return cmdList;
    }

    public String getSince() {
        if (since == null) {
            since = env.getProperty("system.since", "0.1.0");
            ;
        }
        return since;
    }

    public void setMisId(Long misId) {
        this.misId = misId;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public List<CmdVo> getCmdVoList(List<Class<?>> classes) {
        Map<String, String> groupMap = Maps.newHashMap();

        Map<String, List<CmdVo>> extedsCmd = Maps.newHashMap();
        Map<String, CmdVo> cmdMapping = Maps.newHashMap();

        for (Class<?> aClass : classes) {
            proccessCmd(aClass, cmdMapping, groupMap, extedsCmd);
        }

        proccessExtend(cmdMapping, extedsCmd);

        List<CmdVo> collection = Lists.newArrayList(cmdMapping.values());
        for (CmdVo cmd : collection) {
            if (!cmd.getIncludes().isEmpty()) {
                for (String include : cmd.getIncludes()) {
                    if (!cmdMapping.containsKey(include)) {
                        LOG.warn(cmd.getName() + " Include 不存在的cmd：" + include);
                        continue;
                    }

                    CmdVo vo = new CmdVo();
                    vo.setName(cmd.getName() + StringPool.UNDERLINE + include);
                    vo.setGroup("0");
                    vo.setGroupName(cmd.getName());
                    vo.setDescription(getMisId() + StringPool.COLON + include);
                    vo.setSince(getSince());
                    vo.setUsage("");
                    vo.setType("include");
                    vo.setMisId(getMisId());
                    if (cmdMapping.containsKey(vo.getName())) {
                        LOG.warn("含有重复的CmdGroup :" + vo.getName());
                    }
                    cmdMapping.put(vo.getName(), vo);
                }
            }

            if (!cmd.getExcludes().isEmpty()) {
                for (String exclude : cmd.getExcludes()) {
                    if (!cmdMapping.containsKey(exclude)) {
                        LOG.warn(cmd.getName() + " Exclude 不存在的cmd：" + exclude);
                    }
                }
            }


        }

        //分组信息处理
        for (String groupName : groupMap.keySet()) {
            CmdVo vo = new CmdVo();

            vo.setName(groupName);
            vo.setDescription(groupMap.get(groupName));
            vo.setGroup("0");
            vo.setGroupName("0");
            vo.setSince("");
            vo.setUsage("");
            vo.setType("group");
            vo.setMisId(getMisId());
            if (cmdMapping.containsKey(vo.getName())) {
                LOG.warn("含有重复的CmdGroup :" + vo.getName());
            }
            cmdMapping.put(vo.getName(), vo);
        }

        return Lists.newArrayList(cmdMapping.values());
    }

    public void proccessCmd(Class<?> aClass, Map<String, CmdVo> cmdMapping, Map<String, String> groupMap, Map<String, List<CmdVo>> extedsCmd) {
        RestController rc = aClass.getAnnotation(RestController.class);
        if (rc == null) {
            return;
        }
        LOG.debug("ProccessCmd " + aClass.getName());
        MvcGroup mg = aClass.getAnnotation(MvcGroup.class);
        if (mg != null) {
            String group = mg.value();
            String groupName = aClass.getSimpleName();

            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                if (rm != null) {
                    MvcDescription md = method.getAnnotation(MvcDescription.class);
                    CmdVo vo = new CmdVo();

                    vo.setName(rm.value()[0]);
                    vo.setDescription(md.value());
                    vo.setGroupName(groupName);
                    vo.setGroup(group);
                    vo.setCmdClass(aClass);
                    vo.setSince(getSince());
                    vo.setType("cmd");
                    vo.setMisId(getMisId());

                    Set<String> dependents = new HashSet<String>();
                    Set<String> excludes = new HashSet<String>();

                    vo.setIncludes(dependents);
                    vo.setExcludes(excludes);

                    excludes.add(vo.getName());
                    if (mg.includes().length > 0) {//MvcGroup
                        dependents.addAll(Arrays.asList(mg.includes()));
                    }

                    if (md.includes().length > 0) {//MvcDescription
                        dependents.addAll(Arrays.asList(md.includes()));
                    }
                    if (md.excludes().length > 0) {//MvcDescription
                        excludes.addAll(Arrays.asList(md.excludes()));
                    }

                    if (!md.extend().isEmpty()) {
                        vo.setExtend(md.extend());
                        List<CmdVo> list = extedsCmd.get(md.extend());
                        if (list == null) {
                            list = Lists.newArrayList();
                            extedsCmd.put(md.extend(), list);
                        }
                        list.add(vo);
                    } else {
                        dependents.removeAll(excludes);
                    }

                    groupMap.put(groupName, group);

                    if (cmdMapping.containsKey(vo.getName())) {
                        LOG.warn("含有重复的Cmd :" + vo.getName() + " " + method.getName());
                    }
                    cmdMapping.put(vo.getName(), vo);
                }
            }
        } else {
            LOG.warn(String.format("扫描RestController注解的类[%s]，但它没有MvcGroup注解", aClass.getName()));
        }

    }

    public void proccessExtend(Map<String, CmdVo> cmdMapping, Map<String, List<CmdVo>> extedsCmd) {

        for (List<CmdVo> list : extedsCmd.values()) {
            for (CmdVo vo : list) {
                //检测循环继承
                CmdVo parent = getParentCmdVo(cmdMapping, vo);
                Set<String> dependents = vo.getIncludes();

                if (parent == null) {
                    LOG.warn(vo.getName() + " extend [" + vo.getExtend() + "] not exits " + vo.getExtend());
                } else {
                    dependents.addAll(parent.getIncludes());
                }
                dependents.removeAll(vo.getExcludes());
            }
        }
    }

    public CmdVo getParentCmdVo(Map<String, CmdVo> cmdMapping, CmdVo vo) {
        CmdVo parent = cmdMapping.get(vo.getExtend());

        if (parent == null) {
            return null;
        }
        if (parent.getExtend() == null || parent.getExtend().isEmpty())
            return parent;

        List<String> paths = Lists.newArrayList();
        paths.add(parent.getName());
        while (true) {
            CmdVo temp = cmdMapping.get(parent.getExtend());
            if (temp == null) {
                LOG.warn(parent.getName() + " extend [" + parent.getExtend() + "] not exits " + vo.getExtend());
                parent.setExtend("");
            } else {
                parent = temp;
            }

            if (parent.getExtend() == null || parent.getExtend().isEmpty()) {
                //倒序初始化,extend 设置为空
                for (int i = paths.size() - 1; i >= 0; i--) {
                    String name = paths.get(i);
                    CmdVo child = cmdMapping.get(name);

                    Set<String> dependents = child.getIncludes();

                    dependents.addAll(parent.getIncludes());
                    dependents.removeAll(child.getExcludes());

                    child.setExtend("");
                }
                return parent;
            }
            if (paths.contains(parent.getName())) {
                String error = "[" + vo.getName() + " <==> " + parent.getName() + "] 含有循环依赖继承!!";
                LOG.error(error);
                throw new RuntimeException(error);
            } else {
                paths.add(parent.getName());
            }
        }
    }
}
