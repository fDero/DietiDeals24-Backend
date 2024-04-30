package service;

import repository.AccountRepository;
import request.AccountRegistrationRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;


@Service
public class AccountValidationService {

    private final AccountRepository accountRepository;
    static private final ArrayList<String> avialableCountries = new ArrayList<>(Arrays.asList(
            "AL", "AD", "AT", "BY", "BE", "BA", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE",
            "GR", "HU", "IS", "IE", "IT", "VA", "LV", "LI", "LT", "LU", "MK", "MT", "MD", "MC", "GB",
            "ME", "NL", "NO", "PL", "PT", "RO", "RU", "SM", "RS", "SK", "SI", "ES", "SE", "CH", "UA"
    ));

    @Autowired
    AccountValidationService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public void validateEmail(@NotNull String email, ArrayList<String> errors){
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("email not correctly formatted");
        }
    }

    public void validatePassword(@NotNull String password, ArrayList<String> errors){
        if (password.length() < 8) errors.add("password too short (must be at least 8 characters)");
        if (!password.matches(".*[A-Z].*")) errors.add("passwords must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*")) errors.add("passwords must contain at least one lowercase letter");
        if (!password.matches(".*\\W.*"))   errors.add("passwords must contain at least one special character");
        if (password.contains(" ")) errors.add("passwords can't contain spaces");
    }

    public void validateBirthday(@NotNull Date birthday, ArrayList<String> errors){
        LocalDate today = LocalDate.now();
        LocalDate birthdayToLocaldate = birthday.toLocalDate();
        if (birthdayToLocaldate.plusYears(18).isAfter(today)) {
            errors.add("you must be at least 18 years old");
        }
    }

    public void validateCountry(@NotNull String country, ArrayList<String> errors){
        if (!avialableCountries.contains(country)) {
            errors.add("unrecognized country! remember: only EU countries are supported");
        }
    }

    public void validateAccountRegistrationRequest(@NotNull AccountRegistrationRequest account) 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException 
    {
        ArrayList<String> errors = new ArrayList<>();
        this.validateBirthday(account.getBirthday(), errors);
        this.validateEmail(account.getEmail(), errors);
        this.validatePassword(account.getPassword(), errors);
        this.validateCountry(account.getCountry(), errors);
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
        if (accountRepository.existsAccountByEmail(account.getEmail())) {
            throw new AccountAlreadyExistsException("an account already exists with this email address");
        }
    }

    public void validatePassword(@NotNull String password)
        throws 
            AccountValidationException
    {
        ArrayList<String> errors = new ArrayList<>();
        if (password.length() < 8) errors.add("password too short (must be at least 8 characters)");
        if (!password.matches(".*[A-Z].*")) errors.add("passwords must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*")) errors.add("passwords must contain at least one lowercase letter");
        if (!password.matches(".*\\W.*"))   errors.add("passwords must contain at least one special character");
        if (password.contains(" ")) errors.add("passwords can't contain spaces");
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
    }
}
