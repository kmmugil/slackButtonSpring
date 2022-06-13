package com.kmmugil.slackbuttonspring.alarm.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kmmugil.slackbuttonspring.slack.service.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
            return ResponseEntity.ok("INTERNAL_SERVER_ERROR");
        }
        return ResponseEntity.ok(respNode);
    }
}
