package com.kmmugil.slackbuttonspring.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static Map<String, String> getSlackCommonHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("Accept", "application/json");
        return headers;
    }

    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) {
        return null;
    }

    public static String post(String url, String body, Map<String, String> headers) {
        return fetch("POST", url, body, headers);
    }

    public static String fetch(String method, String url, String body, Map<String, String> headers) {
        String response;
        try {
            logger.info("***url******" + url);
//            logger.info("***body******" + body);

            // establish connection
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // assign method
            if(method != null) conn.setRequestMethod(method);

            // assign headers
            if(headers != null) {
                for (String key : headers.keySet()) {
                    conn.addRequestProperty(key, headers.get(key));
                }
            }

            // assign body
            if(body != null) {
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            }

            // read server response
            InputStream inputStream;
            if(conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            response = streamToString(inputStream);
            inputStream.close();

            // handle redirects
            assert headers != null;
            headers.clear();
            Map<String, List<String>> map = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if(entry.getKey() != null)
                    headers.put(entry.getKey().toLowerCase(), String.join(",", entry.getValue()));
            }
            headers.put("status_code",conn.getResponseCode()+"");
            if(conn.getResponseCode() == 301) {
                String location = conn.getHeaderField("Location");
                return fetch(method, location, body, headers);
            }

            // return response
            logger.debug("HTTP Status Code: "+conn.getResponseCode());
            logger.debug(response);
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String streamToString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}
