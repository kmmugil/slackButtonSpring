package com.kmmugil.slackbuttonspring.slack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kmmugil.slackbuttonspring.slack.dao.SlackOAuthExchangePayload;
import com.kmmugil.slackbuttonspring.utils.Constants;
import com.kmmugil.slackbuttonspring.utils.HttpUtils;
import com.kmmugil.slackbuttonspring.utils.Utils;
import com.kmmugil.slackbuttonspring.utils.dto.DefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SlackServiceImpl implements SlackService {

    @Value("${slack.signing.secret}")
    private String signingSecret;

    @Value("${slack.verification.token}")
    private String verificationToken;

    @Value("${slack.version}")
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
            ObjectMapper objectMapper = new ObjectMapper();
            SlackOAuthExchangePayload payload = new SlackOAuthExchangePayload();
            payload.setCode(code);
            payload.setGrant_type(Constants.SLACK_GRANT_TYPE_INSTALL);
            logger.debug(String.valueOf(payload));
            String formEncodedPayload = HttpUtils.getFormEncodedString(objectMapper.convertValue(payload, Map.class));
            Map<String, String> headers = HttpUtils.getSlackOAuthHeaders();
            headers.put("Content-Length", String.valueOf(formEncodedPayload.length()));
            logger.debug(formEncodedPayload);
            logger.debug("Sending exchange request for access token ...");
            String response = HttpUtils.post(Constants.SLACK_OAUTH_V2_ACCESS_URL, formEncodedPayload, headers);
            logger.info("Response received for exchange request");
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
            String response = HttpUtils.post(url, String.valueOf(payload), HttpUtils.getCommonHeaders());
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
     * Service method to handle event callbacks from slack
     * @param reqBody Request body - couldn't retrieve through HttpServletRequest since getInputStream() had already been called for the request
     * @param request HttpServletRequest with request config info
     * @param response HttpServletResponse to be sent
     * Contains event payload with:
     * token        - verification token for Slack app
     * type         - denoting the type of event triggered
     * event        - JSON node containing event details
     * challenge    - for event API endpoint configuration
     * @return
     * Return challenge on configuration and appropriate 201 response if accepted / 400+ response if invalid request
     */
    @Override
    public ResponseEntity<?> handleEvents(String reqBody, HttpServletRequest request, HttpServletResponse response) {
        try {
            JsonNode reqNode = new ObjectMapper().readTree(reqBody);
            ObjectNode respNode = JsonNodeFactory.instance.objectNode();
            logger.debug("X-Slack-Signature: "+request.getHeaders(Constants.SLACK_SIGNATURE_HEADER));
            logger.debug("X-Slack-Request-Timestamp: "+request.getHeaders(Constants.SLACK_TIMESTAMP_HEADER));
            logger.debug("X-OAuth-Scopes: "+request.getHeaders(Constants.SLACK_OAUTH_SCOPES_HEADER));
            logger.debug("X-Accepted-OAuth-Scopes: "+request.getHeaders(Constants.SLACK_ACCEPTED_OAUTH_SCOPES_HEADER));
            if(!reqNode.get("token").textValue().equals(verificationToken)) {
                logger.error("Invalid request, verification token mismatch. Terminating connection");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponse(HttpStatus.BAD_REQUEST.value(), "event_thrown"));
            }
            switch(reqNode.get("type").textValue()) {
                case "url_verification":
                    logger.info("Events configuration request received from slack. Returning request payload");
                    logger.debug(reqNode.toPrettyString());
                    respNode.put("challenge", reqNode.get("challenge").textValue());
                    return ResponseEntity.ok(respNode);
                case "event_callback":
                    logger.debug(reqNode.toPrettyString());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_handled"));
                default:
                    logger.debug(reqNode.toPrettyString());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_ignored"));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
