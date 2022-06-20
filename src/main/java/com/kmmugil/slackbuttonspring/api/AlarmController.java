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

    @GetMapping("/trigger/cpu")
    public ResponseEntity<?> triggerProcessorAlarm() {
        logger.debug("Request triggering default processor alarm");
        return this.alarmService.triggerProcessorAlarm("100%", "52%", "5m", "RockyLinux8-5-SCPU-1GB-MAD1-1", "192.168.1.1");
    }

}
