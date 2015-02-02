package com.handu.apollo.cache;

import config.TestRedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by markerking on 14-4-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestRedisConfig.class})
public class RedisDaoTest {

    @Autowired
    RedisDao _redisDao;

    @Test
    public void test1() throws Exception {
        _redisDao.valueSet("test-key", "test-value");
    }

    @Test
    public void test2() throws Exception {
        assertEquals(_redisDao.valueGet("test-key"), "test-value");
    }

    @Test
    public void test3() {
        assertTrue(_redisDao.exists("test-key"));
    }

    @Test
    public void test4() throws Exception {
        _redisDao.valueDelete("test-key");
    }

    @Test
    public void test5() {
        _redisDao.delete("test-key");
    }

}
