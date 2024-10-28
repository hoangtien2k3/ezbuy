package com.ezbuy.paymentmodel.constants;

public enum OrderState {
    NEW(0),
    IN_PROGRESS(1),
    COMPLETED(3),
    REJECTED(4);

    int value;

    OrderState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
