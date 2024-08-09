package com.ezbuy.framework.config;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.exception.BusinessException;

@Component
public class CipherManager {
    private final Cipher rsaCipher;

    public CipherManager() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        this.rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
    }

    // ma hoa password custom
    public String encrypt(String message, String publicKeyString) {
        try {
            PublicKey publicKey = stringToPublicKey(publicKeyString);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = rsaCipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.HASHING_PASSWORD_FAULT, ex.getMessage());
        }
    }

    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
