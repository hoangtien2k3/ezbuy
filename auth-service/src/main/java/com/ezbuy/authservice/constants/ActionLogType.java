/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.constants;

import io.hoangtien2k3.commons.utils.Translator;
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
