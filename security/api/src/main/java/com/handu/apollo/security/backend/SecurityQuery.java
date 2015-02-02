package com.handu.apollo.security.backend;

/**
 * Created by markerking on 14/10/22.
 */
public interface SecurityQuery {
    public static final String BASE = "shiro.query.";
    public static final String userPk = "getSecurityUserPk";
    public static final String user = "getSecurityUser";
    public static final String roles = "getSecurityRoles";
    public static final String permissions = "getSecurityPermissions";
    public static final String permissionsByMisid = "getSecurityPermissionsByMisid";
}
