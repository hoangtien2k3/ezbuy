package com.ezbuy.authmodel.constants;

import java.util.HashMap;
import java.util.Map;

public final class AuthConstants {
    public static final long[] EMPLOYEE_CODE_MAX = {0L};
    public static final String SUCCESS = "common.success";
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;

    public static final class Notification {
        public static final String SEVERITY = "NORMAL";
        public static final String CONTENT_TYPE = "text/plain";
        public static final String CATEGORY_TYPE = "ANNOUNCEMENT"; // THONG_BAO
        public static final String CHANNEL_TYPE = "EMAIL";
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
        public static final String REDIRECT_URI = "http://10.207.252.223/callback";
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer ";
        public static final String CLIENT_CREDENTIALS = "client_credentials"; // grant_type de lay token by clientId va
        // clientSecret
    }

    public static final class MySign {
        public static final Integer SIGH_HASH_SUCCESS = 1;
        public static final Integer SIGH_HASH_WAIT = 4000;
        public static final int SIGN_HASH_ASYNC = 1;
        public static final String OID_NIST_SHA1 = "1.3.14.3.2.26";
        public static final String OID_NIST_SHA256 = "2.16.840.1.101.3.4.2.1";
        public static final String OID_RSA_RSA = "1.2.840.113549.1.1.1";
    }

    public static final class PositionCode {
        public static final String OWNER = "OWNER";
        public static final String REPRESENTATIVE = "REPRESENTATIVE";
        public static final String LEADER = "LEADER";
    }

    public static final class TenantType {
        public static final String ORGANIZATION = "ORGANIZATION";
        public static final String INDIVIDUAL = "INDIVIDUAL";
        public static final String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
    }

    public static final class IDType {
        public static final String MST = "MST";
        public static final String GPKD = "GPKD";
    }

    public static final class ALGORITHM {
        public static final Map<String, String> ALGORITHM_MAP = new HashMap<>() {
            {
                put("pbkdf2-sha256", "PBKDF2WithHmacSHA256");
            }
        };
    }

    public static final class System {
        public static final String EZBUY = "EZBUY";
    }

    public static final class Proxy {
        // request enable proxy
        public static final Integer ENABLE = 1;
    }

    public static final class Protocol {
        // http
        public static final Integer HTTP = 0;

        // https
        public static final Integer HTTPS = 1;
    }

    public static final class ClientName {
        public static final String EZBUY_CLIENT = "ezbuy-client";
    }

    public static final class Field {
        public static final String STATE = "state";
        public static final String ORGANIZATION_UNIT_ID = "organizationUnitId";
        public static final String ORGANIZATION_ID = "organizationId";
        public static final String PARENT_ID = "parent_id";
    }

    public enum SyncState {
        WAIT,
        PUSHED
    }

    public static class TelecomServiceAlias {
        public static final String MYSIGN = "CA";
        public static final String VCONTRACT = "VCONTRACT";
    }

    public interface OPTION_SET {
        String OPTION_SET_CODE = "optionSetCode";
        String OPTION_SET_VALUE_CODE = "optionSetValueCode";
    }
}
