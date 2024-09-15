package service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import entity.Activity;
import entity.Account;
import entity.Password;
import entity.PersonalLink;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.LinkNotFoundException;
import exceptions.LinkNotYoursException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoAccountWithSuchIdException;
import repository.AccountRepository;
import repository.ActivityRepository;
import repository.PasswordRepository;
import repository.PersonalLinkRepository;
import request.NewPersonalLinkRequest;
import request.ProfileUpdateRequest;
import utils.AccountProfileInformations;
import utils.PendingAccountRegistration;

@Service
public class AccountManagementService {

    private final AuctionManagementService auctionManagementService;
    private final AccountRepository accountRepository;
    private final PasswordRepository passwordRepository;
    private final EncryptionService encryptionService;
    private final PersonalLinkRepository personalLinkRepository;
    private final BidsManagementService bidsManagementService;
    private final ActivityRepository activityRepository;

    @Autowired
    public AccountManagementService(
        AccountRepository accountRepository,
        PasswordRepository passwordRepository,
        EncryptionService encryptionService,
        PersonalLinkRepository personalLinkRepository,
        AuctionManagementService auctionManagementService,
        BidsManagementService bidsManagementService,
        ActivityRepository activityRepository
    ) {
        this.accountRepository = accountRepository;
        this.passwordRepository = passwordRepository;
        this.encryptionService = encryptionService;
        this.personalLinkRepository = personalLinkRepository;
        this.auctionManagementService = auctionManagementService;
        this.bidsManagementService = bidsManagementService;
        this.activityRepository = activityRepository;
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

    public Account getAccountById(Integer accountId) 
        throws 
            AccountValidationException,
            NoAccountWithSuchIdException,
            AccessDeniedBadCredentialsException
    {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NoAccountWithSuchIdException());
        return account;
    }

    public AccountProfileInformations fetchAccountProfileInformations(Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        Account account = accountRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new NoAccountWithSuchEmailException());
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
        auctionManagementService.updateStatuses();
        long activeAuctionsCounter = auctionManagementService.countOnlineAuctionsByCreatorId(id);
        long pastAuctionsCounter = auctionManagementService.countPastAuctionsByCreatorId(id);
        long pastBidsCounter = bidsManagementService.countPastBidsByBidderId(id);
        long activeBidsCounter = bidsManagementService.countActiveBidsByBidderId(id);
        long pastDealsCounter = pastAuctionsCounter + pastBidsCounter;
        AccountProfileInformations accountPrivateInfos = new AccountProfileInformations();
        accountPrivateInfos.setAccount(account);
        accountPrivateInfos.setPersonalLinks(personalLinks);
        accountPrivateInfos.setPastDealsCounter(pastDealsCounter);
        accountPrivateInfos.setOnlineAuctionsCounter(activeAuctionsCounter);
        accountPrivateInfos.setPastAuctionsCounter(pastAuctionsCounter);
        accountPrivateInfos.setPastBidsCounter(pastBidsCounter);
        accountPrivateInfos.setOnlineBidsCounter(activeBidsCounter);
        return accountPrivateInfos;
    }

    public List<Activity> fetchAccountActivityByUserId(
        Integer userId, 
        long pageNumber,
        long pageSize,
        boolean includePastDeals, 
        boolean includeCurrentDeals, 
        boolean includeAuctions,
        boolean includeBids
    ) 
    {
        long zeroIndexedpageNumber = pageNumber - 1;
        Pageable pageable = PageRequest.of((int) zeroIndexedpageNumber, (int) pageSize);
        return activityRepository.findUserActivityByUserById(
            userId, 
            includePastDeals, 
            includeCurrentDeals, 
            includeAuctions, 
            includeBids, 
            pageable
        );
    } 

    public Account createAccount(PendingAccountRegistration pendingAccount) {
        Account account = accountRepository.save(new Account(pendingAccount));
        String passwordSalt = encryptionService.generateRandomSalt();
        String passwordHash = encryptionService.encryptPassword(pendingAccount.getPassword(), passwordSalt);
        Password password = new Password(passwordSalt, passwordHash, account.getId());
        passwordRepository.save(password);
        return account;
    }

    public void savePersonalLink(NewPersonalLinkRequest link, Integer accountId) {
        PersonalLink personalLink = new PersonalLink();
        personalLink.setLink(link.getLink());
        personalLink.setDescription(link.getDescription());
        personalLink.setAccountId(accountId);
        personalLinkRepository.save(personalLink);
    }

    public void deletePersonalLink(Integer linkId, Integer accountId) 
        throws 
            LinkNotFoundException, 
            LinkNotYoursException
    {
        PersonalLink link = personalLinkRepository.findById(linkId).orElseThrow(
            () -> new LinkNotFoundException());
        if (!link.getAccountId().equals(accountId)) {
            throw new LinkNotYoursException();
        }
        personalLinkRepository.deleteById(linkId);
    }

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest, Integer accountId) 
        throws 
            NoAccountWithSuchIdException
    {
        Account account = accountRepository.findById(accountId).orElseThrow(
            () -> new NoAccountWithSuchIdException());
        if (profileUpdateRequest.getNewBio() != null) {
            account.setBio(profileUpdateRequest.getNewBio());
        }
        if (profileUpdateRequest.getNewCountry() != null) {
            account.setCountry(profileUpdateRequest.getNewCountry());
        }
        if (profileUpdateRequest.getNewCity() != null) {
            account.setCity(profileUpdateRequest.getNewCity());
        }
        if (profileUpdateRequest.getNewEmail() != null) {
            account.setEmail(profileUpdateRequest.getNewEmail());
        }
        if (profileUpdateRequest.getNewUsername() != null) {
            account.setUsername(profileUpdateRequest.getNewUsername());
        }
        accountRepository.save(account);
    }
}
