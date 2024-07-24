/*
 * Copyright 2024 - Hoàng Anh Tiến
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.framework.client.properties;

import java.util.List;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.ezbuy.framework.filter.properties.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebClientProperties {
    private String name;
    private String address;
    private String username;
    private String password;
    private String authorization;
    private PoolProperties pool = new PoolProperties();
    private TimeoutProperties timeout = new TimeoutProperties();
    private RetryProperties retry = new RetryProperties();
    private ClientLogProperties log = new ClientLogProperties();
    private MonitoringProperties monitoring = new MonitoringProperties();
    private ProxyProperties proxy = new ProxyProperties();
    private List<ExchangeFilterFunction> customFilters;
    private boolean internalOauth = false;
}
