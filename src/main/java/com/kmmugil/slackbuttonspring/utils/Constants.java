package com.kmmugil.slackbuttonspring.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String SLACK_SIGNATURE_HEADER = "X-Slack-Signature";

    public static final String SLACK_TIMESTAMP_HEADER = "X-Slack-Request-Timestamp";

    public static final String SLACK_OAUTH_SCOPES_HEADER = "X-OAuth-Scopes";

    public static final String SLACK_ACCEPTED_OAUTH_SCOPES_HEADER = "X-Accepted-OAuth-Scopes";

    public static final String SLACK_OAUTH_V2_ACCESS_URL = "https://slack.com/api/oauth.v2.access";

    public static final String SLACK_GRANT_TYPE_REFRESH = "refresh_token";

    public static final String SLACK_GRANT_TYPE_INSTALL = "authorization_code";

    public static Map<String, String> SLACK_OAUTH_ERROR_MAP = new HashMap<String, String>(){{
        put("invalid_grant_type", "Value passed for grant_type was invalid.");
        put("invalid_client_id", "Value passed for client_id was invalid.");
        put("bad_client_secret", "Value passed for client_secret was invalid.");
        put("invalid_code", "Value passed for code was invalid.");
        put("bad_redirect_uri", "Value passed for redirect_uri did not match the redirect_uri in the original request.");
        put("oauth_authorization_url_mismatch", "The OAuth flow was initiated on an incorrect version of the authorization url. The flow must be initiated via " +
                "/oauth/v2/authorize.");
        put("preview_feature_not_available", "Returned when the API method is not yet available on the team in context.");
        put("cannot_install_an_org_installed_app", "Returned when the the org-installed app cannot be installed on a workspace.");
        put("no_scopes", "Missing scope in the request.");
        put("invalid_refresh_token", "The given refresh token is invalid.");
        put("invalid_arguments", "The method was either called with invalid arguments or some detail about the arguments passed are invalid, which is more likely when using " +
                "complex arguments like blocks or attachments.");
        put("invalid_arg_name", "The method was passed an argument whose name falls outside the bounds of accepted or expected values. This includes very long names and names" +
                " with non-alphanumeric characters other than _. If you get this error, it is typically an indication that you have made a very malformed API call.");
        put("invalid_array_arg", "The method was passed an array as an argument. Please only input valid strings.");
        put("invalid_charset", "The method was called via a POST request, but the charset specified in the Content-Type header was invalid. Valid charset names are: utf-8 " +
                "iso-8859-1.");
        put("invalid_form_data", "The method was called via a POST request with Content-Type application/x-www-form-urlencoded or multipart/form-data, but the form data was " +
                "either missing or syntactically invalid.");
        put("invalid_post_type", "The method was called via a POST request, but the specified Content-Type was invalid. Valid types are: application/json " +
                "application/x-www-form-urlencoded multipart/form-data text/plain.");
        put("missing_post_type", "The method was called via a POST request and included a data payload, but the request did not include a Content-Type header.");
        put("team_added_to_org", "The workspace associated with your request is currently undergoing migration to an Enterprise Organization. Web API and other platform " +
                "operations will be intermittently unavailable until the transition is complete.");
        put("ratelimited", "The request has been ratelimited. Refer to the Retry-After header for when to retry the request.");
        put("accesslimited", "Access to this method is limited on the current network");
        put("request_timeout", "The method was called via a POST request, but the POST data was either missing or truncated.");
        put("service_unavailable", "The service is temporarily unavailable");
        put("fatal_error", "The server could not complete your operation(s) without encountering a catastrophic error. It's possible some aspect of the operation succeeded " +
                "before the error was raised.");
        put("internal_error", "The server could not complete your operation(s) without encountering an error, likely due to a transient issue on our end. It's possible some " +
                "aspect of the operation succeeded before the error was raised.");
    }};

    public static Map<String, String> SLACK_INCOMING_WEBHOOKS_ERROR_MAP = new HashMap<String, String>(){{
       put("invalid_payload", "Received request is malformed — perhaps the JSON is structured incorrectly, or the message text is not properly escaped. The request should not" +
               " be retried without correction. HTTP 400 Bad Request");
       put("user_not_found", "User being addressed either do not exist or are invalid. The request should not be retried without modification or until the indicated user or " +
               "channel is set up. HTTP 400 Bad Request");
       put("channel_not_found", "Channel being addressed either do not exist or are invalid. The request should not be retried without modification or until the indicated " +
               "user or channel is set up. HTTP 404 Not Found");
       put("channel_is_archived", "The specified channel has been archived and is no longer accepting new messages. HTTP 410 Gone");
       put("action_prohibited", "An admin has placed some kind of restriction on this avenue of posting messages and that, at least for now, the request should not be " +
               "attempted again. HTTP 403 Forbidden");
       put("posting_to_general_channel_denied", "The incoming webhook attempted to post to the \"#general\" channel for a workspace where posting to that channel is 1) " +
               "restricted and 2) the creator of the same incoming webhook is not authorized to post there. You'll receive this error with a HTTP 403.");
       put("too_many_attachments", "An incoming webhook attempted to post a message with greater than 100 attachments. A message can have a maximum of 100 attachments " +
               "associated with it.");
       put("no_service", "The webhook is either disabled, removed, or invalid.");
       put("no_service_id", "The service_id (B00000000 in our examples above - https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX) was either " +
               "invalid or missing.");
       put("no_team", "The slack workspace is either missing or invalid.");
       put("team_disabled", "The slack workspace is no longer active");
       put("invalid_token", "The token was either invalid or missing");
       put("rollup_error", "Something strange and unusual happened that was likely not your fault at all. HTTP 500 Server Error");
    }};

}
