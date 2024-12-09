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

    public static class SendSmsErrorCode {
        public static final int SUCCESS = 0;                  // Thành công
        public static final int BRAND_ERROR = 1;              // Lỗi thương hiệu
        public static final int DUPLICATE_BRAND = 2;          // Lỗi lặp tin cùng thương hiệu
        public static final int TEMPLATE_ERROR = 3;           // Lỗi mẫu tin
        public static final int PRICE_ERROR = 4;              // Lỗi giá tin
        public static final int INSUFFICIENT_BALANCE = 5;     // Lỗi không đủ tiền
        public static final int BLOCKED_KEYWORDS = 6;         // Lỗi chứa từ khóa bị chặn
        public static final int UNKNOWN_SUBSCRIBER = 7;       // Lỗi thuê bao không có trong danh bạ
        public static final int OPT_OUT = 8;                  // Lỗi thuê bao từ chối nhận tin quảng cáo
        public static final int DUPLICATE_AD = 9;             // Lỗi lặp tin quảng cáo
        public static final int CONTENT_LENGTH_ERROR = 10;    // Lỗi nội dung tin vượt quá số ký tự cho phép
        public static final int NETWORK_ERROR = 11;           // Lỗi kết nối mạng
        public static final int UNKNOWN_SUBSCRIBER_ERROR = 12; // Thuê bao không xác định
        public static final int FAILURE = 13;                 // Thất bại
    }
}
