package com.handu.apollo.security.backend;

import com.handu.apollo.security.domain.SecurityUser;

import java.util.Set;

/**
 * Created by markerking on 14/10/22.
 */
public interface SecurityBackend {
    public Long getUserId(String username);
    public SecurityUser getUser(String username);
    public Set<String> getUserRoles(Long userId);
    public Set<String> getUserPermissions(Long userId);
    public Set<String> getUserPermissionsByMisid(Long userId, String misId);
}
