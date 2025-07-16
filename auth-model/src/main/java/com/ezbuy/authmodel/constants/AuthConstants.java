package com.ezbuy.authmodel.constants;

public final class AuthConstants {

    public static final String SUCCESS = "common.success";
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
        public static final String CUSTOMER_ACTIVE_SUCCESS = "CUSTOMER_ACTIVE_SUCCESS";
        public static final String CUSTOMER_REGISTER_SUCCESS = "CUSTOMER_REGISTER_SUCCESS";
        public static final String EMPLOYEE_REGISTER_SUCCESS = "EMPLOYEE_REGISTER_SUCCESS";
        public static final String ACCOUNT_ACTIVE = "ACCOUNT_ACTIVE";
        public static final String SIGN_UP_PASSWORD = "SIGN_UP_PASSWORD";
        public static final String VERIFY_ACCOUNT_SUCESS = "VERIFY_ACCOUNT_SUCESS";
        public static final String NOTI_VERIFY_ACCOUNT = "NOTI_VERIFY_ACCOUNT";
        public static final String CREATE_ACCOUNT_SUCCESS = "CREATE_ACCOUNT_SUCCESS";

        public static final String CHANGE_PASSWORD_SUCCESS = "CHANGE_PASSWORD_SUCCESS";
        public static final String APPROVE_ORDER_SUCCESS = "APPROVE_ORDER_SUCCESS";
        public static final String APPROVE_ORDER_FAIL = "APPROVE_ORDER_FAIL";
        public static final String NEED_APPROVE = "NEED_APPROVE";
        public static final String VERIFY_IDENTIFY_REJECTED = "VERIFY_IDENTIFY_REJECTED";
    }

    public static final class Otp {
        public static final String REGISTER = "REGISTER";
        public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
        public static final String FORGOT_PASSWORD_CONTENT = "OTP for forgot password user";
        public static final String REGISTER_CONTENT = "OTP for register user";
        public static final Integer EXP_MINUTE = 5;
        public static final Integer EXP_OTP_AM_MINUTE = 5;
    }

    public static final class Message {
        public static final String EMAIL_INVALID = "dto.email.invalid";
        public static final String DATA_NOT_EXIST = "data.notExist";
        public static final String EMPLOYEE_INPUT_NOT_NULL = "employee.input.notNull";
        public static final String EMPLOYEE_CODE = "employee.code";
        public static final String DATA_IS_EXISTS = "data.input.is.exists";
        public static final String USER_NAME = "login.account";
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

    public static final class PERMISSION_TYPE {
        public static final String ROLE = "ROLE";
        public static final String GROUP = "GROUP";
    }

    public static final class TenantType {
        public static final String ORGANIZATION = "ORGANIZATION";
    }

    public static final class System {
        public static final String EZBUY = "EZBUY";
    }

    public static final class Field {
        public static final String STATE = "state";
        public static final String ORGANIZATION_ID = "organizationId";
    }

    public static final class OPTION_SET {
        public static final String OPTION_SET_CODE = "optionSetCode";
        public static final String OPTION_SET_VALUE_CODE = "optionSetValueCode";
    }
}
