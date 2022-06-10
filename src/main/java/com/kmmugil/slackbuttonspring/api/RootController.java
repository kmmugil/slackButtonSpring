package com.kmmugil.slackbuttonspring.api;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api")
public class RootController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @GetMapping
    public ResponseEntity<?> root(HttpServletRequest request) {
        logger.debug("Entered root controller method ");
        ObjectNode respNode = JsonNodeFactory.instance.objectNode();
        respNode.put("status", HttpStatus.OK.value());
        respNode.put("message", "Hello world ! | "+request.getRemoteAddr());
        return ResponseEntity.ok(respNode);
    }
}
