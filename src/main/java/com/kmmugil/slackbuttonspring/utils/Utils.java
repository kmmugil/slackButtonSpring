package com.kmmugil.slackbuttonspring.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * Utility method to hash a given payload with a signing secret using HMAC SHA256 algorithm
     * @param data Payload to be hashed
     * @param key Signing secret using which the payload should be hashed by the sender
     * @return The hex encoded value of the hashed payload
     * @throws NoSuchAlgorithmException Thrown if algorithm isn't available in the JVM/JRE
     * @throws InvalidKeyException Thrown if the signing-secret/key given is invalid
     */
    public static String hmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        logger.debug("HexDigest of Hash: "+String.valueOf(Hex.encode(hmacData)));
        return String.valueOf(Hex.encode(hmacData));
    }

}
