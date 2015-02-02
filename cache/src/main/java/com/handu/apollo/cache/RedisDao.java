package com.handu.apollo.cache;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by markerking on 14-4-17.
 */
@Repository
public class RedisDao {

    @Autowired
    Environment env;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    private final String PREFIX_DEFAULT = "public:redis:";
    private String prefix;

    @PostConstruct
    public void init() {
        prefix = env.getProperty("redisDao.prefix", PREFIX_DEFAULT);
    }

    public void valueSet(String key, String value) {
        valueOperations.set(prefix + key, value);
    }

    public String valueGet(String key) {
        return valueOperations.get(prefix + key);
    }

    public void valueDelete(String key) {
        valueOperations.set(prefix + key, null);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(prefix + key);
    }

    public void delete(String key) {
        redisTemplate.delete(prefix + key);
    }

    public void delete(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        List<String> delKeys = Lists.newArrayList();
        for (String key : keys) {
            delKeys.add(prefix + key);
        }
        redisTemplate.delete(delKeys);
    }
}
