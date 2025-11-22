package com.ezbuy.settingservice.constants;

public class UrlPaths {
    public interface Area {
        String PREFIX = "/v1/area";
    }

    public interface Page {
        String PREFIX = "/v1/page";
        String PAGE_DETAIL = Constants.ID;
        String SEARCH_PAGES = Constants.SEARCH_NEW;
        String POLICY_PAGES = "/policy";
        String CHANGE_STATUS = "/change-status";
    }

    public interface Telecom {
        String PREFIX = "/v1/telecom-services";
        String ORIGIN_FILTER = "/filter";
        String ORIGIN_FILTER_V2 = "/filter/alias"; // Ham lay danh sach cau hinh dich vu theo danh sach alias
        String LIST = Constants.SEARCH_NEW;
        String NON_INIT_FILTER = "/non-filter";
        String LOCK_UNLOCK = "/lock-unlock";
        String INIT_FILTER = "/init-filter";
        String INIT_FILTER_V2 = "/init-filter/alias"; // Ham khoi tao filter cho dich vu theo serviceAlias
        String LIST_REQUEST = Constants.LIST_NEW;
        String SERVICE_TYPES = "/service-types";
        String GET_ALL_ACTIVE = Constants.ALL_ACTIVE_NEW;
        String GET_ADMIN_ROLE = "/admin-role";
        String GET_ADMIN_ROLE_V2 = "/admin-role/alias"; // Ham lay roleAdmin va clientId cua dich vu theo serviceAlias
        String GET_SERVICE_CONFIG = "/service-config";
        String GET_SERVICE_CONFIG_V2 = "/service-config/alias";
        String GET_ALL_TELECOM_SERVICE = "/all";
        String GET_TELECOM_BY_LIST_ORIGIN_ID = "/list-origin-id"; // ham lay danh sach telecom theo list

        String GET_AlIAS_BY_CLIENT_CODE = "/alias/client-code";
    }

    public interface ContentDisplay {
        String PREFIX = "/v1/content-display";
        String DETAILS = Constants.ID;
        String ORIGIN_DETAILS = "/details";
    }

    public interface News {
        String PREFIX = "/v1/news-content";
        String LOCK_UNLOCK = "/{id}/lock-unlock";
        String PATH_VARIABLE_ID = Constants.ID;
        String FIND_BY_NEWS_INFO_ID = "/{newsInfoId}";
    }

    public interface Upload {
        String PREFIX = "/v1/upload-images";
        String CREATE_FOLDER = "/create-folder";
        String DELETE_FOLDER = "/delete-folder";
        String RENAME_FOLDER = "/rename-folder";
        String UPDATE_IMAGES = "/update";
        String FILE_UPLOAD = "/upload";
        String SEARCH = Constants.SEARCH_NEW;
        String DELETE_FILE = "/delete-file";
        String INFO = Constants.ID;
        String FOLDER = "/folders";
    }

    public interface MarketInfo {
        String PREFIX = "v1/market-info";
        String DETAILS = Constants.ID;
        String CREATE = "/create";
        String EDIT = Constants.ID;
        String SEARCH = Constants.SEARCH_NEW;
        String LIST = Constants.LIST_NEW;
        String ALL_ACTIVE = "/all_active";
        String LIST_BY_SERVICE = "/list-by-service";
        String LIST_BY_SERVICE_V2 = "/list-by-service/alias"; // Ham lay danh sach cau hinh market_info theo danh sach
        // alias cua dich vu
    }

    public interface MarketPage {
        String PREFIX = "v1/market-page";
        String DETAILS = "/detail/{id}";
        String ALL_ACTIVE = Constants.ALL_ACTIVE_NEW;
        String LIST_BY_SERVICE = "/service";
        String LIST_BY_SERVICE_V2 = "/service/alias"; // Lay danh sach cau hinh trang theo list alias
        String GET_ALL_MP = Constants.LIST_NEW;
    }

    public interface MarketSection {
        String PREFIX = "v1/market-section";
        String GET_PAGE_V2 = "/alias"; // Lay danh sach cau hinh thanh phan trang theo code cua trang va alias
        String FIND_BY_ID = Constants.ID;
        String SEARCH_MARKET_SECTION = Constants.SEARCH_NEW;
        String DETAILS = "detail/{id}";
        String CREATE = "/create";
        String EDIT = Constants.ID;
        String DELETE = "/delete/{id}";
        String ALL_ACTIVE = Constants.ALL_ACTIVE_NEW;
        String ALL = "/all";

        String FIND_BY_CONTENT_SECTION_ID = "content-section-id/{id}";
        String ALL_ACTIVE_RT = "/all-active-rt";
    }

    public interface GlobalSearch {
        String PREFIX = "/v1/global-search";

        String SERVICES = "/services";
        String NEWS = "/news";
    }

    public interface CustType {
        String PREFIX = "/v1/cust-type";
        String GET_ALL_ACTIVE = Constants.ALL_ACTIVE_NEW;
    }

    public interface ContentSection {
        String PREFIX = "v1/content-section";
        String DETAILS = "/detail/{id}";
        String ALL_ACTIVE = Constants.ALL_ACTIVE_NEW;
        String LIST_BY_SERVICE = "/service";
        String LIST_BY_SERVICE_V2 = "/service-alias"; // Ham lay cau hinh huong dan va tai nguyen theo danh sach Alias
        // (content_section)
        String LiST_BY_SECTION = "/section";
        String GET_ALL_SA = Constants.LIST_NEW;
        String SEARCH = Constants.SEARCH_NEW;
        String DELETE = "/delete/{id}";
        String ALIAS = "/alias";
    }

    public interface GroupNews {
        String PREFIX = "/v1/group-news";
        String UPDATE = Constants.ID;
        String ALL = "/all";
    }

    public interface NewsInfo {
        String PREFIX = "/v1/news-info";
        String UPDATE = Constants.ID;
        String ALL = "/all";
        String DETAIL = "/detail/{id}";
        String RELATE_NEWS = "/relate-news/group-news/{id}";
    }

    public interface Setting {
        String PREFIX = "/v1/setting";
        String FIND_BY_CODE = "/{code}";
        String DETAILS = "/detail/{id}";
        String GET_ALL = Constants.LIST_NEW;
        String ALL_ACTIVE = Constants.ALL_ACTIVE_NEW;
        String EDIT = "/edit/{id}";

        String DELETE = "/delete/{id}";
        String SEARCH = Constants.SEARCH_NEW;
        String CREATE = "/create";
        String GET = "/get";
    }

    public interface OptionSet {
        String PREFIX = "/v1/option-set";
        String FIND_BY_OPTION_SET_CODE = Constants.LIST_NEW; // lay danh sach option_set_value theo optionSetCode
        String FIND_BY_CODE = ""; // lay ra option_set_value theo optionSetCode va optionSetValueCode
        String UPDATE = Constants.ID; // cap nhat option_set theo id
        String ALL = "/all"; // lay ra tat ca option_set
    }

    public interface OptionSetValue {
        String PREFIX = "v1/option-set-value";
        String GET_OPTION_SET_VALUE_BY_OPTION_SET_CODE = "/option-set";
        String GET_OPTION_SET_VALUE_BY_CODE = "/code";
        String GET_ACROYNM_BY_LIST_ALISAS = "/acronym";

        String UPDATE = Constants.ID; // cap nhat option_set_value theo id
    }

    public interface MarketPageSection {
        String PREFIX = "v1/market-page-section";
        String DETAIL = "/detail";
        String CREATE = "/create";
        String EDIT = "/edit/{id}";
    }
}
