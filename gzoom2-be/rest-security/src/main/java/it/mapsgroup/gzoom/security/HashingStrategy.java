package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.rest.InternalServerException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static com.google.common.base.Throwables.throwIfUnchecked;
import static java.lang.String.format;

/**
 * @author Fabio G. Strozzi
 */
@Component
public class HashingStrategy {
    public static final int SALT_BYTE_SIZE = 64;

    /**
     * Creates the hashed value of a password using the specified salt.
     *
     * @param salt     The salt
     * @param password The password
     * @return The hashed password
     * @throws InternalServerException
     */
    public String create(String salt, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPasswords = salt(salt, password);
            byte[] hash = digest.digest(saltedPasswords.getBytes(StandardCharsets.UTF_8));
            return format("%064x", new java.math.BigInteger(1, hash)).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies that a certain password corresponds to the passed hash value using the specified salt.
     *
     * @param salt         The salt
     * @param password     The password
     * @param hashToVerify The hashed value to be verified
     * @return True if password is verified, false otherwise.
     */
    public boolean verify(String salt, String password, String hashToVerify) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPasswords = salt(salt, password);
            byte[] hash = digest.digest(saltedPasswords.getBytes(StandardCharsets.UTF_8));
            String ret = format("%064x", new java.math.BigInteger(1, hash)).toUpperCase();
            return ret.equals(hashToVerify.toUpperCase());
        } catch (NoSuchAlgorithmException e) {
            throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private String salt(String salt, String password) {
        return salt + password;
    }

    public String newSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        r.nextBytes(salt);
        return String.valueOf(Hex.encodeHex(salt));
    }

    /**
     * Encrypt data whit sha-1 or MD5
     *
     * @param data         The data
     * @param algorithm     The algorithm
     * @return hash of data.
     */
    public static String getHash(String data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(data.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : hashBytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String decryptHash(String hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(hash.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


}
