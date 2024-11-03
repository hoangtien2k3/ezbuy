package com.ezbuy.paymentservice.utils;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.SearchOrderStateRequest;
import com.reactify.factory.MarshallerFactory;
import com.reactify.util.DataWsUtil;

import java.text.MessageFormat;
import java.util.List;

public class OrderClientUtils {

    private static final String ORDER_REQUEST_TEMPLATE =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.order.bccs.viettel.com/\">\n"
                    + "   <soapenv:Header>\n"
                    + "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                    + "         <wsse:UsernameToken>\n" + "            <wsse:Username>{0}</wsse:Username>\n"
                    + "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n"
                    + "         </wsse:UsernameToken>\n" + "      </wsse:Security>\n" + "   </soapenv:Header>\n"
                    + "   <soapenv:Body>\n" + "  {2}" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";

    private static final String PLACE_ORDER_BODY_TEMPLATE = "<ser:placeOrder>\n"
            + "\t  <orderType>{0}</orderType>         \n" + "\t  <data>{1}</data>\n" + "      </ser:placeOrder>";

    private static final String SEARCH_ORDER_STATE_TEMPLATE =
            "<ser:searchOrderStateByBpId>\n" + "{0}\n" + "      </ser:searchOrderStateByBpId>";

    private static final String PRICING_PRODUCT =
            "<ser:pricingProductsWithViewModeExt>\n" + "{0}\n" + "</ser:pricingProductsWithViewModeExt>";

    private static String getOrderRequest(String username, String password, String body) {
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE, username, password, body);
    }

    public static String getPlaceOrderPayload(String type, String dataJson, String username, String password) {
        String body = MessageFormat.format(PLACE_ORDER_BODY_TEMPLATE, type, dataJson);
        return getOrderRequest(username, password, body);
    }

    public static String getSearchOrderStatePayload(List<String> orderCodeList, String username, String password) {
        SearchOrderStateRequest request = new SearchOrderStateRequest();
        request.setOrderCodeList(orderCodeList);
        String bodyReq = MarshallerFactory.convertObjectToXML(request, SearchOrderStateRequest.class);
        String bodyDataValue =
                DataWsUtil.getDataByTag(bodyReq, "<searchOrderStateRequest>", "</searchOrderStateRequest>");
        String body = MessageFormat.format(SEARCH_ORDER_STATE_TEMPLATE, bodyDataValue);
        return getOrderRequest(username, password, body);
    }

    public static String getPricingProduct(PricingProductRequest request, String username, String password) {
        String bodyReq = MarshallerFactory.convertObjectToXML(request, PricingProductRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<pricingProductRequest>", "</pricingProductRequest>");
        String body = MessageFormat.format(PRICING_PRODUCT, bodyDataValue);
        return getOrderRequest(username, password, body);
    }
}
