package com.ezbuy.customer.app.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {
    @JsonProperty("admin") ADMIN("admin"), // quyen admin
    @JsonProperty("user") USER("user"),    // quyen user
    @JsonProperty("shop") SHOP("shop");    // quyen shop

    private final String value;
}
