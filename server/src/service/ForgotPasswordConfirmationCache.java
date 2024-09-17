
package service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import utils.PendingForgotPasswordReset;

@Service
public class ForgotPasswordConfirmationCache {

    private final RedisTemplate<String,String> redis;
    private final JsonConversionService jsonConverter;
    private final String cacheKeyPrefix = "pending_forgot_password_confirmation::";

    @Autowired
    public ForgotPasswordConfirmationCache(
        RedisTemplate<String, String> redis, 
        JsonConversionService jsonConverter
    ) {
        this.jsonConverter = jsonConverter;
        this.redis = redis;
    }

    public void store(PendingForgotPasswordReset registrationData, int expirationInMinutes) {
        String key = cacheKeyPrefix + registrationData.getEmail();
        String json = jsonConverter.encode(registrationData);
        redis.opsForValue().set(key, json, expirationInMinutes, TimeUnit.MINUTES);
    }

    public PendingForgotPasswordReset retrieve(String email) {
        String key = cacheKeyPrefix + email;
        String retrieved = redis.opsForValue().get(key);
        return (retrieved != null)
            ? jsonConverter.decode(retrieved, PendingForgotPasswordReset.class)
            : null;
    }

    public void delete(String key) {
        redis.delete(cacheKeyPrefix + key);
    }
}