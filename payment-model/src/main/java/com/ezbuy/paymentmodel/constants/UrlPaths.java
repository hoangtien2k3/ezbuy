package com.ezbuy.paymentmodel.constants;

public class UrlPaths {
    public interface Price {
        String PREFIX = "v1/price";
        String LOGIN = "/products";
    }

    public interface Payment {
        String PREFIX = "v1/payment";
        String CREATE_LINK_CHECKOUT = "/create-link-checkout";
        String PAYMENT_RESULT = "/payment-result";
        String ORDER_STATE = "/order-state";
        String SYNC_PAYMENT = "/sync-payment";
    }
}
