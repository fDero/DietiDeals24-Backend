package service;

import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.WrongConfirmationCodeException;
import utils.JsonConverter;
import utils.KeyValueInMemoryCache;
import utils.PendingAccountRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingAccountRegistrationCacheService {

    private final KeyValueInMemoryCache cache;
    private final JsonConverter jsonConverter;
    private final String cacheKeyPrefix = "pending_account_registration::";

    @Autowired
    public PendingAccountRegistrationCacheService(
        KeyValueInMemoryCache cache, 
        JsonConverter jsonConverter
    ) {
        this.jsonConverter = jsonConverter;
        this.cache = cache;
    }

    public void store(PendingAccountRegistration registrationData, int expirationInMinutes) {
        String key = cacheKeyPrefix + registrationData.getEmail();
        String json = jsonConverter.encode(registrationData);
        cache.store(key, json, expirationInMinutes);
    }

    public PendingAccountRegistration retrieve(String email) {
        String key = cacheKeyPrefix + email;
        String retrieved = cache.retrieve(key);
        return (retrieved != null)
            ? jsonConverter.decode(retrieved, PendingAccountRegistration.class)
            : null;
    }

    public void delete(String email) {
        String key = cacheKeyPrefix + email;
        cache.delete(key);
    }

    public void ensureValidConfirmationCode(PendingAccountRegistration pendingAccount, String confirmationCode)
        throws
            NoPendingAccountConfirmationException,
            WrongConfirmationCodeException,
            TooManyConfirmationCodes
    {
        if (pendingAccount == null || retrieve(pendingAccount.getEmail()) == null) {
            throw new NoPendingAccountConfirmationException();
        }
        if (confirmationCode == null || confirmationCode.length() == 0) {
            throw new WrongConfirmationCodeException();
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
