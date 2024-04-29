package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;


@Service 
public class EncryptionService {

    public String generateRandomSalt() {
        int stringLength = 10;
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            randomString.append(characterSet.charAt(randomIndex));
        }
        return randomString.toString();
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
            String passwordHash = hexString.toString();
            return passwordHash;
        } catch (NoSuchAlgorithmException encryptionError) {
            System.out.println("error encrypting a password with SHA-256");
            throw new RuntimeException(encryptionError);
        }
    }
}
