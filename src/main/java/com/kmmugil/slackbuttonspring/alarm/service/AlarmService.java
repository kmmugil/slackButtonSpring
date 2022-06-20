package com.kmmugil.slackbuttonspring.alarm.service;

import org.springframework.http.ResponseEntity;

public interface AlarmService {

    ResponseEntity<?> triggerDefaultAlarm();

    ResponseEntity<?> triggerProcessorAlarm(String curr_percent, String threshold_percent, String time, String instanceName, String ipAddress);

}
