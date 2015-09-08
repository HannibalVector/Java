package hr.fer.zemris.java.student0177035204.hw15.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 * Utility class used in blog web application.
 * @author ilijan
 */
public class Util {
    /** Date format used for printing timestamps on blog entries and comments. */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE dd/MM/yyyy, HH:mm");


    /**
     * Returns string containing hexadecimal representation of SHA-1 hash for given string.
     * @param input string for which SHA-1 hash is generated.
     * @return hexadecimal representation of hash.
     */
    public static String sha1Hex(String input) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        messageDigest.update(input.getBytes());
            byte[] digest = messageDigest.digest();
            return byteToHex(digest);
    }


    /**
     * Converts array of bytes to string containing pairs of hexadecimal numbers.
     * @param bytes byte array.
     * @return  string of pairs of hexadecimal numbers.
     */
    private static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
