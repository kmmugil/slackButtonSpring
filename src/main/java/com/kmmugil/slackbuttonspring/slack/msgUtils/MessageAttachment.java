package com.kmmugil.slackbuttonspring.slack.msgUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageAttachment {

    private String channel;

    private List<Attachment> attachments;

    public MessageAttachment(String channel) {
        this.channel = channel;
        this.attachments = new ArrayList<>();
    }

    public void insertAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
