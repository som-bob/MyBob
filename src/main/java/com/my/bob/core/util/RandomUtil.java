package com.my.bob.core.util;

import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    private RandomUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String createRandomAlphabet(int length){
        // create traveler, unit uid
        // 97('a') ~ 122('z')
        return random.ints(97, 122 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String createRandomNumeric(int length) {
        // 48('0') ~ 57('9')
        return random.ints(48, 57 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
