package com.ezbuy.paymentmodel.constants;

import lombok.Getter;

@Getter
public enum OrderState {
    NEW(0),
    IN_PROGRESS(1),
    COMPLETED(3),
    REJECTED(4);

    final int value;

    OrderState(int value) {
        this.value = value;
    }
}
