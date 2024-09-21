package com.ezbuy.notisend.constants;

import java.util.Map;

public class CommonConstants {

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
    }

    public static final Map<String, String> TEMPLATE_MAIL_MAP = Map.ofEntries(
            Map.entry(TemplateMail.SIGN_UP, "mail/TransmissionOtpMailSignUp.html"),
            Map.entry(TemplateMail.FORGOT_PASSWORD, "mail/TransmissionOtpMailForgotPassword.html"),
            Map.entry(TemplateMail.CUSTOMER_REGISTER_SUCCESS, "mail/TransmissionOtpMailForgotPassword.html"),
            Map.entry(TemplateMail.SIGN_UP_PASSWORD, "mail/TransmissionOtpMailSignUpPassword.html"),
            Map.entry(TemplateMail.ACCOUNT_ACTIVE, "mail/CreateUser.html"),
            Map.entry(TemplateMail.EMPLOYEE_REGISTER_SUCCESS, "mail/ActiveAccount.html"),
            Map.entry(TemplateMail.VERIFY_ACCOUNT_SUCESS, "mail/TransmissionMailVerifySuccess.html"), // path file html
            // thong bao
            // thanh cong
            Map.entry(TemplateMail.NOTI_VERIFY_ACCOUNT, "mail/TransmissionMailNotiVerify.html") // path file html thong
            // bao thanh cong
            );

    public static final String TEMP_ACTIVE_ACCOUNT_CUSTOMER = "mail/ActiveCustomerAccount.html";
    public static final String TEMP_VERIFY_ACCOUNT_SUCESS = "mail/TransmissionMailVerifySuccess.html";
    public static final String TEMP_NOTI_VERIFY_ACCOUNT = "mail/TransmissionMailNotiVerify.html";
    public static final String TEMP_ACCOUNT_CUSTOMER_INFO = "mail/CustomerInfoAccount.html";
}
