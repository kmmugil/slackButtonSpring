package com.kmmugil.slackbuttonspring.alarm.service;

import org.springframework.http.ResponseEntity;

public interface AlarmService {

    ResponseEntity<?> triggerDefaultAlarm();

}
