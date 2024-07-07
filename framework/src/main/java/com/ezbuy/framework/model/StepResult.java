package com.ezbuy.framework.model;

import lombok.Getter;

@Getter
public class StepResult {

    private boolean success;
    private String message;

    private StepResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static StepResult success() {
        return new StepResult(true, null);
    }

    public static StepResult failure(String message) {
        return new StepResult(false, message);
    }

}
