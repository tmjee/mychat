package com.tmjee.mychat.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author tmjee
 */
public class DigestUtils {

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
}
