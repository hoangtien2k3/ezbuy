package com.ezbuy.auth.constants;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ServiceType {
    CA("7", "CA"),
    SINVOICE("37", "SINVOICE"),
    VCONTRACT("101", "VCONTRACT"),
    ESB("208", "EASYBOOKS");

    private String name;
    private String value;

    ServiceType(String name, String value) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static boolean statusOf(Integer value) {
        return Arrays.stream(values()).anyMatch(v -> v.value.equals(value));
    }

    public String value() {
        return value;
    }

    public static String getValueByName(String name) {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceType.name.equals(name)) {
                return serviceType.value;
            }
        }
        return null; // hoặc giá trị mặc định nếu không tìm thấy
    }
}
