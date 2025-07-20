package com.ezbuy.authservice.constants;

import com.reactify.util.Translator;
import java.util.Map;

public interface ActionLogType {
    String LOGIN = "LOGIN";
    String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    String FORGOT_PASSWORD = "FORGOT_PASSWORD";

    Map<String, String> MAP = Map.ofEntries(
            Map.entry(LOGIN, Translator.toLocale("action.login")),
            Map.entry(CHANGE_PASSWORD, Translator.toLocale("action.change.password")),
            Map.entry(FORGOT_PASSWORD, Translator.toLocale("action.forgot.password")));
}
