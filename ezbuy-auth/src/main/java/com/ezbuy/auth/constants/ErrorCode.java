package com.ezbuy.auth.constants;

public final class ErrorCode {
    public static final class OtpErrorCode {
        public static final String OTP_NOT_MATCH = "0TP_01";
        public static final String OTP_EMPTY = "0TP_02";
    }

    public static final class AuthErrorCode {
        public static final String USER_DISABLED = "AEC_01";
        public static final String INVALID = "AEC_02";
        public static final String USER_EXISTED = "AEC_03";
    }

    public static final class ResponseErrorCode {
        public static final String ERROR_CODE_SUCCESS = "0";
        public static final String ERROR_CODE_FALSE = "-1";
    }
}
