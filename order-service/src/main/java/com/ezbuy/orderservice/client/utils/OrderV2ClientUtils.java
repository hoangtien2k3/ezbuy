package com.ezbuy.orderservice.client.utils;

import com.ezbuy.ordermodel.constants.Constants;
import com.reactify.util.DataUtil;
import java.text.MessageFormat;
import java.util.List;

public class OrderV2ClientUtils {
    private static final String ORDER_REQUEST_TEMPLATE =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.order.viettel.com/\">\n"
                    + "   <soapenv:Header>\n"
                    + "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                    + "         <wsse:UsernameToken>\n" + "            <wsse:Username>{0}</wsse:Username>\n"
                    + "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n"
                    + "         </wsse:UsernameToken>\n" + "      </wsse:Security>\n" + "   </soapenv:Header>\n"
                    + "   <soapenv:Body> " + "  {2} " + "  </soapenv:Body>\n" + " </soapenv:Envelope> ";

    private static final String CREATE_ORDER_BODY_TEMPLATE = "<ser:createOrder>\n"
            + "\t  <orderType>{0}</orderType>         \n" + "\t  <data>{1}</data>\n" + "      </ser:createOrder>";

    public static String getCreateOrderPayload(String type, String dataJson, String username, String password) {
        String body = MessageFormat.format(CREATE_ORDER_BODY_TEMPLATE, type, dataJson);
        return getOrderRequest(username, password, body);
    }

    private static String getOrderRequest(String username, String password, String body) {
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE, username, password, body);
    }

    private static final String GET_ORDER_HISTORY_HUB_BODY_TEMPLATE = "<ser:getOrderHistoryHub>\n"
            + "         <portalAccountId>{0}</portalAccountId>\n" + "         {1}" + "         {2}"
            + "         <limit>{3}</limit>\n" + "         {4}" + "         <pageNo>{5}</pageNo>\n"
            + "         <taxCode>{6}</taxCode>\n" + "</ser:getOrderHistoryHub>";

    private static final String SYSTEM_TYPE_TAG = "<systemType>{0}</systemType>\n";
    private static final String ORDER_TYPE_TAG = "<orderType>{0}</orderType>\n";
    private static final String STATE_TAG = "<state>{0}</state>\n";

    public static String getOrderHistoryHubPayload(
            String taxCode,
            String individualId,
            List<String> systemTypeList,
            List<String> orderTypeList,
            Integer limit,
            Integer pageIndex,
            String state,
            String username,
            String password) {
        StringBuilder systemTypeParam = new StringBuilder("");
        for (String systemType : systemTypeList) {
            systemTypeParam.append(MessageFormat.format(SYSTEM_TYPE_TAG, systemType));
        }
        StringBuilder orderTypeParam = new StringBuilder("");
        for (String orderType : orderTypeList) {
            orderTypeParam.append(MessageFormat.format(ORDER_TYPE_TAG, orderType));
        }
        String stateParam;
        if (DataUtil.isNullOrEmpty(state)) {
            stateParam = "";
        } else {
            stateParam = MessageFormat.format(STATE_TAG, Constants.ORDER_STATE_MAP.get(DataUtil.safeTrim(state)));
        }
        if (DataUtil.isNullOrEmpty(taxCode)) {
            taxCode = "";
        }
        String body = MessageFormat.format(
                GET_ORDER_HISTORY_HUB_BODY_TEMPLATE,
                individualId,
                systemTypeParam.toString(),
                orderTypeParam.toString(),
                limit,
                stateParam,
                pageIndex,
                taxCode);
        return getOrderHistoryHub(username, password, body);
    }

    private static String getOrderHistoryHub(String username, String password, String body) {
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE, username, password, body);
    }
}
