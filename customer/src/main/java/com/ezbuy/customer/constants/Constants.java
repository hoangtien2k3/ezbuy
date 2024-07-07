package com.ezbuy.customer.constants;

public class Constants {
    public static final String EMPTY = "";
    public static final String FULLNAME_PATTERN = "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";
    public static final String NUMBER_PATTERN = "^[0-9]+$";
    public static final String ID_NO_PATTERN = "^[0-9\\-]+$";
    public static final String SALE_NO_PATTERN = "^[a-zA-Z0-9_-]+$";
    public static final String PASSPORT_PATTERN = "^[a-zA-Z0-9-]+$";

    public static class CUSTOMER_GROUP_CODE {
        public static final Integer VIP = 1;
        public static final Integer REGULAR = 2;
        public static final Integer GUEST = 3;
        public static final Integer NORMAL = 4;
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

    public static String CUSTOMER_SOURCE_ALIAS = "HUB_CUSTOMER_INTERNAL";
}
