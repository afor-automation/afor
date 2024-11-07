package nz.co.afor.framework;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Basic encryption and decryption service
 */
public class Encrypt {

    /**
     * A convenience method to generate a key and iv
     *
     * @throws NoSuchAlgorithmException Thrown when the configured encryption algorithm is invalid
     */
    public static void generateKeyAndIv() throws NoSuchAlgorithmException {
        System.out.println("nz.co.afor.encrypt.key=" + Base64.getEncoder().encodeToString(generateKey(128).getEncoded()));
        System.out.println("nz.co.afor.encrypt.iv=" + Base64.getEncoder().encodeToString(generateIv().getIV()));
    }

    /**
     * Generate a key with specified size
     *
     * @param n Key size
     * @return A new secret key
     * @throws NoSuchAlgorithmException Thrown when the configured encryption algorithm is invalid
     */
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptConfig.getEncryptConfig().getKeyAlgorithm());
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    /**
     * Generate a key with specified password and salt
     *
     * @param password Password to use for the key
     * @param salt     Salt to use for the key
     * @return A new secret key
     * @throws NoSuchAlgorithmException Thrown when the configured encryption algorithm is invalid
     * @throws InvalidKeySpecException  Thrown when the configured encryption key spec is invalid
     */
    public static SecretKey generateKey(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), EncryptConfig.getEncryptConfig().getKeyAlgorithm());
    }

    /**
     * Generate an Iv
     *
     * @return A new IV parameter spec
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Encrypts a string
     *
     * @param input The input string to encrypt
     * @return The encrypted string
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encrypt(String input) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncryptConfig encryptConfig = EncryptConfig.getEncryptConfig();
        return encrypt(encryptConfig.getAlgorithm(), input, encryptConfig.getKey(), encryptConfig.getIv());
    }

    private static String encrypt(String algorithm, String input, SecretKey key,
                                  IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    /**
     * Decrypts a string
     *
     * @param cipherText The encrypted string to decrypt
     * @return The decrypted string
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String decrypt(String cipherText) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncryptConfig encryptConfig = EncryptConfig.getEncryptConfig();
        return decrypt(encryptConfig.getAlgorithm(), cipherText, encryptConfig.getKey(), encryptConfig.getIv());
    }


    private static String decrypt(String algorithm, String cipherText, SecretKey key,
                                  IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
}
