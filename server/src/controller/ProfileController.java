package controller;

import entity.Activity;
import exceptions.LinkNotFoundException;
import exceptions.LinkNotYoursException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoAccountWithSuchIdException;
import request.NewPersonalLinkCreationRequest;
import request.ProfileUpdateRequest;
import response.AccountMinimalInformations;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;
import response.UserPrivateActivity;
import response.UserPublicActivity;
import service.AccountManagementService;
import service.ProfileManagementService;
import utils.AccountProfileInformations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import authentication.JwtTokenManager;

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
    private final ProfileManagementService profileManagementService;
    private final JwtTokenManager jwtTokenProvider;

    @Autowired
    public ProfileController(
        AccountManagementService accountManagementService,
        ProfileManagementService profileManagementService,
        JwtTokenManager jwtTokenProvider
    ){
        this.accountManagementService = accountManagementService;
        this.profileManagementService = profileManagementService;
        this.jwtTokenProvider = jwtTokenProvider;
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
        AccountProfileInformations accountProfileInformations = accountManagementService.fetchAccountProfileInformations(id);
        AccountMinimalInformations minimalAccountInformations = new AccountMinimalInformations(accountProfileInformations.getAccount());
        return ResponseEntity.ok().body(minimalAccountInformations);
    }

    @GetMapping(value = "/profile/activity/past-deals/public-view", produces = "application/json")
    public ResponseEntity<UserPublicActivity> sendPublicPastDealsInformations(
        @RequestParam(required = true)         Integer id,
        @RequestParam(defaultValue = "1")      Integer page,
        @RequestParam(defaultValue = "10")     Integer size,
        @RequestParam(defaultValue = "true")   Boolean includeAuctions,
        @RequestParam(defaultValue = "true")   Boolean includeBids
    ) {
        List<Activity> activities = profileManagementService.fetchAccountActivityByUserId(
            id, 
            page,
            size, 
            true, 
            false, 
            includeAuctions, 
            includeBids
        );
        UserPublicActivity userPublicActivity = new UserPublicActivity(activities);
        return ResponseEntity.ok().body(userPublicActivity);
    }

    @GetMapping(value = "/profile/activity/current-deals/public-view", produces = "application/json")
    public ResponseEntity<UserPublicActivity> sendPublicCurrentDealsInformations(
        @RequestParam(required = true)        Integer id,
        @RequestParam(defaultValue = "1")     Integer page,
        @RequestParam(defaultValue = "10")    Integer size
    ) {
        List<Activity> activities = profileManagementService.fetchAccountActivityByUserId(
            id, 
            page,
            size, 
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
        @RequestParam(defaultValue = "1")      Integer page,
        @RequestParam(defaultValue = "10")     Integer size,
        @RequestParam(defaultValue = "false")  Boolean includePastDeals,
        @RequestParam(defaultValue = "true")   Boolean includeCurrentDeals,
        @RequestParam(defaultValue = "true")   Boolean includeAuctions,
        @RequestParam(defaultValue = "false")  Boolean includeBids
    ) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        Integer id = Integer.valueOf(jwtTokenProvider.getIdFromJWT(jwtToken));
        List<Activity> activities = profileManagementService.fetchAccountActivityByUserId(
            id, 
            page,
            size, 
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
        @RequestBody NewPersonalLinkCreationRequest link
    ) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        profileManagementService.savePersonalLink(link, accountId);
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
        profileManagementService.deletePersonalLink(linkId, accountId);
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
        profileManagementService.updateProfile(profileUpdateRequest, accountId);
        return ResponseEntity.ok().body("done");
    }
}