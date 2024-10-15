
package service;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.UnrecognizedCityException;
import exceptions.UnrecognizedCountryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    void t1() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "USA");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("New York", "USA"));
                return cities;
            }
        );

        accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
    }

    @Test
    void t2() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "IT");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Naples", "IT"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t3() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "IT");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Naples", "IT"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t4() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "IT");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Naples", "IT"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t5() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "IT");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Naples", "IT"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t6() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("London", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t7() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("London", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t8() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "USA");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("New York", "USA"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t9() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "USA");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("New York", "USA"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t10() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
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
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "USA");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("New York", "USA"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t11() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Bruce");
        accountRegistrationRequest.setSurname("Wayne");
        accountRegistrationRequest.setUsername("TheBatman");
        accountRegistrationRequest.setBirthday("1952-01-20T00:00:00Z");
        accountRegistrationRequest.setCity("Gotham");
        accountRegistrationRequest.setCountry("USA");
        accountRegistrationRequest.setEmail("bruce.wayne@example.us");
        accountRegistrationRequest.setPassword("GothamKnight77!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "USA");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("New York", "USA"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t12() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Julius");
        accountRegistrationRequest.setSurname("Caesar");
        accountRegistrationRequest.setUsername("TheEmperor");
        accountRegistrationRequest.setBirthday("1952-01-20T00:00:00Z");
        accountRegistrationRequest.setCity("Rome");
        accountRegistrationRequest.setCountry("Roman Empire");
        accountRegistrationRequest.setEmail("julius.ceaser@roman.empire");
        accountRegistrationRequest.setPassword("RomanEmpire77!");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                throw new UnrecognizedCountryException();
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t13() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Keith");
        accountRegistrationRequest.setSurname("Mansfield");
        accountRegistrationRequest.setUsername("funky");
        accountRegistrationRequest.setBirthday("1940-01-01T00:00:00Z");
        accountRegistrationRequest.setCity("Slough");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("keith.mansfield");
        accountRegistrationRequest.setPassword("FunkyFanfare88?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Slough", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t14() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Keith");
        accountRegistrationRequest.setSurname("Mansfield");
        accountRegistrationRequest.setUsername("funky");
        accountRegistrationRequest.setBirthday("1940-01-01T00:00:00Z");
        accountRegistrationRequest.setCity("Slough");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("keith.mansfield");
        accountRegistrationRequest.setPassword("funkyfanfare88?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Slough", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t15() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Keith");
        accountRegistrationRequest.setSurname("Mansfield");
        accountRegistrationRequest.setUsername("funky");
        accountRegistrationRequest.setBirthday("1940-01-01T00:00:00Z");
        accountRegistrationRequest.setCity("Slough");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("keith.mansfield");
        accountRegistrationRequest.setPassword("FUNKYFANFARE88?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Slough", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t16() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Keith");
        accountRegistrationRequest.setSurname("Mansfield");
        accountRegistrationRequest.setUsername("funky");
        accountRegistrationRequest.setBirthday("1940-01-01T00:00:00Z");
        accountRegistrationRequest.setCity("Slough");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("keith.mansfield");
        accountRegistrationRequest.setPassword("FunkyFunfare?");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Slough", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }

    @Test
    void t17() 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException   
    {
        AccountRegistrationRequest accountRegistrationRequest = new AccountRegistrationRequest();
        accountRegistrationRequest.setName("Keith");
        accountRegistrationRequest.setSurname("Mansfield");
        accountRegistrationRequest.setUsername("funky");
        accountRegistrationRequest.setBirthday("1940-01-01T00:00:00Z");
        accountRegistrationRequest.setCity("Slough");
        accountRegistrationRequest.setCountry("EN");
        accountRegistrationRequest.setEmail("keith.mansfield");
        accountRegistrationRequest.setPassword("FunkyFunfare88");
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository, 
            mockGeographicalAwarenessService
        );
        
        Mockito.when(mockGeographicalAwarenessService.fetchCitiesFromCountry(Mockito.any(String.class)))
            .thenAnswer(invocation -> {
                String country = invocation.getArgument(0);
                assert Objects.equals(country, "EN");
                List<GeographicalCityDescriptor> cities = new ArrayList<>();
                cities.add(new GeographicalCityDescriptor("Slough", "EN"));
                return cities;
            }
        );

        Assert.assertThrows(AccountValidationException.class, () -> {
            accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        });
    }
}