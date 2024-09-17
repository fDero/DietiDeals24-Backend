package controller;

import entity.Account;
import entity.Activity;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.LinkNotFoundException;
import exceptions.LinkNotYoursException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoAccountWithSuchIdException;
import exceptions.NoAccountWithSuchUsernameException;
import exceptions.NoPasswordForThisAccountException;
import request.ForgotPasswordInitializationRequest;
import request.NewPersonalLinkRequest;
import request.PasswordChangeRequest;
import request.ProfileUpdateRequest;
import response.AccountMinimalInformations;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;
import response.UserPrivateActivity;
import response.UserPublicActivity;
import service.AccountManagementService;
import service.EmailService;
import utils.AccountProfileInformations;
import utils.PendingForgotPasswordReset;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import authentication.JwtTokenManager;
import service.ForgotPasswordConfirmationCache;
import service.RandomStringGenerationService;

import org.springframework.beans.factory.annotation.Value;
import authentication.RequireJWT;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class ProfileController {

    private final AccountManagementService accountManagementService;
    private final JwtTokenManager jwtTokenProvider;
    private final ForgotPasswordConfirmationCache forgotPasswordPendingConfirmationCache;
    private final EmailService emailService;
    private final String frontendUrl;
    private final RandomStringGenerationService randomStringGenerationService;

    @Autowired
    public ProfileController(
        @Value("${dietideals24.frontend.url}") String frontendUrl,
        RandomStringGenerationService randomStringGenerationService,
        AccountManagementService accountManagementService,
        JwtTokenManager jwtTokenProvider, 
        ForgotPasswordConfirmationCache forgotPasswordPendingConfirmationCache,
        EmailService emailService
    ){
        this.accountManagementService = accountManagementService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.forgotPasswordPendingConfirmationCache = forgotPasswordPendingConfirmationCache;
        this.emailService = emailService;
        this.frontendUrl = frontendUrl;
        this.randomStringGenerationService = randomStringGenerationService;
    }

    @RequireJWT
    @GetMapping(value = "/profile/owner-view", produces = "application/json")
    public ResponseEntity<AccountPrivateProfileInformations> sendPrivateProfileInformations(@RequestHeader(name = "Authorization") String authorizationHeader) 
        throws 
            NoAccountWithSuchEmailException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        Integer id = Integer.valueOf(jwtTokenProvider.getIdFromJWT(jwtToken));
        AccountProfileInformations acountProfileInformations = accountManagementService.fetchAccountProfileInformations(id);
        AccountPrivateProfileInformations accountPrivateProfileInformations = new AccountPrivateProfileInformations(acountProfileInformations);
        return ResponseEntity.ok().body(accountPrivateProfileInformations);
    }

    @GetMapping(value = "/profile/public-view", produces = "application/json")
    public ResponseEntity<AccountPublicProfileInformations> sendPublicProfileInformations(@RequestParam Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        AccountProfileInformations acountProfileInformations = accountManagementService.fetchAccountProfileInformations(id);
        AccountPublicProfileInformations accountPublicProfileInformations = new AccountPublicProfileInformations(acountProfileInformations);
        return ResponseEntity.ok().body(accountPublicProfileInformations);
    }

    @GetMapping(value = "/profile/minimal-view", produces = "application/json")
    public ResponseEntity<AccountMinimalInformations> sendMinimalProfileInformations(@RequestParam Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        AccountProfileInformations acountProfileInformations = accountManagementService.fetchAccountProfileInformations(id);
        AccountMinimalInformations minimalAccountInformations = new AccountMinimalInformations(acountProfileInformations.getAccount());
        return ResponseEntity.ok().body(minimalAccountInformations);
    }

    @GetMapping(value = "/profile/activity/past-deals/public-view", produces = "application/json")
    public ResponseEntity<UserPublicActivity> sendPublicPastDealsInformations(
        @RequestParam(required = true)         Integer id,
        @RequestParam(defaultValue = "1")      Integer pageNumber,
        @RequestParam(defaultValue = "10")     Integer pageSize,
        @RequestParam(defaultValue = "true")   Boolean includeAuctions,
        @RequestParam(defaultValue = "true")   Boolean includeBids
    ) {
        List<Activity> activities = accountManagementService.fetchAccountActivityByUserId(
            id, 
            pageNumber,
            pageSize, 
            true, 
            false, 
            includeAuctions, 
            includeBids
        );
        System.out.println(activities);
        System.out.println(activities.size());
        UserPublicActivity userPublicActivity = new UserPublicActivity(activities);
        return ResponseEntity.ok().body(userPublicActivity);
    }

    @GetMapping(value = "/profile/activity/current-deals/public-view", produces = "application/json")
    public ResponseEntity<UserPublicActivity> sendPublicCurrentDealsInformations(
        @RequestParam(required = true)        Integer id,
        @RequestParam(defaultValue = "1")     Integer pageNumber,
        @RequestParam(defaultValue = "10")    Integer pageSize
    ) {
        List<Activity> activities = accountManagementService.fetchAccountActivityByUserId(
            id, 
            pageNumber,
            pageSize, 
            false, 
            true, 
            true, 
            false
        );
        UserPublicActivity userPublicActivity = new UserPublicActivity(activities);
        return ResponseEntity.ok().body(userPublicActivity);
    }

    @RequireJWT
    @GetMapping(value = "/profile/activity/custom/owner-view", produces = "application/json")
    public ResponseEntity<UserPrivateActivity> sendPrivateActivityInformations(
        @RequestHeader(name = "Authorization") String  authorizationHeader,
        @RequestParam(defaultValue = "1")      Integer pageNumber,
        @RequestParam(defaultValue = "10")     Integer pageSize,
        @RequestParam(defaultValue = "false")  Boolean includePastDeals,
        @RequestParam(defaultValue = "true")   Boolean includeCurrentDeals,
        @RequestParam(defaultValue = "true")   Boolean includeAuctions,
        @RequestParam(defaultValue = "false")  Boolean includeBids
    ) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        Integer id = Integer.valueOf(jwtTokenProvider.getIdFromJWT(jwtToken));
        List<Activity> activities = accountManagementService.fetchAccountActivityByUserId(
            id, 
            pageNumber,
            pageSize, 
            includePastDeals,
            includeCurrentDeals,
            includeAuctions,
            includeBids
        );
        UserPrivateActivity userPublicActivity = new UserPrivateActivity(activities, id);
        return ResponseEntity.ok().body(userPublicActivity);
    }

    @RequireJWT
    @PostMapping(value = "/profile/links/new", produces = "application/json")
    public ResponseEntity<String> createPersonalLink(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody NewPersonalLinkRequest link
    ) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        accountManagementService.savePersonalLink(link, accountId);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @DeleteMapping(value = "/profile/links/delete", produces = "application/json")
    public ResponseEntity<String> deletePersonalLink(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam Integer linkId
    ) 
        throws 
            LinkNotYoursException,
            LinkNotFoundException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        accountManagementService.deletePersonalLink(linkId, accountId);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @PostMapping(value = "/profile/update", produces = "application/json")
    public ResponseEntity<String> updateProfile(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody ProfileUpdateRequest profileUpdateRequest
    ) 
        throws 
            NoAccountWithSuchIdException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        accountManagementService.updateProfile(profileUpdateRequest, accountId);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @PostMapping(value = "/profile/update/password", produces = "text/plain")
    public ResponseEntity<String> updatePassword(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody PasswordChangeRequest passwordChangeRequest
    ) 
        throws 
            NoAccountWithSuchIdException, 
            NoPasswordForThisAccountException, 
            AccountValidationException, 
            AccessDeniedBadCredentialsException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        accountManagementService.updatePassword(passwordChangeRequest, accountId);
        return ResponseEntity.ok().body("done");
    }

    @PostMapping(value = "/profile/forgot/password", produces = "text/plain")
    public ResponseEntity<String> initializeForgotPassword(
        @RequestBody ForgotPasswordInitializationRequest forgotPasswordInitializationRequest
    ) 
        throws 
            NoAccountWithSuchEmailException,
            NoAccountWithSuchUsernameException
    {
        final boolean haveUsername = forgotPasswordInitializationRequest.getUsername() != null;
        final boolean haveEmail = forgotPasswordInitializationRequest.getEmail() != null;
        if (!haveUsername && !haveEmail) {
            return ResponseEntity.badRequest().body("You must provide either an email or a username");
        }
        if (haveUsername && haveEmail) {
            return ResponseEntity.badRequest().body("You must provide either an email or a username, not both");
        }
        final Account account = haveUsername? 
            accountManagementService.fetchAccountByUsername(forgotPasswordInitializationRequest.getUsername()) :
            accountManagementService.fetchAccountByEmail(forgotPasswordInitializationRequest.getUsername()) ;
        String authToken = randomStringGenerationService.generateRandomString(10);
        PendingForgotPasswordReset pendingForgotPasswordReset = new PendingForgotPasswordReset(
            account.getEmail(),
            account.getUsername(),
            account.getId(),
            authToken
        );
        forgotPasswordPendingConfirmationCache.store(pendingForgotPasswordReset, 5);
        try{
          String encodedAccountId = URLEncoder.encode(account.getId().toString(), StandardCharsets.UTF_8.toString());
          String encodedAuthToken = URLEncoder.encode(authToken, StandardCharsets.UTF_8.toString());
          emailService.sendForgotPasswordEmail(account, frontendUrl + "/reset-password/" + encodedAccountId + "/" + encodedAuthToken);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          throw new RuntimeException("Encoding error", e);
        }
        return ResponseEntity.ok().body("an email was sent to: " + account.getEmail());
    }
}