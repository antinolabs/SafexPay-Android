package com.safexpay.android.Utils;

import android.org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static final String ENCRYPTION_IV = "0123456789abcdef";
    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARTSET = "UTF-8";

    /**
     * Encryption
     *
     * @param textToEncrypt
     * @param key
     * @return
     */
    public static String encrypt(final String textToEncrypt, final String key) {
        try {
            final Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(key), makeIv());
            return new String(Base64.encodeBase64(cipher.doFinal(textToEncrypt.getBytes())));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decryption
     *
     * @param textToDecrypt
     * @param key
     * @return
     */
    public static String decrypt(final String textToDecrypt, final String key) {
        try {
            final Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, makeKey(key), makeIv());
            return new String(cipher.doFinal(Base64.decodeBase64(textToDecrypt)));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static AlgorithmParameterSpec makeIv() {
        try {
            return new IvParameterSpec(ENCRYPTION_IV.getBytes(CHARTSET));
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Key makeKey(final String encryptionKey) {
        try {
            final byte[] key = Base64.decodeBase64(encryptionKey);
            return new SecretKeySpec(key, ALGORITHM);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    {
        try {
            final Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField(
                    "isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String generateMerchantKey() {
        String newKey = null;

        try {
            // Get the KeyGenerator
            final KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256); // 128, 192 and 256 bits available

            // Generate the secret key specs.
            final SecretKey skey = kgen.generateKey();
            final byte[] raw = skey.getEncoded();

            newKey = new String(Base64.encodeBase64(raw));
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return newKey;
    }
}
