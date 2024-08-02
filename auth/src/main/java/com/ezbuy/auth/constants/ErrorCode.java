package com.ezbuy.auth.constants;

public final class ErrorCode {
    public static final class UserErrorCode {
        public static final String QUERY_USER_ERROR = "UEC_01";
        public static final String ACCOUNT_NOT_EXIST = "account_not_exist";
    }

    public static final class OtpErrorCode {
        public static final String OTP_NOT_MATCH = "0TP_01";
        public static final String OTP_EMPTY = "0TP_02";
    }

    public static final class AuthErrorCode {
        public static final String USER_DISABLED = "AEC_01";
        public static final String INVALID = "AEC_02";
        public static final String USER_EXISTED = "AEC_03";
    }

    public static final class KeycloakErrorCode {
        public static final String CREATE_GROUP_FAILURE = "KC_GROUP_ADD_ERROR";
        public static final String CREATE_POLICY_FAILURE = "KC_POLICY_ADD_ERROR";
        public static final String GROUP_CODE_DUPLICATE = "group.GROUP_CODE_DUPLICATE";
        public static final String GROUP_NAME_DUPLICATE = "group.GROUP_NAME_DUPLICATE";
        public static final String DELETE_ERROR = "group.DELETE_ERROR";
        public static final String DELETE_NOT_EMPTY_GROUP = "group.delete_not_empty";
    }
}
