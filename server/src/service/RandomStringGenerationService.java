package service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RandomStringGenerationService {
    
    public String generateRandomString(int stringLength) {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            randomString.append(characterSet.charAt(randomIndex));
        }
        return randomString.toString();
    }
}
