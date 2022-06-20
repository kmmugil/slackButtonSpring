package com.kmmugil.slackbuttonspring.slack.msgUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.TextType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextObject extends ContextObject {

    private Enum<TextType> type;

    private String text;

    /**
     * This field can only be used when the type of the textElement object is plain_text
     */
    @JsonIgnore
    private Boolean emoji;

    @JsonIgnore
    private Boolean verbatim;

    public TextObject(Enum<TextType> type, String text) {
        this.type = type;
        this.text = text;
        this.emoji = false;
    }

    public Enum<TextType> getType() {
        return type;
    }

    public void setType(Enum<TextType> type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEmoji() {
        return emoji;
    }

    public void setEmoji(boolean emoji) {
        this.emoji = emoji;
    }

    public boolean isVerbatim() {
        return verbatim;
    }

    public void setVerbatim(boolean verbatim) {
        this.verbatim = verbatim;
    }
}
