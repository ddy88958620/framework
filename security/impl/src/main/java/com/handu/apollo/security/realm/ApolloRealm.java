package com.handu.apollo.security.realm;

import com.handu.apollo.security.backend.SecurityBackend;
import com.handu.apollo.security.domain.SecurityUser;
import com.handu.apollo.security.utils.SessionUtil;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * Created by markerking on 14-5-14.
 */
public class ApolloRealm extends AuthorizingRealm {

    private static final Log LOG = Log.getLog(ApolloRealm.class);

    @Autowired
    private SecurityBackend securityBackend;

    @Autowired
    Environment env;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String misId = env.getProperty(StringPool.MIS_ID);

        String username = (String) principals.getPrimaryPrincipal();
        Long userId = securityBackend.getUserId(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        authorizationInfo.setRoles(securityBackend.getUserRoles(userId));
        if(misId.isEmpty()){
            authorizationInfo.setStringPermissions(securityBackend.getUserPermissions(userId));
        } else{
            authorizationInfo.setStringPermissions(securityBackend.getUserPermissionsByMisid(userId, misId));
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        SecurityUser user = securityBackend.getUser(username);

        if (user == null) {
            LOG.debug("没有找到用户[" + username + "]");
            throw new UnknownAccountException();
        }

        if (user.getRemoved() != null) {
            LOG.debug("用户[" + username + "]已锁定");
            throw new LockedAccountException();
        }

        //将用户ID放入Session中
        SessionUtil.set(StringPool.USER_PK, user.getId());
        SessionUtil.set(StringPool.USER, user);

        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }
}