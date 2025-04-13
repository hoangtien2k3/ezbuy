package com.ezbuy.authmodel.constants;

public class UrlPaths {
    public static final String GET_OPTIONSET_VALUE = "/config/{code}";

    public interface Auth {
        String PREFIX = "v1/auth";
        String LOGIN = "/login";
        String SIGNUP = "/signup";
        String CREATE_ORG_ACCOUNT = "/account";
        String FIND_ORG_ACCOUNT = "/identify";
        String ADD_SERVICE_ADMIN_ROLE = "/admin-permission";
        String CONFIRM_OTP_FOR_CREATE_ACCOUNT = "/confirm-create";
        String FORGOT_PASSWORD = "/forgot-password";
        String GET_ALL_USERID = "/get-all";
        String CHANGE_PASSWORD = "/change-password";
        String CREATE_TEST_PERFORMANCE_USER = "/test-account";

        String REFRESH_TOKEN = "/refresh";
        String LOGOUT = "/logout";
        String GET_TOKEN_FROM_AUTHORIZATION_CODE = "/provider-code";
        String GET_TOKEN_FROM_PROVIDER_CODE = "/client-code";
        String GET_PERMISSION = "/permissions";
        String GET_ORG_PERMISSION = "/org-permissions";
        String RESET_PASSWORD = "/reset-password";

        String GET_TWO_WAY_PASSWORD = "/two-way-password";
        String ACTION_LOGIN = "action-login";
        String GET_CONTRACT = "/contract";
        String RECEIVE_SIGN_RESULT = "/receive-sign-result"; // API lay ket qua chuyen ky tu vcontract
        String GET_INDIVIDUAL_BY_USERNAME = "/username";
        String VIEW_BUSINESS_AUTH_CONTRACT = "/view-business-auth-contract"; // API lay file xac minh doanh nghiep da ky

        String BLOCK_LOGIN = "/block-partner-license-key-login";

        String CONFIRM_OTP = "/confirm-otp"; // ham xac nhan otp
        String GENERATE_OTP = "/generate-otp"; // ham sinh ma otp
    }

    public interface Noti {
        String PREFIX = "/v1/transmission";
        String CREATE_NOTI = "/create-noti";
    }

    public interface User {
        String PREFIX = "v1/user";
        String GET_USER = "";
        String UPDATE_USER = "update";
        String CONTACTS = "contacts";
        String GET_USER_BY_ID = "/id/{id}";
        String GET_CREDENTIALS = "/credentials";
        String SIGN_HASH = "/sign-hash";
        String USER_PROFILES = "search";
        String EXPORT_PROFILES = "export";
        String GET_PROFILES = "{id}";
        String TAX_BRANCHES = "tax-branches";
        String ID_TYPES = "id-types";
        String SYNC_SIGN_HASH = "/sync-sign-hash";
        String KEYCLOAK = "/keycloak";
    }

    public interface Util {
        String PREFIX = "v1/util";
        String JOB_ADD_ROLE_ADMIN_FOR_OLD_USER = "add-role-admin-for-old-user"; // add role admin of Hub for old user
    }

    public interface FILE {
        String PREFIX = "v1/file";
        String DOWNLOAD = "download";
    }

    public interface UserCredential {
        String PREFIX = "v1/user-credential"; // api lay thong tin user dang nhap
    }

    public interface ActionLog {
        String PREFIX = "v1/action-log";
    }

    public interface UserOtp {
        String PREFIX = "v1/user-otp";
    }
}
