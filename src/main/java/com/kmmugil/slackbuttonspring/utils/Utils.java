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

    public static String hmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        logger.debug("v0="+String.valueOf(Hex.encode(hmacData)));
        logger.debug(new String(Base64.encodeBase64URLSafe(hmacData), StandardCharsets.UTF_8));
        return new String(Base64.encodeBase64URLSafe(hmacData), StandardCharsets.UTF_8);
    }

}
