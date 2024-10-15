
package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utils.JsonConverter;
import utils.KeyValueInMemoryCache;
import utils.PendingForgotPasswordReset;

@Service
public class ForgotPasswordConfirmationCache {

    private final KeyValueInMemoryCache cache;
    private final JsonConverter jsonConverter;
    private final String cacheKeyPrefix = "pending_forgot_password_confirmation::";

    @Autowired
    public ForgotPasswordConfirmationCache(
        KeyValueInMemoryCache cache, 
        JsonConverter jsonConverter
    ) {
        this.jsonConverter = jsonConverter;
        this.cache = cache;
    }

    public void store(PendingForgotPasswordReset registrationData, int expirationInMinutes) {
        String key = cacheKeyPrefix + registrationData.getAccountId();
        String json = jsonConverter.encode(registrationData);
        cache.store(key, json, expirationInMinutes);
    }

    public PendingForgotPasswordReset retrieve(Integer accountId) {
        String key = cacheKeyPrefix + accountId;
        String retrieved = cache.retrieve(key);
        return (retrieved != null)
            ? jsonConverter.decode(retrieved, PendingForgotPasswordReset.class)
            : null;
    }

    public void delete(String key) {
        cache.delete(key);
    }
}