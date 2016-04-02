package com.devrimtuncer.sample.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Hash generator utility
 *
 * Created by devrimtuncer on 10/09/15.
 */
public class HashUtils {

    // utility function
    private static String bytesToHexString(byte[] bytes) {

        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String getHash(String valueToBeHashed) {
        MessageDigest digest;
        String hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(valueToBeHashed.getBytes());
            hash = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return hash;
    }
}
