package com.kmmugil.slackbuttonspring.api;

import com.kmmugil.slackbuttonspring.alarm.service.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Alarm Controller")
@RequestMapping(value = "/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping("/trigger")
    public ResponseEntity<?> triggerAlarm() {
        logger.debug("Request triggering default alarm");
        return this.alarmService.triggerDefaultAlarm();
    }

}
