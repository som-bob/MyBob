package com.my.bob.util;

import java.util.Random;

public class RandomUtil {

    public static String createRandomAlphabet(int length){
        // create traveler, unit uid
        Random random = new Random();
        // 97('a') ~ 122('z')
        return random.ints(97, 122 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String createRandomNumeric(int length) {
        Random random = new Random();
        // 48('0') ~ 57('9')
        return random.ints(48, 57 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
