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
package com.ezbuy.authmodel.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum State {
    ACTIVE(0, "ACTIVE"),
    INACTIVE(1, "INACTIVE");

    private final Integer value;

    State(Integer value, String name) {
        this.value = value;
    }

    @JsonCreator
    public static boolean statusOf(Integer value) {
        return Arrays.stream(values()).anyMatch(v -> v.value.equals(value));
    }

    public Integer value() {
        return value;
    }
}
