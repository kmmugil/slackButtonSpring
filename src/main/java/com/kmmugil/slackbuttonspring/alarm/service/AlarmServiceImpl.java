package com.kmmugil.slackbuttonspring.alarm.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kmmugil.slackbuttonspring.slack.dto.SlackAlertAttachment;
import com.kmmugil.slackbuttonspring.slack.service.SlackService;
import com.kmmugil.slackbuttonspring.utils.dto.DefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl implements AlarmService {

    private final SlackService slackService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    AlarmServiceImpl(SlackService slackService) {
        this.slackService = slackService;
    }

    /**
    * @return JSON response indicating whether alarm triggered message post through Slack app successfully
    */
    @Override
    public ResponseEntity<?> triggerDefaultAlarm() {
        logger.debug("Triggering alarm with default payload ...");
        ObjectNode payload = JsonNodeFactory.instance.objectNode();
        payload.put("text", "Hi :zap:");
        ObjectNode respNode = slackService.triggerIncomingWebhook(System.getenv().get("SLACK_DEF_IW_URI"), payload);
        logger.info("Triggered default alarm.");
        try {
            assert respNode != null;
            logger.debug(respNode.toPrettyString());
        } catch(AssertionError e) {
            logger.error("INTERNAL_SERVER_ERROR");
            return ResponseEntity.ok(new DefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"));
        }
        return ResponseEntity.ok(respNode);
    }

    @Override
    public ResponseEntity<?> triggerProcessorAlarm(String curr_percent, String threshold_percent, String time, String instanceName, String ipAddress) {
        try {
            SlackAlertAttachment slackAlertAttachment = new SlackAlertAttachment(null);
            slackAlertAttachment.composeProcessorAlert(curr_percent, threshold_percent, time, instanceName, ipAddress);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            ObjectNode payloadNode = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(slackAlertAttachment));
            payloadNode.remove("channel");
            logger.debug(payloadNode.toPrettyString());
            ObjectNode respNode = slackService.triggerIncomingWebhook(System.getenv().get("SLACK_DEF_IW_URI"), payloadNode);
            logger.info("Triggered default alarm.");
            try {
                assert respNode != null;
                logger.debug(respNode.toPrettyString());
            } catch(AssertionError e) {
                logger.error("INTERNAL_SERVER_ERROR");
                return ResponseEntity.ok(new DefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"));
            }
            return ResponseEntity.ok(respNode);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"));
        }
    }
}
