
package service;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;
import org.junit.jupiter.api.Assertions;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.UnrecognizedCountryException;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import repository.AccountRepository;
import request.AccountRegistrationRequest;
import utils.GeographicalCityDescriptor;

@ExtendWith(MockitoExtension.class)
class AccountValidationServiceTest {
    
    
    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private GeographicalAwarenessService mockGeographicalAwarenessService;

    @Test
    void t1() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("ThirdWorldMan19!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockAccountRepository.existsAccountByEmail(Mockito.any(String.class)))
            .thenAnswer(invocation -> { return false; }
        );
        Mockito.when(mockAccountRepository.existsAccountByUsername(Mockito.any(String.class)))
            .thenAnswer(invocation -> { return false; }
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assertions.assertDoesNotThrow(() -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t2() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("R");
        accountRegistrationRequest.setSurname("Carosone");
        accountRegistrationRequest.setUsername("MamboKing");
        accountRegistrationRequest.setBirthday("1920-01-03T00:00:00Z");
        accountRegistrationRequest.setCity("Naples");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("renato.carosone@mambo.italiano");
        accountRegistrationRequest.setPassword("LunaRossa22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Naples", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t3() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName(null);
        accountRegistrationRequest.setSurname("Carosone");
        accountRegistrationRequest.setUsername("MamboKing");
        accountRegistrationRequest.setBirthday("1920-01-03T00:00:00Z");
        accountRegistrationRequest.setCity("Naples");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("renato.carosone@mambo.italiano");
        accountRegistrationRequest.setPassword("LunaRossa22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Naples", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t4() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Renato");
        accountRegistrationRequest.setSurname("C");
        accountRegistrationRequest.setUsername("MamboKing");
        accountRegistrationRequest.setBirthday("1920-01-03T00:00:00Z");
        accountRegistrationRequest.setCity("Naples");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("renato.carosone@mambo.italiano");
        accountRegistrationRequest.setPassword("LunaRossa22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Naples", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t5() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Renato");
        accountRegistrationRequest.setSurname(null);
        accountRegistrationRequest.setUsername("MamboKing");
        accountRegistrationRequest.setBirthday("1920-01-03T00:00:00Z");
        accountRegistrationRequest.setCity("Naples");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("renato.carosone@mambo.italiano");
        accountRegistrationRequest.setPassword("LunaRossa22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Naples", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t6() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Jimmy");
        accountRegistrationRequest.setSurname("Page");
        accountRegistrationRequest.setUsername("J");
        accountRegistrationRequest.setBirthday("1944-01-09T00:00:00Z");
        accountRegistrationRequest.setCity("London");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("jimmy.page@led.zeppelin");
        accountRegistrationRequest.setPassword("HowManyMoreTimes??");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("London", "EN")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t7() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Jimmy");
        accountRegistrationRequest.setSurname("Page");
        accountRegistrationRequest.setUsername(null);
        accountRegistrationRequest.setBirthday("1944-01-09T00:00:00Z");
        accountRegistrationRequest.setCity("London");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("jimmy.page@led.zeppelin");
        accountRegistrationRequest.setPassword("HowManyMoreTimes??");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t8() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Paul");
        accountRegistrationRequest.setSurname("Stanley");
        accountRegistrationRequest.setUsername("PaulStanley");
        accountRegistrationRequest.setBirthday("1952-01-20");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("paul.stanley@kiss.us");
        accountRegistrationRequest.setPassword("LoveGun77!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t9() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Paul");
        accountRegistrationRequest.setSurname("Stanley");
        accountRegistrationRequest.setUsername("PaulStanley");
        accountRegistrationRequest.setBirthday("");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("paul.stanley@kiss.us");
        accountRegistrationRequest.setPassword("LoveGun77!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t10() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Paul");
        accountRegistrationRequest.setSurname("Stanley");
        accountRegistrationRequest.setUsername("PaulStanley");
        accountRegistrationRequest.setBirthday(null);
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("paul.stanley@kiss.us");
        accountRegistrationRequest.setPassword("LoveGun77!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t11() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword(null);
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t12() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("Short?0");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t13() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("NoNumbers??");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t14() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("NoSpecials00");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t15() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("noupper0??");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t16() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("UPPPERONLY12!!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t17() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Renato");
        accountRegistrationRequest.setSurname("Carosone");
        accountRegistrationRequest.setUsername("MamboKing");
        accountRegistrationRequest.setBirthday("1920-01-03T00:00:00Z");
        accountRegistrationRequest.setCity("Naples");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail(null);
        accountRegistrationRequest.setPassword("LunaRossa22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Naples", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t18() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Renato");
        accountRegistrationRequest.setSurname("Carosone");
        accountRegistrationRequest.setUsername("MamboKing");
        accountRegistrationRequest.setBirthday("1920-01-03T00:00:00Z");
        accountRegistrationRequest.setCity("Naples");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("renato_carosone");
        accountRegistrationRequest.setPassword("LunaRossa22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Naples", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t19() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("John");
        accountRegistrationRequest.setSurname("Doe");
        accountRegistrationRequest.setUsername("jDoe");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("Magione");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("johnDoe@example.com");
        accountRegistrationRequest.setPassword("examplePassword22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Milan", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t20() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Arthur");
        accountRegistrationRequest.setSurname("Fleck");
        accountRegistrationRequest.setUsername("theJoker");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("Gotham");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("theJoker@example.com");
        accountRegistrationRequest.setPassword("examplePassword22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t21() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("John");
        accountRegistrationRequest.setSurname("Doe");
        accountRegistrationRequest.setUsername("jDoe");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("IT");
        accountRegistrationRequest.setEmail("johnDoe@example.com");
        accountRegistrationRequest.setPassword("examplePassword22?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("Milan", "IT")));
        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t22() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("ThirdWorldMan19!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockAccountRepository.existsAccountByEmail(Mockito.any(String.class)))
            .thenAnswer(invocation -> { return true; }
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountAlreadyExistsException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t23() throws UnrecognizedCountryException {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Donald");
        accountRegistrationRequest.setSurname("Fagen");
        accountRegistrationRequest.setUsername("SteelyVoice");
        accountRegistrationRequest.setBirthday("1971-01-10T00:00:00Z");
        accountRegistrationRequest.setCity("New York");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("donald.fagen@steely.dan");
        accountRegistrationRequest.setPassword("ThirdWorldMan19!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        Mockito.when(mockAccountRepository.existsAccountByEmail(Mockito.any(String.class)))
            .thenAnswer(invocation -> { return false; }
        );
        Mockito.when(mockAccountRepository.existsAccountByUsername(Mockito.any(String.class)))
            .thenAnswer(invocation -> { return true; }
        );
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenReturn(List.of(new GeographicalCityDescriptor("New York", "USA")));
        Assert.assertThrows(AccountAlreadyExistsException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }
}