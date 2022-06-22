package com.kmmugil.slackbuttonspring.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String SLACK_SIGNATURE_HEADER = "X-Slack-Signature";

    public static final String SLACK_TIMESTAMP_HEADER = "X-Slack-Request-Timestamp";

    public static final String SLACK_OAUTH_SCOPES_HEADER = "X-OAuth-Scopes";

    public static final String SLACK_ACCEPTED_OAUTH_SCOPES_HEADER = "X-Accepted-OAuth-Scopes";

    public static final String SLACK_OAUTH_V2_ACCESS_URL = "https://slack.com/api/oauth.v2.access";

    public static final String SLACK_BOT_INFO_URL = "https://slack.com/api/bots.info";

    public static final String SLACK_USERS_LIST_URL = "https://slack.com/api/users.list";

    public static final String SLACK_AUTH_TEST_URL = "https://slack.com/api/auth.test";

    public static final String SLACK_POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";

    public static final String SLACK_APP_UNINSTALL_URL = "https://slack.com/api/apps.uninstall";

    public static final String SLACK_GRANT_TYPE_REFRESH = "refresh_token";

    public static final String SLACK_GRANT_TYPE_INSTALL = "authorization_code";

    public static final String SLACK_CPU_ALERT_HEADER = ":rotating_light: *Monitor triggered: CPU is running high* \nCPU Utilization Percent is currently at &curr_percent&, " +
            "above setting of &threshold_percent& for the last &time&";

    public static final String SLACK_EDIT_MONITOR_LINK = "https://idrivecompute.com/app/settings-main";

    public static final String SLACK_INSTANCE_ALERTS_LINK = "https://idrivecompute.com/app/index";

    public static final String SLACK_IDRIVE_IMG_LINK = "https://gdm-catalog-fmapi-prod.imgix.net/ProductLogo/99f03878-7288-4834-be63-18908e02beb6.png?auto=format&size=150";

    public static final HashMap<String, String> colorMap = new HashMap<String, String>() {{
        put("danger", "#FF0000");
        put("compute", "#0071BC");
    }};

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

    public static Map<String, String> SLACK_MESSAGE_ERROR_MAP = new HashMap<String, String>(){{
        put("duplicate_channel_not_found", "Channel associated with client_msg_id was invalid.");
        put("duplicate_message_not_found", "No duplicate message exists associated with client_msg_id.");
        put("not_in_channel", "Cannot post user messages to a channel they are not in.");
        put("is_archived", "Channel has been archived.");
        put("msg_too_long", "Message text is too long");
        put("no_text", "No message text provided");
        put("restricted_action", "A workspace preference prevents the authenticated user from posting.");
        put("restricted_action_read_only_channel", "Cannot post any message into a read-only channel.");
        put("restricted_action_thread_only_channel", "Cannot post top-level messages into a thread-only channel.");
        put("restricted_action_non_threadable_channel", "Cannot post thread replies into a non_threadable channel.");
        put("restricted_action_thread_locked", "Cannot post replies to a thread that has been locked by admins.");
        put("too_many_attachments", "Too many attachments were provided with this message. A maximum of 100 attachments are allowed on a message.");
        put("too_many_contact_cards", "Too many contact_cards were provided with this message. A maximum of 10 contact cards are allowed on a message.");
        put("rate_limited", "Application has posted too many messages, read the Rate Limit documentation for more information");
        put("as_user_not_supported", "The as_user parameter does not function with workspace apps.");
        put("ekm_access_denied", "Administrators have suspended the ability to post a message.");
        put("slack_connect_file_link_sharing_blocked", "Admin has disabled Slack File sharing in all Slack Connect communications");
        put("invalid_blocks", "Blocks submitted with this message are not valid");
        put("invalid_blocks_format", "The blocks is not a valid JSON object or doesn't match the Block Kit syntax.");
        put("messages_tab_disabled", "Messages tab for the app is disabled.");
        put("metadata_too_large", "Metadata exceeds size limit");
        put("team_access_not_granted", "The token used is not granted the specific workspace access required to complete this request.");
        put("invalid_metadata_format", "Invalid metadata format provided");
        put("invalid_metadata_schema", "Invalid metadata schema provided");
        put("metadata_must_be_sent_from_app", "Message metadata can only be posted or updated using an app token");
        put("not_authed", "No authentication token provided.");
        put("invalid_auth", "Some aspect of authentication cannot be validated. Either the provided token is invalid or the request originates from an IP address disallowed " +
                "from making the request.");
        put("access_denied", "Access to a resource specified in the request is denied.");
        put("account_inactive", "Authentication token is for a deleted user or workspace when using a bot token.");
        put("token_revoked", "Authentication token is for a deleted user or workspace or the app has been removed when using a user token.");
        put("token_expired", "Authentication token has expired");
        put("no_permission", "The workspace token used in this request does not have the permissions necessary to complete the request. Make sure your app is a member of the " +
                "conversation it's attempting to post a message to.");
        put("org_login_required", "The workspace is undergoing an enterprise migration and will not be available until migration is complete.");
        put("missing_scope", "The token used is not granted the specific scope permissions required to complete this request.");
        put("not_allowed_token_type", "The token type used in this request is not allowed.");
        put("method_deprecated", "The method has been deprecated.");
        put("deprecated_endpoint", "The endpoint has been deprecated.");
        put("two_factor_setup_required", "Two factor setup required");
        put("enterprise_is_restricted", "The method cannot be called from an Enterprise.");
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

    public static Map<String, String> SLACK_BOT_INFO_ERROR_MAP = new HashMap<String, String>() {{
        put("bot_not_found", "Value passed for bot was invalid.");
        put("bots_not_found", "At least one value passed for bots was invalid.");
        put("missing_argument", "A required argument is missing.");
        put("not_authed", "No authentication token provided.");
        put("invalid_auth", "Some aspect of authentication cannot be validated. Either the provided token is invalid or the request originates from an IP address disallowed " +
                "from making the request.");
        put( "access_denied", "Access to a resource specified in the request is denied.");
        put("account_inactive", "Authentication token is for a deleted user or workspace when using a bot token.");
        put("token_revoked", "Authentication token is for a deleted user or workspace or the app has been removed when using a user token.");
        put("token_expired", "Authentication token has expired");
        put("no_permission", "The workspace token used in this request does not have the permissions necessary to complete the request. Make sure your app is a member of the " +
                "conversation it's attempting to post a message to.");
        put("org_login_required", "The workspace is undergoing an enterprise migration and will not be available until migration is complete.");
        put("ekm_access_denied", "Administrators have suspended the ability to post a message.");
        put("missing_scope", "The token used is not granted the specific scope permissions required to complete this request.");
        put("not_allowed_token_type", "The token type used in this request is not allowed.");
        put("method_deprecated", "The method has been deprecated.");
        put("deprecated_endpoint", "The endpoint has been deprecated.");
        put("two_factor_setup_required", "Two factor setup is required.");
        put("enterprise_is_restricted", "The method cannot be called from an Enterprise.");
        put("team_access_not_granted", "The token used is not granted the specific workspace access required to complete this request.");
        put("invalid_arguments", "The method was either called with invalid arguments or some detail about the arguments passed are invalid, which is more likely when " +
                "using complex arguments like blocks or attachments.");
        put("invalid_arg_name", "The method was passed an argument whose name falls outside the bounds of accepted or expected values. This includes very long names and " +
                "names with non-alphanumeric characters other than _. If you get this error, it is typically an indication that you have made a very malformed API call.");
        put("invalid_array_arg", "The method was passed an array as an argument. Please only input valid strings.");
        put("invalid_charset", "The method was called via a POST request, but the charset specified in the Content-Type header was invalid. Valid charset names are: utf-8" +
                " iso-8859-1.");
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
        put("fatal_error", "The server could not complete your operation(s) without encountering a catastrophic error. It's possible some aspect of the operation " +
                "succeeded before the error was raised.");
        put("internal_error", "The server could not complete your operation(s) without encountering an error, likely due to a transient issue on our end. It's possible some " +
                "aspect of the operation succeeded before the error was raised.");

    }};
}
