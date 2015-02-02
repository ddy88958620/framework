package com.handu.apollo.core;

import com.handu.apollo.base.CmdVo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by markerking on 14-7-14.
 */
public interface CmdList {
    /**
     * 必须增加注解@PostConstruct
     * */
    public void init();
    /**
     * 获得系统Id
     * @return
     */
    public Long getMisId();
    public String getPackage();
    public List<CmdVo> getCommands();
}
