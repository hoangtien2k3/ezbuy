package com.ezbuy.settingmodel.constants;

import java.util.List;

public class Constants {

    public static final Long STATUS_ACTIVE = 1L;

    public interface UPLOAD_TYPE {
        Integer FILE = 1;
        Integer FOLDER = 2;
    }

    public interface UPLOAD_STATUS {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
    }

    public interface MINIO_BUCKET {
        String SME_PUBLIC = "sme-public";
    }

    public static class INDEX {
        public static final String SERVICES = "services";
        public static final String SERVICES_TEST = "servicestest";
        public static final String NEWS = "news";


        public static final List<String> ALLOW_INDEX = List.of(
                SERVICES, NEWS
        );

    }

    public static class DEFAULT_VALUE {

        public static final Integer FROM = 0;
        public static final Integer SIZE = 5;
    }
    public static final String ID = "/{id}";
    public static final String SEARCH_NEW = "/search";
    public static final String LIST_NEW = "/list";
    public static final String ALL_ACTIVE_NEW = "/all-active";
}
