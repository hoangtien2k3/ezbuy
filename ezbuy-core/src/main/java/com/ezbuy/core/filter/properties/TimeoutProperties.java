/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
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
package com.ezbuy.core.filter.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * The TimeoutProperties class is a record that encapsulates configuration
 * settings for timeouts related to HTTP requests. It specifies the read timeout
 * and the connection timeout values in milliseconds.
 * </p>
 *
 * <p>
 * The default constructor initializes the read timeout to 180,000 milliseconds
 * (3 minutes) and the connection timeout to 500 milliseconds (0.5 seconds).
 * </p>
 *
 * @author hoangtien2k3
 */
@Getter
@Setter
public class TimeoutProperties {

    /**
     * the read timeout value in milliseconds
     */
    private int read;

    /**
     * the connection timeout value in milliseconds
     */
    private int connection;

    public TimeoutProperties() {
        this(180000, 500); // Default read timeout: 3 minutes, connection timeout: 0.5 seconds
    }

    public TimeoutProperties(int read, int connection) {
        this.read = read;
        this.connection = connection;
    }
}
