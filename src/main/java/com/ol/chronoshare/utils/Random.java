package com.ol.chronoshare.utils;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class Random {

    // Ensemble de caractères à utiliser pour la génération
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Random instance, you can also use SecureRandom for better randomness
    private static final java.util.Random RANDOM = new SecureRandom();

    // Méthode pour générer une chaîne aléatoire
    public static String generateRandomString(Integer n) {
        StringBuilder stringBuilder = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }
        return stringBuilder.toString();
    }
}
