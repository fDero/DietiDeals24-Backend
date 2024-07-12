package service;

import repository.AccountRepository;
import request.AccountRegistrationRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountValidationService {

    private final AccountRepository accountRepository;

    @Autowired
    AccountValidationService(
        AccountRepository accountRepository
    ){
        this.accountRepository = accountRepository;
    }

    public void validateEmail(String email, ArrayList<String> errors){
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

    public void validatePassword(String password, ArrayList<String> errors){
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

    public void validateBirthday(String birthdayString, ArrayList<String> errors){
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

    public void validateGeographicalData(String country, String city, ArrayList<String> errors){
        // for now, every city / country is valid
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
        this.validateGeographicalData(account.getCountry(), account.getCity(), errors);
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
        if (password == null){
            throw new AccountValidationException(
                "missing password field (it must contains at least 8 characters, one special char, one lowercase and one uppercase letter)"
            );
        }
        
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
