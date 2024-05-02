package controller;

import entity.Account;
import entity.ContactInformation;
import entity.PersonalLink;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import repository.ContactInformationRepository;
import repository.PersonalLinkRepository;
import response.AccountProfileInformations;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    
    private final AccountRepository accountRepository;
    private final ContactInformationRepository contactInformationRepository;
    private final PersonalLinkRepository personalLinkRepository;

    @Autowired
    public ProfileController(
        AccountRepository accountRepository,
        ContactInformationRepository contactInformationRepository,
        PersonalLinkRepository personalLinkRepository
    ) {
        this.accountRepository = accountRepository;
        this.contactInformationRepository = contactInformationRepository;
        this.personalLinkRepository = personalLinkRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<AccountProfileInformations> sendProfileInformations(@RequestParam String email) 
        throws 
            NoAccountWithSuchEmailException 
    {
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new NoAccountWithSuchEmailException());
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
        List<ContactInformation> contactInformations = contactInformationRepository.findByAccountId(account.getId());
        AccountProfileInformations accountProfileInformations = new AccountProfileInformations(account, personalLinks, contactInformations);
        return ResponseEntity.ok().body(accountProfileInformations);
    }
}
