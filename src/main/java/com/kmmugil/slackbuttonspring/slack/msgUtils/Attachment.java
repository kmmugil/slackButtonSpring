package com.kmmugil.slackbuttonspring.slack.msgUtils;

import java.util.ArrayList;
import java.util.List;

public class Attachment {

    private String color;

    private List<Block> blocks;

    public Attachment(String color) {
        this.color = color;
        this.blocks = new ArrayList<>();
    }

    public void insertBlock(Block block) {
        this.blocks.add(block);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

}
