package com.ezbuy.customer.app.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Lock {
    LOCKED((short) 1), // trang thai khoa
    UNLOCKED((short) 0); // trang thai mo khoa

    private final short value;
}
