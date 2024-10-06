package com.ezbuy.paymentmodel.constants;

public class ClientUris {
    public interface Product {
        String GET_PRODUCT_PRICES = "/v1/prices";
    }

    public interface Order {
        String UPDATE_PAYMENT_RESULT = "/v1/order/payment-result";
        String PLACE_BCCS_ORDER = "/v1/order/place-bccs-order";
    }
}
