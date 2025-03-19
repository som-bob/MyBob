package com.my.bob.stub;

import com.my.bob.core.external.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RedisStubService implements RedisService {

    private static final Map<String, String> redisMap = new HashMap<>();

    @Override
    public void set(String key, String value) {
        redisMap.put(key, value);
    }

    @Override
    public String get(String key) {
        return redisMap.get(key);
    }

    @Override
    public void del(String key) {
        redisMap.remove(key);
    }
}
