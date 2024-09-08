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
package com.ezbuy.customer.constants;

public class Const {
    public static final String EMPTY = "";
    public static final String FULLNAME_PATTERN =
            "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";
    public static final String NUMBER_PATTERN = "^[0-9]+$";
    public static final String ID_NO_PATTERN = "^[0-9\\-]+$";
    public static final String SALE_NO_PATTERN = "^[a-zA-Z0-9_-]+$";
    public static final String PASSPORT_PATTERN = "^[a-zA-Z0-9-]+$";

    public static Boolean TRUE = true;
    public static Boolean FALSE = false;

    public static final class ErrorCode {
        public static final String ERROR_CODE_SUCCESS = "0";
        public static final String ERROR_CODE_FALSE = "-1";
    }

    public static final class Notification {
        public static final String SEVERITY = "NORMAL";
        public static final String CONTENT_TYPE = "text/plain";
        public static final String CATEGORY_TYPE = "THONG_BAO";
        public static final String CHANNEL_TYPE = "EMAIL";
    }

    public static class CUSTOMER_GROUP_CODE {
        public static final Long VIP = 1L;
        public static final Long REGULAR = 2L;
        public static final Long GUEST = 3L;
        public static final Long NORMAL = 4L;
    }

    public static class VERIFIED_EMAIL {
        public static final Integer NOT_VERIFIED = 0;
        public static final Integer VERIFIED = 1;
    }

    public static class CUST_TYPE {
        public static Integer INDIVIDUAL = 1;
        public static Integer ORGANIZATION = 2;
    }

    public static class ADDITION_INFO_KEY {
        public static final String fieldOfActivities = "fieldOfActivities"; // Lĩnh vực hoạt động
        public static final String organizationType = "organizationType"; // Loại tổ chức
        public static final String positionCode = "positionCode"; // Chức vụ
        public static final String organizationUnitCode = "organizationUnitCode"; // Loại đơn vị
    }

    public static class TemplateMail {
        public static final String CUSTOMER_ACTIVE_SUCCESS = "CUSTOMER_ACTIVE_SUCCESS";
        public static final String CUSTOMER_REGISTER_SUCCESS = "CUSTOMER_REGISTER_SUCCESS";
    }

    public static class TEMPLATE_IMPORT {
        public static final String CUSTOMER_IND_IMPORT_RESULT = "customer_individual_import_result.xlsx";
        public static final String CUSTOMER_ORG_IMPORT_RESULT = "customer_organization_import_result.xlsx";
        public static final String CUSTOMER_IND_IMPORT_NAME = "customer_individual_import.xlsx";
        public static final String CUSTOMER_ORG_IMPORT_NAME = "customer_organization_import.xlsx";
        public static final String CUSTOMER_IND_TEMPLATE_PATH = "/template/customer_individual_import.xlsx";
        public static final String CUSTOMER_ORG_TEMPLATE_PATH = "/template/customer_organization_import.xlsx";
    }

    public static class OPTION_SET_CONFIG {
        public static String CUST_TYPE = "CUST_TYPE";
        public static String STATE = "STATE";
        public static String GENDER = "GENDER";
        public static String FIELD_OF_ACTIVITIES = "FIELD_OF_ACTIVITIES";
        public static String DOCUMENT_TYPE = "DOCUMENT_TYPE";
        public static String PAYMENT_TYPE = "PAYMENT_TYPE";
        public static String POSISIONS = "POSISIONS";
    }

    public static class SYNC_HISTORY_STATE {
        public static String WAIT_SYNC = "waitSync";
        public static String DONE = "done";
        public static String FAIL = "fail";
        public static String IN_PROGRESS = "inProgress";
    }

    public static class PAYMENT_TYPE {
        public static String BANK = "1";
        public static String CASH = "2";
    }

    public static class IS_VALID {
        public static String ERROR_MES_EMPTY = "";
    }

    public static class CUST_PASSWORD {
        public static Integer MIN_LENGTH = 8;
        public static Integer MAX_LENGTH = 20;
    }

    public static class STATUS_CODE {
        public static Integer SUCCESS = 200;
        public static Integer BAD_REQUEST = 400;
        public static Integer UNAUTHORIZED = 401;
        public static Integer FORBIDDEN = 403;
        public static Integer NOT_FOUND = 404;
        public static Integer INTERNAL_SERVER_ERROR = 500;
    }

    public static class STATUS_ACCOUNT {
        public static Integer ACTIVE = 1;
        public static Integer INACTIVE = 2;
    }

    public static class ROLE {
        public static String ADMIN = "ADMIN";
        public static String USER = "USER";
        public static String SHOP = "SHOP";
    }

    public static class CUST_GROUP {
        public static Long VIP = 1L;
        public static Long GOLD = 2L;
        public static Long SILVER = 3L;
        public static Long REGULAR = 4L;
        public static Long NEW = 5L;
    }

    public static class VERIFY {
        public static String NOT_VERIFY = "0";
        public static String VERIFY = "1";
    }
}
