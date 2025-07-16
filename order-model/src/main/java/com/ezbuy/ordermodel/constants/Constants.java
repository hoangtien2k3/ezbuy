package com.ezbuy.ordermodel.constants;

import java.util.List;
import java.util.Map;

public class Constants {

    public static final String EMPTY = "";

    public static final String ERROR_CODE_SUCCESS = "0";
    public static final String SUCCESS_TRUE = "true";
    public static final Long VALUE_0_LONG = 0L;
    public static final String SPACE = " ";

    public static final Map<String, Integer> ORDER_STATE_MAP = Map.ofEntries(
            Map.entry(OrderState.IN_PROGRESS.name(), OrderState.IN_PROGRESS.getValue()),
            Map.entry(OrderState.COMPLETED.name(), OrderState.COMPLETED.getValue()),
            Map.entry(OrderState.REJECTED.name(), OrderState.REJECTED.getValue()));

    public static class OrderType {
        public static final String PRE_ORDER = "PRE_ORDER";
        public static final String PAID_ORDER = "PAID_ORDER";
        public static final String ORDER = "ORDER";

        public static final List<String> ALLOW_ORDER_TYPES = List.of(PRE_ORDER);

        public static final String CONNECT_SME = "CONNECT_SME";
        public static final String ADD_MEMBER_GROUP_CA = "ADD_MEMBER_GROUP_CA";
        public static final String DELETE_MEMBER_GROUP_CA = "DELETE_MEMBER_GROUP_CA";
        public static final String CHANGE_MEMBER_GROUP_CA = "CHANGE_MEMBER_GROUP_CA";
        public static final String CONNECT_SME_PORTAL = "CONNECT_SME_PORTAL";
        public static final String COMBO_SME_HUB = "COMBO_SME_HUB";
    }

    public static class OrderConfigType {
        public static final int NO_DISPLAY = 0;
        public static final int DISPLAY = 1;
        public static final int REQUIRED = 2;
    }

    public static class CustomerInfoProperty {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String AREA_CODE = "areaCode";
        public static final String DISTRICT = "district";
        public static final String PRECINCT = "precinct";
        public static final String PROVINCE = "province";
        public static final String ADDRESS = "address";

        public static final String DISTRICT_NAME = "districtName";
        public static final String PRECINCT_NAME = "precinctName";
        public static final String PROVINCE_NAME = "provinceName";

        public static final String ID_NO = "idNo";
        public static final String ORGANIZATION_ID = "organizationId";
        public static final String TOTAL_FEE = "totalFee";
        public static final String SYSTEM_TYPE = "systemType";
        public static final String COMPANY_NAME = "companyName";

        public static final String FROM_STAFF = "fromStaff";
        public static final String STAFF_AM_SUPPORT = "amStaff";
    }

    public static class PreOrderInfo {
        public static final String DURATION = "DURATION";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String TOTAL_PRICE = "TOTAL_PRICE";
        public static final String FROM_STAFF = "FROM_STAFF";
        public static final String STAFF_AM_SUPPORT = "STAFF_AM_SUPPORT";
    }

    public static class Actor {
        public static final String SYSTEM = "SYSTEM";
    }

    public static class DataType {
        public static final String STRING = "String";
        public static final String LONG = "Long";
    }

    public static class Order {
        public static final String CURRENCY_VND = "VND";
    }

    public static class ProductCharacteristic {
        public static final String PREPAID_DURATION = "PREPAID_DURATION";
        public static final String PREPAID_DURATION_DAY = "PREPAID_DURATION_DAY";
        public static final String ACCOUNT_ISDN = "ACCOUNT_ISDN";
    }

    public static class Currency {
        public static final String VND = "VND";
    }

    public static class SystemType {
        public static final String SME_HUB = "SME_HUB";
    }

    public static class TransactionPlace {
        public static final String HOME = "HOME";
        public static final String SHOP = "SHOP";
    }

    public static class OrderItemActionType {
        public static final String ADD = "add";
        public static final String MODIFY = "modify";
        public static final String DELETE = "delete";
        public static final String NO_CHANGE = "noChange";
    }

    public static class BCCSOrderType {
        public static final String AS_CA_EXTEND_PACKAGE = "AS_CA_EXTEND_PACKAGE";
        public static final String ADD_MEMBER_GROUP_CA = "ADD_MEMBER_GROUP_CA";
        public static final String DELETE_MEMBER_GROUP_CA = "DELETE_MEMBER_GROUP_CA";
        // trong nhom CA
        public static final String CHANGE_MEMBER_GROUP_CA = "CHANGE_MEMBER_GROUP_CA";
        // thanh vien nhom CA
    }

    public static class BCCSCmSystem {
        public static final String SUCCESS_CODE = "0";
    }

    public static class CharacteristicKey {
        public static final String NUMBER_OF_SIGNATURES = "NUMBER_OF_SIGNATURES";
        public static final String MAX_SUB = "MAX_SUB";
        public static final String ADD_MONTHS = "ADD_MONTHS";
        public static final String GROUP_NAME = "GROUP_NAME";
        public static final String GROUP_ID = "GROUP_ID";
        public static final String MAIN_ISDN = "MAIN_ISDN";
        public static final String SUB_ISDN = "SUB_ISDN";
        public static final String CONTACT_NAME = "CONTACT_NAME";
        public static final String CUS_INFO_POSITION = "CUS_INFO_POSITION";
        public static final String VERSION_USAGE = "VERSION_USAGE";
        public static final String GROUP_ISDN_SME = "GROUP_ISDN_SME";
        public static final String GROUP_ID_CREATE_OCS = "GROUP_ID_CREATE_OCS";
        public static final String GROUP_TYPE_CA_OCS = "GROUP_TYPE_CA_OCS";
        public static final String GROUP_PRODUCT_OFERRING_CODE = "GROUP_PRODUCT_OFERRING_CODE";
        public static final String GROUP_MEMBER_ID = "GROUP_MEMBER_ID";
        public static final String FROM_STAFF = "FROM_STAFF";
        public static final String EMAIL_ACCOUNT = "EMAIL_ACCOUNT";
        public static final String TYPE_CIRCULARS = "TYPE_CIRCULARS";
        public static final String STAFF_AM_SUPPORT = "STAFF_AM_SUPPORT";
        public static final String ACCOUNT_PWD = "ACCOUNT_PWD"; // mat khau
        public static final String ACCOUNT_HUB = "ACCOUNT_HUB"; // tai khoan
        public static final String CONTACT_EMAIL = "CONTACT_EMAIL"; // email nguoi lien he
        public static final String CONTACT_PHONE = "CONTACT_PHONE"; // so dien thoai nguoi lien he
        public static final String CLUE_ISDN = "CLUE_ISDN"; // so dien thoai dau moi tiep nhan
        public static final String EMAIL = "EMAIL"; // Email mac dinh
        public static final String DVM_CUST_EMAIL = "DVM_CUST_EMAIL"; // Email khach hang customer
        public static final String DVM_CUST_TEL = "DVM_CUST_TEL"; // sdt khach hang customer
        public static final String LICENSE_KEY = "LICENSE_KEY"; // license key
    }

    public static class Common {
        public static final String ACTIVE_CODE = "1";
        public static final Integer STATUE_ACTIVE = 1;
        public static final Long CA_TELECOM_SERVICE_ID = 7L;
        public static final String VALUE_0_STRING = "0";
        public static final String VALUE_1_STRING = "1";
    }

    public static class OrderProductOrderItem {
        public static final String ACTION_ADD = "add";
    }

    public static class CustIndentityType {
        public static final String BUS = "BUS";
        public static final String TIN = "TIN";
    }

    public static class ORDER_STATE {
        public static final Integer NEW = 0;
        public static final Integer IN_PROGRESS = 1;
        public static final Integer COMPLETED = 3;
        public static final Integer REJECTED = 4;
    }

    public static class OrderBccsData {
        public static final Integer STATUS_ACTIVE = 1;
    }

    public static class RequestBanking {
        public static final Integer STATE_DONE = 1;
        public static final Integer STATE_FAIL = -1;
    }

    public static final String SYSTEM = "system";

    public static class TelecomServiceId {
        public static final Long CA = 7L;
        public static final Long EASY_BOOK = 208L;
        public static final Long SINVOICE = 37L;
        public static final Long VCONTRACT = 101L;
        public static final Long VBHXH = 151L;
    }

    public static final class OptionSetCode {
        public static final String DATA_POLICY = "DATA_POLICY";
    }

    public static class TelecomServiceAlias {
        public static final String CA = "CA";
        public static final String EASY_BOOK = "EASYBOOKS";
        public static final String SINVOICE = "SINVOICE";
        public static final String VCONTRACT = "VCONTRACT";
        public static final String OCA = "OCA"; // CA truyen thong
    }

    public static final class ROW_TEMPLATE_NAME {
        public static final String SUB_ISDN = "order.import.column.sub.isdn";
        public static final String NUMBER_OF_SIGN = "order.import.column.number.of.sign";
        public static final String OBLIGATORY = " (*)";
        public static final String SUB_ISDN_HINT = "order.import.column.sub.isdn.hint";
    }

    public static final String NUM_REGEX = "[0-9]+";
    public static final String SUB_ISDN_REGEX = "^CA_[0-9]+$";

    public static class OrderExt {
        public static final String ORDER_TRUST_IDENTITY = "ORDER_TRUST_IDENTITY";
    }

    public static class TRUST_STATUS {
        public static final Integer WAIT_APPROVE = 3;
        public static final Integer WAIT_CREATE_CTS = 2;
        public static final Integer NOT_SIGN = 5;
    }

    public static final String GROUP_TYPE_ORGANIZATION = "1";
}
