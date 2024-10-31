package com.ezbuy.notificationsend.constants

object CommonConstants {
    object TemplateMail {
        const val FORGOT_PASSWORD = "FORGOT_PASSWORD"
        const val SIGN_UP = "SIGN_UP"
        const val CUSTOMER_ACTIVE_SUCCESS = "CUSTOMER_ACTIVE_SUCCESS"
        const val CUSTOMER_REGISTER_SUCCESS = "CUSTOMER_REGISTER_SUCCESS"
        const val EMPLOYEE_REGISTER_SUCCESS = "EMPLOYEE_REGISTER_SUCCESS"
        const val ACCOUNT_ACTIVE = "ACCOUNT_ACTIVE"
        const val SIGN_UP_PASSWORD = "SIGN_UP_PASSWORD"
        const val VERIFY_ACCOUNT_SUCCESS = "VERIFY_ACCOUNT_SUCCESS"
        const val NOTI_VERIFY_ACCOUNT = "NOTI_VERIFY_ACCOUNT"
    }

    val TEMPLATE_MAIL_MAP: Map<String, String> = mapOf(
        TemplateMail.SIGN_UP to "mail/TransmissionOtpMailSignUp.html",
        TemplateMail.FORGOT_PASSWORD to "mail/TransmissionOtpMailForgotPassword.html",
        TemplateMail.CUSTOMER_REGISTER_SUCCESS to "mail/TransmissionOtpMailForgotPassword.html",
        TemplateMail.SIGN_UP_PASSWORD to "mail/TransmissionOtpMailSignUpPassword.html",
        TemplateMail.ACCOUNT_ACTIVE to "mail/CreateUser.html",
        TemplateMail.EMPLOYEE_REGISTER_SUCCESS to "mail/ActiveAccount.html",
        TemplateMail.VERIFY_ACCOUNT_SUCCESS to "mail/TransmissionMailVerifySuccess.html", // path file html
        TemplateMail.NOTI_VERIFY_ACCOUNT to "mail/TransmissionMailNotiVerify.html" // path file html thong
    )

    const val TEMP_ACTIVE_ACCOUNT_CUSTOMER = "mail/ActiveCustomerAccount.html"
    const val TEMP_VERIFY_ACCOUNT_SUCCESS = "mail/TransmissionMailVerifySuccess.html"
    const val TEMP_NOTI_VERIFY_ACCOUNT = "mail/TransmissionMailNotiVerify.html"
    const val TEMP_ACCOUNT_CUSTOMER_INFO = "mail/CustomerInfoAccount.html"
}
