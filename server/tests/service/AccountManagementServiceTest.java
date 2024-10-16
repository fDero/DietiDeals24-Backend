
package service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import entity.Password;

import repository.AccountRepository;
import repository.ActivityRepository;
import repository.OAuthAccountBindingRepository;
import repository.PasswordRepository;
import repository.PersonalLinkRepository;
import request.PasswordChangeRequest;
import utils.EncryptionManager;
import utils.RandomStringGenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class AccountManagementServiceTest {
    
    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private PasswordRepository mockPasswordRepository;

    private EncryptionManager encryptionManager 
        = new EncryptionManager(new RandomStringGenerator());

    @Mock
    private PersonalLinkRepository mockPersonalLinkRepository;

    @Mock
    private AuctionManagementService mockAuctionManagementService;

    @Mock
    private BidsManagementService mockBidsManagementService;

    @Mock
    private ActivityRepository mockActivityRepository;

    @Mock
    private GeographicalAwarenessService mockGeographicalAwarenessService;

    @Mock
    private UploadedResourcesManagementService mockUploadedResourcesManagementService;

    @Mock
    private OAuthAccountBindingRepository mockOAuthAccountBindingRepository;

    private AccountManagementService createAccountManagementService() {
        AccountValidationService accountValidationService = new AccountValidationService(
            mockAccountRepository,
            mockGeographicalAwarenessService
        );
        return new AccountManagementService(
            mockAccountRepository,
            mockPasswordRepository,
            encryptionManager,
            mockPersonalLinkRepository,
            mockAuctionManagementService,
            mockBidsManagementService,
            mockActivityRepository,
            accountValidationService,
            mockUploadedResourcesManagementService,
            mockOAuthAccountBindingRepository
        );
    }

    class ExamplePassword extends Password {

        private static AtomicInteger idGenerator = new AtomicInteger(0);

        ExamplePassword(Integer accountId, String plainTextPassword) {
            String salt = "salt";
            String hash = encryptionManager.encryptPassword(plainTextPassword, salt);
            super.setPasswordSalt(salt);
            super.setPasswordHash(hash);
            super.setAccountId(accountId);
            super.setPasswordLastChange(Timestamp.from(Instant.now()));
            super.setId(idGenerator.incrementAndGet());
        }
    }

    private static final String OLDPASSWORD = "oldPassword22?";
    private static final String NEWPASSWORD = "newPassword22?";
    private static final String DIFFERENT_PASSWORD = "differentPassword22?";


    @Test
    void t1() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, NEWPASSWORD);
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertDoesNotThrow(() -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t2() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, "Short0?");
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccountValidationException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t3() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, "NoNumbers??");
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccountValidationException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t4() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, "NoSymbols22");
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccountValidationException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t5() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, "nouppercase11?");
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccountValidationException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t6() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, "UPPERONLY11?");
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccountValidationException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t7() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(null, NEWPASSWORD);
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccessDeniedBadCredentialsException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t8() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, null);
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccountValidationException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }

    @Test
    void t9() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(OLDPASSWORD, NEWPASSWORD);
        Assertions.assertThrows(NullPointerException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, null));
    }

    @Test
    void t10() {
        AccountManagementService accountManagementService = createAccountManagementService();
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(DIFFERENT_PASSWORD, NEWPASSWORD);
        int accountId = 1;
        Password password = new ExamplePassword(accountId, OLDPASSWORD);
        Mockito.when(mockPasswordRepository.findPasswordByAccountId(accountId)).thenReturn(Optional.of(password));
        Assertions.assertThrows(AccessDeniedBadCredentialsException.class, () -> accountManagementService.updatePassword(passwordChangeRequest, accountId));
    }
}