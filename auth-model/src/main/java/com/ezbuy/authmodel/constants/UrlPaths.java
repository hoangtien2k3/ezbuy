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
        // dang base64 tu vcontract de
        // view len giao dien Hub

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

    public interface Organization {
        String PREFIX = "v1/organization";
        String GET_UNIT_TYPE = "/unit-type";
        String GET_POSIONS = "/unit/positions";
        String GET_ORGANIZATION = "";
        String GET_ORGANIZATION_BY_INDIVIDUAL_ID = "get-organization-by-individual-id";

        String CREATE_ORGANIZATION = "";
        String UPDATE_ORGANIZATION = "update";

        String GET_LIST_ORGANIZATION = "/list";
    }

    public interface Organization_Unit {
        String CREATE_ORGANIZATION_UNIT = "/unit/create";
        String UPDATE_ORGANIZATION_UNIT = "/unit/update/{organizationUnitId}";
        String DELETE_ORGANIZATION_UNIT = "/unit/delete/{organizationUnitId}";
        String GET_UNIT = "/unit";
        String GET_ORGANIZATION_UNIT = "/unit/{organizationUnitId}";
        String GET_UNIT_TEMPLATE = "/unit/template";
        String DOWNLOAD_RESULT_IMPORT = "/unit/import/result";
        String IMPORT_UNIT = "/unit/import";
        String VALIDATE_IMPORT_UNIT = "/unit/validate-import";
        String GET_UNIT_RELATION = "/unit/relation";
        String GET_ALL_ORGANIZATION_UNIT_ACTIVE = "/unit/all-organization-active";
        String GET_ROOT_UNIT = "/unit/root-unit"; // api lay don vi goc
        String GET_UNIT_BY_CODE = "/unit/code/{organizationId}/{code}"; // api lay don vi theo ma code
        String GET_UNIT_SUB_BY_PARENT_ID = "/unit/sub"; // api lay don vi con
        String GET_UNIT_BY_USER_ID_AND_ORGANIZATION_ID = "/unit/user-organization"; // api lay danh sach don vi theo
        // user va to chuc
        String GET_USER_UNIT = "/user-units"; // api lay danh sach don vi theo user
    }

    public interface IndividualPermission {
        String PREFIX = "/sme-user/v1";
        String GET_PERMISSION = "/permissionPolicy";
        String DELETE_PERMISSION = "/permission-policy/{permissionPolicyId}";
        String ADD_PERMISSION = "/permission/add";
        String UPDATE_PERMISSION = "/permission/update";
        String GET_TELECOMSERVICE = "/permission/get-telecomservice";
    }

    public interface INDIVIDUAL {
        String PREFIX = "v1/individual";
        String GET_INDIVIDUAL = "";
        String GET_OWNER_ORGANIZATION_BY_INDIVIDUAL_ID = "get-owner-organization-by-individual-id";
        String UPDATE_INDIVIDUAL = "update";
        String UPDATE_REPRESENTATIVE = "representative";
        String FIND_INDIVIDUAL_ID_BY_USER_ID_AND_ORG_ID = "/user-id";
        String FIND_USER_ID_BY_INDIVIDUAL_ID = "/user-id/individual-id";
        String FIND_INDIVIDUAL_LOGIN = "/current";
        String FIND_INDIVIDUAL_ID_BY_USER_ID = "/find-by-user-id";
        String FIND_REPRESENTATIVE_BY_ORGANIZATION_ID = "/find-representative-by-organization-id";
    }

    public interface Identify {
        String PREFIX = "v1/identify";
        String UPDATE_IDENTIFY = "update";
        String TRUSTED_IDNO = "trusted-idNo";
        String GET_IDNO = "idNo";
        String TRUSTED_IDNO_ORGANIZATION = "trusted-idNo-organization";
        String TRUSTED_IDNO_ORGANIZATION_MEMBER = "trusted-idNo-organization-member";
        String GET_ID_NO_ORGANIZATION = "idNo-organization";
        String LST_TRUSTED_IDNO_ORGANIZATION = "lst-trusted-idNo-organization";
        String GET_INFO_TENANT_IDENTIFY = "info-tenant-identify"; // lay thong tin giay to theo loai giay to
        String FIND_BY_TENANT_ID_AND_TYPE = "find-by-tenant-id-and-type"; // lay thong tin giay to theo loai giay to va
        // id
        String IDENTIFY_BY_TYPE_TENANT_ID_TRUST_STATUS = "/type-tenant-trust-status"; // lay danh sach tenant identify
        // theo dieu kien truyen vao
        String UPDATE_ORG_TRUST_STATUS = "/org-trust-status"; // ham cap nhat trang thai xac minh doanh nghiep
        String GET_CTS_LIST_CA = "/cts-list-ca"; // lay danh sach chung thu so
        String UPDATE_ORG_TRUST_STATUS_BY_TENANT_ID = "/org-trust-status/tenant-id";
        String UPDATE_BUSINESS_AUTH_TENANT = "update/business-auth-tenant"; // lay thong tin giay to theo loai giay to
        String GET_INFO_TENANT_IDENTIFY_BY_TYPE = "tenant-identify/type"; // lay thong tin giay to theo loai giay to

        String UPDATE_ORG_TRUST_STATUS_NOT_SIGN = "/org-trust-status/not-sign"; // ham cap nhat trang thai xac thuc
        // doanh nghiep sau khi tao don cts
        String GET_INFO_TENANT_IDENTIFY_INPROGRESS = "info-tenant-identify/in-progress"; // lay thong tin giay to dang
        // xac thuc
    }

    public interface SyncData {
        String PREFIX = "v1/sync-data";
        String INDIVIDUAL = "individual";
        String ORGANIZATION = "organization";
        String ORGANIZATION_UNIT = "organization-unit";
        String POLICY = "policy";
        String PASSWORD = "password";
        String TRIGGER = "trigger";
        String RETRY = "retry";
        String JOB_SYNC_PASSWORD = "job-sync-password";
        String HISTORY_RETRY = "history-retry";
    }

    public interface UserGroup {
        String PREFIX = "v1/permission-policy";
        String RESOURCE_PERMISSION = "/resource-permissions";
        String GROUP_PERMISSION = "/group-permissions";
        String ROLES = "/roles";
    }

    public interface Util {
        String PREFIX = "v1/util";
        String JOB_ADD_ROLE_ADMIN_FOR_OLD_USER = "add-role-admin-for-old-user"; // add role admin of Hub for old user
    }

    public interface EMPLOYEE {
        String PREFIX = "v1/employee";
        String GET_EMPLOYEE = "";
        String CREATE_EMPLOYEE = "create";
        String UPDATE_EMPLOYEE = "update";
        String DELETE_EMPLOYEE = "delete";
        String DETAIL_EMPLOYEE = "detail";
        String GENERATE_EMPLOYEE_CODE = "generate-employee-code";
        String DOWNLOAD_TEMPLATE_EMPLOYEE = "employee-resource-template";
        String DOWNLOAD_TEMPLATE_ROLE = "role-resource-template";
        String IMPORT_EMPLOYEE = "/import/employees";
        String IMPORT_ROLE = "/import/roles";
    }

    public interface FILE {
        String PREFIX = "v1/file";
        String DOWNLOAD = "download";
    }

    public interface INDIVIDUAL_UNIT_POSITION {
        String PREFIX = "v1/individual-unit-position";
        String FIND_UNIT_ID_BY_IND_ID_AND_ORG_ID = "";
    }

    public interface UserCredential {
        String PREFIX = "v1/user-credential"; // api lay thong tin user dang nhap
    }

    public interface ActionLog {
        String PREFIX = "v1/action-log";
    }

    public interface TenantFile {
        String PREFIX = "v1/tenant-file";
    }

    public interface OrgBusinessInfo {
        String PREFIX = "v1/org-business-info";
    }
}
