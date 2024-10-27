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
package io.hoangtien2k3.reactify.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * <p>
 * GatewayContext class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Getter
@Setter
@ToString
public class GatewayContext {
    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";
    protected Boolean readRequestData = true;
    protected Boolean readResponseData = true;
    protected String requestBody;
    protected Object responseBody;
    protected HttpHeaders requestHeaders;
    protected MultiValueMap<String, String> formData;
    protected MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);
    protected Long startTime;
}
