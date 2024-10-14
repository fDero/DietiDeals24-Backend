package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import entity.Activity;
import entity.Account;
import entity.PersonalLink;
import exceptions.LinkNotFoundException;
import exceptions.LinkNotYoursException;
import exceptions.NoAccountWithSuchIdException;
import repository.AccountRepository;
import repository.ActivityRepository;
import repository.PersonalLinkRepository;
import request.NewPersonalLinkCreationRequest;
import request.ProfileUpdateRequest;

@Service
public class ProfileManagementService {

    private final AccountRepository accountRepository;
    private final PersonalLinkRepository personalLinkRepository;
    private final ActivityRepository activityRepository;
    private final UploadedResourcesManagementService uploadedResourcesManagementService;

    @Autowired
    public ProfileManagementService(
            AccountRepository accountRepository,
            PersonalLinkRepository personalLinkRepository,
            ActivityRepository activityRepository,
            UploadedResourcesManagementService uploadedResourcesManagementService
    ) {
        this.accountRepository = accountRepository;
        this.personalLinkRepository = personalLinkRepository;
        this.activityRepository = activityRepository;
        this.uploadedResourcesManagementService = uploadedResourcesManagementService;
    }

    public void savePersonalLink(NewPersonalLinkCreationRequest link, Integer accountId) {
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
}
