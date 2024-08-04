package service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Account;
import entity.Password;
import entity.PersonalLink;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import repository.AuctionRepository;
import repository.PasswordRepository;
import repository.PersonalLinkRepository;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;

@Service
public class AccountManagementService {

    private final AccountRepository accountRepository;
    private final PasswordRepository passwordRepository;
    private final EncryptionService encryptionService;
    private final PersonalLinkRepository personalLinkRepository;
    private final AuctionRepository auctionRepository;

    @Autowired
    public AccountManagementService(
        AccountRepository accountRepository,
        PasswordRepository passwordRepository,
        EncryptionService encryptionService,
        PersonalLinkRepository personalLinkRepository,
        AuctionRepository auctionRepository
    ) {
        this.accountRepository = accountRepository;
        this.passwordRepository = passwordRepository;
        this.encryptionService = encryptionService;
        this.personalLinkRepository = personalLinkRepository;
        this.auctionRepository = auctionRepository;
    }

    public Account performAccountLogin(String email, String candidatePlainTextPassword) 
        throws 
            AccountValidationException,
            NoAccountWithSuchEmailException,
            AccessDeniedBadCredentialsException
    {
        Account account = accountRepository.findAccountByEmail(email).orElseThrow(() -> new NoAccountWithSuchEmailException());
        Password password = passwordRepository.findPasswordByAccountId(account.getId()).orElseThrow(() -> new NoAccountWithSuchEmailException());
        String realPasswordSalt = password.getPasswordSalt();
        String realPasswordHash = password.getPasswordHash();
        String candidatePasswordHash = encryptionService.encryptPassword(candidatePlainTextPassword, realPasswordSalt);
        if (!candidatePasswordHash.equals(realPasswordHash)){
            throw new AccessDeniedBadCredentialsException();
        }
        account.setLastLogin(new Timestamp(System.currentTimeMillis()));
        accountRepository.save(account);
        return account;
    }

    public AccountPublicProfileInformations fetchAccountPublicProfileInfo(Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        Account account = accountRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new NoAccountWithSuchEmailException());
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
        long onlineAuctionsCounter = auctionRepository.countOnlineAuctionsById(account.getId());
        long pastDealsCounter = auctionRepository.countPastDealsById(account.getId());
        return new AccountPublicProfileInformations(account, personalLinks, onlineAuctionsCounter, pastDealsCounter);
    }

    public AccountPrivateProfileInformations fetchAccountPrivateProfileInfo(Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        Account account = accountRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new NoAccountWithSuchEmailException());
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
        long onlineAuctionsCounter = auctionRepository.countOnlineAuctionsById(account.getId());
        long pastDealsCounter = auctionRepository.countPastDealsById(account.getId());
        return new AccountPrivateProfileInformations(account, personalLinks, onlineAuctionsCounter, pastDealsCounter);
    }
}
