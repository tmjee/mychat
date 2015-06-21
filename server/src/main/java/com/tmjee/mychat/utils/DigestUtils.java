package com.tmjee.mychat.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author tmjee
 */
public class DigestUtils {

    private static final long start = 100000000L;
    private static final long end = 999999999L;
    private static final long diff = end - start;

    public static String hashPassword(String password, String saltInHex) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(new BigInteger(saltInHex, 16).toByteArray());
        digest.update(password.getBytes());
        byte[] digestedBytes = digest.digest();

        StringBuilder hashedPassword = new StringBuilder();
        for(byte b : digestedBytes) {
            hashedPassword.append(Integer.toHexString(b));
        }
        return hashedPassword.toString();
    }

    public static long randomizeNumber() {
        double d = ThreadLocalRandom.current().nextDouble();
        return (start + ((long)(diff * d)));
    }


    public static String toHex(long l) {
        return Long.toHexString(l);
    }
}
