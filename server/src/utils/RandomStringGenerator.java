package utils;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomStringGenerator {

    private final Random random = new SecureRandom();

    public String generateRandomString(int stringLength) {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            randomString.append(characterSet.charAt(randomIndex));
        }
        return randomString.toString();
    }

    public String generateRandomNumericalString(int limit) {
        StringBuilder randomGeneratedCode = new StringBuilder();
        for (int i = 0; i < limit; i++)
            randomGeneratedCode.append(random.nextInt(10));
        return randomGeneratedCode.toString();
    }
}
