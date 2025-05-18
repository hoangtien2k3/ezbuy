package com.ezbuy.notisendservice.constants;

import java.util.Map;

public class CommonConstants {

    public static class TemplateMail {
        public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
        public static final String SIGN_UP = "SIGN_UP";
        public static final String CUSTOMER_ACTIVE_SUCCESS = "CUSTOMER_ACTIVE_SUCCESS";
        public static final String CUSTOMER_REGISTER_SUCCESS = "CUSTOMER_REGISTER_SUCCESS";
        public static final String EMPLOYEE_REGISTER_SUCCESS = "EMPLOYEE_REGISTER_SUCCESS";
        public static final String ACCOUNT_ACTIVE = "ACCOUNT_ACTIVE";
        public static final String SIGN_UP_PASSWORD ="SIGN_UP_PASSWORD";
        public static final String VERIFY_ACCOUNT_SUCESS = "VERIFY_ACCOUNT_SUCESS";
        public static final String NOTI_VERIFY_ACCOUNT = "NOTI_VERIFY_ACCOUNT";
        public static final String CREATE_ACCOUNT_SUCCESS ="CREATE_ACCOUNT_SUCCESS"; //luong mail thong bao tao tai khoan thanh cong
        public static final String CHANGE_PASSWORD_SUCCESS ="CHANGE_PASSWORD_SUCCESS"; //luong mail thong bao doi mat khau thanh cong
        public static final String APPROVE_ORDER_SUCCESS = "APPROVE_ORDER_SUCCESS"; //template gui mail thong bao phe duyet don hang thanh cong
        public static final String APPROVE_ORDER_FAIL = "APPROVE_ORDER_FAIL"; //template gui mail thong bao phe duyet don hang that bai
        public static final String NEED_APPROVE = "NEED_APPROVE";//template gui mail thong bao can phe duyet cho admin
        public static final String VERIFY_IDENTIFY_REJECTED = "VERIFY_IDENTIFY_REJECTED";//template gui mail thong bao tu choi xac thuc danh tinh
        public static final String VERIFY_IDENTIFY_SUCCESS = "VERIFY_IDENTIFY_SUCCESS";//template gui mail thong bao xac thuc danh tinh thanh cong
        public static final String ADTECH_TERMINATE_NOTI = "ADTECH_TERMINATE_NOTI";//template gui mail thong bao cham dut hop dong voi adtech
        public static final String MIGRATE_EMPLOYEE_USER = "MIGRATE_EMPLOYEE_USER";
    }

    public static final Map<String, String> TEMPLATE_MAIL_MAP = Map.ofEntries(
            Map.entry(TemplateMail.SIGN_UP, "mail/TransmissionOtpMailSignUp.html"),
            Map.entry(TemplateMail.FORGOT_PASSWORD, "mail/TransmissionOtpMailForgotPassword.html"),
            Map.entry(TemplateMail.CUSTOMER_REGISTER_SUCCESS, "mail/TransmissionOtpMailForgotPassword.html"),
            Map.entry(TemplateMail.SIGN_UP_PASSWORD, "mail/TransmissionOtpMailSignUpPassword.html"),
            Map.entry(TemplateMail.ACCOUNT_ACTIVE, "mail/CreateUser.html"),
            Map.entry(TemplateMail.EMPLOYEE_REGISTER_SUCCESS, "mail/ActiveAccount.html"),
            Map.entry(TemplateMail.VERIFY_ACCOUNT_SUCESS, "mail/TransmissionMailVerifySuccess.html"), // path file html thong bao thanh cong
            Map.entry(TemplateMail.NOTI_VERIFY_ACCOUNT, "mail/TransmissionMailNotiVerify.html"), // path file html thong bao thanh cong
            Map.entry(TemplateMail.CREATE_ACCOUNT_SUCCESS, "mail/TransmissionMailCreateAccountSuccess.html"), // file html gui mail thong bao tao tai khoan thanh cong
            Map.entry(TemplateMail.CHANGE_PASSWORD_SUCCESS, "mail/TransmissionMailChangePasswordSuccess.html"), // file html gui mail thong bao doi mat khau thanh cong
            Map.entry(TemplateMail.APPROVE_ORDER_SUCCESS, "mail/TransmissionMailApproveSuccess.html"), // path file html thong bao thanh cong luong phe duyet don hang
            Map.entry(TemplateMail.APPROVE_ORDER_FAIL, "mail/TransmissionMailApproveFail.html"), // path file html thong bao that bai luong phe duyet don hang
            Map.entry(TemplateMail.ADTECH_TERMINATE_NOTI, "mail/AdtechTerminateNoti.html"), // path file html thong bao that bai luong phe duyet don hang
            Map.entry(TemplateMail.MIGRATE_EMPLOYEE_USER, "mail/TransmissionMailMigrateAccountSuccess.html") // path file html thong bao migrate tai khoan tu EB to HUB
    );

    public static final String TEMP_ACTIVE_ACCOUNT_CUSTOMER = "mail/ActiveCustomerAccount.html";
    public static final String TEMP_VERIFY_ACCOUNT_SUCESS = "mail/TransmissionMailVerifySuccess.html";
    public static final String TEMP_NOTI_VERIFY_ACCOUNT = "mail/TransmissionMailNotiVerify.html";
    public static final String TEMP_ACCOUNT_CUSTOMER_INFO = "mail/CustomerInfoAccount.html";

    public static final String TEMP_CREATE_ACCOUNT_SUCCESS = "mail/TransmissionMailCreateAccountSuccess.html"; //file html gui mail thong bao tao tai khoan thanh cong
    public static final String TEMP_CHANGE_PASSWORD_SUCCESS = "mail/TransmissionMailChangePasswordSuccess.html"; //file html gui mail thong bao doi mat khau thanh cong
    public static final String TEMP_APPROVE_ORDER_SUCCESS = "mail/TransmissionMailApproveSuccess.html"; // path file html thong bao thanh cong luong phe duyet don hang
    public static final String TEMP_APPROVE_ORDER_FAIL = "mail/TransmissionMailApproveFail.html"; // path file html thong bao that bai luong phe duyet don hang
    public static final String NOTI_NEED_APPROVE = "mail/TransmissionMailNoticeNeedApprove.html"; //path file html thong bao can phe duyet
    public static final String NOTI_VERIFY_IDENTIFY_REJECTED = "mail/TransmissionMailVerifyIdentifyRejected.html";//path file html thong bao tu choi xac thuc
    public static final String NOTI_VERIFY_IDENTIFY_SUCCESS = "mail/TransmissionMailVerifyIdentifySuccess.html";//path file html thong bao xac thuc thanh cong
    public static final String TEMP_ADTECH_TERMINATE_NOTI = "mail/AdtechTerminateNoti.html"; //path file html thong bao cham dut hop dong adtech

    public static class VariableContextMail {
        public static final String EMAIL_RECIPIENT = "EMAIL_RECIPIENT"; //email nguoi nhan ban giao
        public static final String PHONE_RECIPIENT = "PHONE_RECIPIENT"; // so dien thoai nguoi nhan ban giao
        public static final String LINK_APPROVE = "LINK_APPROVE"; //link den man nghiem thu don hang tren hub
        public static final String CANCEL_REASON = "CANCEL_REASON"; //li do huy
        public static final String DATE_TERMINATE = "DATE_INACTIVE"; //ngay he thong adtech dong bang
        public static final String USERNAME = "USERNAME"; // ten dang nhap
        public static final String PASSWORD = "PASSWORD"; // mat khau
    }
}
