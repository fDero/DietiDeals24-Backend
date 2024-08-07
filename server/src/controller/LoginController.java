package controller;

import entity.Account;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import request.LoginRequest;
import response.MinimalAccountInformations;
import service.AccountManagementService;
import service.AccountValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import authentication.JwtTokenManager;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class LoginController {

    private final AccountValidationService accountValidationService;
    
    private AccountManagementService accountManagementService;
    private final JwtTokenManager jwtTokenProvider;

    @Autowired
    public LoginController(
        AccountValidationService accountValidationService,
        AccountManagementService accountManagementService,
        JwtTokenManager jwtTokenProvider
    ){
        this.accountValidationService = accountValidationService;
        this.accountManagementService = accountManagementService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<MinimalAccountInformations> login(@RequestBody @NotNull LoginRequest request) 
        throws 
            AccountValidationException,
            NoAccountWithSuchEmailException,
            AccessDeniedBadCredentialsException
    {
        accountValidationService.validatePassword(request.getPassword());
        Account account = accountManagementService.performAccountLogin(request.getEmail(), request.getPassword());
        MinimalAccountInformations accountView = new MinimalAccountInformations(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", jwtTokenProvider.generateToken(account.getId().toString()));
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("X-Auth-Token");
        headers.setAccessControlExposeHeaders(allowedHeaders);
        return ResponseEntity.ok().headers(headers).body(accountView);
    }
}
