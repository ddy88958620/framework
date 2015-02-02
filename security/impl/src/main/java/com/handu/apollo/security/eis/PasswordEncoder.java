package com.handu.apollo.security.eis;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha512Hash;

import java.security.SecureRandom;

/**
 * Created by markerking on 14-5-16.
 */
public class PasswordEncoder {

    public static final String DEFAULT_SALT = "HANDU_APOLLO_DEFAULT_SALT_!)@$";
    public static final int DEFAULT_HASH_ITERATIONS = 1024;

    public static String encodePassword(String password) {
        return encodePassword(password, DEFAULT_SALT, DEFAULT_HASH_ITERATIONS);
    }

    public static String encodePassword(String password, String salt) {
        return encodePassword(password, salt, DEFAULT_HASH_ITERATIONS);
    }

    public static String encodePassword(String password, String salt, int hashIterations) {
        return new Sha512Hash(password, salt, hashIterations).toBase64();
    }

    public static String generatSalt(String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = DEFAULT_SALT;
        }
        SecureRandom sesssionKeyRandom = new SecureRandom();
        byte sessionKeyBytes[] = new byte[20];
        sesssionKeyRandom.nextBytes(sessionKeyBytes);
        String base64Random = Base64.encodeToString(sessionKeyBytes);
        return encodePassword(base64Random + salt);
    }
}