package service;

import temporary.PendingAccountRegistration;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheManagementService {

    RedisTemplate<String,String> runtime_cache;

    @Autowired
    public CacheManagementService(RedisTemplate<String, String> runtime_cache) {
        this.runtime_cache = runtime_cache;
    }

    public void store(String email, PendingAccountRegistration registrationData, int expirationInMinutes) {
        try {
            String json = PendingAccountRegistration.jsonEncode(registrationData);
            runtime_cache.opsForValue().set(email, json, expirationInMinutes, TimeUnit.MINUTES);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(
                "can't store in runtime cache because not json serializable: " +
                "source=" + registrationData + " error=" + exception.getMessage());
        }
    }

    public PendingAccountRegistration retrieve(String email) {
        try {
            String retrieved = runtime_cache.opsForValue().get(email);
            if (retrieved != null) return PendingAccountRegistration.jsonDecode(retrieved);
            else return null;
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(
                "can't retrieve from runtime cache because of a json error: " +
                "key=" + email + " error=" + exception.getMessage());
        }
    }

    public void delete(String email) {
        runtime_cache.delete(email);
    }
}
