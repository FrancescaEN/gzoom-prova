package it.mapsgroup.gzoom.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TokenGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }
}
