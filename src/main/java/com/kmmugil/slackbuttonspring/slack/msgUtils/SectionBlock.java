package com.kmmugil.slackbuttonspring.slack.msgUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.BlockType;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.TextType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionBlock extends Block {

    private final Enum<BlockType> type = BlockType.section;

    private TextObject text;

    private List<TextObject> fields;

    private ElementObject accessory;

    public SectionBlock(TextObject text) {
        this.text = text;
    }

    public SectionBlock(TextObject text, List<TextObject> fields) {
        this.text = text;
        this.fields = fields;
    }

    public void insertTextObject(Enum<TextType> type, String text) {
        TextObject textObject = new TextObject(type, text);
        this.fields.add(textObject);
    }

    public Enum<BlockType> getType() {
        return type;
    }

    public TextObject getText() {
        return text;
    }

    public void setText(TextObject text) {
        this.text = text;
    }

    public List<TextObject> getFields() {
        return fields;
    }

    public void setFields(List<TextObject> fields) {
        this.fields = fields;
    }

    public ElementObject getAccessory() {
        return accessory;
    }

    public void setAccessory(ElementObject accessory) {
        this.accessory = accessory;
    }

}
