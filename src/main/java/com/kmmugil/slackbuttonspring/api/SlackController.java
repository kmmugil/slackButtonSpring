package com.kmmugil.slackbuttonspring.api;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kmmugil.slackbuttonspring.slack.service.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "SlackController")
@RequestMapping(value = "/api/slack")
public class SlackController {

    private final SlackService slackService;

    @Value("${slack.dev.oauth.state}")
    private final String state;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    SlackController(SlackService slackService) {
        this.slackService = slackService;
        this.state = null;
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> slackOAuthRedirect(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, @RequestParam(required = false, name =
            "error") String error) {
        try {
            ObjectNode respNode = JsonNodeFactory.instance.objectNode();
            logger.debug("Checking if user denied access ...");
            if(error != null && error.equalsIgnoreCase("access_denied")) {
                logger.error("User denied request for OAuth permission, terminating slack integration ...");
                respNode.put("status", HttpStatus.FORBIDDEN.value());
                respNode.put("message", "User Access Denied");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respNode);
            }
            logger.info("User OAuth access granted for slack app");
            logger.debug("Validating slack OAuth response state ...");
            assert this.state != null;
            if(!this.state.equals(state)) {
                respNode.put("status", HttpStatus.BAD_REQUEST.value());
                respNode.put("message", "Malicious request, invalid state");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respNode);
            }
            logger.info("Slack OAuth response state matched, valid redirect.");
            return this.slackService.handleOAuthFlow(code);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
