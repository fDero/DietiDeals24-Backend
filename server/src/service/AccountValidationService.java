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

    public void validateEmail(@NotNull String email){
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("email not correctly formatted");
        }
    }

    public void validatePassword(@NotNull String password){
        if (password.length() < 8) throw new IllegalArgumentException("password too short (must be at least 8 characters)");
        if (!password.matches(".*[A-Z].*")) throw new IllegalArgumentException("passwords must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*")) throw new IllegalArgumentException("passwords must contain at least one lowercase letter");
        if (!password.matches(".*\\W.*"))   throw new IllegalArgumentException("passwords must contain at least one special character");
        if (password.contains(" ")) throw new IllegalArgumentException("passwords can't contain spaces");
    }

    public void validateBirthday(@NotNull Date birthday){
        LocalDate today = LocalDate.now();
        LocalDate birthdayToLocaldate = birthday.toLocalDate();
        if (birthdayToLocaldate.plusYears(18).isAfter(today)) {
            throw new IllegalArgumentException("you must be at least 18 years old");
        }
    }

    public void validateCountry(@NotNull String country){
        if (!avialableCountries.contains(country)) {
            throw new IllegalArgumentException("unrecognized country! remember: only EU countries are supported");
        }
    }

    public void ensureEmailAvailable(@NotNull String email){
        if (accountRepository.existsAccountByEmail(email)) {
            throw new IllegalArgumentException("another account already exists with that email (email already in use)");
        }
    }

    public void validateAccountRegistrationRequest(@NotNull AccountRegistrationRequest account) {
        this.validateBirthday(account.getBirthday());
        this.validateEmail(account.getEmail());
        this.validatePassword(account.getPassword());
        this.ensureEmailAvailable(account.getEmail());
        this.validateCountry(account.getCountry());
    }

    public boolean isValidPassword(String password) {
        try {
            this.validatePassword(password);
            return true;
        } catch (IllegalArgumentException invalidPasswordexception){
            return false;
        }
    }
}
