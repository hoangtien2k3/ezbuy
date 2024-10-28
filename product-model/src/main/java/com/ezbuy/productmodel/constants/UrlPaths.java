package com.ezbuy.productmodel.constants;

public class UrlPaths {

    public static final String DEFAULT_V1_PREFIX = "/v1";
    public static final String FILTER_TEMPLATE = "/filter-template";
    public static final String GET_DETAIL = "/filter-template/get";
    public static final String EDIT_FILTER = "/filter-template/edit";
    public static final String CREATE_FILTER = "/filter-template/create";
    public static final String DELETE_FILTER = "/filter-template/delete";

    public static final String PREVIEW_FILTER = "/filter-template/preview";
    public static final String SYNC_FILTER_TEMPLATE = "/sync-filter-template";
    public static final String FILTER_PRODUCT_TEMPLATE = "/filter-product-template";
    public static final String STATISTIC_SUBSCRIBER = "/statistic-subscriber";
    public static final String GET_PRODUCT_SPECIFICATION = "/product-specifications";
    public static final String GET_LIST_SUBSCRIBER = "/get-list-subscriber";
    public static final String SYNC_SUBSCRIBER = "/sync-subscriber";
    public static final String GET_LIST_SUBSCRIBER_ACTIVE = "/get-list-subscriber-active";
    public static final String GET_LIST_SUBSCRIBER_ACTIVE_BY_ALIAS = "/get-list-subscriber-active-alias";
    public static final String GET_LIST_SUBSCRIBER_ACTIVE_BY_ALIAS_AND_LIST_IDNO = "/get-subscribers-active";
    public static final String SYNC_SUBSCRIBER_ORDER = "/sync-subscriber-order";
    public static final String GET_GROUP_AND_SERVICE_ACTIVE = "/service-group/group-and-service-active";
    public static final String GET_SUBSCRIBER_BY_ID_NO = "/subscriber-by-id-no/{idNo}";
    public static final String GET_TELECOM_SERVICE = "/telecom-service";
    public static final String GET_TELECOM_SERVICE_CONNECT = "/telecom-service-connect";
    public static final String GET_TELECOM_SERVICE_RELATED = "/telecom-service-related";
    public static final String GET_REGISTERED_TELECOM_SERVICE_BY_ID_NO_LIST = "/subscriber/registered-services";
    public static final String GET_REGISTERED_TELECOM_SERVICE_ALIAS_BY_ID_NO_LIST =
            "/subscriber/registered-services-alias";
    public static final String GET_LIST_PRODUCT_OFFERING_COMBO = "/get-list-product-offering-combo";
    public static final String GET_LIST_PRODUCT_OFFERING_COMBO_V2 = "/get-list-product-offering-combo-v2";
    public static final String GET_LIST_TEMPLATE_COMBO = "/get-list-template-combo";

    public static final String TELECOM_SERVICE_INFO = "/telecom-service-info";
    public static final String GET_SUBSCRIBER_SME_INFO = "/get-subscriber-sme-info/{telecomServiceId}";
    public static final String GET_PRODUCT_OFFERING_SPECIFICATION_FOR_MBCCS =
            "/get-product-offering-specification-for-mbccs";
    public static final String GET_PRODUCT_FOR_MEGA_MENU = "/mega-menu";
    public static final String GET_PRODUCT_TEMPLATE_DETAIL = "/product-template-detail";
    public static final String EXPORT_REPORT = "/export-report";
    public static final String GET_DATA_REPORT = "/data-report";

    public interface ServiceGroup {
        String PREFIX = "/v1/service-group";
        String UPDATE = "/{id}";
        String ALL = "/all";
    }

    public interface Service {
        String PREFIX = "/v1/telecom-service";
        String ALL = "/all";
        String GET_BY_ALIAS = "/get-by-alias";
        String GET_BY_LST_ALIAS = "/get-by-lst-alias";
    }

    public interface ACTIVATE_TELECOM {
        String PREFIX = DEFAULT_V1_PREFIX + "/active-telecom";
    }

    public interface Product {
        String SEARCH_PRODUCT = "/product";
        String CREATE_PRODUCT = "/product/create"; // api them hang hoa
        String UPDATE_PRODUCT = "/product/update"; // api sua hang hoa
        String DELETE_PRODUCT = "/product/delete"; // api xoa hang hoa
        String DETAIL_PRODUCT = "/product/detail"; // api lay thong tin hang hoa
        String LOCK_PRODUCT = "/product/lock"; // api khoa hang hoa
        String PRODUCT_IMPORT_TEMPLATE = "/product/template"; // api lay file import hang hoa
        String PRODUCT_DOWNLOAD_IMPORT_RESULT = "/product/download-result"; // api download ket qua import hang hoa
        String PRODUCT_VALIDATE_IMPORT = "/product/validate-import"; // api validate data import hang hoa
        String IMPORT_PRODUCT = "/product/import"; // api import hang hoa
        String LOCK_PRODUCT_MULTI = "/product/lock-multi"; // api khoa nhieu hang hoa
        String GET_PRODUCT_INFO = "/product/get-product-info"; // api truy van dong bo thong tin hang hoa
        String VALIDATE_SUB_INS = "/validate-sub-ins"; // validate thue bao vBHXH
        String GET_LIST_AREA_INS = "/get-list-area-ins"; // lay danh sach tinh/co quan quan ly
        String CREATE_SUMMARY_REPORT = "/create-summary-report";
    }
}
