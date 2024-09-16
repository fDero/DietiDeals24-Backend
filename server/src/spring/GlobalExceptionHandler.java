package spring;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;
import exceptions.*;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

    static HttpHeaders errorResponseHeaders = new HttpHeaders();
    static { errorResponseHeaders.setContentType(MediaType.TEXT_PLAIN); }

    @ExceptionHandler(AccountValidationException.class)
    public ResponseEntity<String> handleAccountValidationException(AccountValidationException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UnrecognizedCountryException.class)
    public ResponseEntity<String> handleAccountValidationException(UnrecognizedCountryException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UnrecognizedCityException.class)
    public ResponseEntity<String> handleAccountValidationException(UnrecognizedCityException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<String> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotificationNotYoursException.class)
    public ResponseEntity<String> handleNoPendingAccountConfirmationException(NotificationNotYoursException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoPendingAccountConfirmationException.class)
    public ResponseEntity<String> handleNoPendingAccountConfirmationException(NoPendingAccountConfirmationException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TooManyConfirmationCodes.class)
    public ResponseEntity<String> handleTooManyConfirmationCodes(TooManyConfirmationCodes ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongConfirmationCodeException.class)
    public ResponseEntity<String> handleWrongConfirmationCodeException(WrongConfirmationCodeException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoAuctionWithSuchIdException.class)
    public ResponseEntity<String> handleNoAccountWithSuchEmailException(NoAuctionWithSuchIdException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoAccountWithSuchEmailException.class)
    public ResponseEntity<String> handleNoAccountWithSuchEmailException(NoAccountWithSuchEmailException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedBadCredentialsException.class)
    public ResponseEntity<String> handleAccessDeniedBadCredentialsException(AccessDeniedBadCredentialsException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuctionNotActiveException.class)
    public ResponseEntity<String> handleAuctionNotActiveException(AuctionNotActiveException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BidOnYourOwnAuctionException.class)
    public ResponseEntity<String> handleBidOnYourOwnAuctionException(BidOnYourOwnAuctionException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchPaymentMethodException.class)
    public ResponseEntity<String> handleIbanNotFoundException(NoSuchPaymentMethodException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PaymentMethodNotYoursException.class)
    public ResponseEntity<String> handleIbanNotYoursException(PaymentMethodNotYoursException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingPaymentMethodException.class)
    public ResponseEntity<String> handleMissingPaymentMethodException(MissingPaymentMethodException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoPasswordForThisAccountException.class)
    public ResponseEntity<String> handleNoPasswordForThisAccountException(NoPasswordForThisAccountException ex){
        return new ResponseEntity<String>(ex.getMessage(), errorResponseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}