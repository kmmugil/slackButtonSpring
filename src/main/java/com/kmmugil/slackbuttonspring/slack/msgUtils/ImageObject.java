package com.kmmugil.slackbuttonspring.slack.msgUtils;

import com.kmmugil.slackbuttonspring.slack.msgUtils.enums.ElementType;

public class ImageObject extends ContextObject {

    private final Enum<ElementType> type = ElementType.image;

    private String image_url;

    private String alt_text;

    public ImageObject(String image_url, String alt_text) {
        this.image_url = image_url;
        this.alt_text = alt_text;
    }

    public Enum<ElementType> getType() {
        return type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAlt_text() {
        return alt_text;
    }

    public void setAlt_text(String alt_text) {
        this.alt_text = alt_text;
    }

}
