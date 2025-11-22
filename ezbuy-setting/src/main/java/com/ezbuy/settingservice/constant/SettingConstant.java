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
