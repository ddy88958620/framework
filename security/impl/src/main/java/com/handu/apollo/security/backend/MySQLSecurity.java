package com.handu.apollo.security.backend;

import com.google.common.collect.Maps;
import com.handu.apollo.data.Dao;
import com.handu.apollo.security.domain.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by markerking on 14/10/22.
 */
public class MySQLSecurity implements SecurityBackend {

    @Autowired
    private Dao _dao;

    @Transactional(readOnly = true)
    public Long getUserId(String username) {
        return _dao.get(SecurityQuery.BASE, SecurityQuery.userPk, username);
    }

    @Transactional(readOnly = true)
    public SecurityUser getUser(String username) {
        return _dao.get(SecurityQuery.BASE, SecurityQuery.user, username);
    }

    @Transactional(readOnly = true)
    public Set<String> getUserRoles(Long userId) {
        List<String> userRoles = _dao.getList(SecurityQuery.BASE, SecurityQuery.roles, userId);
        Set<String> roles = new HashSet<String>();
        for (String userRole : userRoles) {
            roles.add(userRole);
        }
        return roles;
    }

    @Transactional(readOnly = true)
    public Set<String> getUserPermissions(Long userId) {
        List<String> userCmds = _dao.getList(SecurityQuery.BASE, SecurityQuery.permissions, userId);
        Set<String> cmds = new HashSet<String>();
        for (String userCmd : userCmds) {
            cmds.add(userCmd);
        }
        return cmds;
    }

    @Transactional(readOnly = true)
    public Set<String> getUserPermissionsByMisid(Long userId,String misId) {
        Map<String, String> params = Maps.newHashMap();
        params.put("userId", userId.toString());
        params.put("misId",misId);

        List<String> userCmds = _dao.getList(SecurityQuery.BASE, SecurityQuery.permissionsByMisid, params);
        Set<String> cmds = new HashSet<String>();
        for (String userCmd : userCmds) {
            cmds.add(userCmd);
        }
        return cmds;
    }
}
