package service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.WrongConfirmationCodeException;
import org.testng.Assert;
import org.junit.jupiter.api.Assertions;
import utils.JsonConverter;
import utils.KeyValueInMemoryCache;
import utils.PendingAccountRegistration;

class PendingAccountRegistrationCacheServiceTest {
    
    class MapKeyValueInMemoryCache extends KeyValueInMemoryCache {
        
        private final Map<String, String> cache = new HashMap<>();
        
        public MapKeyValueInMemoryCache(RedisTemplate<String, String> redis) {
            super(redis);
        }

        @Override
        public void store(String key, String value, int expirationInMinutes) {
            cache.put(key, value);
        }
        
        @Override
        public String retrieve(String key) {
            return cache.get(key);
        }
        
        @Override
        public void delete(String key) {
            cache.remove(key);
        }
    }

    @Test
    void t1() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String matchingConfirmationCode = "12345";
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assertions.assertDoesNotThrow(() -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode));
    }

    @Test
    void t2() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String matchingConfirmationCode = "";
        Integer errorsCounter = 0;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        pendingAccountRegistrationCacheService.store(registrationData, 10);
    
        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t3() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String matchingConfirmationCode = null;
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t4() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String matchingConfirmationCode = "12345";
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        pendingAccountRegistrationCacheService.store(registrationData, 10);
        
        Assert.assertThrows(
            TooManyConfirmationCodes.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t5() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String matchingConfirmationCode = "";
        Integer errorsCounter = 3;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t6() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String matchingConfirmationCode = null;
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
    
        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t7() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String providedConfirmationCode = "12345";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t8() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String providedConfirmationCode = "";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 0;


        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t9() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String providedConfirmationCode = null;
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t10() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String providedConfirmationCode = "12345";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 3;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            TooManyConfirmationCodes.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t11() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String providedConfirmationCode = "";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t12() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String providedConfirmationCode = null;
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        pendingAccountRegistrationCacheService.store(registrationData, 10);

        Assert.assertThrows(
            WrongConfirmationCodeException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t13() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String matchingConfirmationCode = "12345";
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);


        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t14() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String matchingConfirmationCode = "";
        Integer errorsCounter = 0;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
    
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t15() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String matchingConfirmationCode = null;
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t16() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String matchingConfirmationCode = "12345";
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t17() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String matchingConfirmationCode = "";
        Integer errorsCounter = 3;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t18() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String matchingConfirmationCode = null;
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(matchingConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
    
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, matchingConfirmationCode)
        );
    }

    @Test
    void t19() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String providedConfirmationCode = "12345";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t20() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String providedConfirmationCode = "";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 0;


        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);

        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t21() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String providedConfirmationCode = null;
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 0;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t22() {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();

        String providedConfirmationCode = "12345";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 3;

        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t23 () {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String providedConfirmationCode = "";
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }

    @Test
    void t24 () {
        MapKeyValueInMemoryCache cache = new MapKeyValueInMemoryCache(null);
        JsonConverter jsonConverter = new JsonConverter();
        PendingAccountRegistrationCacheService pendingAccountRegistrationCacheService 
            = new PendingAccountRegistrationCacheService(cache, jsonConverter);
        PendingAccountRegistration registrationData = new PendingAccountRegistration();
        
        String providedConfirmationCode = null;
        String expectedConfirmationCode = "99999";
        Integer errorsCounter = 3;
        
        registrationData.setName("Benny");  
        registrationData.setSurname("Goodman");
        registrationData.setBirthday(Timestamp.valueOf("1909-05-04 00:00:00"));
        registrationData.setCountry("USA");
        registrationData.setCity("Chicago");
        registrationData.setEmail("benny.goodman@columbia.records");
        registrationData.setUsername("BennyGoodman");
        registrationData.setPassword("Clarinet33!");
        registrationData.setConfirmationCode(expectedConfirmationCode);
        registrationData.setErrorsCounter(errorsCounter);
        
        Assert.assertThrows(
            NoPendingAccountConfirmationException.class, 
            () -> pendingAccountRegistrationCacheService.ensureValidConfirmationCode(registrationData, providedConfirmationCode)
        );
    }
}
