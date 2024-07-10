package controller;

import entity.Account;
import entity.ContactInformation;
import entity.PersonalLink;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import repository.ContactInformationRepository;
import repository.PersonalLinkRepository;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import authentication.JwtTokenProvider;
import authentication.RequireJWT;

@RestController
public class ProfileController {
    
    private final AccountRepository accountRepository;
    private final ContactInformationRepository contactInformationRepository;
    private final PersonalLinkRepository personalLinkRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ProfileController(
        AccountRepository accountRepository,
        ContactInformationRepository contactInformationRepository,
        PersonalLinkRepository personalLinkRepository,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.accountRepository = accountRepository;
        this.contactInformationRepository = contactInformationRepository;
        this.personalLinkRepository = personalLinkRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequireJWT
    @GetMapping(value = "/profile/owner-view", produces = "application/json")
    public ResponseEntity<AccountPrivateProfileInformations> sendPrivateProfileInformations(@RequestHeader(name = "Authorization") String authorizationHeader) 
        throws 
            NoAccountWithSuchEmailException 
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String email = jwtTokenProvider.getEmailFromJWT(jwtToken);
        Account account = accountRepository.findAccountByEmail(email).orElseThrow(() -> new NoAccountWithSuchEmailException());
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
        List<ContactInformation> contactInformations = contactInformationRepository.findByAccountId(account.getId());
        AccountPrivateProfileInformations accountPrivateInformations = new AccountPrivateProfileInformations(account, personalLinks, contactInformations);
        return ResponseEntity.ok().body(accountPrivateInformations);
    }

    @GetMapping(value = "/profile/public-view", produces = "application/json")
    public ResponseEntity<AccountPublicProfileInformations> sendPublicProfileInformations(@RequestParam String email) 
        throws 
            NoAccountWithSuchEmailException
    {
        Account account = accountRepository.findAccountByEmail(email).orElseThrow(() -> new NoAccountWithSuchEmailException());
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
        AccountPublicProfileInformations acountPublicInformations = new AccountPublicProfileInformations(account, personalLinks);
        return ResponseEntity.ok().body(acountPublicInformations);
    }
}
