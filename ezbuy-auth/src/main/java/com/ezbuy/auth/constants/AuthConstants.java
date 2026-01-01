package com.ezbuy.auth.constants;

import com.ezbuy.core.util.Translator;

import java.util.Map;

public final class AuthConstants {

    public static final String LOGIN = "LOGIN";
    public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";

    public static final Map<String, String> MAP = Map.ofEntries(
            Map.entry(LOGIN, Translator.toLocale("action.login")),
            Map.entry(CHANGE_PASSWORD, Translator.toLocale("action.change.password")),
            Map.entry(FORGOT_PASSWORD, Translator.toLocale("action.forgot.password")));
    
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;

    public static final class Notification {
        public static final String SEVERITY = "NORMAL";
        public static final String CONTENT_TYPE = "text/plain";
        public static final String CATEGORY_TYPE = "ANNOUNCEMENT"; // THONG_BAO
        public static final String CHANNEL_TYPE = "EMAIL";
    }

    public static class TemplateMail {
        public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
        public static final String SIGN_UP = "SIGN_UP";
    }

    public static final class Otp {
        public static final String REGISTER = "REGISTER";
        public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
        public static final Integer EXP_MINUTE = 5;
        public static final Integer EXP_OTP_AM_MINUTE = 5;
    }

    public static final class Message {
        public static final String EMAIL_INVALID = "dto.email.invalid";
    }

    public static final class OAuth {
        public static final String AUTHOR_CODE = "authorization_code";
        public static final String UMA_TICKET = "urn:ietf:params:oauth:grant-type:uma-ticket";
        public static final String RESPONSE_MODE_PERMISSION = "permissions";
        public static final String REDIRECT_URI = "http://localhost:8089/callback";
        public static final String CLIENT_CREDENTIALS = "client_credentials";
    }

    public static final class RoleName {
        public static final String SYSTEM = "system";
        public static final String USER = "user";
    }

    public static final class System {
        public static final String EZBUY = "EZBUY";
    }

    public static final class OPTION_SET {
        public static final String OPTION_SET_CODE = "optionSetCode";
        public static final String OPTION_SET_VALUE_CODE = "optionSetValueCode";
    }
}
