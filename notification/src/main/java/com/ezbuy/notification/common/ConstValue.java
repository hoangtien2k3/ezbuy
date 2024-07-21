package com.ezbuy.notification.common;

public class ConstValue {
    public static final class Channel {
        public static final String CHANNEL_SMS = "SMS";
        public static final String CHANNEL_REST = "REST";
        public static final String CHANNEL_WEB_SOCKET = "WEB_SOCKET";
        public static final String CHANNEL_EMAIL = "EMAIL";
    }

    public static final class Category {
        public static final String NEW = "TIN_TUC";
        public static final String NOTICE = "THONG_BAO";
    }

    public static final class NotiServerity {
        public static final String NORMAL = "NORMAL";
        public static final String CRITICAL = "CRITICAL";
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
        public static final String TIN_TUC = "TIN_TUC";
        public static final String THONG_BAO = "THONG_BAO";
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
}
