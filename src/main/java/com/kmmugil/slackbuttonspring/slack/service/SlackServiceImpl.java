package com.kmmugil.slackbuttonspring.slack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class SlackServiceImpl implements SlackService {

    @Value("${slack.client.id}")
    private String client_id;

    @Value("${slack.client.secret}")
    private String client_secret;

    @Value("${slack.signing.secret}")
    private String signingSecret;

    @Value("${slack.verification.token}")
    private String verificationToken;

    @Value("${slack.version}")
    private final String version = "v0";

    @Value("${slack.bot.oauth.token}")
    private String botToken;

    @Value("${slack.client.oauth.token}")
    private String clientToken;

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
     * Utility method to send an HTTP request to slack exchanging OAuth payload for access token
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
            String formEncodedPayload = HttpUtils.getFormEncodedString(objectMapper.convertValue(payload, new TypeReference<Map<String, String>>(){}));
            Map<String, String> headers = HttpUtils.getSlackOAuthHeaders();
            headers.put("Content-Length", String.valueOf(formEncodedPayload.length()));
            logger.debug("Sending exchange request for access token ...");
            String response = HttpUtils.post(Constants.SLACK_OAUTH_V2_ACCESS_URL, formEncodedPayload, headers);
            logger.info("Response received for exchange request");
            return (ObjectNode) objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            logger.error("Error while obtaining access token from slack OAuth exchange ...");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectNode getBotInfo(String botToken, String botId, String teamId) {
        try {
            logger.debug("Generating form-encoded payload for bot info request ...");
            botToken = botToken == null ? this.botToken : botToken;
            Map<String, String> payload = new HashMap<>();
            if(botId != null) payload.put("bot", botId);
            if(teamId != null) payload.put("team_id", teamId);
            logger.debug(String.valueOf(payload));
            String formEncodedPayload = HttpUtils.getFormEncodedString(payload);
            Map<String, String> headers = HttpUtils.getSlackOAuthHeaders();
            headers.put("Content-Length", String.valueOf(formEncodedPayload.length()));
            headers.put("Authorization", "Bearer " + botToken);
            String url = formEncodedPayload.length() == 0 ? Constants.SLACK_BOT_INFO_URL : Constants.SLACK_BOT_INFO_URL+"?"+formEncodedPayload;
            String response = HttpUtils.get(url, headers);
            logger.info("Response received from slack for bot info");
            ObjectNode respNode = (ObjectNode) new ObjectMapper().readTree(response);
            if(!respNode.get("ok").booleanValue()) {
                logger.error("Error getting bot info from slack: "+respNode.get("error").textValue());
                respNode.put("details", Constants.SLACK_BOT_INFO_ERROR_MAP.get(respNode.get("error").textValue()));
            }
            return respNode;
        } catch (JsonProcessingException e) {
            logger.error("Error while getting bot info: 500");
            throw new RuntimeException(e);
        }
    }

    /**
     * Service method to post messages using incoming-webhook URL to a Slack channel
     * @param url The incoming webhook url received during the standard OAuth install flow
     * @param payload The JSON payload to be used for posting the message in the specific channel and workspace the user has installed the app
     * @return Response returned from slack parsed using JSON object mapper
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
                logger.error("Error triggering incoming webhook! "+respNode.get("error"));
            }
            return respNode;
        } catch (Exception e) {
            logger.error("Error while triggering incoming webhook! 500");
            throw new RuntimeException(e);
        }
    }

    /**
     * Service method to post messages to a Slack channel
     * Many other optional arguments aren't mentioned here, which can be added later when necessitated by the application
     * @param asUser Boolean value denoting if the message should be posted as the bot user / on behalf of the client
     * @return Response returned from slack converted to JSON node
     */
    @Override
    public ObjectNode postMessage(ObjectNode payload, Boolean asUser) {
        try {
            logger.debug("Triggering action to post message ...");
            ObjectNode respNode;
            payload.put("asUser", asUser);
            logger.debug(payload.toPrettyString());
            String token = asUser ? this.clientToken : this.botToken;
            Map<String, String> headers = HttpUtils.getCommonHeaders();
            headers.put("Authorization", "Bearer " + token);
            String response = HttpUtils.post(Constants.SLACK_POST_MESSAGE_URL, String.valueOf(payload), headers);
            logger.info("Response received from slack after posting message");
            respNode = (ObjectNode) new ObjectMapper().readTree(response);
            if(!respNode.get("ok").booleanValue()) {
                respNode.put("details", Constants.SLACK_MESSAGE_ERROR_MAP.get(respNode.get("error").textValue()));
                logger.error("Error triggering action to post message! "+respNode.get("error"));
            }
            return respNode;
        } catch (JsonProcessingException e) {
            logger.error("Error triggering action to post message! 500");
            throw new RuntimeException(e);
        }
    }

    /**
     * Service method to handle event callbacks from slack
     * @param requestBody Request body - couldn't retrieve through HttpServletRequest since getInputStream() had already been called for the request
     * @param request HttpServletRequest with request config info
     * @param response HttpServletResponse to be sent
     * Contains event payload with:
     * token        - verification token for Slack app (deprecated way of verifying origin, instead using slack headers with signing secret)
     * type         - denoting the type of event triggered
     * event        - JSON node containing event details
     * challenge    - for event API endpoint configuration
     * @return
     * Return challenge on configuration and appropriate 201 response if accepted / 400+ response if invalid request
     */
    @Override
    public ResponseEntity<?> handleEvents(String requestBody, HttpServletRequest request, HttpServletResponse response) {
        try {
            JsonNode requestNode = new ObjectMapper().readTree(requestBody);
            ObjectNode respNode = JsonNodeFactory.instance.objectNode();
            logger.debug("X-Slack-Signature: "+request.getHeader(Constants.SLACK_SIGNATURE_HEADER));
            logger.debug("X-Slack-Request-Timestamp: "+request.getHeader(Constants.SLACK_TIMESTAMP_HEADER));
            logger.debug("X-OAuth-Scopes: "+request.getHeader(Constants.SLACK_OAUTH_SCOPES_HEADER));
            logger.debug("X-Accepted-OAuth-Scopes: "+request.getHeader(Constants.SLACK_ACCEPTED_OAUTH_SCOPES_HEADER));
            assert this.verifySigningSecret(request, requestBody);
            logger.info("Event origin confirmed using signing secret ...");
            switch(requestNode.get("type").textValue()) {
                case "url_verification":
                    logger.info("Events configuration request received from slack. Returning request payload ...");
                    logger.debug(requestNode.toPrettyString());
                    respNode.put("challenge", requestNode.get("challenge").textValue());
                    return ResponseEntity.ok(respNode);
                case "event_callback":
                    logger.debug(requestNode.toPrettyString());
                    switch(requestNode.get("event").get("type").textValue()) {
                        case "app_mention":
                            logger.info("Event triggered: app_mention ...");
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_handled"));
                        case "tokens_revoked":
                            logger.info("Event triggered: tokens_revoked ... triggering repo cleanup");
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_handled"));
                        case "app_uninstalled":
                            logger.info("Event triggered: app_uninstalled ... triggering repo cleanup");
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_handled"));
                    }
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_handled"));
                default:
                    logger.debug(requestNode.toPrettyString());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DefaultResponse(HttpStatus.ACCEPTED.value(), "event_ignored"));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (AssertionError e) {
            logger.error("Event origin cannot be verified, hence discarded ...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponse(HttpStatus.BAD_REQUEST.value(), "event_thrown"));
        }
    }


    /**
     * Utility method to verify if the event/message/command originated from Slack
     * @param request HttpServletRequest with the headers to validate authenticity of request
     * @return Boolean true/false denoting the validity
     */
    @Override
    public boolean verifySigningSecret(HttpServletRequest request, String requestBody) {
        try {
            String requestSignature = request.getHeader(Constants.SLACK_SIGNATURE_HEADER);
            String requestTimestamp = request.getHeader(Constants.SLACK_TIMESTAMP_HEADER);
            String data = this.version+":"+requestTimestamp+":"+requestBody;
            if (Math.abs(System.currentTimeMillis()/1000 - Long.parseLong(requestTimestamp)) > 5*60) {
                logger.error("Chance of replay attack, terminating connection ...");
                return false;
            }
            return requestSignature.equals("v0="+Utils.hmacSHA256(data, this.signingSecret));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Service method to uninstall Slack app from the workspace & channel the corresponding bot token is associated with
     * @return Response returned from slack converted to JSON node
     */
    @Override
    public ObjectNode appUninstall() {
        try {
            logger.debug("Triggering app.uninstall bot action in slack ...");
            Map<String, String> payloadMap = new HashMap<String, String>() {{
                put("client_id", client_id);
                put("client_secret", client_secret);
            }};
            String formEncodedPayload = HttpUtils.getFormEncodedString(payloadMap);
            Map<String, String> headers = HttpUtils.getSlackOAuthHeaders();
            headers.put("Authorization", "Bearer " + this.botToken);
            String response = HttpUtils.get(Constants.SLACK_APP_UNINSTALL_URL+"?"+formEncodedPayload, headers);
            logger.info("Response received from slack after triggering app.uninstall");
            ObjectNode respNode = (ObjectNode) new ObjectMapper().readTree(response);
            if(!respNode.get("ok").booleanValue()) {
                logger.error("Failed to uninstall app! "+respNode.get("error"));
                respNode.put("details", Constants.SLACK_OAUTH_ERROR_MAP.get(respNode.get("error").textValue()));
            }
            return respNode;
        } catch (JsonProcessingException e) {
            logger.error("Error while uninstalling app! 500");
            throw new RuntimeException(e);
        }
    }
}
