package com.ezbuy.auth.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum State {
    ACTIVE(0, "ACTIVE"),
    INACTIVE(1, "INACTIVE");

    private final Integer value;
    private final String name;

    State(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static boolean statusOf(Integer value) {
        return Arrays.stream(values()).anyMatch(v -> v.value.equals(value));
    }
}
