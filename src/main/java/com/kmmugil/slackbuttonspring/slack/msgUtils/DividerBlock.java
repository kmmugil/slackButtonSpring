package com.kmmugil.slackbuttonspring.slack.msgUtils;

import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.BlockType;

public class DividerBlock extends Block {

    private final Enum<BlockType> type = BlockType.divider;

    public Enum<BlockType> getType() {
        return type;
    }

}
