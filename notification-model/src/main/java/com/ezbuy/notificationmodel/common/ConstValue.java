package com.ezbuy.notificationmodel.common;

public class ConstValue {
    public static final class Channel {
        public static final String CHANNEL_SMS = "SMS";
        public static final String CHANNEL_REST = "REST";
        public static final String CHANNEL_EMAIL = "EMAIL";
        public static final String CHANNEL_WEB_SOCKET = "WEB_SOCKET";
    }

    public static final class Category {
        public static final String NEW = "NEWS"; // tin tuc
        public static final String NOTICE = "NOTICE"; // thong bao
    }

    public static final class NotiServerity {
        public static final String NORMAL = "NORMAL"; // binh thuong
        public static final String CRITICAL = "CRITICAL"; // phe binh
    }

    public static final class TransmissionState {
        public static final String FAILED = "FAILED";
        public static final String UNREAD = "UNREAD";
        public static final String PENDING = "PENDING";
        public static final String READ = "READ";
        public static final String NEW = "NEW";
        public static final String SENT = "SENT";
    }

    public static final class NotificationConstant {
        public static final String NEWS = "NEWS";
        public static final String ANNOUNCEMENT = "ANNOUNCEMENT";
        public static final Integer PAGE_SIZE = 7;
    }

    public static final class ControllerPath {
        public static final String TRANSMISSION_PATH = "/v1/transmission";
    }

    public static final class ContentTypeConstant {
        public static final String TEXT = "text/plain";
        public static final String HTML = "html/plain";
    }

    public static final class CommonMessageNoti {
        public static final String INVALID_FORMAT_SPEC = "params.invalid.format.spec";
    }

    public static final class Status {
        public static final int ACTIVE = 1;
        public static final int INACTIVE = 0;
    }

    public static class SendSmsStatus {
        public static final Long NEW = 1L;          //Moi
        public static final Long SENDING = 2L;      //Dang gui
        public static final Long SENT = 3L;         //Da gui thanh cong
        public static final Long SENT_FAILED = 4L;  //Gui loi
    }


    ///// send sms
    public static final class SEND_SMS {
        public static final String STATUS_NEW_SMS = "1";    // Trang thai sms da duoc xu ly
        public static final String STATUS_SEND_SMS = "0";    //STATUS_SEND_SMS
        public static final Long NOT_RETRY = 0L;
        public static final String STATUS_ACTIVE = "1";

        public static final String DEFAULT_SMS_CP_CODE = "155";
        public static final String DEFAULT_SMS_ALIAS = "155";

        public static final String REQUEST_ID = "1";
        public static final String COMMAND_CODE = "bulksms";
        public static final String CONTENT_TYPE_VIETNAM = "1";
        public static final String CONTENT_TYPE_VIETNAM_NON = "0";

        public static final Long CCAPI_RESULT_ERROR = 0L;
        public static final Long CCAPI_RESULT_SUCCESS = 1L;
        public static final Long CCAPI_RESULT_AUTHEN = 3L;
        public static final Long CCAPI_RESULT_ERROR_SYSTEM = -1L;

        public static final String GECO_002 = "GECO_002"; //gui tin nhan thanh cong
    }

    public static final class OptionSet {
        public static final String MODE_SEND_SMS_CALL_API = "MODE_SEND_SMS_CALL_API";//null hoac 1 thi goi soap, 2 thi goi rest
        public static final String MODE_SEND_SMS_CONVERT_VN = "MODE_SEND_SMS_CONVERT_VN";//0: khong dau, 1: co dau, mac dinh la co dau

    }

    public static final class SendSmsConvertVN {
        public static final Integer UNACCENTED = 0; //gui tin nhan khong dau
        public static final Integer ACCENTED = 1; //gui tin nhan co dau
    }

    public static final class Common {
        public static final String STRING_ACTIVE_STATUS = "1";

    }

}
