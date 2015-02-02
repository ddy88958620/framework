package com.handu.apollo.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpUtilTest {

    private HttpUtil httpUtil;

    @Before
    public void init() {
        httpUtil = new HttpUtil();
    }

    @Test
    public void testGet() throws Exception {
        Assert.assertTrue(httpUtil.get("http://e.handuyishe.com/", null).contains("http://www.w3.org/1999/xhtml"));
    }

    @Test
    public void testPost() throws Exception {
        Assert.assertTrue(httpUtil.post("http://e.handuyishe.com/", null).contains("405 Not Allowed"));
    }
}