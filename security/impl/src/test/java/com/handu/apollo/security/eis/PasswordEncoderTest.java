package com.handu.apollo.security.eis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() throws Exception {
        String str1 = PasswordEncoder.encodePassword("password", "username");
        String str2 = PasswordEncoder.encodePassword("password", "username");
        assertEquals(str1, str2);

        String salt = PasswordEncoder.generatSalt("username");
        String str3 = PasswordEncoder.encodePassword("password", salt);
        String str4 = PasswordEncoder.encodePassword("password", salt);
        assertEquals(str3, str4);
    }
}