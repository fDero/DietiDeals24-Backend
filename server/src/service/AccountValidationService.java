package service;

import repository.AccountRepository;
import request.AccountRegistrationRequest;
import request.OAuthAccountRegistrationRequest;
import utils.GeographicalCityDescriptor;
import utils.TimestampFormatter;

import java.sql.Timestamp;
import java.time.LocalDate;
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
            errors.add("email not correctly formatted");
        }
    }

    public void validatePassword(String password, List<String> errors){
        if (password == null) {
            errors.add("missing password field (it must contains at least 8 characters, one special char, one lowercase and one uppercase letter)");
            return;
        }
        if (password.length() < 8)                errors.add("password too short (must be at least 8 characters)");
        if (!password.matches(".*[A-Z].*")) errors.add("passwords must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*")) errors.add("passwords must contain at least one lowercase letter");
        if (!password.matches(".*\\W.*"))   errors.add("passwords must contain at least one special character");
        if (!password.matches(".*\\d.*"))   errors.add("passwords must contain at least one digit");
        if (password.contains(" "))               errors.add("passwords can't contain spaces");
    }

    public void validateBirthday(String birthdayString, List<String> errors){
        Timestamp eighteenYearsAgo = Timestamp.valueOf(LocalDate.now().atStartOfDay().minusYears(18));
        Timestamp birthday = null;
        if (birthdayString == null){
            errors.add("missing birthday field (it must follow the ISO 8601 standard: yyyy-mm-dd)");
            return;
        }
        try {
            birthday = TimestampFormatter.parseFromClientRequest(birthdayString);
        }
        catch(Exception error){
            errors.add("birthday not correctly formatted (it must follow the ISO 8601 standard: yyyy-MM-ddThh:mm:ssZ)");
            return;
        }
        if (birthday.after(eighteenYearsAgo)) {
            errors.add("you must be at least 18 years old");
        }
    }

    public void validateGeographicalData(String country, String city)
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

    public void validateGeographicalData(String country, String city, List<String> errors)
        throws 
            UnrecognizedCityException, 
            UnrecognizedCountryException
    {
        try {
            List<GeographicalCityDescriptor> cities = geographicalAwarenessService.fetchCitiesFromCountry(country);
            for (GeographicalCityDescriptor cityDescriptor : cities) {
                if (cityDescriptor.getName().equals(city)) {
                    return;
                }
            }
        }
        catch (UnrecognizedCountryException e) {
            errors.add("invalid pair country");
        }
        errors.add("invalid pair country-city");
    }

    private void validateName(String name, List<String> errors) {
        if (name == null) {
            errors.add("missing name field");
        }
        else if (name.length() < 2) {
            errors.add("name must be at least 2 characters long");
        }
    }

    private void validateSurname(String name, List<String> errors) {
        if (name == null) {
            errors.add("missing surname field");
        }
        else if (name.length() < 2) {
            errors.add("surname must be at least 2 characters long");
        }
    }

    private void validateUsername(String username, List<String> errors) {
        if (username == null) {
            errors.add("missing username field");
        }
        else if (username.length() < 4) {
            errors.add("username must be at least 4 characters long");
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

    public void validateAccountRegistrationRequest(String email, OAuthAccountRegistrationRequest accountRegistrationRequest)
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException,
            UnrecognizedCityException,
            UnrecognizedCountryException 
    {
        ArrayList<String> errors = new ArrayList<>();
        this.validateBirthday(accountRegistrationRequest.getBirthday(), errors);
        if (accountRepository.existsAccountByEmail(email)) {
            throw new AccountAlreadyExistsException("an account already exists with this email address");
        }
        String country = accountRegistrationRequest.getCountry();
        String city = accountRegistrationRequest.getCity();
        this.validateGeographicalData(country, city, errors);
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
        if (accountRepository.existsAccountByUsername(accountRegistrationRequest.getUsername())) {
            throw new AccountAlreadyExistsException("an account already exists with this username");
        }
    }

    public void validateAccountRegistrationRequest(@NotNull AccountRegistrationRequest account) 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException,
            UnrecognizedCityException,
            UnrecognizedCountryException 
    {
        ArrayList<String> errors = new ArrayList<>();
        this.validateName(account.getName(), errors);
        this.validateSurname(account.getSurname(), errors);
        this.validateUsername(account.getUsername(), errors);
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
}
