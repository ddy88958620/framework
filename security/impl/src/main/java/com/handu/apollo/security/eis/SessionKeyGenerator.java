package com.handu.apollo.security.eis;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * Created by markerking on 14-5-15.
 */
public class SessionKeyGenerator implements SessionIdGenerator {
    @Override
    public Serializable generateId(Session session) {
        SecureRandom sesssionKeyRandom = new SecureRandom();
        byte sessionKeyBytes[] = new byte[20];
        sesssionKeyRandom.nextBytes(sessionKeyBytes);
        //Tomcat中Cookie value结尾是=号会出现问题，why?
        return Base64.encodeToString(sessionKeyBytes).replaceAll("=", "/");
    }
}