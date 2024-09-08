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
package com.ezbuy.settingmodel.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

// request to create or update MarketInfo
@Data
public class MarketInfoRequest {
    private String serviceId;
    private String serviceAlias; // alias of service PYCXXX/LuongToanTrinhScontract

    @Size(max = 500, message = "market.info.title.length.500.invalid")
    private String title;

    @Size(max = 500, message = "market.info.navigator.url.length.500.invalid")
    private String navigatorUrl;

    private Integer marketOrder;
    private String marketImageUrl;
    private String relatedTelecomServiceId;
    private Integer status;
    private String image;
    private String imageName;
}
