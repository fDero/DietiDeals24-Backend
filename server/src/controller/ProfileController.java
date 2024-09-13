package controller;

import exceptions.NoAccountWithSuchEmailException;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;
import response.MinimalAccountInformations;
import response.UserPublicActivity;
import response.UserPrivateActivity;
import service.AccountManagementService;
import utils.AccountProfileInformations;

import org.springframework.http.ResponseEntity;
import authentication.JwtTokenManager;
import entity.Activity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import authentication.RequireJWT;


@RestController
public class ProfileController {
    
    private final AccountManagementService accountManagementService;
    private final JwtTokenManager jwtTokenProvider;

    @Autowired
    public ProfileController(
        AccountManagementService accountManagementService,
        JwtTokenManager jwtTokenProvider
    ) {
        this.accountManagementService = accountManagementService;
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
    public ResponseEntity<MinimalAccountInformations> sendMinimalProfileInformations(@RequestParam Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        AccountProfileInformations acountProfileInformations = accountManagementService.fetchAccountProfileInformations(id);
        MinimalAccountInformations minimalAccountInformations = new MinimalAccountInformations(acountProfileInformations.getAccount());
        return ResponseEntity.ok().body(minimalAccountInformations);
    }

    @GetMapping(value = "/profile/activity/past-deals/public-view", produces = "application/json")
    public ResponseEntity<UserPublicActivity> sendPublicPastDealsInformations(
        @RequestParam(required = true)         Integer id,
        @RequestParam(defaultValue = "1")      Integer pageNumber,
        @RequestParam(defaultValue = "10")     Integer pageSize
    ) {
        List<Activity> activities = accountManagementService.fetchAccountActivityByUserId(
            id, 
            pageNumber,
            pageSize, 
            true, 
            false, 
            true, 
            true
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
}
