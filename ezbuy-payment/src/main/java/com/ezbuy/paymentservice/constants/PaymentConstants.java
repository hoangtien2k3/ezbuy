package com.ezbuy.paymentservice.constants;

import java.util.List;

public class PaymentConstants {

    public static class OrderType {
        public static final String PAID_ORDER = "PAID_ORDER";
        public static final String SELFCARE_CONNECT_CA = "SELFCARE_CONNECT_CA";
        public static final String SELFCARE_CONNECT_ESB = "SELFCARE_CONNECT_ESB";
        public static final String SELFCARE_CONNECT_SINVOICE = "SELFCARE_CONNECT_SINVOICE";
        public static final String SELFCARE_CONNECT_VCONTRACT = "SELFCARE_CONNECT_VCONTRACT";
        public static final String SELFCARE_CONNECT_VBHXH = "SELFCARE_CONNECT_VBHXH";
        public static final String COMBO_SME_HUB = "COMBO_SME_HUB";

        public static final List<String> ALLOW_ORDER_TYPES = List.of(
                PAID_ORDER,
                SELFCARE_CONNECT_CA,
                SELFCARE_CONNECT_ESB,
                SELFCARE_CONNECT_SINVOICE,
                SELFCARE_CONNECT_VCONTRACT,
                SELFCARE_CONNECT_VBHXH,
                COMBO_SME_HUB);
    }

    public static class PaymentStatus {
        public static final Integer SUCCESS = 1;
        public static final Integer STATUS_ACTIVE = 1;
        public static final Integer STATUS_INACTIVE = 0;
    }

    public static class RoleName {
        public static final String SYSTEM = "system";
    }

    public static class OptionSet {
        public static final String MERCHANT_CODE = "MERCHANT_CODE";
        public static final String MERCHANT_CODE_PAYGATE = "MERCHANT_CODE_PAYGATE";
    }
}
