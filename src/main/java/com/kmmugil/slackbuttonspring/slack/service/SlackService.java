package com.kmmugil.slackbuttonspring.slack.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SlackService {

    ResponseEntity<?> handleOAuthFlow(String code);

    ObjectNode getAccessToken(String code);

    ObjectNode getBotInfo(String botToken, String botId, String teamId);

    ObjectNode triggerIncomingWebhook(String url, ObjectNode payload);

    ObjectNode postMessage(ObjectNode payload, Boolean asUser);

    ResponseEntity<?> handleEvents(String requestBody, HttpServletRequest request, HttpServletResponse response);

    boolean verifySigningSecret(HttpServletRequest request, String requestBody);

    ObjectNode appUninstall();

}
