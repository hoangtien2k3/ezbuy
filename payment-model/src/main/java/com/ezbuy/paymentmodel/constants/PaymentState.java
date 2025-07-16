package com.ezbuy.paymentmodel.constants;

import lombok.Getter;

@Getter
public enum PaymentState {
    NEW(0),
    DONE(1);

    final int value;

    PaymentState(int value) {
        this.value = value;
    }
}
