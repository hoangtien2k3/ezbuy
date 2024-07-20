package com.ezbuy.customer.app.common.enums.Gender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    @JsonProperty("male") MALE,
    @JsonProperty("female") FEMALE,
    @JsonProperty("other") OTHER;
}
