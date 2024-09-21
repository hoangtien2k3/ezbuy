package com.ezbuy.customer.app.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Enable {
    ENABLED((short) 1), // trang thai hoat dong
    DISABLED((short) 0); // trang thai khong hoat dong

    private final short value;
}
