package controller;

import entity.Account;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import request.LoginRequest;
import response.MinimalAccountInformations;
import service.AccountValidationService;
import service.AuthorizationService;
import service.EncryptionService;
import service.JsonConversionService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.sql.Timestamp;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    
    @GetMapping("/profile")
    public ResponseEntity<MinimalAccountInformations> sendProfileInformations() {
        return null;
    }
}
