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
package com.ezbuy.settingservice.constant;

import java.util.List;

public class SettingConstant {
    public static final String MESSAGE_SUCCESS = "success";
    public static final String FILE_SEPARATOR = "/";
    public static final String SPLITER = ",";

    public interface MINIO_FOLDER {
        String ICON_FOLDER = "/icon";
        String BACKGROUND_FOLDER = "/background";
    }

    public interface PAGE_STATUS {
        Integer LOCK = 0;
        Integer UNLOCK = 1;
    }

    public interface MINIO_BUCKET {
        String EZBUY_BUCKET = "ezbuy-bucket";
    }

    public static final List<String> TEAMPLATE_TYPE = List.of(
            "HEADER_INFO",
            "TAB_VIEW",
            "RICH_TEXT",
            "SLIDE",
            "PRICE_VIEW",
            "RATING_VIEW",
            "RELATED_SERVICE_INFO",
            "HELP_DESK");

    public static final class OptionSetCode {
        public static final String POLICY_CODE = "POLICY_CODE";
    }
}
