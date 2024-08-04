package controller;

import exceptions.NoAccountWithSuchEmailException;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;
import service.AccountManagementService;
import org.springframework.http.ResponseEntity;
import authentication.JwtTokenManager;

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
        AccountPrivateProfileInformations acountPrivateInformations = accountManagementService.fetchAccountPrivateProfileInfo(id);
        return ResponseEntity.ok().body(acountPrivateInformations);
    }

    @GetMapping(value = "/profile/public-view", produces = "application/json")
    public ResponseEntity<AccountPublicProfileInformations> sendPublicProfileInformations(@RequestParam Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        AccountPublicProfileInformations acountPublicInformations = accountManagementService.fetchAccountPublicProfileInfo(id);
        return ResponseEntity.ok().body(acountPublicInformations);
    }
}
