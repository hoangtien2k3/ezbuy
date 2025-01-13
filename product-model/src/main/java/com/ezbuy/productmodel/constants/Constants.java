package com.ezbuy.productmodel.constants;

import java.util.List;

public class Constants {
    public static final class Message {
        public static final String SUCCESS = "success";
        public static final String FAIL = "fail";
    }

    public static final String ORDER_SYSTEM = "ORDER_SYSTEM";
    public static final String PRICE_FILTER = "PRICE_FILTER";
    public static final String PHASE_EXTEND_PACKAGE = "EXTEND_PACKAGE";

    public static final List<String> TAX_RATIO_LIST = List.of("NV", "ND", "0", "5", "8", "10");
    public static final List<String> REVENUE_RATIO_LIST = List.of("1", "2", "3", "5");

    public static final class LOCK_STATUS {
        public static final Integer LOCK_STATUS_UNLOCK = 0;
        public static final Integer LOCK_STATUS_LOCK = 1;
    }

    public static final String CHAR_AND_NUM_REGEX = "^[a-zA-Z0-9\\-\\_]*$";
    public static final String PRICE_REGEX = "^(?!0+\\.00)(?=.{1,10}(\\.|$))\\d{1,3}(,\\d{3})*(\\.\\d+)?$";
    public static final String VIE_CHAR_AND_NUM_REGEX =
            "^([0-9a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀẾỂưăạảấầẩẫậắằẳẵặẹẻẽềếểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹý\\s\\-\\_]+)$";
    public static final String VIE_CHAR_REGEX =
            "^([a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀẾỂưăạảấầẩẫậắằẳẵặẹẻẽềếểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹý\\s]+)$";

    public static final class ROW_TEMPLATE_NAME {
        public static final String CODE = "product.import.column.code";
        public static final String NAME = "product.import.column.name";
        public static final String PRICE_IMPORT = "product.import.column.price.import";
        public static final String PRICE_EXPORT = "product.import.column.price.export";
        public static final String UNIT = "product.import.column.unit";
        public static final String TAX_RATIO = "product.import.column.tax.ratio";
        public static final String DISCOUNT = "product.import.column.discount";
        public static final String REVENUE_RATIO = "product.import.column.revenue.ratio";
        public static final String OBLIGATORY = " (*)";
    }

    public static class ServiceId {
        public static final String VBHXH = "151";
    }

    public static class TelecomServiceAlias {
        public static final String VBHXH = "VBHXH";
    }

    //trang thai state bang voucher
    public static final class VOUCHER_STATE {
        public static final String NEW = "new"; // voucher moi chua su dung
        public static final String LOCKED = "locked"; // voucher da lock de thuc hien thanh toan
        public static final String USED = "used"; // voucher da duoc gan cho user
        public static final String INACTIVE = "inactive"; // voucher khong hieu luc
    }
    //trang thai state bang voucher_batch
    public static final class VOUCHER_BATCH_STATE {
        public static final String NEW = "NEW"; // lo voucher dang trang thai chua su dung
        public static final String INPROGRESS = "INPROGRESS"; // trang thai lo voucher dang duoc xu ly
        public static final String COMPLETE = "DONE";//trang thai lo voucher da xu ly xong
        public static final String FAILED = "FAILED";
    }

    //trang thai state bang voucher_use
    public static final class VOUCHER_USE_STATE {
        public static final String ACTIVE = "active";
        public static final String PRE_ACTIVE = "preActive";
        public static final String INACTIVE = "inactive";
        public static final String USING = "using";// dang trong luong don hang
        public static final String USED = "used";
    }

    //trang thai state bang voucher_transaction
    public static final class VOUCHER_TRANSACTION_STATE {
        public static final String ACTIVE = "active"; // trang thai trong khi giao dich thanh toan thanh cong
        public static final String PRE_ACTIVE = "preActive";
        public static final String INACTIVE = "inactive";
        public static final String USED = "used";
    }

    //trang thai transaction_type bang voucher_transaction
    public static final class VOUCHER_TRANSACTION_TYPE {
        public static final String CONNECT = "CONNECT";
        public static final String AFTER = "AFTER";
    }

    public static class VOUCHER_TYPE {
        public static final Integer CODE_MAX_LENGTH = 100;
        public static final Integer DESCRIPTION_MAX_LENGTH = 1000;
        public static final String CODE_REGEX = "[^a-zA-Z0-9_]+";
        public static final String ACTION_TYPE_DISCOUNT = "discount";
        public static final String ACTION_TYPE_FIXED = "fixed";
        public static final String ACTION_TYPE_ITEM = "item";
    }
}
