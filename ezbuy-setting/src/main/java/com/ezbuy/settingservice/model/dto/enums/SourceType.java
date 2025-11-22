package com.ezbuy.settingservice.model.dto.enums;

public enum SourceType {
    EXTERNAL(0),
    INTERNAL(1);

    public final int value;

    SourceType(int val) {
        this.value = val;
    }
}
