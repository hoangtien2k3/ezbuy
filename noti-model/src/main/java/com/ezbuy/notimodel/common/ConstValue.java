/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.notimodel.common;

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
}
