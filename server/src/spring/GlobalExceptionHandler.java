package spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;
import exceptions.*;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountValidationException.class)
    public ResponseEntity<String> handleAccountValidationException(AccountValidationException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<String> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoPendingAccountConfirmationException.class)
    public ResponseEntity<String> handleNoPendingAccountConfirmationException(NoPendingAccountConfirmationException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooManyConfirmationCodes.class)
    public ResponseEntity<String> handleTooManyConfirmationCodes(TooManyConfirmationCodes ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(WrongConfirmationCodeException.class)
    public ResponseEntity<String> handleWrongConfirmationCodeException(WrongConfirmationCodeException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoAccountWithSuchEmailException.class)
    public ResponseEntity<String> handleNoAccountWithSuchEmailException(NoAccountWithSuchEmailException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedBadCredentialsException.class)
    public ResponseEntity<String> handleAccessDeniedBadCredentialsException(AccessDeniedBadCredentialsException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }
}