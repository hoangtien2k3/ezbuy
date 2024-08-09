package com.ezbuy.sendnotification.constants;

import java.util.Map;

import static com.ezbuy.framework.constants.Constants.Otp.FORGOT_PASSWORD;
import static com.ezbuy.framework.constants.Constants.TemplateMail.*;

public class CommonConstants {

    public static final Map<String, String> TEMPLATE_MAIL_MAP = Map.ofEntries(
            Map.entry(SIGN_UP, "mail/TransmissionOtpMailSignUp.html"),
            Map.entry(FORGOT_PASSWORD, "mail/TransmissionOtpMailForgotPassword.html"),
            Map.entry(CUSTOMER_REGISTER_SUCCESS, "mail/TransmissionOtpMailForgotPassword.html"),
            Map.entry(SIGN_UP_PASSWORD, "mail/TransmissionOtpMailSignUpPassword.html"),
            Map.entry(ACCOUNT_ACTIVE, "mail/CreateUser.html"),
            Map.entry(EMPLOYEE_REGISTER_SUCCESS, "mail/ActiveAccount.html"),
            Map.entry(VERIFY_ACCOUNT_SUCESS, "mail/TransmissionMailVerifySuccess.html"), // path file html thong bao thanh cong
            Map.entry(NOTI_VERIFY_ACCOUNT, "mail/TransmissionMailNotiVerify.html") // path file html thong bao thanh cong
    );

    public static final String TEMP_ACTIVE_ACCOUNT_CUSTOMER = "mail/ActiveCustomerAccount.html";
    public static final String TEMP_VERIFY_ACCOUNT_SUCESS = "mail/TransmissionMailVerifySuccess.html";
    public static final String TEMP_NOTI_VERIFY_ACCOUNT = "mail/TransmissionMailNotiVerify.html";
    public static final String TEMP_ACCOUNT_CUSTOMER_INFO = "mail/CustomerInfoAccount.html";
}
