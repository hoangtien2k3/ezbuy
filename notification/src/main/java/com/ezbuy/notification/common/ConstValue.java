package com.ezbuy.notification.common;

public class ConstValue {
    public interface Channel {
        String CHANNEL_SMS = "SMS";
        String CHANNEL_REST = "REST";
        String CHANNEL_WEB_SOCKET = "WEB_SOCKET";
        String CHANNEL_EMAIL = "EMAIL";
    }

    public interface Category {
        String NEW = "TIN_TUC";
        String NOTICE = "THONG_BAO";
    }

    public interface NotiServerity {
        String NORMAL = "NORMAL";
        String CRITICAL = "CRITICAL";
    }

    public interface TransmissionState {
        String FAILED = "FAILED";
        String UNREAD = "UNREAD";
        String PENDING = "PENDING";
        String READ = "READ";
        String NEW = "NEW";
        String SENT = "SENT";
    }

    public interface NotificationConstant {
        String TIN_TUC = "TIN_TUC";
        String THONG_BAO = "THONG_BAO";
        Integer PAGE_SIZE = 7;
    }

    public interface ControllerPath {
        String TRANSMISSION_PATH = "/v1/transmission";
    }

    public interface ContentTypeConstant {
        String TEXT = "text/plain";
        String HTML = "html/plain";
    }

    public interface CommonMessageNoti {
        String INVALID_FORMAT_SPEC = "params.invalid.format.spec";
    }
}
