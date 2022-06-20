package com.kmmugil.slackbuttonspring.slack.dto;

import com.kmmugil.slackbuttonspring.slack.msgUtils.*;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.TextType;
import com.kmmugil.slackbuttonspring.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SlackAlertAttachment extends MessageAttachment {

    public SlackAlertAttachment(String channel) {
        super(channel);
    }

    public void composeProcessorAlert(String curr_percent, String threshold_percent, String time, String instanceName, String ipAddress) {
        String sectionText = Constants.SLACK_CPU_ALERT_HEADER.replace("&curr_percent&", curr_percent).replace("&threshold_percent&", threshold_percent).replace("&time&", time);
        String color = Constants.colorMap.get("danger");
        TextObject textObject = new TextObject(TextType.mrkdwn, sectionText+":zap:");
        List<TextObject> textFields = new ArrayList<TextObject>() {{
                add(new TextObject(TextType.mrkdwn, "*Instance*"));
                add(new TextObject(TextType.mrkdwn, "*IP*"));
                add(new TextObject(TextType.mrkdwn, "<"+Constants.SLACK_INSTANCE_ALERTS_LINK+"|"+instanceName+">"));
                add(new TextObject(TextType.mrkdwn, ipAddress));
        }};
        SectionBlock sectionBlock = new SectionBlock(textObject, textFields);
        sectionBlock.setAccessory(new ImageObject("https://cdn-icons-png.flaticon.com/512/167/167482.pngUpdat", "iDrive"));
        ContextBlock contextBlock = new ContextBlock();
        contextBlock.insertTextObject(TextType.mrkdwn, "<"+Constants.SLACK_EDIT_MONITOR_LINK+"|Edit monitor settings>");
        Attachment attachment = new Attachment(color);
        attachment.insertBlock(sectionBlock);
        attachment.insertBlock(contextBlock);
        this.insertAttachment(attachment);
    }

}
