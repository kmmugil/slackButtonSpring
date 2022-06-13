package com.kmmugil.slackbuttonspring.slack.dao;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;

public class SlackOAuthExchangePayload {

    @Value("${slack.client.id}")
    private String client_id;

    @Value("${slack.client.secret}")
    private String client_secret;

    private String code;

    @Value("${slack.redirect.uri}")
    private String redirect_uri;

    private String grant_type;

    public SlackOAuthExchangePayload() {
        this.client_id = System.getenv().get("SLACK_CLIENT_ID");
        this.client_secret = System.getenv().get("SLACK_CLIENT_SECRET");
        this.redirect_uri = System.getenv().get("SLACK_REDIRECT_URI");
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    /**
    * @return JSON view of the class
    */
    @Override
    public String toString() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("client_id", this.client_id);
        objectNode.put("client_secret", this.client_secret);
        objectNode.put("code", this.getCode());
        objectNode.put("redirect_uri", this.redirect_uri);
        objectNode.put("grant_type", this.grant_type);
        return objectNode.toPrettyString();
    }
}
