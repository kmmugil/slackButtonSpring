package com.kmmugil.slackbuttonspring.slack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kmmugil.slackbuttonspring.slack.dao.SlackOAuthExchangePayload;
import com.kmmugil.slackbuttonspring.utils.Constants;
import com.kmmugil.slackbuttonspring.utils.HttpUtils;
import com.kmmugil.slackbuttonspring.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class SlackServiceImpl implements SlackService {

    @Value("${slack.signing.secret}")
    private String signingSecret;

//    @Value("${slack.version}")
    private final String version = "v0";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
    * Handle successful sign-in or error response from Slack
    * @param code The code param returned via the OAuth callback
    * @return Redirect to proper UI endpoint ... but right now just send a default alert to incoming webhook URI
    */
    @Override
    public ResponseEntity<?> handleOAuthFlow(String code) {
        ObjectNode respNode;
        try {
            respNode = this.getAccessToken(code);
            if(!respNode.get("ok").booleanValue()) {
                respNode.put("details", Constants.SLACK_OAUTH_ERROR_MAP.get(respNode.get("error").textValue()));
                logger.error("OAuth Exchange unsuccessful! "+respNode.get("details"));
            }
            return ResponseEntity.ok(respNode);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            respNode = JsonNodeFactory.instance.objectNode();
            respNode.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            respNode.put("message", "INTERNAL_SERVER_ERROR");
            return ResponseEntity.internalServerError().body(respNode);
        }
    }

    /**
     * @param code The code param returned via the OAuth callback
     * @return Successful Sign in with Slack response or Error response
     */
    @Override
    public ObjectNode getAccessToken(String code) {
        try {
            logger.debug("Generating payload for OAuth exchange ...");
            SlackOAuthExchangePayload payload = new SlackOAuthExchangePayload();
            payload.setCode(code);
            logger.debug(String.valueOf(payload));
            Map<String, String> headers = HttpUtils.getSlackCommonHeaders();
            logger.debug("Sending exchange request for access token ...");
            String response = HttpUtils.post(Constants.SLACK_OAUTH_V2_ACCESS_URL, String.valueOf(payload), headers);
            logger.info("Response received for exchange request");
            ObjectMapper objectMapper = new ObjectMapper();
            return (ObjectNode) objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param url The incoming webhook url received during the standard OAuth install flow
     * @param payload The JSON payload to be used for posting the message in the specific channel and workspace the user has installed the app
     * @return The response returned from slack parsed using JSON object mapper
     */
    @Override
    public ObjectNode triggerIncomingWebhook(String url, ObjectNode payload) {
        try {
            ObjectNode respNode = JsonNodeFactory.instance.objectNode();
            logger.debug("Triggering incoming webhook ...");
            String response = HttpUtils.post(url, String.valueOf(payload), HttpUtils.getSlackCommonHeaders());
            logger.info("Response received from slack after triggering webhook.");
            if(response.equalsIgnoreCase("ok")) {
                respNode.put("ok", true);
            } else {
                respNode.put("ok", false);
                respNode.put("error", response);
                respNode.put("details", Constants.SLACK_INCOMING_WEBHOOKS_ERROR_MAP.get(respNode.get("error").textValue()));
                logger.error("Error triggering incoming webhook! "+respNode.get("details"));
            }
            return respNode;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }



    /**
     * @param request HttpServletRequest with the headers to validate authenticity of request
     * @return Boolean true/false denoting the validity
     */
    @Override
    public boolean verifySigningSecret(HttpServletRequest request) {
        try {
            String requestSignature = request.getHeader(Constants.SLACK_SIGNATURE_HEADER);
            String requestTimestamp = request.getHeader(Constants.SLACK_TIMESTAMP_HEADER);
            String requestBody = request.getReader().toString();
            String data = this.version+":"+requestTimestamp+":"+requestBody;
            return requestSignature.equals(Utils.hmacSHA256(data, signingSecret));
        } catch (IOException | NoSuchAlgorithmException |InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
