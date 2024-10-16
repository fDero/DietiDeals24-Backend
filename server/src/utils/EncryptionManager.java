package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import exceptions.EncryptionFailureException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service 
public class EncryptionManager {

    private final RandomStringGenerator randomStringGenerationService;

    @Autowired
    public EncryptionManager(RandomStringGenerator randomStringGenerationService) {
        this.randomStringGenerationService = randomStringGenerationService;
    }

    public String generateRandomSalt() {
        return randomStringGenerationService.generateRandomString(10);
    }

    public String encryptPassword(@NotNull String plainTextPassword, @NotNull String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = plainTextPassword + salt;
            byte[] encodedHash = digest.digest(saltedPassword.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException encryptionError) {
            throw new EncryptionFailureException();
        }
    }
}
