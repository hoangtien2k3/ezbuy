package com.ezbuy.searchmodel.constants;

import java.util.List;

public class Constants {
    public static class INDEX {
        public static final String SERVICES = "services";
        public static final String NEWS = "news";
        public static final String SERVICES_TEST = "servicestest";

        public static final List<String> ALLOW_INDEX = List.of(
                SERVICES, SERVICES_TEST, NEWS
        );
    }

    public static class DEFAULT_VALUE {
        public static final Integer FROM = 0;
        public static final Integer SIZE = 5;
    }
}
