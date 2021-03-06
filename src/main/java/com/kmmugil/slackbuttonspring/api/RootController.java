package com.kmmugil.slackbuttonspring.api;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.servlet.http.HttpServletRequest;

import com.kmmugil.slackbuttonspring.utils.dto.DefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Root Controller")
@RequestMapping(value = "/api")
public class RootController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @GetMapping
    public ResponseEntity<?> root(HttpServletRequest request) {
        logger.debug("Entered root controller method ");
        return ResponseEntity.ok(new DefaultResponse(HttpStatus.OK.value(), "Hello world! | "+request.getRemoteAddr()));
    }
}
