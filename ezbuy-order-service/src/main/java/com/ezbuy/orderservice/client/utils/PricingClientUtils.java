package com.ezbuy.orderservice.client.utils;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.core.factory.MarshallerFactory;
import com.ezbuy.core.util.DataWsUtil;
import java.text.MessageFormat;

public class PricingClientUtils {
    private static final String PRICING_REQUEST_TEMPLATE =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.order.bccs.viettel.com/\">\n"
                    + "   <soapenv:Header>\n"
                    + "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                    + "         <wsse:UsernameToken>\n" + "            <wsse:Username>{0}</wsse:Username>\n"
                    + "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n"
                    + "         </wsse:UsernameToken>\n" + "      </wsse:Security>\n" + "   </soapenv:Header>\n"
                    + "   <soapenv:Body>\n" + "  {2}" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";

    private static final String PRICING_PRODUCT =
            "<ser:pricingProductsWithViewModeExt>\n" + "{0}\n" + "</ser:pricingProductsWithViewModeExt>";

    private static String getPricingRequest(String username, String password, String body) {
        return MessageFormat.format(PRICING_REQUEST_TEMPLATE, username, password, body);
    }

    public static String getPricingProduct(PricingProductRequest request, String username, String password) {
        String bodyReq = MarshallerFactory.convertObjectToXML(request, PricingProductRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<pricingProductRequest>", "</pricingProductRequest>");
        String body = MessageFormat.format(PRICING_PRODUCT, bodyDataValue);
        return getPricingRequest(username, password, body);
    }
}
