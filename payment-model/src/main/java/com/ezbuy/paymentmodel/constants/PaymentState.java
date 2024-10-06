package com.ezbuy.paymentmodel.constants;

public enum PaymentState {

    NEW(0),
    DONE(1);

    int value;

    PaymentState(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}
