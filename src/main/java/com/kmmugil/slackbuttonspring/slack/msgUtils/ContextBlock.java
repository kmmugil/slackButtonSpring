package com.kmmugil.slackbuttonspring.slack.msgUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.BlockType;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.TextType;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContextBlock extends Block {

    private final Enum<BlockType> type = BlockType.context;

    private List<ContextObject> elements;

    public ContextBlock() {
        this.elements = new ArrayList<>();
    }

    public void insertTextObject(Enum<TextType> type, String text) {
        TextObject textObject = new TextObject(type, text);
        this.elements.add(textObject);
    }

    public void insertImageObject(String image_url, String alt_text) {
        ImageObject imageObject = new ImageObject(image_url, alt_text);
        this.elements.add(imageObject);
    }

    public Enum<BlockType> getType() {
        return type;
    }

    public List<ContextObject> getElements() {
        return elements;
    }

    public void setElements(List<ContextObject> elements) {
        this.elements = elements;
    }
}
