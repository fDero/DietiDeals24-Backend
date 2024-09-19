package service;

import repository.AccountRepository;
import request.AccountRegistrationRequest;
import utils.GeographicalCityDescriptor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.UnrecognizedCityException;
import exceptions.UnrecognizedCountryException;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountValidationService {

    private final AccountRepository accountRepository;
    private final GeographicalAwarenessService geographicalAwarenessService;

    @Autowired
    AccountValidationService(
        AccountRepository accountRepository,
        GeographicalAwarenessService geographicalAwarenessService
    ){
        this.accountRepository = accountRepository;
        this.geographicalAwarenessService = geographicalAwarenessService;
    }

    public void validateEmail(String email, List<String> errors){
        if (email == null){
            errors.add("email field is missing");
            return;
        }
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("email not correctly formatted");
        }
    }

    public void validatePassword(String password, List<String> errors){
        if (password == null){
            errors.add("missing password field (it must contains at least 8 characters, one special char, one lowercase and one uppercase letter)");
            return;
        }
        if (password.length() < 8) errors.add("password too short (must be at least 8 characters)");
        if (!password.matches(".*[A-Z].*")) errors.add("passwords must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*")) errors.add("passwords must contain at least one lowercase letter");
        if (!password.matches(".*\\W.*"))   errors.add("passwords must contain at least one special character");
        if (password.contains(" ")) errors.add("passwords can't contain spaces");
    }

    public void validateBirthday(String birthdayString, List<String> errors){
        Timestamp eighteenYearsAgo = Timestamp.valueOf(LocalDate.now().atStartOfDay().minusYears(18));
        Timestamp birthday = null;
        if (birthdayString == null){
            errors.add("missing birthday field (it must follow the ISO 8601 standard: yyyy-mm-dd)");
            return;
        }
        try {
            Instant birthdayInstant = Instant.parse(birthdayString);
            birthday = Timestamp.from(birthdayInstant);
        }
        catch(DateTimeParseException | NullPointerException error){
            errors.add("birthday not correctly formatted (it must follow the ISO 8601 standard: yyyy-mm-dd)");
            return;
        }
        if (birthday.after(eighteenYearsAgo)) {
            errors.add("you must be at least 18 years old");
        }
    }

    public void validateGeographicalData(String country, String city, List<String> errors)
        throws 
            UnrecognizedCityException, 
            UnrecognizedCountryException
    {
        List<GeographicalCityDescriptor> cities = geographicalAwarenessService.fetchCitiesFromCountry(country);
        for (GeographicalCityDescriptor cityDescriptor : cities) {
            if (cityDescriptor.getName().equals(city)) {
                return;
            }
        }
        throw new UnrecognizedCityException();
    }

    public void validateAccountRegistrationRequest(@NotNull AccountRegistrationRequest account) 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException,
            UnrecognizedCityException,
            UnrecognizedCountryException 
    {
        ArrayList<String> errors = new ArrayList<>();
        this.validateBirthday(account.getBirthday(), errors);
        this.validateEmail(account.getEmail(), errors);
        this.validatePassword(account.getPassword(), errors);
        this.validateGeographicalData(account.getCountry(), account.getCity(), errors);
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
        if (accountRepository.existsAccountByEmail(account.getEmail())) {
            throw new AccountAlreadyExistsException("an account already exists with this email address");
        }
        if (accountRepository.existsAccountByUsername(account.getUsername())) {
            throw new AccountAlreadyExistsException("an account already exists with this username");
        }
    }

    public void validatePassword(@NotNull String password)
        throws 
            AccountValidationException
    {
        List<String> errors = new ArrayList<>();
        validatePassword(password, errors);
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
    }
}
