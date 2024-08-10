package com.ezbuy.authmodel.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum ServiceType {
    CA("7", "CA"),
    SINVOICE("37", "SINVOICE"),
    VCONTRACT("101", "VCONTRACT"),
    ESB("208", "EASYBOOKS");

    private final String name;
    private final String value;

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
        return null;
    }
}
