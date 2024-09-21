package com.ezbuy.settingmodel.dto.enums;

public enum ContentNewsStatus {
    LOCK(0),
    UNLOCK(1);

    public final Integer value;

    ContentNewsStatus(int value) {
        this.value = value;
    }
}
