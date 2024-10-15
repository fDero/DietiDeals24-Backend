package utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class KeyValueInMemoryCache {
    
    private final RedisTemplate<String, String> redis;

    @Autowired
    public KeyValueInMemoryCache(
        RedisTemplate<String, String> redis
    ) {
        this.redis = redis;
    }

    public void store(String key, String value, int expirationInMinutes) {
        redis.opsForValue().set(key, value, expirationInMinutes, TimeUnit.MINUTES);
    }

    public String retrieve(String key) {
        return redis.opsForValue().get(key);
    }

    public void delete(String key) {
        redis.delete(key);
    }
}
