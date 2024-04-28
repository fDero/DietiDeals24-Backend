package service;

import entity.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service public class EncryptionService {

    public String encryptPassword(@NotNull String plain_text_password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(plain_text_password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException encryption_error) {
            System.out.println("error encrypting a password with SHA-256");
            throw new RuntimeException(encryption_error);
        }
    }

    public void encryptPassword(@NotNull Account account){
        String encrypted_password = encryptPassword(account.getPassword());
        account.setPassword(encrypted_password);
    }
}
