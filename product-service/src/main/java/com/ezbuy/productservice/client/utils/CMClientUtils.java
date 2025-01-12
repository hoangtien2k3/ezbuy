package com.ezbuy.productservice.client.utils;

import com.ezbuy.productmodel.dto.request.GetLstSubscriberRequest;
import com.reactify.factory.MarshallerFactory;
import com.reactify.util.DataWsUtil;
import java.text.MessageFormat;

public class CMClientUtils {

    private static final String CM_REQUEST_TEMPLATE =
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                    + "   <soap:Body>\n"
                    + "   \t<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ext=\"http://externalSystem.service.sale.bccs.viettel.com/\">\n"
                    + "   \t<soapenv:Header>\n"
                    + "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                    + "         <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n"
                    + "            <wsse:Username>{0}</wsse:Username>\n"
                    + "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n"
                    + "         </wsse:UsernameToken>\n" + "      </wsse:Security>\n" + "   </soapenv:Header>\n"
                    + "   <soapenv:Body>\n" + "{2}\n" + "   </soapenv:Body>\n" + "   \t</soapenv:Envelope>\n"
                    + "   </soap:Body>\n" + "</soap:Envelope>";

    private static final String GET_SUBSCRIBER =
            "<ext:getListSubscriberByIdNo>\n" + "{0}\n" + "</ext:getListSubscriberByIdNo>";

    private static final String GET_CUSTOMER_SUBSCRIBER_SME_INFO =
            "<ext:getCustomerSubscriberSmeInfo>\n" + "{0}\n" + "</ext:getCustomerSubscriberSmeInfo>";

    private static String getSaleRequest(String username, String password, String body) {
        return MessageFormat.format(CM_REQUEST_TEMPLATE, username, password, body);
    }

    public static String getListSubscriberByIdNo(String idNo, String username, String password) {
        GetLstSubscriberRequest request = new GetLstSubscriberRequest();
        // TODO: 30_8 o day dang chua truyen telecomServiceId
        request.setIdNo(idNo);

        String bodyReq = MarshallerFactory.convertObjectToXML(request, GetLstSubscriberRequest.class);
        String bodyDataValue =
                DataWsUtil.getDataByTag(bodyReq, "<getLstSubscriberRequest>", "</getLstSubscriberRequest>");
        String body = MessageFormat.format(GET_SUBSCRIBER, bodyDataValue);
        return getSaleRequest(username, password, body);
    }

    public static String getCustomerSubscriberSmeInfo(
            Long telecomServiceId, String idNo, String isdn, String username, String password) {
        GetLstSubscriberRequest request = new GetLstSubscriberRequest();
        request.setTelecomServiceId(telecomServiceId);
        request.setIdNo(idNo);
        request.setIsdn(isdn);

        String bodyReq = MarshallerFactory.convertObjectToXML(request, GetLstSubscriberRequest.class);
        String bodyDataValue =
                DataWsUtil.getDataByTag(bodyReq, "<getLstSubscriberRequest>", "</getLstSubscriberRequest>");
        String body = MessageFormat.format(GET_CUSTOMER_SUBSCRIBER_SME_INFO, bodyDataValue);
        return getSaleRequest(username, password, body);
    }
}
