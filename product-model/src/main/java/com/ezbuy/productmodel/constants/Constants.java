package com.ezbuy.productmodel.constants;

import java.util.List;

public class Constants {
    public static final class Message {
        public static final String SUCCESS = "success";
        public static final String FAIL = "fail";
    }

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

    public static class SYNC_HISTORY_STATE {
        public static String WAIT_SYNC = "waitSync";
        public static String DONE = "done";
        public static String FAIL = "fail";
        public static String IN_PROGRESS = "inProgress";
    }

    public static class SYNC_HISTORY_DETAIL {
        public static String SOURCE_ALIAS_HUB_PRODUCT = "HUB_PRODUCT_INTERNAL";
    }

    public static class TelecomServiceId {
        public static final Long CA = 7L;
        public static final Long EASY_BOOK = 208L;
        public static final Long SINVOICE = 37L;
        public static final Long VCONTRACT = 101L;
        public static final Long VBHXH = 151L;
    }

    public static class ServiceId {
        public static final String VBHXH = "151";
    }

    public static class TelecomServiceAlias {
        public static final String VBHXH = "VBHXH";
    }
}
