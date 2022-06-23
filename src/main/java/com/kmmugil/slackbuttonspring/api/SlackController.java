package com.kmmugil.slackbuttonspring.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kmmugil.slackbuttonspring.slack.msgUtils.*;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.TextType;
import com.kmmugil.slackbuttonspring.slack.service.SlackService;
import com.kmmugil.slackbuttonspring.utils.dto.DefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            "error") String error, HttpServletResponse response) {
        try {
            logger.debug("Checking if user denied access ...");
            if(error != null && error.equalsIgnoreCase("access_denied")) {
                logger.error("User denied request for OAuth permission, terminating slack integration ...");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DefaultResponse(HttpStatus.FORBIDDEN.value(), "User denied access"));
            }
            logger.info("User OAuth access granted for slack app");
            logger.debug("Validating slack OAuth response state ...");
            assert this.state != null;
            if(!this.state.equals(state)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponse(HttpStatus.BAD_REQUEST.value(), "Malicious request, invalid state"));
            }
            logger.info("Slack OAuth response state matched, valid redirect.");
            response.sendRedirect("https://www.idrivecompute.com");
            return this.slackService.handleOAuthFlow(code);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get/bot/info")
    public ResponseEntity<?> slackGetBotInfo(@RequestParam(name = "bot_token", required = false) String botToken,
                                             @RequestParam(name = "bot_id", required = false) String botId, @RequestParam(name = "team_id", required = false) String teamId) {
        try {
            logger.debug("Request received to fetch bot info ...");
            return ResponseEntity.ok(this.slackService.getBotInfo(botToken, botId, teamId));
        } catch(RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"));
        }
    }

    @PostMapping("/event")
    public ResponseEntity<?> slackEventReceiver(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Event received from slack ...");
        return slackService.handleEvents(requestBody, request, response);
    }

    @GetMapping("/post/message/default")
    public ResponseEntity<?> slackPostDefaultMessage(@RequestParam(name = "text", required = false) String text) throws JsonProcessingException {
        logger.debug("Request to trigger action to post default message to slack channel received ...");
        SectionBlock sectionBlock = new SectionBlock(new TextObject(TextType.mrkdwn, "> Hello :zap:"));
        Message message = new Message("C03K4Q3E1S9", text);
        message.createBlocks();
        message.insertBlock(sectionBlock);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectNode payload = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(message));
        ObjectNode respNode = this.slackService.postMessage(payload, false);
        return ResponseEntity.status(HttpStatus.OK).body(respNode);
    }

    @GetMapping("/uninstall")
    public ResponseEntity<?> slackAppUninstall() {
        try {
            logger.debug("Request to trigger action to uninstall app received ...");
            ObjectNode respNode = this.slackService.appUninstall();
            return ResponseEntity.ok(respNode);
        } catch(RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"));
        }
    }

}
