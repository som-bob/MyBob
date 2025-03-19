package com.my.bob.core.external.redis.service;

public interface RedisService {
    void set(String key, String value);
    String get(String key);
    void del(String key);
}
