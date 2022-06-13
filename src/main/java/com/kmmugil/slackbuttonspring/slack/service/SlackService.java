package com.kmmugil.slackbuttonspring.slack.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface SlackService {

    ResponseEntity<?> handleOAuthFlow(String code);

    ObjectNode getAccessToken(String code);

    ObjectNode triggerIncomingWebhook(String url, ObjectNode payload);

    boolean verifySigningSecret(HttpServletRequest request);

}
