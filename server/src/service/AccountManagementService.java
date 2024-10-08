package service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import entity.Activity;
import entity.OAuthAccountBinding;
import entity.Account;
import entity.Password;
import entity.PersonalLink;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccessDeniedWrongAccountProviderException;
import exceptions.AccountValidationException;
import exceptions.LinkNotFoundException;
import exceptions.LinkNotYoursException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoAccountWithSuchIdException;
import exceptions.NoAccountWithSuchUsernameException;
import exceptions.NoPasswordForThisAccountException;
import repository.AccountRepository;
import repository.ActivityRepository;
import repository.OAuthAccountBindingRepository;
import repository.PasswordRepository;
import repository.PersonalLinkRepository;
import request.ForgotPasswordResetRequest;
import request.NewPersonalLinkRequest;
import request.OAuthAccountRegistrationRequest;
import request.PasswordChangeRequest;
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
    private final AccountValidationService accountValidationService;
    private final UploadedResourcesManagementService uploadedResourcesManagementService;
    private final OAuthAccountBindingRepository oAuthAccountBindingRepository;

    @Autowired
    public AccountManagementService(
        AccountRepository accountRepository,
        PasswordRepository passwordRepository,
        EncryptionService encryptionService,
        PersonalLinkRepository personalLinkRepository,
        AuctionManagementService auctionManagementService,
        BidsManagementService bidsManagementService,
        ActivityRepository activityRepository,
        AccountValidationService accountValidationService,
        UploadedResourcesManagementService uploadedResourcesManagementService,
        OAuthAccountBindingRepository oAuthAccountBindingRepository
    ) {
        this.accountRepository = accountRepository;
        this.passwordRepository = passwordRepository;
        this.encryptionService = encryptionService;
        this.personalLinkRepository = personalLinkRepository;
        this.auctionManagementService = auctionManagementService;
        this.bidsManagementService = bidsManagementService;
        this.activityRepository = activityRepository;
        this.accountValidationService = accountValidationService;
        this.uploadedResourcesManagementService = uploadedResourcesManagementService;
        this.oAuthAccountBindingRepository = oAuthAccountBindingRepository;
    }

    public Account fetchAccountByUsername(String username) 
        throws 
            NoAccountWithSuchUsernameException
    {
        return accountRepository.findAccountByUsername(username)
            .orElseThrow(NoAccountWithSuchUsernameException::new);
    }

    public Account fetchAccountById(Integer id) 
        throws 
            NoAccountWithSuchIdException
    {
        return accountRepository.findById(id)
            .orElseThrow(NoAccountWithSuchIdException::new);
    }

    public Account fetchAccountByEmail(String email)
        throws 
            NoAccountWithSuchEmailException
    {
        return accountRepository.findAccountByEmail(email)
            .orElseThrow(NoAccountWithSuchEmailException::new);
    }

    public Account performAccountLogin(String email, String candidatePlainTextPassword) 
        throws
            NoAccountWithSuchEmailException,
            AccessDeniedBadCredentialsException, 
            AccessDeniedWrongAccountProviderException
    {
        Account account = accountRepository.findAccountByEmail(email)
            .orElseThrow(NoAccountWithSuchEmailException::new);
        if (!account.getAccountProvider().equals("DIETIDEALS24")) {
            throw new AccessDeniedWrongAccountProviderException();
        }
        Password password = passwordRepository.findPasswordByAccountId(account.getId()).get();
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
            NoAccountWithSuchIdException
    {
        return accountRepository.findById(accountId).orElseThrow(NoAccountWithSuchIdException::new);
    }

    public AccountProfileInformations fetchAccountProfileInformations(Integer id) 
        throws 
            NoAccountWithSuchEmailException
    {
        Account account = accountRepository.findById(id).orElseThrow(NoAccountWithSuchEmailException::new);
        List<PersonalLink> personalLinks = personalLinkRepository.findByAccountId(account.getId());
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
        long page,
        long size,
        boolean includePastDeals, 
        boolean includeCurrentDeals, 
        boolean includeAuctions,
        boolean includeBids
    ) {
        long zeroIndexedPageNumber = page - 1;
        Pageable pageable = PageRequest.of((int) zeroIndexedPageNumber, (int) size);
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
        Account account = new Account(pendingAccount);
        account = accountRepository.save(account);
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
        PersonalLink link = personalLinkRepository.findById(linkId)
            .orElseThrow(LinkNotFoundException::new);
        if (!link.getAccountId().equals(accountId)) {
            throw new LinkNotYoursException();
        }
        personalLinkRepository.deleteById(linkId);
    }

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest, Integer accountId) 
        throws 
            NoAccountWithSuchIdException
    {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(NoAccountWithSuchIdException::new);
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
        if (profileUpdateRequest.getNewProfilePictureUrl() != null) {
            String newProfilePictureUrl = uploadedResourcesManagementService
                .updateUrlAndKeepResource(profileUpdateRequest.getNewProfilePictureUrl());
            account.setProfilePictureUrl(newProfilePictureUrl);
        }
        accountRepository.save(account);
    }

    public void updatePassword(PasswordChangeRequest passwordChangeRequest, Integer accountId) 
        throws 
            NoPasswordForThisAccountException, 
            AccountValidationException, 
            AccessDeniedBadCredentialsException
    {
        Password dbPassword = passwordRepository.findPasswordByAccountId(accountId)
            .orElseThrow(NoPasswordForThisAccountException::new);
        List<String> errors = new ArrayList<>();
        accountValidationService.validatePassword(passwordChangeRequest.getNewPassword(), errors);
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
        String userProvidedOldPassword = passwordChangeRequest.getOldPassword();
        String oldPasswordSalt = dbPassword.getPasswordSalt();
        String userProvidedoldPasswordHash = encryptionService.encryptPassword(userProvidedOldPassword, oldPasswordSalt); 
        if (!userProvidedoldPasswordHash.equals(dbPassword.getPasswordHash())) {
            throw new AccessDeniedBadCredentialsException();
        }
        String newSalt = encryptionService.generateRandomSalt();
        String newHash = encryptionService.encryptPassword(passwordChangeRequest.getNewPassword(), newSalt);
        dbPassword.setPasswordHash(newHash);
        dbPassword.setPasswordSalt(newSalt);
        passwordRepository.save(dbPassword);
    }

    public void updatePassword(ForgotPasswordResetRequest passwordChangeRequest, Integer accountId) 
        throws 
            NoPasswordForThisAccountException, 
            AccountValidationException, 
            AccessDeniedBadCredentialsException 
    {
        Password dbPassword = passwordRepository.findPasswordByAccountId(accountId)
            .orElseThrow(NoPasswordForThisAccountException::new);
        List<String> errors = new ArrayList<>();
        accountValidationService.validatePassword(passwordChangeRequest.getNewPassword(), errors);
        if (!errors.isEmpty()) {
            throw new AccountValidationException(String.join(", ", errors));
        }
        String newSalt = encryptionService.generateRandomSalt();
        String newHash = encryptionService.encryptPassword(passwordChangeRequest.getNewPassword(), newSalt);
        dbPassword.setPasswordHash(newHash);
        dbPassword.setPasswordSalt(newSalt);
        passwordRepository.save(dbPassword);
    }

    public Account performGoogleLogin(GoogleIdToken idToken) 
        throws 
            AccessDeniedBadCredentialsException
    {
        String oauthAccountId = idToken.getPayload().getSubject();
        String oauthProvider = idToken.getPayload().getIssuer();
        OAuthAccountBinding retrievedOAuthAccountBinding = 
            oAuthAccountBindingRepository.findByOauthAccountIdAndOauthProvider(oauthAccountId, oauthProvider)
                .orElseThrow(AccessDeniedBadCredentialsException::new);
        return accountRepository.findById(retrievedOAuthAccountBinding.getInternalAccountId())
            .orElseThrow(AccessDeniedBadCredentialsException::new);
    }

    public void updateProfilePicture(Account account, String newProfilePictureUrl) {
        account.setProfilePictureUrl(newProfilePictureUrl);
        accountRepository.save(account);
    }

    public Account createAccountWithGoogle(
        GoogleIdToken idToken,
        OAuthAccountRegistrationRequest accountRegistrationRequest
    ) {
        String oauthProvider = idToken.getPayload().getIssuer();
        String oauthAccountId = idToken.getPayload().getSubject();
        String email = idToken.getPayload().getEmail();
        Account account = new Account(oauthProvider, email, accountRegistrationRequest);
        account = accountRepository.save(account);
        OAuthAccountBinding oAuthAccountBinding = new OAuthAccountBinding();
        oAuthAccountBinding.setOauthProvider(oauthProvider);
        oAuthAccountBinding.setOauthAccountId(oauthAccountId);
        oAuthAccountBinding.setInternalAccountId(account.getId());
        oAuthAccountBindingRepository.save(oAuthAccountBinding);
        return account;
    }
}
