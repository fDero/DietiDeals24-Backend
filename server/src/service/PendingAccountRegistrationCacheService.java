package service;

import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.WrongConfirmationCodeException;
import utils.PendingAccountRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
public class PendingAccountRegistrationCacheService {

    private final RedisTemplate<String,String> redis;
    private final JsonConversionService jsonConverter;
    private final String cacheKeyPrefix = "pending_account_registration::";

    @Autowired
    public PendingAccountRegistrationCacheService(
        RedisTemplate<String, String> redis, 
        JsonConversionService jsonConverter
    ) {
        this.jsonConverter = jsonConverter;
        this.redis = redis;
    }

    public void store(PendingAccountRegistration registrationData, int expirationInMinutes) {
        String key = cacheKeyPrefix + registrationData.getEmail();
        String json = jsonConverter.encode(registrationData);
        redis.opsForValue().set(key, json, expirationInMinutes, TimeUnit.MINUTES);
    }

    public PendingAccountRegistration retrieve(String email) {
        String key = cacheKeyPrefix + email;
        String retrieved = redis.opsForValue().get(key);
        return (retrieved != null)
            ? jsonConverter.decode(retrieved, PendingAccountRegistration.class)
            : null;
    }

    public void delete(String key) {
        redis.delete(key);
    }

    public void ensureValidConfirmationCode(PendingAccountRegistration pendingAccount, String confirmationCode)
            throws
            NoPendingAccountConfirmationException,
            WrongConfirmationCodeException,
            TooManyConfirmationCodes
    {
        if (pendingAccount == null) {
            throw new NoPendingAccountConfirmationException();
        }
        else if (!pendingAccount.getConfirmationCode().equals(confirmationCode) && !pendingAccount.hasTooManyErrors()) {
            delete(pendingAccount.getEmail());
            pendingAccount.incrementErrorsCounter();
            store(pendingAccount, 10);
            throw new WrongConfirmationCodeException();
        }
        else if (pendingAccount.hasTooManyErrors()) {
            delete(pendingAccount.getEmail());
            throw new TooManyConfirmationCodes();
        }
    }
}
