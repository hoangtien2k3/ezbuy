package com.ezbuy.ordermodel.constants;

public class UrlPaths {

    public interface Order {
        String PRE_FIX = "/v1/order";
        String PRE_ORDER = "/pre-order";
        String SYNC_ORDER = "/sync-order";
        String DETAIL = "/detail/{orderId}";
        String PRICING_PRODUCT_INTERNAL = "/pricing-product-internal";
        String PRICING_PRODUCT = "/pricing-product";
        String PAID_ORDER = "/paid-order";

        String PAYMENT_RESULT = "/payment-result";
        String VALIDATE_CONNECT_FIRST = "/validate-connect-first";
        String CA_GET_GROUP_INFO = "/ca/get-group-info";
        String CA_GET_GROUP_MEMBER_INFO = "/ca/get-group-member-info";
        String CA_UPDATE_GROUP_MEMBER = "/ca/update-group-member";
        String CA_REMOVE_GROUP_MEMBER = "/ca/remove-group-member";
        String CA_ADD_GROUP_MEMBER = "/ca/add-group-member";
        String SERVICE_GET_DATA_PROFILE = "ca/service-get-data-profile";
        String CA_CONNECT_SELFCARE = "/ca/connect-selfcare";
        String CA_GET_TOTAL_SIGN = "/ca/get-total-sign";//lay tong so luong ky
        String CA_GET_NUMBER_SIGN = "/ca/get-number-sign";//lay so luong da ky
        String ORDER_HISTORY = "/history";
        String GET_ADVICE = "/get-advice";
        String VALIDATE_DATA_ORDER = "/validate-data-order";
        String UPDATE_STATUS_PRE_ORDER = "/update-status-pre-order";
        String CONNECT_ORDER_SELF_CARE = "/connect-order-self-care";
        String SERVICE_GET_DOC_DATA_POLICY = "ca/doc-data-policy";
        String SEARCH_ORDER_HISTORY = "/order-history";
        String SEARCH_ORDER_V2 = "/search-order-v2";
        String FILE_CONTRACT_TO_VIEW = "/file-contract-to-view"; //api view file hsdt v2
        String CREATE_ORDER_CTS = "/cts"; // api tao don cts luong xac minh
        String CA_GROUP_MEMBER_TEMPLATE = "/ca/group-member/template";
        String CA_GROUP_MEMBER_VALIDATE_IMPORT = "/ca/group-member/validate-import";
        String CA_GROUP_MEMBER_IMPORT = "/ca/group-member/import";
        String CA_DOWNLOAD_GROUP_MEMBER_IMPORT_RESULT = "/ca/group-member/download-result";
        String ORDER_REPORT = "/order-report";

        String GET_LIST_SERVICE_RECORD_PROFILE = "/get-list-service-record";
        String GET_LIST_PRODUCT_OFFERING_RECORD_PROFILE = "/get-list-product-offering-record";
    }

    public interface OrderFieldConfig {
        String PRE_FIX = "/v1/order-field-config";
    }

    public interface OrderItem {
        String PRE_FIX = "/v1/order-item";
        String REVIEW = "/review";
    }

    public interface InvoiceInfo {
        String PRE_FIX = "/v1/invoice-info";
        String ID = "/{id}";
    }

    public interface InvoiceInfoHisTory {
        String PRE_FIX = "/v1/invoice-info-history";

        String GET = "/get-history";
    }

}
