package com.kmmugil.slackbuttonspring.slack.msgUtils;

import java.util.ArrayList;
import java.util.List;

public class Message {

    /**
     * The channel ID in which the message should be posted
     */
    private String channel;

    /**
     * The text message to be posted, can be absent if rich JSON payload like blocks or attachments are provided
     */
    private String text;

    /**
     * Rich JSON layout built using the block-kit specification
     */
    private List<Block> blocks;

    /**
     * Rich JSON attachments built using the block-kit specification
     */
    private List<Attachment> attachments;

    public Message(String channel, String text) {
        this.channel = channel;
        this.text = text;
    }

    public void createBlocks() {
        this.blocks = new ArrayList<>();
    }

    public void insertBlock(Block block) { this.blocks.add(block); }

    public void createAttachments() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
