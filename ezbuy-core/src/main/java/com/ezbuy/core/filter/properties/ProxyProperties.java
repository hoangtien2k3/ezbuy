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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * The ProxyProperties class is a record that holds configuration properties for
 * an HTTP/HTTPS proxy. It specifies whether the proxy is enabled and the host
 * and port settings for both HTTP and HTTPS connections.
 * </p>
 *
 * <p>
 * The default constructor initializes the proxy as disabled (not enabled) and
 * sets all host and port values to null.
 * </p>
 *
 * @author hoangtien2k3
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProxyProperties {

    /**
     * a boolean indicating whether the proxy is enabled
     */
    private boolean enable;

    /**
     * the host for HTTP connections
     */
    private String httpHost;

    /**
     * the port for HTTP connections
     */
    private Integer httpPort;

    /**
     * the host for HTTPS connections
     */
    private String httpsHost;

    /**
     * the port for HTTPS connections
     */
    private Integer httpsPort;
}
